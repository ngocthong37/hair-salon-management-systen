package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hairsalon.entity.ProductItem;
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
                    userModel.setUserName(user.getName());
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
                    userModel.setUserName(user.getName());
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
        Optional<User> customer = userRepository.findById(customerId);
        if (customer.isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setId(customer.get().getId());
            userModel.setUserName(customer.get().getName());
            userModel.setEmail(customer.get().getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userModel));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
    }

    public ResponseEntity<ResponseObject> findEmployeeById(Integer employeeId) {
        Optional<User> customer = userRepository.findById(employeeId);
        if (customer.isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setId(customer.get().getId());
            userModel.setUserName(customer.get().getName());
            userModel.setEmail(customer.get().getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userModel));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
    }

    public ResponseEntity<Object> update(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            Integer id = jsonNode.get("id") != null ? jsonNode.get("id").asInt() : -1;
            String fullName = jsonNode.get("fullName") != null ? jsonNode.get("fullName").asText() : "";
            String address = jsonNode.get("address") != null ? jsonNode.get("address").asText() : null;
            String phoneNumber = jsonNode.get("phoneNumber") != null ? jsonNode.get("phoneNumber").asText() : "";
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFullName(fullName);
                user.setAddress(address);
                user.setPhoneNumber(phoneNumber);
                User updatedUser = userRepository.save(user);
                if (updatedUser.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", user.getId()));
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ERROR", "Have error when update user", ""));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }

    }

}
