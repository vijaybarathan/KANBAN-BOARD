package com.bej.authentication.controller;

import com.bej.authentication.exception.UserAlreadyExistsException;
import com.bej.authentication.exception.InvalidCredentialsException;
import com.bej.authentication.exception.UserNotFoundException;
import com.bej.authentication.security.SecurityTokenGenerator;
import com.bej.authentication.service.IUserService;
import com.bej.authentication.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class UserController {

    private final IUserService userService;
    private final SecurityTokenGenerator tokenGenerator;

    @Autowired
    public UserController(IUserService userService, SecurityTokenGenerator tokenGenerator) {
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/saveuser")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws UserAlreadyExistsException {
        try {
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws InvalidCredentialsException {
        try {
            User retrievedUser = userService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());
            System.out.println(retrievedUser);
            String token = tokenGenerator.createToken(user);
            Map<String, String> map=new HashMap<>();
            map.put("token",token);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (InvalidCredentialsException e)
        {
            return new ResponseEntity<>("Invalid Credentails",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateuser/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email,@RequestBody User user)throws UserNotFoundException {
        try {
            return new ResponseEntity<>(userService.updateUser(email,user), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @GetMapping("/getallusers")
    public ResponseEntity getAllUsers(){
        try {
            return new ResponseEntity<>(userService.getAllVerifiedUsers(), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteuser/{email}")
    public ResponseEntity deleteUser(@PathVariable String email)throws UserNotFoundException {
        try {
            return new ResponseEntity<>(userService.deleteUser(email),HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @GetMapping("/getuseremail")
    public ResponseEntity getUserEmail() throws UserNotFoundException {
        try {
            return new ResponseEntity<>(userService.getUserEmail(),HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

}
