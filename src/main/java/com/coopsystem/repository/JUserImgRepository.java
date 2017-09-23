package com.coopsystem.repository;


import com.coopsystem.domain.JUserImg;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the JUserImg entity.
 */
@SuppressWarnings("unused")
public interface JUserImgRepository extends JpaRepository<JUserImg,Long> {

    @Transactional
    @Query("select jUserImg from JUserImg jUserImg join fetch jUserImg.juser inner join jUserImg.juser juser where juser.id =:id")
    JUserImg findJImgOfJUser(@Param("id") Long id);

    @Transactional
    JUserImg findJUserImgByJuserId(Long juserId);
}
