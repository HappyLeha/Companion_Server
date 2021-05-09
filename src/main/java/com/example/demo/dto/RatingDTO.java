package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    @Min(value = 1, message = "rating shouldn't be less than 1")
    @Max(value = 5, message = "rating shouldn't be greater than 5")
    private int rating;
}
