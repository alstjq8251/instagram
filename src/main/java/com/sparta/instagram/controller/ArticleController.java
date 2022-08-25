package com.sparta.instagram.controller;


import com.sparta.instagram.domain.Article;
import com.sparta.instagram.domain.dto.requestdto.ArticleRequestDto;
import com.sparta.instagram.domain.dto.responsedto.ArticleResponseDto;
import com.sparta.instagram.domain.dto.responsedto.HeartResponseDto;
import com.sparta.instagram.repository.ArticleRepository;
import com.sparta.instagram.service.ArticleService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

@Api(tags = {"ARTICLE API"})
@RequiredArgsConstructor
@RequestMapping("/api/article")
@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleRepository articleRepository;

    @ApiOperation(value = "게시글 전체 조회 API", notes = "전체 조회 후 수정시간 기준 내림차순하여 리턴")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("/auth")
    public List<ArticleResponseDto> readArticle(@AuthenticationPrincipal UserDetails userDetails) {
        return articleService.readArticle(userDetails);
    }

    @ApiOperation(value = "게시글 작성 API", notes = "토큰 검사 후 게시글 작성")
    @ApiImplicitParam(name = "Dto", value = "게시글 작성할 이미지list및 내용 입력")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @Secured("ROLE_USER")
    @PostMapping(value = "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ArticleResponseDto CreateArticle(@RequestPart(value = "dto") ArticleRequestDto requestDto, // 예외처리를 위해 @Valid 어노테이션 제거
                                 @RequestPart(required = false) List<MultipartFile> multipartFile,// 예외처리를위해 required=false추가
                                 @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return articleService.CreateArticle(requestDto, multipartFile,userDetails);
    }

    @ApiOperation(value = "게시글 수정 API", notes = "토큰 검사 및 작성자 검사 후 게시글 수정")
    @ApiImplicitParam(name = "Dto", value = "게시글 수정시 사용할 이미지list및 내용 입력")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @Secured("ROLE_USER")
    @PatchMapping("/auth/{id}")
    public Article fixArticle(@PathVariable Long id, @RequestBody ArticleRequestDto articleRequestDto, @AuthenticationPrincipal UserDetails userDetails){
        return articleService.fixArticle(id, articleRequestDto, userDetails);
    }

    @ApiOperation(value = "게시글 삭제 API", notes = "토큰 검사 및 작성자 검사 후 게시글 삭제")
    @ApiImplicitParam(name = "token", value = "로그인 및 작성자 검사 위한 토큰 전달")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @Secured("ROLE_USER")
    @DeleteMapping("/auth/{id}")
    public boolean deleteArticle(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return articleService.deleteArticle(id, userDetails);
    }

    @ApiOperation(value = "게시글 좋아요 달기 API", notes = "토큰 검사 및 작성자 검사 후 게시글 좋아요 달기")
    @ApiImplicitParam(name = "Dto", value = "게시글 수정시 사용할 이미지list및 내용 입력")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @Secured("Role_USER")
    @PatchMapping("/auth/heart/{id}")
    public HeartResponseDto addArticleHeart(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return articleService.addArticleHeart(id,userDetails);
    }


    @Secured("Role_USER")
    @GetMapping("/auth/scroll")
    public Slice<ArticleResponseDto> scrollArticle(Pageable pageable, UserDetails userDetails){
        return articleRepository.searchScroll(pageable,userDetails);
    }

}
