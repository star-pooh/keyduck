package org.team1.keyduck.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
