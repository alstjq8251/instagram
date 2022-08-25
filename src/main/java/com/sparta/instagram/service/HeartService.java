package com.sparta.instagram.service;

//import com.sparta.instagram.domain.Article;
//import com.sparta.instagram.domain.Comment;
//import com.sparta.instagram.domain.Heart;
//import com.sparta.instagram.repository.ArticleRepository;
//import com.sparta.instagram.repository.CommentRepository;
//import com.sparta.instagram.repository.HeartRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class HeartService {
//
//    private final HeartRepository heartRepository;
//    private final ArticleRepository articleRepository;
//    private final CommentRepository commentRepository;
//
//    public boolean addLikeToMemo(Long articleid, UserDetails userDetails) {
//        String info[] = userDetails.getUsername().split(" ");
//        Article article = articleRepository.findById(articleid)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
//        if(heartRepository.findByUserIdAndArticle(info[0],article).isPresent()){
//            heartRepository.delete(heartRepository.findByUserIdAndArticle(info[0],article).get());
//            return false;
//        }
//        else {
//            Heart heart = Heart.builder()
//                    .article(article)
//                    .userId(info[0])
//                    .build();
//            heartRepository.save(heart);
//        }
//            return true;
//    }
//
//    public boolean addLikeToComment(Long commentId, UserDetails userDetails) {
//        String info[] = userDetails.getUsername().split(" ");
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
//        if(heartRepository.findByUserIdAndComment(info[0],comment).isPresent()){
//            heartRepository.delete(heartRepository.findByUserIdAndComment(info[0],comment).get());
//            return false;
//        }
//        else {
//            Heart heart = Heart.builder()
//                    .userId(info[0])
//                    .comment(comment)
//                    .build();
//            heartRepository.save(heart);
//            return true;
//        }
//    }
//}
