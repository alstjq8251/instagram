package com.sparta.instagram.service;


import com.sparta.instagram.domain.Member;
import com.sparta.instagram.domain.RefreshToken;
import com.sparta.instagram.domain.dto.requestdto.MemberCheckupDto;
import com.sparta.instagram.domain.dto.requestdto.MemberRequestDto;
import com.sparta.instagram.domain.dto.responsedto.TokenDto;
import com.sparta.instagram.domain.dto.requestdto.TokenRequestDto;
import com.sparta.instagram.jwt.Principaldetail;
import com.sparta.instagram.jwt.TokenProvider;
import com.sparta.instagram.repository.MemberRepository;
import com.sparta.instagram.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public boolean signup(MemberRequestDto memberRequestDto) {
        //if(!(Pattern.matches("[a-zA-Z0-9]*$",memberRequestDto.getUserNic()) && (memberRequestDto.getUserNic().length() >= 5 && memberRequestDto.getUserNic().length() <=15)
        //        && Pattern.matches("[a-z?A-Z+0-9+\\S]*$",memberRequestDto.getPassword()) && (memberRequestDto.getPassword().length() >= 8))
        //&&Pattern.matches("[a-zA-Z0-9]*$", memberRequestDto.getUserName()) && (memberRequestDto.getUserName().length() >=5 && memberRequestDto.getUserName().length() <= 15)){
        //    throw new IllegalArgumentException("아이디 혹은 닉네임 혹은 비밀번호 조건을 확인해주세요.");
        //}
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return true;
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
//        if (!memberRepository.existsByNickname(memberRequestDto.getNickname()) ||
//                !memberRepository.existsByPassword(passwordEncoder.encode(memberRequestDto.getPassword()))) {
//            throw new RuntimeException("사용자를 찾을 수 없습니다");
//        }
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

//            UserDetails userDetails = (UserDetails)
            Principaldetail principaldetail = (Principaldetail)authentication.getPrincipal();

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication,principaldetail.getMember());

            // 4. RefreshToken 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);


            // 5. 토큰 발급
            return tokenDto;
        } catch (Exception e){
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        Principaldetail principaldetail = (Principaldetail)authentication.getPrincipal();

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication,principaldetail.getMember());

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    public boolean checkup(MemberCheckupDto memberCheckupDto) {
        String userId = memberCheckupDto.getVal();
        if(memberRepository.findByUserId(userId).isPresent()) // 중복된 아이디 일 시 false
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        else
            return true;
    }
}