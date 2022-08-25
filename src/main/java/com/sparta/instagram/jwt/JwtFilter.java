package com.sparta.instagram.jwt;


import com.sparta.instagram.domain.Code;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 헤더에서 bearer 를 제외한 access 토큰을 빼와 jwt에 저장
        String jwt = resolveToken(request);

        // 토큰이 없을 때 entrypoint Code 클래스의 저장한 유형의 메세지를 entrypoint로 보내 처리
        if(jwt == null){
            request.setAttribute("exception", Code.UNKNOWN_ERROR.getCode());
        }

        // token이 있을 때 유효성 검사를 통과하면 인증 정보를 뽑고 contexthol
        try{
            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(ExpiredJwtException e) {
            request.setAttribute("exception", Code.EXPIRED_TOKEN.getCode());
        }catch(MalformedJwtException e) {
            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
        }catch(SignatureException e){
            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
        }
        filterChain.doFilter(request, response);
    }
    // Authorization 헤더에서 토큰을 빼
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

