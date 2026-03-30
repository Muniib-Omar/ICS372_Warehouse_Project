package com.group.parser;

import com.group.dto.ParsedOrder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XmlOrderParserTest {

    // Creates a temporary XML file for a test case
    private File createTempXml(String content) throws Exception {
        File tempFile = File.createTempFile("orders", ".xml");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }

        return tempFile;
    }

    @Test
    void parserShouldReadValidXmlOrder() throws Exception {
        // Valid XML with one correct order
        String xml = """
                <Orders>
                  <Order id="485">
                    <OrderType>Delivery</OrderType>
                    <Item type="Rubber duck">
                      <Price>13.45</Price>
                      <Quantity>2</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        // Check that the order was read correctly
        assertEquals(1, orders.size());
        ParsedOrder order = orders.get(0);

        assertEquals(485, order.getOrderId());
        assertEquals("Delivery", order.getOrderType());
        assertEquals("WallyWorld", order.getSource());
        assertEquals(1, order.getItems().size());
        assertEquals("Rubber duck", order.getItems().get(0).getItemType());
        assertEquals(13.45, order.getItems().get(0).getPrice(), 0.0001);
        assertEquals(2, order.getItems().get(0).getQuantity());
    }

    @Test
    void parserShouldSkipOrderWithMissingOrderType() throws Exception {
        // Missing OrderType should make the order invalid
        String xml = """
                <Orders>
                  <Order id="100">
                    <Item type="Soap">
                      <Price>5.25</Price>
                      <Quantity>2</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        // Invalid order should be skipped
        assertTrue(orders.isEmpty());
    }

    @Test
    void parserShouldSkipOrderWithStringPrice() throws Exception {
        // Non-numeric price should make the order invalid
        String xml = """
                <Orders>
                  <Order id="101">
                    <OrderType>Pickup</OrderType>
                    <Item type="Soap">
                      <Price>abc</Price>
                      <Quantity>2</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        assertTrue(orders.isEmpty());
    }

    @Test
    void parserShouldSkipOrderWithNegativePrice() throws Exception {
        // Negative price should be rejected
        String xml = """
                <Orders>
                  <Order id="102">
                    <OrderType>Ship</OrderType>
                    <Item type="Soap">
                      <Price>-5.25</Price>
                      <Quantity>2</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        assertTrue(orders.isEmpty());
    }

    @Test
    void parserShouldSkipOrderWithZeroQuantity() throws Exception {
        // Quantity must be greater than 0
        String xml = """
                <Orders>
                  <Order id="103">
                    <OrderType>Ship</OrderType>
                    <Item type="Soap">
                      <Price>5.25</Price>
                      <Quantity>0</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        assertTrue(orders.isEmpty());
    }

    @Test
    void parserShouldSkipOrderWithMissingItemType() throws Exception {
        // Missing item type should make the order invalid
        String xml = """
                <Orders>
                  <Order id="104">
                    <OrderType>Pickup</OrderType>
                    <Item>
                      <Price>5.25</Price>
                      <Quantity>1</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        assertTrue(orders.isEmpty());
    }

    @Test
    void parserShouldKeepGoodOrderEvenIfAnotherOrderIsBad() throws Exception {
        // One valid order and one invalid order in the same file
        String xml = """
                <Orders>
                  <Order id="201">
                    <OrderType>Ship</OrderType>
                    <Item type="Soap">
                      <Price>5.25</Price>
                      <Quantity>2</Quantity>
                    </Item>
                  </Order>
                  <Order id="202">
                    <OrderType>Pickup</OrderType>
                    <Item type="Bad Item">
                      <Price>abc</Price>
                      <Quantity>1</Quantity>
                    </Item>
                  </Order>
                </Orders>
                """;

        File file = createTempXml(xml);

        XmlOrderParser parser = new XmlOrderParser();
        List<ParsedOrder> orders = parser.parse(file);

        // Good order should still be parsed
        assertEquals(1, orders.size());
        assertEquals(201, orders.get(0).getOrderId());
    }
}