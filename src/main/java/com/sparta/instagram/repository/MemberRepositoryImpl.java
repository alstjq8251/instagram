package com.sparta.instagram.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instagram.domain.Member;
import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.Time;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;
import com.sparta.instagram.domain.dto.responsedto.MemberResponseDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.instagram.domain.QArticle.article;
import static com.sparta.instagram.domain.QMember.member;
import static com.sparta.instagram.domain.dto.Time.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<MemberDto> search(MemberSearchCondition condition) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(userNicEq(condition.getUserNic())).fetch();
        List <MemberDto> memberDtoList = new ArrayList<>();
        for(Member member: fetch) {
            memberDtoList.add(MemberDto.builder()
                    .userId(member.getUserId())
                    .userName(member.getUserName())
                    .userNic(member.getUserNic())
                    .id(member.getId())
                    .build());
        }
        return memberDtoList;


//        return queryFactory
//                .select(new QMemberTeamDto(
//                        member.id,
//                        member.username,
//                        member.age,
//                        team.id,
//                        team.name))
//                .from(member)
//                .leftJoin(member.team, team)
//                .where(usernameEq(condition.getUserId())
//                        )
//                .fetch();
    }


    private BooleanExpression userNicEq(String userNic) {
        return isEmpty(userNic) ? null : member.userNic.contains(userNic);
    }
}
