package com.voltMoney.carService.Service;


import com.voltMoney.carService.DTO.UserDTO;
import com.voltMoney.carService.Entity.Operator;
import com.voltMoney.carService.Entity.User;
import com.voltMoney.carService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public ResponseEntity<Object> createUser(UserDTO userDTO) {
        User user = User.builder().name(userDTO.getName()).build();
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    public ResponseEntity<Object> getUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body("User doesn't exist!"));
    }
}
