package org.team1.keyduck.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.id = :id and m.isDeleted = :isDeleted")
    Member findByIdAndIsDeleted(Long id, boolean isDeleted);

}
