package com.sparta.instagram.repository;


import com.sparta.instagram.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{
    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserName(String name);
}
