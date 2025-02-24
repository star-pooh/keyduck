package org.team1.keyduck.member.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
@Embeddable
public class Address {

    @NotBlank(message = ValidationErrorMessage.CITY_IS_NOT_NULL)
    private String city;

    @NotBlank(message = ValidationErrorMessage.STATE_IS_NOT_NULL)
    private String state;

    @NotBlank(message = ValidationErrorMessage.STREET_IS_NOT_NULL)
    private String street;

    private String detailAddress1;

    private String detailAddress2;

    public Address() {
    }

    public Address(String city, String state, String street, String detailAddress1,
            String detailAddress2) {
        this.city = city;
        this.state = state;
        this.street = street;
        this.detailAddress1 = detailAddress1;
        this.detailAddress2 = detailAddress2;
    }
}
