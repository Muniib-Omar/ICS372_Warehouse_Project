package com.group.parser;

import com.group.dto.ParsedOrder;

import java.io.File;
import java.util.List;

/**
 * Interface for all parsers (XML and JSON).
 */
public interface OrderParser {
    List<ParsedOrder> parse(File file);
}