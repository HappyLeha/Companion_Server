package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripForm {

    private Calendar startFromDate;

    private Calendar endFromDate;

    private String from;

    private String to;

    @DecimalMin(value = "0.0", message = "cost shouldn't be less than 0.0")
    private Double cost;

    @DecimalMax(value = "5.0",
            message = "rating parameter should be less or equal 5.0")
    @DecimalMin(value = "1.0",
            message = "rating parameter should be greater or equal 1.0")
    private Double rating;
}
