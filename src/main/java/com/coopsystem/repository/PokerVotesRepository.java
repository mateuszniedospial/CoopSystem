package com.coopsystem.repository;

import com.coopsystem.domain.PokerVotes;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the PokerVotes entity.
 */
@SuppressWarnings("unused")
public interface PokerVotesRepository extends JpaRepository<PokerVotes,Long> {

    List<PokerVotes> findPokerVotesByPokerId(Long pokerID);

    @Transactional
    void deleteByPokerId(Long pokerID);
}
