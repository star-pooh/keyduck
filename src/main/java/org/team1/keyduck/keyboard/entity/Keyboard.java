package org.team1.keyduck.keyboard.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column
    private String description;

//    private String imageUrl;
// TODO 확인해주세요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Builder
    public Keyboard(Member member, String name, String description) {
        this.member = member;
        this.name = name;
        this.description = description;
    }
}
