package com.sparta.instagram.service;


import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;
import com.sparta.instagram.domain.dto.responsedto.MemberResponseDto;
import com.sparta.instagram.repository.MemberRepository;
import com.sparta.instagram.config.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String userId) {
        return memberRepository.findByUserId(userId)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저 정보가 없습니다."));
    }

    public List<MemberDto> getMember(MemberSearchCondition condition, UserDetails userDetails) {
        return memberRepository.search(condition);
    }
}
