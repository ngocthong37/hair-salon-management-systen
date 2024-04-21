package com.hairsalon.service;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.User;
import com.hairsalon.model.UserModel;
import com.hairsalon.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<ResponseObject> findAll() {
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAll();
        List<UserModel> userModelList = userList.stream()
                .map(user -> {
                    UserModel userModel = new UserModel();
                    userModel.setId(user.getId());
                    userModel.setUserName(user.getUsername());
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


}
