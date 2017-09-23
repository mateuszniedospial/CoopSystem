package com.coopsystem.repository;

import com.coopsystem.domain.JUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the JUser entity.
 */
@SuppressWarnings("unused")
public interface JUserRepository extends JpaRepository<JUser,Long> {

    @Query("select jUser from JUser jUser where jUser.user.id=:id")
    JUser findJUserUsingJhiUser(@Param("id") Long id);

}
