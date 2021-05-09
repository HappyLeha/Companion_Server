package com.example.demo.util;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Calendar;

public class CalendarSerializer extends JsonSerializer<Calendar> {

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider sp)
            throws IOException {
        gen.writeString(value.get(Calendar.YEAR) + "-" + value.get(Calendar.MONTH) +
                        "-" + value.get(Calendar.DAY_OF_MONTH) + " " +
                        value.get(Calendar.HOUR_OF_DAY)+":"+value.get(Calendar.MINUTE));
    }
}

