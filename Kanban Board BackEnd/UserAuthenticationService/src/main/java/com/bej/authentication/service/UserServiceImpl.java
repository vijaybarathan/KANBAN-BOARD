package com.bej.authentication.service;

import com.bej.authentication.domain.User;
import com.bej.authentication.exception.UserAlreadyExistsException;
import com.bej.authentication.exception.InvalidCredentialsException;
import com.bej.authentication.exception.UserNotFoundException;
import com.bej.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        if(userRepository.findById(user.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException();
        }
        else {
            user= userRepository.save(user);
        }
        return user;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws InvalidCredentialsException {

        User user = userRepository.findByEmailAndPassword(email, password);

        if (user == null) {
            throw new InvalidCredentialsException();
        }
        return user;
    }
    @Override
    public User updateUser(String email, User user) throws UserNotFoundException {
        if(userRepository.findById(email).isEmpty()){
            throw new UserNotFoundException();
        }
        User user1=userRepository.findById(email).get();
        if(user1.getEmail().equals(email)){
            user1.setPassword(user.getPassword());
        }
        return userRepository.save(user1);
    }

    @Override
    public String deleteUser(String email) throws UserNotFoundException {
        if(userRepository.findById(email).isEmpty()){
            throw new UserNotFoundException();
        }
        else{
            userRepository.deleteById(email);
        }
        return "User Deleted Successfully !!!";
    }

    @Override
    public List<User> getAllVerifiedUsers() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public List<String> getUserEmail()  {
        List<User>  us=userRepository.findAll();
        List<String> list=new ArrayList<>();
        for(User u:us){
            list.add(u.getEmail());
        }
        return list;
    }

}
