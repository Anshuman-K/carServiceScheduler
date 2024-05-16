package com.voltMoney.carService.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity{

    private String name;

//    @OneToMany(mappedBy = "user")
//    private List<Appointment> bookedAppointments;

}
