/**
 * OUTPUT:
 * Returns List<ParsedOrder>
 *
 * Next step (integration):
 * This output should be passed into OrderMapper
 * to convert into real Order objects.
 */
package com.group.parser;

import com.group.dto.ParsedItem;
import com.group.dto.ParsedOrder;
import com.group.util.ParseLogger;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses WallyWorld XML files.
 * Skips bad orders and logs errors.
 */
public class XmlOrderParser implements OrderParser {

    // Source name stored in each parsed order
    private static final String SOURCE = "WallyWorld";

    /**
     * Parses the XML file and returns a list of valid orders.
     * Bad orders are skipped and logged.
     */
    @Override
    public List<ParsedOrder> parse(File file) {
        List<ParsedOrder> orders = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList orderList = doc.getElementsByTagName("Order");

            for (int i = 0; i < orderList.getLength(); i++) {
                Node node = orderList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element orderElement = (Element) node;

                    try {
                        ParsedOrder order = parseOrder(orderElement);
                        orders.add(order);
                        ParseLogger.logInfo("Successfully parsed order id " + order.getOrderId());
                    } catch (Exception e) {
                        String orderId = orderElement.getAttribute("id");
                        if (orderId == null || orderId.isBlank()) {
                            orderId = "unknown";
                        }

                        ParseLogger.logError("Skipping bad order with id " + orderId + ": " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            ParseLogger.logError("Failed to parse XML file '" + file.getName() + "': " + e.getMessage());
        }

        return orders;
    }

    /**
     * Parses one <Order> element into a ParsedOrder object.
     */
    private ParsedOrder parseOrder(Element element) {
        // Read and validate order id
        String idText = element.getAttribute("id");
        if (idText == null || idText.isBlank()) {
            throw new IllegalArgumentException("Missing order id.");
        }

        int id;
        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid order id: '" + idText + "'");
        }

        // Read and validate order type
        String type = getRequiredText(element, "OrderType");

        ParsedOrder order = new ParsedOrder(id, type, SOURCE);

        // Read items
        NodeList itemList = element.getElementsByTagName("Item");

        if (itemList.getLength() == 0) {
            throw new IllegalArgumentException("Order has no items.");
        }

        for (int i = 0; i < itemList.getLength(); i++) {
            Node node = itemList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                ParsedItem item = parseItem(itemElement);
                order.addItem(item);
            }
        }

        return order;
    }

    /**
     * Parses one <Item> element into a ParsedItem object.
     */
    private ParsedItem parseItem(Element itemElement) {
        // Read and validate item type
        String itemType = itemElement.getAttribute("type");
        if (itemType == null || itemType.isBlank()) {
            throw new IllegalArgumentException("Item is missing type attribute.");
        }

        // Read price and quantity text
        String priceText = getRequiredText(itemElement, "Price");
        String quantityText = getRequiredText(itemElement, "Quantity");

        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price value: '" + priceText + "'");
        }

        try {
            quantity = Integer.parseInt(quantityText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity value: '" + quantityText + "'");
        }

        // Extra validation
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        return new ParsedItem(itemType, price, quantity);
    }

    /**
     * Helper method:
     * Gets text from a required child tag.
     * Throws an error if the tag is missing or blank.
     */
    private String getRequiredText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);

        if (list.getLength() == 0) {
            throw new IllegalArgumentException("Missing required tag: " + tagName);
        }

        String text = list.item(0).getTextContent();

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty value for tag: " + tagName);
        }

        return text.trim();
    }
}