package com.voltMoney.carService.Controller;


import com.voltMoney.carService.DTO.UserDTO;
import com.voltMoney.carService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(path = "/create-user")
    public ResponseEntity<Object>createUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping(path = "/get-user/{userId}")
    public ResponseEntity<Object>getUser(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
