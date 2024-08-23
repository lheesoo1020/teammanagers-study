package kr.teammanagers.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.application.module.MemberModuleService;
import kr.teammanagers.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${TOKEN_SECRET}")
    private String secretKeyString;
    private SecretKey secretKey;

    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private long accessTokenExpireTime;

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long refreshTokenExpireTime;

    private static final String KEY_ROLE = "role";
    private final MemberModuleService memberModuleService;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenExpireTime);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshTokenExpireTime);
    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        String username = getUsername(authentication);

        return Jwts.builder()
                .setSubject(username)
                .claim(KEY_ROLE, authorities)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        Member member = memberModuleService.findMemberByProviderId(claims.getSubject())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        PrincipalDetails principal = PrincipalDetails.builder()
                .member(member)
                .build();
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLE).toString()));
    }

    public String reissueAccessToken(String refreshToken) {
        if (StringUtils.hasText(refreshToken)) {
            if (validateToken(refreshToken)) {
                return generateAccessToken(getAuthentication(refreshToken));
            }
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new GeneralException(ErrorStatus.AUTH_INVALID_TOKEN);
        } catch (SecurityException e) {
            throw new GeneralException(ErrorStatus.AUTH_INVALID_JWT_SIGNATURE);
        }
    }

    // authentication.getPrincipal();시 가져오는 클래스형에 따라 형변환 진행 후 ProviderId를 리턴.
    private String getUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
            return ((PrincipalDetails) principal).getUsername();
        } else if (principal instanceof User) {
            return ((User) principal).getUsername();
        } else {
            throw new IllegalArgumentException("Unexpected principal type: " + principal.getClass().getName());
        }
    }
}