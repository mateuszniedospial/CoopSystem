package com.coopsystem.repository;

import com.coopsystem.domain.JGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the JGroup entity.
 */
@SuppressWarnings("unused")
public interface JGroupRepository extends JpaRepository<JGroup,Long> {

    @Transactional
    @Query("select distinct jGroup from JGroup jGroup left join fetch jGroup.jusers")
    List<JGroup> findAllWithEagerRelationships();

    @Transactional
    @Query("select jGroup from JGroup jGroup left join fetch jGroup.jusers where jGroup.id =:id")
    JGroup findOneWithEagerRelationships(@Param("id") Long id);

    @Transactional
    @Query("select distinct jGroup from JGroup jGroup left join fetch jGroup.jusers inner join jGroup.jusers jusers where jusers.id in (:id)")
    List<JGroup> findAllJUserJGroups(@Param("id") Long id);

    @Transactional
    @Query("select distinct jGroup from JGroup jGroup left join fetch jGroup.jusers   where (select jUser from JUser jUser where jUser.id=:id) member of jGroup.jusers ")
    List<JGroup> findJGroupsByJUserId(@Param("id")Long id);
}
