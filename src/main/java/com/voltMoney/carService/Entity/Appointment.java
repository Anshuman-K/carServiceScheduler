package com.voltMoney.carService.Entity;

import com.voltMoney.carService.Enum.Status;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="Appointments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Appointment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "operator_Id")
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    /*I am using Integer here only to simplify the solution, we can make it realistic by using
    LocalDatetime and then use @DateTimeFormat annotation and then proceed the operation on the same. */
    @Column(name = "start")
    private Integer startTimeHour;

    @Column(name = "end")
    private Integer endTimeHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name ="Booking_reason")
    private String reason;

}
