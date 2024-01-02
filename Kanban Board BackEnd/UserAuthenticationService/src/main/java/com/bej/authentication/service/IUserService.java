package com.bej.authentication.service;

import com.bej.authentication.domain.User;
import com.bej.authentication.exception.UserAlreadyExistsException;
import com.bej.authentication.exception.InvalidCredentialsException;
import com.bej.authentication.exception.UserNotFoundException;

import java.util.List;

public interface IUserService {
    User saveUser(User user) throws UserAlreadyExistsException;
    User getUserByEmailAndPassword(String email, String password) throws InvalidCredentialsException;
    User updateUser(String email,User user) throws UserNotFoundException;

    String deleteUser(String email)throws UserNotFoundException;

    List<User> getAllVerifiedUsers() throws Exception;

    List<String> getUserEmail() throws UserNotFoundException;
}
