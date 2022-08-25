package com.sparta.instagram.repository;


import com.sparta.instagram.domain.DeletedUrlPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedUrlPathRepository extends JpaRepository<DeletedUrlPath, Long> {
}
