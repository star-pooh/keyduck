package org.team1.keyduck.keyboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.keyboard.entity.Keyboard;

import java.util.List;

public interface KeyboardRepository extends JpaRepository<Keyboard, Long> {

    List<Keyboard> findAllBySellerId(Long sellerId);
}
