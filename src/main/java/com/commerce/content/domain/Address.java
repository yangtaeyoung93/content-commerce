package com.commerce.content.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String state;
    private String city;
    private String street;
    private String zipcode;

    @Builder
    public Address(String state, String city, String street, String zipcode) {
        this.state = state;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
