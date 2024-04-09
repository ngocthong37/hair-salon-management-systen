package com.hairsalon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AppointmentModel {
    private Integer id;
    private String customerName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String serviceName;
    private String status;
    private String salonName;
    private String userName;
    private String salonAddress;
}
