package com.coopsystem.repository;

import com.coopsystem.domain.ProjectImg;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the ProjectImg entity.
 */
@SuppressWarnings("unused")
public interface ProjectImgRepository extends JpaRepository<ProjectImg,Long> {

    @Transactional
    @Query("select projectImg from ProjectImg projectImg join fetch projectImg.project project where project.id = :id")
    ProjectImg findImgOfProject(@Param("id") Long id);
}
