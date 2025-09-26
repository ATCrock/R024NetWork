package com.example.r024network.Filter;

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
            return;
        }

        // 提取 Authorization 头
        String authHeader = request.getHeader("Authorization");

        // 如果没有 Authorization 头或格式不正确
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, "Missing or invalid Authorization header");
            return;
        }

        // 提取 JWT Token
        String token = authHeader.substring(7);

        try {
            // 验证 JWT Token
            JWTTokenUtil.JwtValidationResult validationResult = jwtTokenUtil.validateJWT(token);

            if (!validationResult.isValid()) {
                sendUnauthorizedResponse(response, validationResult.getMessage());
                return;
            }

            // Token有效，将用户信息存入请求属性
            Integer userAccount = Integer.valueOf(jwtTokenUtil.getUsernameFromToken(token));
            Integer userId = jwtTokenUtil.getUserIdFromToken(token);

            request.setAttribute("user_account", userAccount);
            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("JWT verification error:", e);
            sendUnauthorizedResponse(response, "Token verification failed");
        }
    }

    /**
     * 配置不需要JWT验证的路径
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 公开接口不需要验证
        return path.startsWith("/apifox/user/login") ||
                path.startsWith("/apifox/user/register") ||
                path.startsWith("/swagger") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars/");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String responseBody = String.format("{\"code\": 401, \"message\": \"%s\"}", message);
        response.getWriter().write(responseBody);
    }
}