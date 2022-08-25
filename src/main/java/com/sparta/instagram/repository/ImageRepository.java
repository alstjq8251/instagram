package com.sparta.instagram.repository;

import com.sparta.instagram.domain.Article;
import com.sparta.instagram.domain.Image;
import com.sparta.instagram.service.ArticleService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByArticle(Article article);
}
