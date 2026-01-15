package com.fatigue.monitoring.security;

import com.fatigue.monitoring.entity.User;
import com.fatigue.monitoring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务实现类
 * 实现Spring Security的UserDetailsService接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        if (!"1".equals(user.getStatus())) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 添加角色权限
        if (user.getRoles() != null) {
            for (String role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        
        // 添加菜单权限
        if (user.getPermissions() != null) {
            for (String permission : user.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(!"1".equals(user.getStatus()))
                .build();
    }
}
