package com.coopsystem.repository;

import com.coopsystem.domain.Comment;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Transactional
    List<Comment> findCommentsByTaskId(Long taskId);
}
