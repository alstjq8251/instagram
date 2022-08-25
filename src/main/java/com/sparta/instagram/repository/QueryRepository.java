package com.sparta.instagram.repository;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class QueryRepository {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    public QueryRepository(JPAQueryFactory jpaQueryFactory) {
//        this.jpaQueryFactory = jpaQueryFactory;
//    }
//
//    public List<Post> findAllPostsInnerFetchJoin() {
//        return jpaQueryFactory.selectFrom(post)
//                .innerJoin(post.comments)
//                .fetchJoin()
//                .fetch();
//    }
//    Page
//
//    public List<Orphan> findALlOrphans() {
//        return jpaQueryFactory.selectFrom(orphan)
//                .distinct()
//                .leftJoin(orphan.parent).fetchJoin()
//                .where(orphan.name.contains("abc"))
//                .fetch();
//    }
//}