package kr.teammanagers.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.auth.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static kr.teammanagers.auth.constant.AuthConstant.ACCESS_TOKEN_CONSTANT;
import static kr.teammanagers.auth.constant.AuthConstant.IS_NEW_USER_CONSTANT;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Value("${url.redirect}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = tokenProvider.generateAccessToken(authentication);
        tokenProvider.generateRefreshToken(authentication);

        response.sendRedirect(
                UriComponentsBuilder.fromUriString(redirectUrl)
                        .queryParam(ACCESS_TOKEN_CONSTANT, accessToken)
                        .queryParam(IS_NEW_USER_CONSTANT, principalDetails.isNewUser())
                        .build().toUriString()
        );
    }
}