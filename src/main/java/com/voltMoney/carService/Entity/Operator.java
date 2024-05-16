package com.voltMoney.carService.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;



@Entity
@Table(name="Operators")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Operator extends BaseEntity{

    @Column(name = "Name")
    private String name;

//    @OneToMany(mappedBy = "operator")
//    private List<Appointment> bookedAppointments;

}
