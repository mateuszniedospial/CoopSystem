package com.coopsystem.repository;

import com.coopsystem.domain.JGroupImg;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the JGroupImg entity.
 */
@SuppressWarnings("unused")
public interface JGroupImgRepository extends JpaRepository<JGroupImg,Long> {

    @Transactional
    @Query("select jGroupImg from JGroupImg jGroupImg join fetch jGroupImg.jgroup inner join jGroupImg.jgroup jgroup where jgroup.id =:id")
    JGroupImg findJImgOfJGroup(@Param("id") Long id);

}
