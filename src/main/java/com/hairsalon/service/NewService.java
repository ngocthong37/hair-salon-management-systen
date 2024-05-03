package com.hairsalon.service;

import com.hairsalon.entity.Appointment;
import com.hairsalon.entity.News;
import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.User;
import com.hairsalon.model.AppointmentModel;
import com.hairsalon.respository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewService {

    @Autowired
    private NewsRepository newsRepository;

    public ResponseEntity<ResponseObject> getAll() {
        List<News> newsList = null;
        newsList = newsRepository.findAll();

        if (!newsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", newsList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }
}
