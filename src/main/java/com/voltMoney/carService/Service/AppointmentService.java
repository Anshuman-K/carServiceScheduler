package com.voltMoney.carService.Service;


import com.voltMoney.carService.DTO.AppointmentDTO;
import com.voltMoney.carService.Entity.Appointment;
import com.voltMoney.carService.Entity.Operator;
import com.voltMoney.carService.Entity.User;
import com.voltMoney.carService.Enum.Status;
import com.voltMoney.carService.Repository.AppointmentRepository;
import com.voltMoney.carService.Repository.OperatorRepository;
import com.voltMoney.carService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final OperatorRepository operatorRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository,
                              OperatorRepository operatorRepository){
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.operatorRepository = operatorRepository;
    }

    //Method to book appointment.
    public ResponseEntity<Object> bookAppointment(AppointmentDTO appointmentDTO){
        Optional<User> user = userRepository.findById(appointmentDTO.getUserId());
        if(user.isPresent()){
            boolean validateTime = validateTimeFormat(appointmentDTO.getEndTime(), appointmentDTO.getStartTime());
            if(validateTime){
                if(Objects.nonNull(appointmentDTO.getOperatorId())){
                    Optional<Operator> operator = operatorRepository.findById(appointmentDTO.getOperatorId());
                    if (operator.isPresent()) {
                        return bookWithSpecificOperator(appointmentDTO);
                    } else {
                        return ResponseEntity.badRequest().body("The given Operator doesn't exist! Try with Valid Operator.");
                    }
                }else{
                    return bookWithRandomOperator(appointmentDTO);
                }
            }else{
                return ResponseEntity.badRequest().body("Incorrect Time or format! Please correct the Time.");
            }
        }else{
            return ResponseEntity.badRequest().body("User doesn't found. Please enter correct UserId!");
        }
    }

    //Method to reschedule appointment
    public ResponseEntity<Object> rescheduleAppointment(Integer appointmentId, AppointmentDTO appointmentDTO) {
       ResponseEntity<Object> responseEntity = cancelAppointment(appointmentId, appointmentDTO.getUserId());
       if(responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(400))){
           return responseEntity;
       }
        return bookAppointment(appointmentDTO);
    }


    //Method to book with random operator which is present in the given time slot.
    private ResponseEntity<Object> bookWithRandomOperator(AppointmentDTO appointmentDTO){
        Random random = new Random();
        List<Integer> availableOperator = getAvailableOperator(appointmentDTO);
        int randomIndex = random.nextInt(availableOperator.size());
        Integer randomOperatorId = availableOperator.get(randomIndex);
        appointmentDTO.setOperatorId(randomOperatorId);
        return bookWithSpecificOperator(appointmentDTO);
    }

    //Method to book with the specific operator.
    private ResponseEntity<Object> bookWithSpecificOperator(AppointmentDTO appointmentDTO){
        Appointment appointment = appointmentRepository.findAppointmentByIdAndTime(appointmentDTO.getOperatorId(),appointmentDTO.getStartTime(), appointmentDTO.getEndTime());
        if(Objects.isNull(appointment)){
            Appointment appointment1 = Appointment.builder()
                    .operator(operatorRepository.findById(appointmentDTO.getOperatorId()).get())
                    .user(userRepository.findById(appointmentDTO.getUserId()).get())
                    .endTimeHour(appointmentDTO.getEndTime())
                    .startTimeHour(appointmentDTO.getStartTime())
                    .status(Status.BOOKED)
                    .reason(appointmentDTO.getBookingReason()).build();

            appointmentRepository.save(appointment1);

            return ResponseEntity.ok(appointment1);
        }else{
            return ResponseEntity.status(403).body("Operator is not available for the given time slot! Please try again with different operator or Slot!");
        }
    }

    //Method to validate the time, if the given time format is right or not.
    private boolean validateTimeFormat(int endTime, int startTime) {
        if(endTime > 24 || endTime < 0) return false;
        if(startTime >24 || startTime <0) return false;
        return (startTime < endTime) && (endTime - startTime == 1);
    }


    //To proceed with the available operator, a method to find the available operator in the given slots.
    private List<Integer> getAvailableOperator(AppointmentDTO appointmentDTO) {
        List<Integer> busyOperators = appointmentRepository.getBusyOperator(appointmentDTO.getStartTime(), appointmentDTO.getEndTime());
        List<Integer> allOperators = operatorRepository.findAllOperatorIds();
        return allOperators.stream().filter(num -> !busyOperators.contains(num)).collect(Collectors.toList());
    }


    //Method to cancel the appointment.
    public ResponseEntity<Object> cancelAppointment(Integer appointmentId, Integer userId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isPresent()){
            Integer preUserId = appointment.get().getUser().getId();
            if(preUserId.equals(userId)) {
                Status status = appointment.get().getStatus();
                if (status.equals(Status.CANCELLED)) {
                    return ResponseEntity.badRequest().body("Appointment is already Cancelled or Rescheduled!");
                }
                //move the existing appointment to CANCELLED status
                appointment.get().setStatus(Status.CANCELLED);
                appointmentRepository.save(appointment.get());
                return ResponseEntity.ok("Appointment Id: "+ appointmentId + " is Cancelled!");
            }else{
                return ResponseEntity.badRequest().body("User doesn't match, please enter correct UserId for this appointment.");
            }
        }else{
            return ResponseEntity.badRequest().body("Appointment doesn't exist! Please enter correct appointmentId.");
        }
    }
}
