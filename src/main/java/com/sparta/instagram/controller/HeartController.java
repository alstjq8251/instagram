package com.sparta.instagram.controller;
//
//import com.sparta.instagram.service.HeartService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RequestMapping("/api/heart/auth/")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
//@RestController
//public class HeartController {
//    private final HeartService heartService;
//
//    @Secured("ROLE_USER")
//    @PostMapping("/{articleid}")
//    public boolean addLikeToMemo(@PathVariable Long articleid, @AuthenticationPrincipal UserDetails userDetails) {
//        return heartService.addLikeToMemo(articleid, userDetails);
//    }
//
//    @Secured("ROLE_USER")
//    @PostMapping("/comment/{commentId}")
//    public boolean addLikeToComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
//        return heartService.addLikeToComment(commentId, userDetails);
//    }
//}
