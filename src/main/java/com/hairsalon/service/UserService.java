package com.hairsalon.service;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.User;
import com.hairsalon.model.UserModel;
import com.hairsalon.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<ResponseObject> findAllCustomer() {
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllCustomer();
        List<UserModel> userModelList = userList.stream()
                .map(user -> {
                    UserModel userModel = new UserModel();
                    userModel.setId(user.getId());
                    userModel.setUserName(user.getCustomerName());
                    userModel.setEmail(user.getEmail());
                    return userModel;
                })
                .toList();
        if (!userModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userModelList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ResponseEntity<ResponseObject> findAllEmployee() {
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllEmployee();
        List<UserModel> userModelList = userList.stream()
                .map(user -> {
                    UserModel userModel = new UserModel();
                    userModel.setId(user.getId());
                    userModel.setUserName(user.getCustomerName());
                    userModel.setEmail(user.getEmail());
                    return userModel;
                })
                .toList();
        if (!userModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userModelList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }


    public ResponseEntity<ResponseObject> findCustomerById(Integer customerId) {
        Optional<User> customer  = userRepository.findById(customerId);
        if (customer.isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setId(customer.get().getId());
            userModel.setUserName(customer.get().getCustomerName());
            userModel.setEmail(customer.get().getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userModel));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
    }

    public ResponseEntity<ResponseObject> findEmployeeById(Integer employeeId) {
        Optional<User> customer  = userRepository.findById(employeeId);
        if (customer.isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setId(customer.get().getId());
            userModel.setUserName(customer.get().getCustomerName());
            userModel.setEmail(customer.get().getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userModel));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
    }
}
