package com.sparta.instagram.controller;


import com.sparta.instagram.domain.Member;
import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.requestdto.MemberCheckupDto;
import com.sparta.instagram.domain.dto.requestdto.MemberRequestDto;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;
import com.sparta.instagram.domain.dto.responsedto.TokenDto;
import com.sparta.instagram.domain.dto.requestdto.TokenRequestDto;
import com.sparta.instagram.repository.MemberRepository;
import com.sparta.instagram.service.AuthService;
import com.sparta.instagram.service.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Api(tags = {"AUTH API"})
@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup") // 회원가입
    @ApiOperation(value = "회원가입 API", notes = "회원가입시 사용되는 API에 대한것들.")
    @ApiImplicitParam(name = "Dto", value = "회원가입시 사용하는 내용들")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    public boolean signup(@RequestBody MemberRequestDto memberRequestDto) {
        return authService.signup(memberRequestDto);
    }

    @ApiOperation(value = "중복검사 API", notes = "중복검사 후 true false 리턴")
    @ApiImplicitParam(name = "userId", value = "회원가입시 사용할 ID 중복검사 위해 입력")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @PostMapping("/checkup") // 중복체크
    public boolean checkup(@RequestBody MemberCheckupDto memberCheckupDto) {
        return authService.checkup(memberCheckupDto);
    }

    @ApiOperation(value = "로그인 API", notes = "중복검사 후 true false 리턴")
    @ApiImplicitParam(name = "Dto", value = "로그인시 ID,PW 입력")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })

    @PostMapping("/login") // 로그인
    public Optional<Member> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(memberRequestDto);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.setHeader("Access-Token-Expire-Time", String.valueOf(tokenDto.getAccessTokenExpiresIn()));
        return memberRepository.findByUserId(memberRequestDto.getUserId());
    }

    @ApiOperation(value = "토큰 재발급 API", notes = "토큰검사 후 access토큰 재발급 또는 예외처리 리턴")
    @ApiImplicitParam(name = "Refreshtoken", value = "재발급을 위해 검증할 refreshtoken 요청")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @PostMapping("/reissue")  //재발급을 위한 로직
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "검색 API", notes = "파라미터로 검색 내용을 전달하여 검색")
    @ApiImplicitParam(name = "condition", value = "userNic에서 쓰이는 내용들을 파라미터로 날리기")  // Swagger에 사용하는 파라미터에 대해 설명
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 400, message = "Request타입 에러"),
            @ApiResponse(code = 500, message = "서버 에러")
    })

    @Secured("Role_USER")
    @GetMapping("/auth/search")
    public List<MemberDto> getMember(MemberSearchCondition condition){
        return memberService.getMember(condition);
    }
}