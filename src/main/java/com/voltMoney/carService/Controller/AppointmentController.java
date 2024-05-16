package com.voltMoney.carService.Controller;


import com.voltMoney.carService.DTO.AppointmentDTO;
import com.voltMoney.carService.Service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @PostMapping(path = "/appointment/book")
    public ResponseEntity<Object> bookAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.bookAppointment(appointmentDTO));
    }

    @PutMapping(path = "/appointment/reschedule/{appointmentId}")
    public ResponseEntity<Object> rescheduleAppointment(@PathVariable Integer appointmentId, @RequestBody AppointmentDTO appointmentDTO ){
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(appointmentId, appointmentDTO));
    }

    @PutMapping(path = "/appointment/cancel/{appointmentId}")
    public ResponseEntity<Object> cancelAppointment(@PathVariable Integer appointmentId, @RequestParam Integer userId){
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId, userId));
    }

}
