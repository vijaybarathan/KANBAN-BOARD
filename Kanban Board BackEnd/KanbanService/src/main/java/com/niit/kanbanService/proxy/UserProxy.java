package com.niit.kanbanService.proxy;

import com.niit.kanbanService.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="user-authentication-service",url="localhost:8083")
public interface UserProxy {
    @PostMapping("/api/v1/saveuser")
    public ResponseEntity<?> saveUser(@RequestBody User user);

    @DeleteMapping("/api/v1/deleteuser/{email}")
    public ResponseEntity deleteUser(@RequestParam("email")String email);

    @PutMapping("api/v1/updateuser/{email}")
    public ResponseEntity updateUser(@RequestParam("email")String email, @RequestBody User user);

}

