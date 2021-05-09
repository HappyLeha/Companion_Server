package com.example.demo.dto;
import com.example.demo.util.CalendarDeserializer;
import com.example.demo.util.CalendarSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {

    private int id;

    @NotBlank(message = "fromPlace shouldn't be empty")
    private String fromPlace;

    @NotBlank(message = "toPlace shouldn't be empty")
    private String toPlace;

    @NotNull(message = "fromDateTime shouldn't be empty")
    /*@JsonSerialize(using = CalendarSerializer.class)
    @JsonDeserialize(using= CalendarDeserializer.class)*/
    private Calendar fromDateTime;

    @NotNull(message = "toDateTime shouldn't be empty")
    /*@JsonSerialize(using = CalendarSerializer.class)
    @JsonDeserialize(using = CalendarDeserializer.class)*/
    private Calendar toDateTime;

    @NotBlank(message = "transport shouldn't be empty")
    private String transport;

    @NotNull(message = "cost shouldn't be empty")
    @Digits(message = "Invalid cost", integer = Integer.MAX_VALUE, fraction = 2)
    @Min(value = 0, message = "cost should be greater than 0")
    private double cost;

    private String note;

    private UserSummaryDTO userSummary;
}
