package org.team1.keyduck.auth.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.PaymentFormRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.MemberCreateResponseDto;
import org.team1.keyduck.auth.dto.response.PaymentFormResponseDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.common.config.JwtUtil;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.util.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.common.service.CommonService;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final JwtBlacklistService jwtBlacklistService;
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

    public PaymentFormResponseDto paymentFormLogin(PaymentFormRequestDto dto) {
        Member member = memberRepository.findByEmailAndIsDeletedFalse(dto.getEmail())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.LOGIN_FAILED, null));

        if (member.isDeleted()) {
            throw new DataInvalidException(ErrorCode.DUPLICATE_DELETED,
                    ErrorMessageParameter.MEMBER);
        }

        if (member.getMemberRole().equals(MemberRole.SELLER)) {
            throw new OperationNotAllowedException(ErrorCode.FORBIDDEN_PAYMENT_LOGIN_FORM, null);
        }

        commonService.comparePassword(dto.getPassword(), member.getPassword());

        String bearerToken = jwtUtil.createToken(member.getId(), member.getMemberRole());

        return new PaymentFormResponseDto(bearerToken, dto.getAmount());
    }

    public void verifyJwtToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(token);
        Claims claims = jwtUtil.extractClaims(jwt);
        if (claims == null) {
            throw new DataInvalidException(ErrorCode.INVALID_TOKEN, ErrorMessageParameter.TOKEN);
        }

        MemberRole memberRole = MemberRole.valueOf(claims.get("memberRole", String.class));

        if (memberRole.equals(MemberRole.SELLER)) {
            throw new OperationNotAllowedException(ErrorCode.FORBIDDEN_PAYMENT_LOGIN_FORM, null);
        }
    }
}
