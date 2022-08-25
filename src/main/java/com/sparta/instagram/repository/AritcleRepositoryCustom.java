package com.sparta.instagram.repository;

import com.sparta.instagram.domain.dto.responsedto.ArticleResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;

public interface AritcleRepositoryCustom {
    Slice<ArticleResponseDto> searchScroll(Pageable pageable, UserDetails userDetails);
}
