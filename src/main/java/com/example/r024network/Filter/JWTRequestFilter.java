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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 提取 Authorization 头
        String authHeader = request.getHeader("Authorization");

        // 如果没有 Authorization 头或格式不正确，返回 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        // 提取 JWT Token
        String token = authHeader.substring(7); // "Bearer ".length() = 7

        try {
            // 验证 JWT Token
            JWTTokenUtil.JwtValidationResult validationResult = jwtTokenUtil.validateJWT(token);

            // 检查 Token 是否有效
            if (!validationResult.isValid()) {
                sendUnauthorizedResponse(response, validationResult.getMessage());
                return;
            }

            // Token 有效，继续处理请求
            // 可以从 validationResult.getClaims() 中获取用户信息
            // 例如: String username = validationResult.getClaims().getSubject();

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // JWT 验证过程中发生错误
            logger.error("JWT verification error:", e);
            sendUnauthorizedResponse(response, "Token verification failed");
        }
    }

    /**
     * 发送未授权响应
     * @param response HTTP 响应
     * @param message 错误消息
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setHeader("WWW-Authenticate", "Bearer realm=\"User Visible Realm\", error=\"invalid_token\"");

        // 可选：返回 JSON 格式的错误消息
        String responseBody = String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", message);
        response.getWriter().write(responseBody);
    }

    /**
     * 配置不需要 JWT 验证的路径
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //String path = request.getServletPath();
        // 不需要 JWT 验证的路径（例如登录、公开API等）
        //return path.startsWith("/api/auth/") || path.startsWith("/public/");
        return true;
    }
}
