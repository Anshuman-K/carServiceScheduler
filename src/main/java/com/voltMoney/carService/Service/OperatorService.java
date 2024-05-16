package com.voltMoney.carService.Service;

import com.voltMoney.carService.DTO.OperatorDTO;
import com.voltMoney.carService.Entity.Appointment;
import com.voltMoney.carService.Entity.Operator;
import com.voltMoney.carService.Entity.User;
import com.voltMoney.carService.Repository.AppointmentRepository;
import com.voltMoney.carService.Repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OperatorService {

    public final OperatorRepository operatorRepository;
    public final AppointmentRepository appointmentRepository;

    @Autowired
    public OperatorService(OperatorRepository operatorRepository,
                           AppointmentRepository appointmentRepository){
        this.operatorRepository = operatorRepository;
        this.appointmentRepository = appointmentRepository;
    }


    //Method to get the booked slots of an operator.
    public ResponseEntity<Object> getAppointmentsByOperatorId(Integer operatorId) {
        if(Objects.nonNull(operatorId)){
            Optional<Operator> operator = operatorRepository.findById(operatorId);
            if(operator.isPresent()){
                List<Integer[]> bookedTimeSlots =getBookedSlotsByOperatorId(operatorId);

                Map<Integer, List<Integer[]>> bookedSlotsWithOperator = new HashMap<>();
                bookedSlotsWithOperator.put(operatorId,bookedTimeSlots);

                return ResponseEntity.ok(bookedSlotsWithOperator);

            }else{
                return ResponseEntity.badRequest().body("This operator doesn't exist!");
            }
        }else{
            ResponseEntity.badRequest().body("Invalid Input! Please enter correct details.");
        }
        return ResponseEntity.internalServerError().build();
    }


    //Method to get all the available slots in merged manner.
    public ResponseEntity<Object> getOpenSlotsByOperator(Integer operatorId) {
        if(Objects.nonNull(operatorId)) {
            Optional<Operator> operator = operatorRepository.findById(operatorId);
            if(operator.isPresent()){
                List<Integer []> bookedSlots = getBookedSlotsByOperatorId(operatorId);

                List<Integer[]> openSlots = new ArrayList<>();

                int start = bookedSlots.get(0)[0];
                int end = bookedSlots.get(0)[1];
                if(start != 0){
                    openSlots.add(new Integer[]{0, start});
                }
                for(int i =1; i<bookedSlots.size(); i++){
                    Integer [] intervals = bookedSlots.get(i);
                    int first = intervals[0];
                    int second = intervals[1];
                    if(first - end >=1){
                        openSlots.add(new Integer[]{end, first});
                    }
                    end = second;
                }
                if(end != 24) openSlots.add(new Integer[]{end, 24});


                Map<Integer, List<Integer[]>> openSlotForOperator = new HashMap<>();

                openSlotForOperator.put(operatorId,openSlots);

                return ResponseEntity.ok(openSlotForOperator);
            }else{
                return ResponseEntity.badRequest().body("This operator doesn't exist!");
            }
        }else{
            return ResponseEntity.badRequest().body("Invalid Input! Please enter correct details.");
        }
    }

    //Method to create user.
    public ResponseEntity<Object> createOperator(OperatorDTO operatorDTO) {
        Operator operator = Operator.builder().name(operatorDTO.getName()).build();
        return ResponseEntity.ok().body(operatorRepository.save(operator));
    }


    private List<Integer[]> getBookedSlotsByOperatorId(Integer operatorId) {
        List<Appointment> appointmentList = appointmentRepository.findByOperatorId(operatorId);

        List<Integer[]> bookedTimeSlots = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            Integer[] integers = new Integer[]{appointment.getStartTimeHour(), appointment.getEndTimeHour()};
            bookedTimeSlots.add(integers);
        }

        bookedTimeSlots.sort(Comparator.comparingInt(interval -> interval[0]));

        return bookedTimeSlots;
    }

    public ResponseEntity<Object> getOperator(Integer userId) {
        Optional<Operator> operator = operatorRepository.findById(userId);
        return operator.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body("User doesn't exist!"));
    }
}
