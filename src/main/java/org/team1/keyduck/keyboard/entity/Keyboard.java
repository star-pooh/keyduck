package org.team1.keyduck.keyboard.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.team1.keyduck.common.entity.BaseTime;
import org.team1.keyduck.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "keyboard")
public class Keyboard extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String name;

//    private String imageUrl;
// TODO 확인해주세요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Builder
    public Keyboard(Member member, String name) {
        this.member = member;
        this.name = name;
    }

}
