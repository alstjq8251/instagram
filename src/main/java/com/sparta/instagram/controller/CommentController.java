package com.sparta.instagram.controller;


import com.sparta.instagram.domain.dto.requestdto.CommentRequestDto;
import com.sparta.instagram.domain.dto.responsedto.CommentResponseDto;
import com.sparta.instagram.domain.dto.responsedto.HeartResponseDto;
import com.sparta.instagram.repository.CommentRepository;
import com.sparta.instagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
@RestController
public class CommentController {
    private final CommentService commentService;

    private final CommentRepository commentRepository;

    @GetMapping("/auth/all")
    public List<CommentResponseDto> readCommentall(@AuthenticationPrincipal UserDetails userDetails){
        return commentService.readCommentall(userDetails);
    }

    @GetMapping("/auth/baby/all")
    public List<CommentResponseDto> readbabycommentall(@AuthenticationPrincipal UserDetails userDetails){
        return commentService.readbabyCommentAll(userDetails);
//        return commentRepository.findAllByParent_IdIsNotNullOrderByModifiedAtDesc();
    }

    @Secured("ROLE_USER")
    @PostMapping("/auth/{postid}")
    public CommentResponseDto createComment(@PathVariable Long postid, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetails userDetails){
        return commentService.createComment(postid, commentRequestDto,userDetails);
    }

    @Secured("ROLE_USER")
    @PostMapping("/auth/baby/{commentid}")
    public CommentResponseDto createbabyComment(@PathVariable Long commentid, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetails userDetails){
        return commentService.createbabyComment(commentid , commentRequestDto, userDetails);
    }

    @Secured("ROLE_USER")
    @PatchMapping("/auth/{commentid}")
    public CommentResponseDto fixComment(@PathVariable Long commentid,@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetails userDetails){
        return commentService.fixComment(commentid,commentRequestDto, userDetails);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/auth/{commentid}")
    public boolean deleteComment(@PathVariable Long commentid, @AuthenticationPrincipal UserDetails userDetails){
        return commentService.deleteComment(commentid, userDetails);
    }

    @Secured("Role_USER")
    @PatchMapping("auth/heart/{id}")
    public HeartResponseDto addArticleHeart(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return commentService.addCommentHeart(id,userDetails);
    }
}
