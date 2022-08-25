package com.sparta.instagram.repository;

import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberDto> search(MemberSearchCondition condition);
}
