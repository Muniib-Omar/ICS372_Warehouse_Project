package com.group.parser

import com.google.gson.JsonParser
import com.group.dto.ParsedItem
import com.group.dto.ParsedOrder
import com.group.util.ParseLogger
import java.io.File

class JsonOrderParser : OrderParser {

    private val source = "Bullseye"

    override fun parse(file: File): List<ParsedOrder> {
        val orders = mutableListOf<ParsedOrder>()

        try {
            val root = JsonParser.parseString(file.readText()).asJsonObject
            val orderArray = root.getAsJsonArray("orders")

            for (orderElement in orderArray) {
                val orderJson = orderElement.asJsonObject

                try {
                    val id = orderJson.get("id").asInt
                    val orderType = orderJson.get("orderType").asString

                    val parsedOrder = ParsedOrder(id, orderType, source)

                    val itemsArray = orderJson.getAsJsonArray("items")

                    for (itemElement in itemsArray) {
                        val itemJson = itemElement.asJsonObject

                        val itemType = itemJson.get("type").asString
                        val price = itemJson.get("price").asDouble
                        val quantity = itemJson.get("quantity").asInt

                        if (itemType.isBlank()) {
                            throw IllegalArgumentException("Item type cannot be blank.")
                        }

                        if (price < 0) {
                            throw IllegalArgumentException("Price cannot be negative.")
                        }

                        if (quantity <= 0) {
                            throw IllegalArgumentException("Quantity must be greater than 0.")
                        }

                        parsedOrder.addItem(ParsedItem(itemType, price, quantity))
                    }

                    orders.add(parsedOrder)
                    ParseLogger.logInfo("Successfully parsed JSON order id ${parsedOrder.orderId}")

                } catch (e: Exception) {
                    ParseLogger.logError("Skipping bad JSON order: ${e.message}")
                }
            }

        } catch (e: Exception) {
            ParseLogger.logError("Failed to parse JSON file '${file.name}': ${e.message}")
        }

        return orders
    }
}

