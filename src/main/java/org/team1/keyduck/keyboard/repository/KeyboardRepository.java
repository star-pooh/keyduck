package org.team1.keyduck.keyboard.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.keyboard.entity.Keyboard;

import java.util.List;

public interface KeyboardRepository extends JpaRepository<Keyboard, Long> {

    @Query("SELECT k FROM Keyboard k WHERE k.id = :keyboardId AND k.isDeleted = false")
    Optional<Keyboard> findByIdAndIsDeletedFalse(Long keyboardId);

    // 조회시 생성일 기준 내림차순 정렬
    List<Keyboard> findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(Long sellerId);

}
