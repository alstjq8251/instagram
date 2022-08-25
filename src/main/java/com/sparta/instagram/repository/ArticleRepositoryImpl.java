package com.sparta.instagram.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instagram.domain.Article;
import com.sparta.instagram.domain.Image;
import com.sparta.instagram.domain.QArticle;
import com.sparta.instagram.domain.dto.Time;
import com.sparta.instagram.domain.dto.responsedto.ArticleResponseDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sparta.instagram.domain.QArticle.*;

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

    //    public Slice<BakeryMenuResponse> findBakeryMenuPageableByBakeryId(Long bakeryId, Pageable pageable) {
//        List<BakeryMenuResponse> bakeryMenuResponseList = jpaQueryFactory
//                .select(Projections.fields(BakeryMenuResponse.class,
//                        menuReviews.menus.id.as("menuId"),
//                        ...
//        menuReviews.rating.avg().as("avgRating")))
//                .from(menuReviews)
//                .where(menuReviews.bakeries.id.eq(bakeryId))
//                .groupBy(menuReviews.menus.id, menuReviews.menus.breadCategories.id, menuReviews.menus.breadCategories.name, menuReviews.menus.name, menuReviews.menus.price, menuReviews.menus.imgPath)
//                .orderBy(menuReviews.id.count().desc(), menuReviews.rating.avg().desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize() + 1) // limit보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true를 넣어 알림
//                .fetch();
//
//        List<BakeryMenuResponse> content = new ArrayList<>();
//        for (BakeryMenuResponse eachBakeryMenuResponse: bakeryMenuResponseList) {
//            content.add(new BakeryMenuResponse(eachBakeryMenuResponse));
//
//        }
//
//        boolean hasNext = false;
//        if (content.size() > pageable.getPageSize()) {
//            content.remove(pageable.getPageSize());
//            hasNext = true;
//        }
//        return new SliceImpl<>(content, pageable, hasNext);
//    }
}
