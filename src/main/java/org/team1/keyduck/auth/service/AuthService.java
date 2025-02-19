package org.team1.keyduck.auth.service;

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
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SigninResponseDto login(SigninRequestDto signinRequest) {
        Member member = memberRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.LOGIN_FAILED, null));

        if (member.isDeleted()) {
            throw new DataInvalidException(ErrorCode.DUPLICATE_DELETED, "멤버");
        }

        if (!passwordEncoder.matches(signinRequest.getPassword(), member.getPassword())) {
            throw new DataNotMatchException(ErrorCode.LOGIN_FAILED, null);
        }

        String bearerToken = jwtUtil.createToken(member.getId(), member.getMemberRole());

        return new SigninResponseDto(bearerToken);
    }

    public MemberCreateResponseDto joinMember(MemberCreateRequestDto requestDto,
            MemberRole memberRole) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_EMAIL, "이메일");
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
            throw new DataInvalidException(ErrorCode.DUPLICATE_DELETED, "멤버");
        }

        if (member.getMemberRole().equals(MemberRole.SELLER)) {
            throw new OperationNotAllowedException(ErrorCode.FORBIDDEN_PAYMENT_LOGIN_FORM, null);
        }

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new DataNotMatchException(ErrorCode.LOGIN_FAILED, null);
        }

        String bearerToken = jwtUtil.createToken(member.getId(), member.getMemberRole());

        return new PaymentFormResponseDto(bearerToken, dto.getAmount());
    }
}
