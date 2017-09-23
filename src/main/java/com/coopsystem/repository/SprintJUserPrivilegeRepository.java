package com.coopsystem.repository;

import com.coopsystem.domain.SprintJUserPrivilege;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the SprintJUserPrivilege entity.
 */
@SuppressWarnings("unused")
public interface SprintJUserPrivilegeRepository extends JpaRepository<SprintJUserPrivilege,Long> {

}
