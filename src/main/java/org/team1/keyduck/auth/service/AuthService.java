package org.team1.keyduck.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.MemberCreateResponseDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.common.config.JwtUtil;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.service.CommonService;
import org.team1.keyduck.common.util.ErrorMessage;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import lombok.extern.slf4j.Slf4j;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final CommonService commonService;

    public SigninResponseDto login(SigninRequestDto signinRequest) {
        Member member = memberRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.INVALID_DATA_VALUE,
                        ErrorMessageParameter.EMAIL));

        if (member.isDeleted()) {
            throw new DataInvalidException(ErrorCode.DUPLICATE_DELETED,
                    ErrorMessageParameter.MEMBER);
        }

        commonService.comparePassword(signinRequest.getPassword(), member.getPassword());

        String bearerToken = jwtUtil.createToken(member.getId(), member.getMemberRole());

        return new SigninResponseDto(bearerToken);
    }

    public MemberCreateResponseDto joinMember(MemberCreateRequestDto requestDto,
            MemberRole memberRole) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_EMAIL,
                    ErrorMessageParameter.EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = Member.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .memberRole(memberRole)
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .build();

        return MemberCreateResponseDto.of(memberRepository.save(member));
    }

    public void verifyJwtToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String token = request.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(token);

        try {
            Claims claims = jwtUtil.extractClaims(jwt);

            if (claims == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        ErrorMessage.INVALID_TOKEN);
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.error(ErrorMessage.INVALID_TOKEN_SIGNATURE, e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    ErrorMessage.INVALID_TOKEN_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error(ErrorMessage.EXPIRED_TOKEN, e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error(ErrorMessage.UNSUPPORTED_TOKEN, e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    ErrorMessage.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            log.error(ErrorMessage.INTERNAL_SERVER_ERROR, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
