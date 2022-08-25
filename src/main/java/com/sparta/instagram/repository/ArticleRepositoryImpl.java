package com.sparta.instagram.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instagram.domain.Article;
import com.sparta.instagram.domain.Image;
import com.sparta.instagram.domain.Member;
import com.sparta.instagram.domain.QArticle;
import com.sparta.instagram.domain.dto.ArticleSearchCondition;
import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.Time;
import com.sparta.instagram.domain.dto.responsedto.ArticleResponseDto;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.sparta.instagram.domain.QArticle.*;
import static com.sparta.instagram.domain.QMember.member;
import static org.springframework.util.ObjectUtils.isEmpty;

public class ArticleRepositoryImpl implements AritcleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<ArticleResponseDto> searchScroll(Pageable pageable, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        long count=0;
        boolean userFlag=false;
        QueryResults<Article> result = queryFactory
                .selectFrom(article)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(article.id.desc())
                .fetchResults();

        List<ArticleResponseDto> dto = new ArrayList<>();

        for (Article article : result.getResults()) {
            List<String> imgUrl = article.getImageList()
                    .stream()
                    .map(Image::getImgUrl)
                    .collect(Collectors.toList());
            if(info[0].equals(article.getUserId())){
                userFlag=true;
            }
            Set<Map.Entry<String,Boolean>> entryset1 = article.getHeartmap().entrySet();
            for(Map.Entry<String,Boolean> entryset2 : entryset1){
                if(entryset2.getValue())
                    count++;
            }
            dto.add(ArticleResponseDto.builder()
                    .id(article.getId())
                    .TimeMsg(Time.calculateTime(article.getCreatedAt()))
                    .userId(article.getUserId())
                    .userNic(article.getUserNic())
                    .userName(article.getUserName())
                    .createdAt(article.getCreatedAt())
                    .modifiedAt(article.getModifiedAt())
                    .ImgUrl(imgUrl)
                    .heartcnt(count)
                    .commentcnt(article.getCommentList().size())
                    .userLike(article.getHeartmap().get(info[0]))
                    .userFlag(userFlag)
                    .build());
        }
        boolean hasNext = false;
        if (dto.size() > pageable.getPageSize()) {
            dto.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(dto, pageable, hasNext);
    }

    @Override
    public List<ArticleResponseDto> search(ArticleSearchCondition condition, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        List<Article> fetch = queryFactory
                .selectFrom(article)
                .where(userNicEq(condition.getContent())).fetch();
        List <ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        for(Article article1: fetch) {
            List<String> imgUrl = article1.getImageList()
                    .stream()
                    .map(Image::getImgUrl)
                    .collect(Collectors.toList());
            long count=0;
            boolean userFlag = false;
            boolean userLike = false;
            if(article1.getHeartmap().size()>0) {
                Set<Map.Entry<String, Boolean>> entryset1 = article1.getHeartmap().entrySet();
                for (Map.Entry<String, Boolean> entryset2 : entryset1) {
                    if (entryset2.getValue())
                        count++;
                }
            }
            if(info[0].equals(article1.getUserId())){
                userFlag=true;
            }
            if(article1.getHeartmap().containsKey(info[0])){
                userLike = article1.getHeartmap().get(info[0]);
            }
            articleResponseDtoList.add(ArticleResponseDto.builder()
                            .ImgUrl(imgUrl)
                            .id(article1.getId())
                            .userId(article1.getUserId())
                            .userNic(article1.getUserNic())
                            .TimeMsg(Time.calculateTime(article1.getCreatedAt()))
                            .createdAt(article1.getCreatedAt())
                            .modifiedAt(article1.getModifiedAt())
                            .content(article1.getContent())
                            .imageList(article1.getImageList())
                            .heartcnt(count)
                            .userLike(userLike)
                            .userFlag(userFlag)
                            .commentcnt(article1.getCommentList().size())
                    .build());
        }
        return articleResponseDtoList.stream().sorted(Comparator.comparing(ArticleResponseDto::getModifiedAt).reversed()).collect(Collectors.toList());



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

    private BooleanExpression userNicEq(String content) {
        return isEmpty(content) ? null : article.content.contains(content);
    }

}
