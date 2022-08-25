package com.sparta.instagram.repository;

import com.sparta.instagram.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByParent_IdIsNullOrderByModifiedAtDesc();

    List<Comment> findAllByParent_IdIsNotNullOrderByModifiedAtDesc();
}
