package com.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ReviewServiceHair extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "rating")
    private Integer ratingValue;
    @Column(name = "comment")
    private String comment;


    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "service_id")
    private ServiceHair serviceHair;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private User customer;
}
