package com.hairsalon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hairsalon.entity.ResponseObject;
import com.hairsalon.respository.AppointmentRepository;
import com.hairsalon.respository.OrderRepository;
import com.hairsalon.respository.imp.RevenueRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class RevenueService {
    @Autowired
    RevenueRepositoryImp revenueImp;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public ResponseEntity<ResponseObject> getRevenueFromService() {
        List<Double> listResult = new ArrayList<>(); // Khởi tạo danh sách mới
        Double price = revenueImp.getRevenueFromService();
        listResult.add(price);
        listResult.add(0.0);
        if (!listResult.isEmpty()) {
            // Chuyển đổi danh sách thành một mảng các đối tượng JSON
            ArrayNode dataArray = convertListToArray(listResult);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", dataArray));
        } else {
            // Nếu không tìm thấy dữ liệu, bạn có thể trả về một danh sách rỗng hoặc một thông báo lỗi
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", new ArrayList<>()));
        }
    }

    private ArrayNode convertListToArray(List<Double> list) {
        // Khởi tạo một mảng JSON
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode dataArray = objectMapper.createArrayNode();

        // Lặp qua danh sách và thêm mỗi phần tử vào mảng JSON dưới dạng một đối tượng JSON
        for (Double value : list) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("value", value);
            dataArray.add(objectNode);
        }

        return dataArray;
    }


    public ResponseEntity<ResponseObject> getRevenueFromProduct() {
        List<Double> listResult = new ArrayList<>(); // Khởi tạo danh sách mới
        Double price = revenueImp.getRevenueFromProduct();
        listResult.add(price);
        listResult.add(0.0);
        if (!listResult.isEmpty()) {
            // Chuyển đổi danh sách thành một mảng các đối tượng JSON
            ArrayNode dataArray = convertListToArray(listResult);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", dataArray));
        } else {
            // Nếu không tìm thấy dữ liệu, bạn có thể trả về một danh sách rỗng hoặc một thông báo lỗi
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", new ArrayList<>()));
        }
    }

    public ResponseEntity<ResponseObject> getRevenueFromServiceByYear(Integer year) {
        Map<String, Object> results = new TreeMap<String, Object>();
        Double price = revenueImp.getRevenueFromServiceByYear(year);
        results.put("totalMoney", price);
        results.size();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", results));
    }

    public ResponseEntity<ResponseObject> getRevenueFromServiceByMonth(Integer year, Integer month) {
        List<Double> listResult = new ArrayList<>(); // Khởi tạo danh sách mới
        Map<String, Object> results = new TreeMap<String, Object>();
        Double price = revenueImp.getRevenueFromServiceByMonth(year, month);
        results.put("totalMoney", price);
        listResult.add(price);
        listResult.add(0.0);
        results.size();
        if (!listResult.isEmpty()) {
            // Chuyển đổi danh sách thành một mảng các đối tượng JSON
            ArrayNode dataArray = convertListToArray(listResult);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", dataArray));
        } else {
            // Nếu không tìm thấy dữ liệu, bạn có thể trả về một danh sách rỗng hoặc một thông báo lỗi
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", new ArrayList<>()));
        }
    }

    public ResponseEntity<ResponseObject> getRevenueServiceBetweenDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Double> listResult = new ArrayList<>();
        Map<String, Object> results = new TreeMap<String, Object>();
        Double price = appointmentRepository.findRevenueBetweenDates(startDate, endDate);
        if (price == null) {
            price = 0.0;
        }
        results.put("totalMoney", price);
        listResult.add(price);
        listResult.add(0.0);
        results.size();
        ArrayNode dataArray = convertListToArray(listResult);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", dataArray));
    }

    public ResponseEntity<ResponseObject> getRevenueProductBetweenDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Double> listResult = new ArrayList<>();
        Map<String, Object> results = new TreeMap<String, Object>();
        Double price = orderRepository.findRevenueBetweenDates(startDate, endDate);
        if (price == null) {
            price = 0.0;
        }
        results.put("totalMoney", price);
        listResult.add(price);
        listResult.add(0.0);
        results.size();
        ArrayNode dataArray = convertListToArray(listResult);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", dataArray));
    }



}
