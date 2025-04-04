package com.GDG.Festi.domain.user;

import com.GDG.Festi.domain.user.dto.request.LoginRequestDTO;
import com.GDG.Festi.jwtUtil.JwtToken;
import com.GDG.Festi.jwtUtil.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken login(LoginRequestDTO loginRequestDTO) {
        try {
            // 1. username + password 를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
            log.info("authenticationToken : " + authenticationToken);

            // 2. 실제 검증 authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.info("authentication : " + authentication);

            // TODO. DTO로 반환되게 바꾸기
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            return jwtTokenProvider.generateToken(authentication);
        } catch (Exception e) {
            log.error("login Exception : ", e);
            throw new RuntimeException("로그인 실패", e);
        }
    }
}