package com.coopsystem.repository;

import com.coopsystem.domain.Poker;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Poker entity.
 */
@SuppressWarnings("unused")
public interface PokerRepository extends JpaRepository<Poker,Long> {

    Poker findTaskByTaskId(Long taskID);
}
