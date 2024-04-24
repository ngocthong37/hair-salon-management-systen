package com.hairsalon.service;

import com.hairsalon.entity.*;
import com.hairsalon.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hairsalon.respository.*;
import com.hairsalon.respository.imp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

//    @Autowired
//    private CustomerRepository customerRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private ServiceHairRepository serviceHairRepository;

    @Autowired
    private AppointmentStatusRepository appointmentStatusRepository;

    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<ResponseObject> getAll() {
        List<Appointment> appointmentList = null;
        appointmentList = appointmentRepository.findAll();
        List<AppointmentModel> appointmentModelList = appointmentList.stream()
                        .map(appointment -> {
                            AppointmentModel appointmentModel = new AppointmentModel();
                            appointmentModel.setId(appointment.getId());
                            Optional<User> customer;
                            Optional<User> stylist;
                            customer = userRepository.findById(appointment.getCustomer().getId());
                            stylist = userRepository.findById(appointment.getUser().getId());
                            appointmentModel.setCustomerName(customer.get().getName());
                            appointmentModel.setAppointmentTime(appointment.getAppointmentTime());
                            appointmentModel.setAppointmentDate(appointment.getAppointmentDate());
                            appointmentModel.setSalonName(appointment.getSalon().getSalonName());
                            appointmentModel.setUserName(stylist.get().getName());
                            appointmentModel.setServiceName(appointment.getServiceHair().getServiceName());
                            appointmentModel.setStatus(appointment.getAppointmentStatus().getStatus());
                            appointmentModel.setSalonAddress(appointment.getSalon().getAddress());
                            return appointmentModel;
                        }).collect(Collectors.toList());

        if (!appointmentModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", appointmentModelList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ResponseEntity<Object> makeAppointment(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (json == null || json.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("ERROR", "Empty JSON", ""));
            }
            JsonNode jsonObjectAppointment = objectMapper.readTree(json);
            Integer customerId = jsonObjectAppointment.get("customerId") != null ?
                    Integer.parseInt(jsonObjectAppointment.get("customerId").asText()) : 1;
            Integer serviceId = jsonObjectAppointment.get("serviceId") != null ?
                    jsonObjectAppointment.get("serviceId").asInt() : 1;
            Integer salonId = jsonObjectAppointment.get("salonId") != null ?
                    jsonObjectAppointment.get("salonId").asInt() : 1;
            String appointmentDate = jsonObjectAppointment.get("appointmentDate") != null ?
                    jsonObjectAppointment.get("appointmentDate").asText() : "";
            String appointmentTime = jsonObjectAppointment.get("appointmentTime") != null ?
                    jsonObjectAppointment.get("appointmentTime").asText() : "";
            Integer userId = jsonObjectAppointment.get("userId") != null ?
                    Integer.parseInt(jsonObjectAppointment.get("userId").asText()) : 1;

            Appointment appointment = new Appointment();

            Optional<Salon> salonModel = salonRepository.findById(salonId);
            Salon salon = new Salon();
            salon.setId(salonModel.get().getId());
            salon.setSalonName(salonModel.get().getSalonName());

            Optional<ServiceHair> hairServiceModel = serviceHairRepository.findById(serviceId);
            ServiceHair serviceHair = new ServiceHair();
            serviceHair.setId(hairServiceModel.get().getId());
            serviceHair.setServiceName(hairServiceModel.get().getServiceName());

            Optional<AppointmentStatus> appointmentStatusModel = appointmentStatusRepository.findById(1);
            AppointmentStatus appointmentStatus = new AppointmentStatus();
            appointmentStatus.setId(appointmentStatusModel.get().getId());
            appointmentStatus.setStatus(appointmentStatusModel.get().getStatus());

            Optional<User> user = userRepository.findById(userId);
            User newUser = new User();
            newUser.setId(user.get().getId());
            newUser.setUserName(user.get().getUsername());
            newUser.setRole(user.get().getRole());

            Optional<User> customer = userRepository.findById(customerId);
            User newCustomer = new User();
            newCustomer.setId(customer.get().getId());
            newCustomer.setUserName(customer.get().getUsername());
            newCustomer.setRole(customer.get().getRole());


            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalTime parsedTime = LocalTime.parse(appointmentTime, timeFormatter);
            LocalDate parsedDate = LocalDate.parse(appointmentDate, dateFormatter);
            appointment.setSalon(salon);
            appointment.setAppointmentDate(parsedDate);
            appointment.setAppointmentTime(parsedTime);
            appointment.setCustomer(newCustomer);
            appointment.setServiceHair(serviceHair);
            appointment.setAppointmentStatus(appointmentStatus);

            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            appointment.setUser(newUser);


            Appointment savedAppointment = appointmentRepository.save(appointment);

            String[] cc = {"n20dccn152@student.ptithcm.edu.vn"};

            if (savedAppointment.getAppointmentTime() != null) {
//                emailSendService.sendMail(customer.getEmail(), cc, "Lịch hẹn của bạn đã được " +
//                        "ghi lại", "Cảm ơn bé: " + customer.getCustomerName()  + " đã tin tưởng dịch vụ của chúng tôi." +
//                        " Vui lòng để ý điện thoại để nhận được những thông báo sớm nhất.");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("OK", "Successfully", ""));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Can not make an appointment", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "An error occurred", e.getMessage()));
        }
    }

    public ResponseEntity<ResponseObject> getAllByStatusId(Integer statusId) {
        Map<String, Object> results = new TreeMap<String, Object>();
        List<Appointment> appointmentList = null;
        appointmentList = appointmentRepository.findAppointmentByStatusId(statusId);
        results.put("appointmentList", appointmentList);

        if (results.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", results));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ResponseEntity<ResponseObject> getAllByCustomerId(Integer customerId) {
        List<Appointment> appointmentList = null;
        appointmentList = appointmentRepository.findAppointmentByCustomerId(customerId);

        List<AppointmentModel> appointmentModelList = appointmentList.stream()
                .map(appointment -> {
                    AppointmentModel appointmentModel = new AppointmentModel();
                    appointmentModel.setId(appointment.getId());
                    Optional<User> customer;
                    Optional<User> stylist;
                    customer = userRepository.findById(appointment.getCustomer().getId());
                    stylist = userRepository.findById(appointment.getUser().getId());
                    appointmentModel.setCustomerName(customer.get().getName());
                    appointmentModel.setAppointmentTime(appointment.getAppointmentTime());
                    appointmentModel.setAppointmentDate(appointment.getAppointmentDate());
                    appointmentModel.setSalonName(appointment.getSalon().getSalonName());
                    appointmentModel.setUserName(stylist.get().getName());
                    appointmentModel.setServiceName(appointment.getServiceHair().getServiceName());
                    appointmentModel.setStatus(appointment.getAppointmentStatus().getStatus());
                    appointmentModel.setSalonAddress(appointment.getSalon().getAddress());
                    return appointmentModel;
                }).collect(Collectors.toList());

        if (!appointmentModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", appointmentModelList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ResponseEntity<Object> updateStatusAppointment(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        int statusCode;
        int appointmentId;
        try {
            jsonNode = jsonMapper.readTree(json);
            appointmentId = jsonNode.get("appointmentId") != null ? jsonNode.get("appointmentId").asInt() : -1;
            statusCode = jsonNode.get("statusCode") != null ? jsonNode.get("statusCode").asInt() : -1;
            Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

            if (optionalAppointment.isPresent()) {
                Appointment appointment = optionalAppointment.get();

                AppointmentStatus appointmentStatus = new AppointmentStatus();
                appointmentStatus.setId(statusCode);

                appointment.setAppointmentStatus(appointmentStatus);
                appointmentRepository.save(appointment);
            }
            else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Have error when update status code appointment", ""));
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", ""));
    }


}
