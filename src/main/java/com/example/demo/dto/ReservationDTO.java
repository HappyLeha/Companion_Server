package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private int id;
    private boolean approved;
    private Integer driverRate;
    private Integer passengerRate;
    private UserSummaryDTO userSummary;
}
