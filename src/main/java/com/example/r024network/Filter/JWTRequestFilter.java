package com.example.r024network.Filter;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Helper.ResultHelper;
import com.example.r024network.jwt.JWTTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Resource
    private JWTTokenUtil jwtTokenUtil;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 如果是公开接口，直接放行
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
        }
        String authHeader = request.getHeader("Authorization");
        // 如果没有Authorization或格式不正确
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, 415, "jwt不存在或jwt格式错误");
            throw new APIException(415, "jwt不存在或jwt格式错误");
        }else{
        // 提取JWT Token
        String token = authHeader.substring(7);
        // 验证JWT Token
        JWTTokenUtil.JwtValidationResult validationResult = jwtTokenUtil.validateJWT(token);

        if (validationResult == null || !validationResult.valid()) {
            sendUnauthorizedResponse(response, 414, "jwt已过期");
        }
        else {
            // Token有效，将用户信息存入请求属性
            Integer userAccount = Integer.valueOf(jwtTokenUtil.getUsernameFromToken(token));
            Integer userId = jwtTokenUtil.getUserIdFromToken(token);
            request.setAttribute("user_account", userAccount);
            request.setAttribute("userId", userId);
            filterChain.doFilter(request, response);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 公开接口不需要验证
        return path.startsWith("/apifox/user/login") ||
                path.startsWith("/apifox/user/register");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, Integer errCode, String message) throws IOException {
        // 处理前端响应并返回无响应头报错
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String responseBody = String.format("{\"code\": %d, \"message\": \"%s\"}", errCode, message);
        response.getWriter().write(responseBody);
    }
}