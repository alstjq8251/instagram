package com.sparta.instagram.jwt;


import com.sparta.instagram.domain.Code;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = request.getAttribute("exception").toString();

        if(exception == null){
            setResponse(response, Code.UNKNOWN_ERROR);
        }
        else if(exception.equals(Code.UNKNOWN_ERROR.getCode())){
            setResponse(response, Code.UNKNOWN_ERROR);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(Code.WRONG_TYPE_TOKEN.getCode())) {
            setResponse(response, Code.WRONG_TYPE_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception.equals(Code.EXPIRED_TOKEN.getCode())) {
            setResponse(response, Code.EXPIRED_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(Code.UNSUPPORTED_TOKEN.getCode())) {
            setResponse(response, Code.UNSUPPORTED_TOKEN);
        }
        else {
            setResponse(response, Code.ACCESS_DENIED);
        }
    }



    //401상태 에러 http json 타입으로 만들어 리턴
    public void setResponse(HttpServletResponse response, Code code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // response에 한글 메세지를 담기위해 JSONObject 선언
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", code.getMessage()); // 넘어온 code의 메세지 입력
        responseJson.put("code", code.getCode());
        if(code.getCode().equals("1004")) // 해당 코드에 맞춰 상태메세지 200 변환
            response.setStatus(HttpServletResponse.SC_OK);
        else if(code.getCode().equals("1003"))
            response.setStatus(HttpServletResponse.SC_OK);
        else if(code.getCode().equals("1005"))
            response.setStatus(HttpServletResponse.SC_OK);
        else if(code.getCode().equals("1006"))
            response.setStatus(HttpServletResponse.SC_OK);
        else if(code.getCode().equals("1007"))
            response.setStatus(HttpServletResponse.SC_OK);
        // response의 header와 body담아 리턴
        response.getWriter().print(responseJson);
    }
}