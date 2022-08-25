package com.sparta.instagram.repository;

import com.sparta.instagram.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> , AritcleRepositoryCustom{
    List<Article> findAllByUserId(String userId);

    List<Article> findAllByOrderByModifiedAtDesc();
}
