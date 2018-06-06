package com.lolsearch.lolrecordsearch.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    
    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }
    
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    
        ObjectCodec codec = p.getCodec();
        TextNode node = (TextNode)codec.readTree(p);
        String dateString = node.textValue();
        Instant instant = Instant.parse(dateString);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        
        return dateTime;
    }
}
