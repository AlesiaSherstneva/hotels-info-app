package com.example.hotelsinfoapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Contacts {
    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;
}