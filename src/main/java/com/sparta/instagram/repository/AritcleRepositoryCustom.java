package com.sparta.instagram.repository;

import com.sparta.instagram.domain.dto.ArticleSearchCondition;
import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.responsedto.ArticleResponseDto;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AritcleRepositoryCustom {
    Slice<ArticleResponseDto> searchScroll(Pageable pageable, UserDetails userDetails);

    List<ArticleResponseDto> search(ArticleSearchCondition condition, UserDetails userDetails);
}
