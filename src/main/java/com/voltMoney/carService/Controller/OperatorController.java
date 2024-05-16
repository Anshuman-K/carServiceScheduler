package com.voltMoney.carService.Controller;


import com.voltMoney.carService.DTO.OperatorDTO;
import com.voltMoney.carService.Service.OperatorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OperatorController {
    private final OperatorService operatorService;
    @Autowired
    public OperatorController(OperatorService operatorService){
        this.operatorService = operatorService;
    }

    @GetMapping("/operator/{operatorId}/booked")
    public ResponseEntity<Object> getAppointmentsByOperator(@PathVariable Integer operatorId){
        return ResponseEntity.ok(operatorService.getAppointmentsByOperatorId(operatorId));
    }

    @GetMapping("/operator/{operatorId}/open")
    public  ResponseEntity<Object> getOpenSlotsByOperator(@PathVariable Integer operatorId){
        return ResponseEntity.ok(operatorService.getOpenSlotsByOperator(operatorId));
    }

    @PostMapping(path = "/create-operator")
    public ResponseEntity<Object>createUser(@Valid @RequestBody OperatorDTO operatorDTO){
        return ResponseEntity.ok(operatorService.createOperator(operatorDTO));
    }

    @GetMapping(path = "/get-operator/{operatorId}")
    public ResponseEntity<Object>getUser(@PathVariable Integer operatorId){
        return ResponseEntity.ok(operatorService.getOperator(operatorId));
    }
}
