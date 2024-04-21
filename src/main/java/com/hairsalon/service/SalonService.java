package com.hairsalon.service;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.Salon;
import com.hairsalon.entity.ServiceHair;
import com.hairsalon.respository.SalonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SalonService {
    @Autowired
    private SalonRepository salonRepository;

    public ResponseEntity<ResponseObject> getAll() {
        List<Salon> hairServiceList = null;
        hairServiceList = salonRepository.findAll();

        if (!hairServiceList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", hairServiceList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

}
