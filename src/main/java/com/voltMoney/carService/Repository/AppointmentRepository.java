package com.voltMoney.carService.Repository;

import com.voltMoney.carService.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query(value ="select * from Appointments where operator_id = :operatorId and status = 'BOOKED'", nativeQuery = true)
    List<Appointment> findByOperatorId(@Param("operatorId")Integer operatorId);

    @Query("select a from Appointment a where a.user.id = :userId")
    List<Appointment> findByUserId(@Param("userId") Integer userId);

//    @Query("Select case when count(a) > 0 THEN true ELSE false END  " +
//            "From Appointment a where a.operator.id = :operatorId" +
//            "And a.status = 'BOOKED' "+
//            "AND (a.startTimeHour = :startHour AND a.endTimeHour = :endHour)")
//    boolean isOperatorBusy(@Param("operatorId") Integer operatorId,
//                           @Param("startHour") Integer startHour,
//                           @Param("endHour") Integer endHour);
    @Query("Select a from Appointment a where a.operator.id = :operatorId and a.startTimeHour = :startHour and a.endTimeHour = :endHour")
    Appointment findAppointmentByIdAndTime(@Param("operatorId") Integer operatorId,
                                           @Param("startHour") Integer startHour,
                                           @Param("endHour") Integer endHour);

    @Query(value = "SELECT DISTINCT operator_id " +
            "FROM appointments " +
            "WHERE start = :startTime AND end = :endTime", nativeQuery = true)
    List<Integer> getBusyOperator(@Param("startTime") Integer startHour,
                                  @Param("endTime") Integer endTime);
}
