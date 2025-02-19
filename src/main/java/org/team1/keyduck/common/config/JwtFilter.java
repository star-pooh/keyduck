package org.team1.keyduck.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.auth.service.JwtBlacklistService;
import org.team1.keyduck.common.util.ErrorMessage;
import org.team1.keyduck.member.entity.MemberRole;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (url.startsWith("/api/auth") || url.endsWith(".html") || url.startsWith("style")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = jwtUtil.getToken(httpRequest);

        if (bearerJwt == null) {
            // 토큰이 없는 경우 400을 반환합니다.
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.REQUIRED_TOKEN);
            return;
        }

        if (jwtBlacklistService.isBlacklisted(bearerJwt)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    ErrorMessage.NOT_AVAILABLE_TOKEN_DELETE_MEMBER);
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        ErrorMessage.INVALID_TOKEN);
                return;
            }

            Long userId = Long.parseLong(claims.getSubject());
            MemberRole memberRole = MemberRole.valueOf(claims.get("memberRole", String.class));

            AuthMember authMember = new AuthMember(userId, memberRole);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(authMember, null,
                            authMember.getAuthorities()));

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error(ErrorMessage.INVALID_TOKEN_SIGNATURE, e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    ErrorMessage.INVALID_TOKEN_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error(ErrorMessage.EXPIRED_TOKEN, e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error(ErrorMessage.UNSUPPORTED_TOKEN, e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    ErrorMessage.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            log.error(ErrorMessage.INTERNAL_SERVER_ERROR, e);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
