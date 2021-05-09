package com.example.demo.util;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarDeserializer extends JsonDeserializer<Calendar> {

    @Override
    public Calendar deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        String string = jp.readValueAs(String.class);
        String[] elements=string.split(" |:|-");
        return new GregorianCalendar(Integer.parseInt(elements[0]),
                Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),
                Integer.parseInt(elements[3]),Integer.parseInt(elements[4]));
    }
}
