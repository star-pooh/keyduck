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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.team1.keyduck.common.entity.BaseTime;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
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
        @AttributeOverride(name = "city", column = @Column(name = "city", nullable = false)),
        @AttributeOverride(name = "state", column = @Column(name = "state", nullable = false)),
        @AttributeOverride(name = "street", column = @Column(name = "street", nullable = false)),
        @AttributeOverride(name = "detailAddress1", column = @Column(name = "detail_address1")),
        @AttributeOverride(name = "detailAddress2", column = @Column(name = "detail_address2"))

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

    public void updateUser(MemberUpdateRequestDto requestDto) {
        if (requestDto.getName() != null) {
            this.name = requestDto.getName();
        }
        if (requestDto.getEmail() != null) {
            this.email = requestDto.getEmail();
        }
        if (requestDto.getAddress() != null) {
            this.address = requestDto.getAddress();
        }
    }
}
