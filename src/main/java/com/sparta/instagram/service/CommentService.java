package com.sparta.instagram.service;

import com.sparta.instagram.domain.Article;
import com.sparta.instagram.domain.Comment;
//import com.sparta.instagram.domain.Heart;
import com.sparta.instagram.domain.dto.requestdto.CommentRequestDto;
import com.sparta.instagram.domain.dto.responsedto.CommentResponseDto;
import com.sparta.instagram.domain.dto.Time;
import com.sparta.instagram.domain.dto.responsedto.HeartResponseDto;
import com.sparta.instagram.repository.ArticleRepository;
import com.sparta.instagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public List<CommentResponseDto> readCommentall(UserDetails userDetails) {
        List<Comment> commentList = commentRepository.findAllByParent_IdIsNullOrderByModifiedAtDesc();
        String info[] = userDetails.getUsername().split(" ");
        boolean userFlag = false;
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            if(info[0].equals(comment.getUserId())){
                userFlag = true;
            }
            long count=0;
            Set<Map.Entry<String,Boolean>> entryset1 = comment.getHeartmap().entrySet();
            for(Map.Entry<String,Boolean> entryset2 : entryset1){
                if(entryset2.getValue() && entryset2.getKey().equals(info[0]))
                    count++;
            }
            commentResponseDtoList.add(CommentResponseDto.builder()
                    .id(comment.getId())
                    .createdAt(comment.getCreatedAt())
                    .articleid(comment.getArticle().getId())
                    .userId(comment.getUserId())
                    .userNic(comment.getUserNic())
                    .userName(comment.getUserNic())
                    .heartcnt(count)
                    .commentbabycnt(comment.getCommentbabylist().size())
                    .modifiedAt(comment.getModifiedAt())
                    .userFlag(userFlag)
                    .TimeMsg(Time.calculateTime(comment.getCreatedAt()))
                    .content(comment.getContent())
                    .build());
        }
        return commentResponseDtoList;
    }
    public List<CommentResponseDto> readbabyCommentAll(UserDetails userDetails) {
        List<Comment> commentList = commentRepository.findAllByParent_IdIsNotNullOrderByModifiedAtDesc();
        String info[] = userDetails.getUsername().split(" ");
        boolean userFlag = false;
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            if(info[0].equals(comment.getUserId())){
                userFlag=true;
            }
            long count=0;
            Set<Map.Entry<String,Boolean>> entryset1 = comment.getHeartmap().entrySet();
            for(Map.Entry<String,Boolean> entryset2 : entryset1){
                if(entryset2.getValue() && entryset2.getKey().equals(info[0]))
                    count++;
            }
            commentResponseDtoList.add(CommentResponseDto.builder()
                    .id(comment.getId())
                    .createdAt(comment.getCreatedAt())
                    .articleid(comment.getArticle().getId())
                    .userId(comment.getUserId())
                    .userNic(comment.getUserNic())
                    .userName(comment.getUserNic())
                    .heartcnt(count)
                    .commentbabycnt(comment.getCommentbabylist().size())
                    .modifiedAt(comment.getModifiedAt())
                    .userFlag(userFlag)
                    .TimeMsg(Time.calculateTime(comment.getCreatedAt()))
                    .content(comment.getContent())
                    .build());
        }
        return commentResponseDtoList;
    }

    public CommentResponseDto createComment(Long postid, CommentRequestDto commentRequestDto, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        Article article = articleRepository.findById(postid)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Map<String, Boolean> map = new HashMap() {{  // 용량을 동적으로 늘리는 Map을 만들어라 -> 순서가 유지안됨 iterator 넣은순서가유지가안됨!
            put(info[0], false);
        }};
        Comment comment = Comment.builder()
                .article(article)
                .userId(info[0])
                .userNic(info[1])
                .userName(info[2])
                .content(commentRequestDto.getContent())
                .heartmap(map)
                .build();
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .articleid(comment.getArticle().getId())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .userId(comment.getUserId())
                .userNic(comment.getUserNic())
                .userName(comment.getUserName())
                .content(comment.getContent())
                .userLike(comment.getHeartmap().get(info[0]))
                .userFlag(true)
                .TimeMsg(Time.calculateTime(comment.getCreatedAt()))
                .build();
        return commentResponseDto;
    }

    public CommentResponseDto createbabyComment(Long commentid, CommentRequestDto commentRequestDto, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        Comment comment = commentRepository.findById(commentid)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        Map<String, Boolean> map = new HashMap() {{  // 용량을 동적으로 늘리는 Map을 만들어라 -> 순서가 유지안됨 iterator 넣은순서가유지가안됨!
            put(info[0], false);
            put(info[1], false);
        }};
        Comment babyComment = Comment.builder()
                .parent(comment)
                .userId(info[0])
                .userNic(info[1])
                .userName(info[2])
                .heartmap(map)
                .content(commentRequestDto.getContent())
                .build();
        commentRepository.save(babyComment);
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .userId(comment.getUserId())
                .userNic(comment.getUserNic())
                .userName(comment.getUserName())
                .content(comment.getContent())
                .userLike(comment.getHeartmap().get(info[0]))
                .TimeMsg(Time.calculateTime(comment.getCreatedAt()))
                .userFlag(comment.getHeartmap().get(info[1]))
                .build();
        return commentResponseDto;
    }

    @Transactional
    public CommentResponseDto fixComment(Long commentid, CommentRequestDto commentRequestDto, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        Comment comment = commentRepository.findById(commentid)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        if(!info[0].equals(comment.getUserId())){
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }
        comment.fixComment(commentRequestDto.getContent());
        long count=0;
        Set<Map.Entry<String,Boolean>> entryset1 = comment.getHeartmap().entrySet();
        for(Map.Entry<String,Boolean> entryset2 : entryset1){
            if(entryset2.getValue() && entryset2.getKey().equals(info[0]))
                count++;
        }
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .articleid(comment.getArticle().getId())
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .heartcnt(count)
                .commentbabycnt(comment.getCommentbabylist().size())
                .userId(comment.getUserId())
                .userNic(comment.getUserNic())
                .userName(comment.getUserName())
                .content(comment.getContent())
                .userFlag(true)
                .TimeMsg(Time.calculateTime(comment.getCreatedAt()))
                .build();
        return commentResponseDto;
    }

    public boolean deleteComment(Long commentid, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        Comment comment = commentRepository.findById(commentid)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        if(!info[0].equals(comment.getUserId())){
            throw new IllegalArgumentException("작성자만 삭제 가능합니다.");
        }
        commentRepository.delete(comment);
        return true;
    }

    public HeartResponseDto addCommentHeart(Long id, UserDetails userDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        long count=0;
        String info[] = userDetails.getUsername().split(" ");
        comment.fixCommentMap(info[0], !comment.getHeartmap().get(info[0]));
        Set<Map.Entry<String,Boolean>> entryset1 = comment.getHeartmap().entrySet();
        for(Map.Entry<String,Boolean> entryset2 : entryset1){
            if(entryset2.getValue() && entryset2.getKey().equals(info[0]))
                count++;
        }
        return HeartResponseDto.builder()
                .likeCount(count)
                .likeFlag(comment.getHeartmap().get(info[0]))
                .build();
    }
}
