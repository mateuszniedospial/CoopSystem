package com.coopsystem.repository;

import com.coopsystem.domain.JoinJGroupRequest;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the JoinJGroupRequest entity.
 */
@SuppressWarnings("unused")
public interface JoinJGroupRequestRepository extends JpaRepository<JoinJGroupRequest,Long> {

    @Transactional
    @Query("select requests from JoinJGroupRequest requests left join fetch requests.jGroup jgroup where jgroup.id =:id")
    List<JoinJGroupRequest> findAllRequestsToGroup(@Param("id") Long id);

}
