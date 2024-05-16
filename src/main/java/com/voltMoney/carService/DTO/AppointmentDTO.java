package com.voltMoney.carService.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDTO {
    private Integer operatorId;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer startTime;

    @NotNull
    private Integer endTime;

    @NotNull
    private String bookingReason;
}
