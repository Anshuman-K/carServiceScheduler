package com.voltMoney.carService.Repository;

import com.voltMoney.carService.Entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Integer> {

    @Query(value = "Select id from operators", nativeQuery = true)
    List<Integer> findAllOperatorIds();
}
