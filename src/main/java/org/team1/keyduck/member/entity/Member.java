package org.team1.keyduck.member.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.team1.keyduck.common.entity.BaseTime;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(nullable = false)),
            @AttributeOverride(name = "state", column = @Column(nullable = false)),
            @AttributeOverride(name = "street", column = @Column(nullable = false)),
            @AttributeOverride(name = "detailAddress1", column = @Column),
            @AttributeOverride(name = "detailAddress2", column = @Column)
    })
    private Address address;

    @Builder
    public Member(String name, String email, String password, MemberRole memberRole,
            Address address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
        this.address = address;
    }

    public void updateMember(MemberUpdateRequestDto requestDto) {
        if (requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            this.name = requestDto.getName();
        }
        if (requestDto.getEmail() != null) {

            if (this.email.equals(requestDto.getEmail())) {
                throw new DataInvalidException(ErrorCode.BEFORE_INFO_NOT_AVAILABLE,
                        ErrorMessageParameter.EMAIL);
            }

            Pattern pattern = Pattern.compile(
                    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
            if (!(pattern.matcher(requestDto.getEmail()).matches())) {
                throw new DataNotMatchException(ErrorCode.INVALID_DATA_VALUE,
                        ErrorMessageParameter.EMAIL);
            }
            this.email = requestDto.getEmail();

        }
        if (requestDto.getAddress() != null) {
            this.address = requestDto.getAddress();
        }
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void deleteMember() {
        this.isDeleted = true;
    }
}
