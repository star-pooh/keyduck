package org.team1.keyduck.member.dto.request;

import lombok.Getter;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.util.ErrorCode;
import org.team1.keyduck.member.entity.Address;

@Getter
public class MemberUpdateRequestDto {

    private String name;

    private String email;

    private Address address;

    public void isAllFieldsEmpty() {
        if ((name == null || name.isBlank())
                && (email == null || email.isBlank())
                && address == null) {
            throw new DataInvalidException(ErrorCode.EMPTY_REQUEST_BODY, null);
        }
    }

}
