package com.misoweather.misoweatherservice.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    // TODO 토큰 시간 줄여야 된다.
    @Value("${app.auth.tokenSecret}")
    private String secretKey;
    private static final long TOKEN_VALID_TIME = 36000 * 60 * 1000L;
    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String memberId, String socialId, String socialType) {
        Claims claims = Jwts.claims().setSubject(memberId); // JWT payload 에 저장되는 정보단위
        claims.put("socialId", socialId); // 정보는 key / value 쌍으로 저장된다.
        claims.put("socialType", socialType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String serverToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(serverToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String serverToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(serverToken).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "TOKEN" : "TOKEN 값"
    //////// FrontEnd와 약속해서 일치시켜야 하는 부분 /////////'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("serverToken");
    }

    // 토큰의 유효성 + 만료일자 확인  // -> 토큰이 expire되지 않았는지 True/False로 반환해줌.
    public boolean validateToken(String serverToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(serverToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}