package com.commerce.content.domain;

import com.commerce.content.dto.AddUserRequest;
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

    public Address(AddUserRequest dto) {
        this.state = dto.getState();
        this.city = dto.getCity();
        this.street = dto.getStreet();
        this.zipcode = dto.getZipcode();
    }
}
