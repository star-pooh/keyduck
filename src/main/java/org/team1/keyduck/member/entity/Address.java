package org.team1.keyduck.member.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Embeddable
public class Address {

    @NotBlank(message = "시는 필수 입력 값입니다.")
    private String city;

    @NotBlank(message = "군/구는 필수 입력 값입니다.")
    private String state;

    @NotBlank(message = "읍/면/동은 필수 입력 값입니다.")
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
