package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.User;
import com.hairsalon.model.UserModel;
import com.hairsalon.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
                    userModel.setPassword(user.getPassword());
                    userModel.setAddress(user.getAddress());
                    userModel.setStatus(user.getStatus());
                    userModel.setFullName(user.getFullName());
                    userModel.setPhoneNumber(user.getPhoneNumber());
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
                    userModel.setRole(user.getRole().toString());
                    userModel.setPassword(user.getPassword());
                    userModel.setAddress(user.getAddress());
                    userModel.setStatus(user.getStatus());
                    userModel.setFullName(user.getFullName());
                    userModel.setPhoneNumber(user.getPhoneNumber());
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
        List<UserModel> userList = new ArrayList<>();
        if (customer.isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setId(customer.get().getId());
            userModel.setUserName(customer.get().getName());
            userModel.setEmail(customer.get().getUsername());
            userModel.setAddress(customer.get().getAddress());
            userModel.setStatus(customer.get().getStatus());
            userModel.setFullName(customer.get().getFullName());
            userModel.setPhoneNumber(customer.get().getPhoneNumber());
            userList.add(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userList));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
    }

    public ResponseEntity<ResponseObject> findEmployeeById(Integer employeeId) {
        Optional<User> user = userRepository.findById(employeeId);
        List<UserModel> userList = new ArrayList<>();
        if (user.isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setId(user.get().getId());
            userModel.setUserName(user.get().getName());
            userModel.setEmail(user.get().getEmail());
            userModel.setRole(user.get().getRole().toString());
            userModel.setPassword(user.get().getPassword());
            userModel.setAddress(user.get().getAddress());
            userModel.setStatus(user.get().getStatus());
            userModel.setFullName(user.get().getFullName());
            userModel.setPhoneNumber(user.get().getPhoneNumber());
            userList.add(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", userList));
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

    public ResponseEntity<Object> updateStatusUser(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            Integer id = jsonNode.get("id") != null ? jsonNode.get("id").asInt() : -1;
            String status = jsonNode.get("status") != null ? jsonNode.get("status").asText() : "OK";
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayNode dataArray = objectMapper.createArrayNode();
                List<String> response = new ArrayList<>();
                User user = userOptional.get();
                user.setStatus(status);
                User updatedUser = userRepository.save(user);
                response.add(id.toString());
                response.add(status);
                for (String value : response) {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("value", value);
                    dataArray.add(objectNode);
                };
                if (updatedUser.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", dataArray));
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ERROR", "Have error when update user", ""));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }

    }

    public ResponseEntity<Object> addEmployee(String json){
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
