package com.coopsystem.repository;

import com.coopsystem.domain.UGroup;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the UGroup entity.
 */
@SuppressWarnings("unused")
public interface UGroupRepository extends JpaRepository<UGroup,Long> {

    public UGroup findUGroupByName(String name);
}
