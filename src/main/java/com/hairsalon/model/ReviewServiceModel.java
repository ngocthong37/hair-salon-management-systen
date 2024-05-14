package com.hairsalon.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewServiceModel {
    private Integer reviewServiceId;
    private String customerName;
    private String comment;
    private Integer rating;
    private LocalDateTime reviewDate;
}
