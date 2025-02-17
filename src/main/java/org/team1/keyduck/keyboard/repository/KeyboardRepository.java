package org.team1.keyduck.keyboard.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.keyboard.entity.Keyboard;

public interface KeyboardRepository extends JpaRepository<Keyboard, Long> {

    List<Keyboard> findAllByMemberIdAndIsDeletedFalse(Long sellerId);
}
