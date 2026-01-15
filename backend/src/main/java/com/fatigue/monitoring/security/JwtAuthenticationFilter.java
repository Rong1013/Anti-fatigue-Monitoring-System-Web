package com.fatigue.monitoring.security;

import com.fatigue.monitoring.common.constant.JwtConstant;
import com.fatigue.monitoring.entity.User;
import com.fatigue.monitoring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 * 拦截请求，验证JWT令牌
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            
            if (StringUtils.hasText(jwt) && jwtTokenUtil.validateToken(jwt, getUserDetails(jwt))) {
                String username = jwtTokenUtil.getUsernameFromToken(jwt);
                
                UserDetails userDetails = getUserDetails(jwt);
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("无法设置用户认证: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中解析JWT令牌
     *
     * @param request 请求
     * @return JWT令牌
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(JwtConstant.HEADER);
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(JwtConstant.TOKEN_PREFIX)) {
            return headerAuth.substring(JwtConstant.TOKEN_PREFIX.length());
        }
        
        return null;
    }

    /**
     * 根据JWT令牌获取用户详情
     *
     * @param jwt JWT令牌
     * @return 用户详情
     */
    private UserDetails getUserDetails(String jwt) {
        String username = jwtTokenUtil.getUsernameFromToken(jwt);
        User user = userService.selectByUsername(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
