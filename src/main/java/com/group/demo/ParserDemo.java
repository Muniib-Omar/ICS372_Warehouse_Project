package com.group.demo;

import com.group.dto.ParsedOrder;
import com.group.mapper.OrderMapper;
import com.group.parser.XmlOrderParser;
import model.Order;

import java.io.File;
import java.util.List;

public class ParserDemo {

    public static void main(String[] args) {

        XmlOrderParser parser = new XmlOrderParser();
        OrderMapper mapper = new OrderMapper();

        File file = new File("src/main/resources/ExampleOrder1.xml");

        List<ParsedOrder> orders = parser.parse(file);

        System.out.println("Orders found: " + orders.size());

        for (ParsedOrder parsedOrder : orders) {
            Order order = mapper.map(parsedOrder);
            System.out.println(order);
        }
    }
}