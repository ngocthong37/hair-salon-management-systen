package com.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ServiceHair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "price")
    private Double price;
    @Column(name = "description")
    private String description;
    @Column(name = "url")
    private String url;
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "serviceHair", fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Collection<Appointment> appointments;

    @JsonIgnore
    @OneToMany(mappedBy = "serviceHair", fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Collection<ReviewServiceHair> reviews;
}
