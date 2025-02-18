package org.team1.keyduck.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.PaymentFormRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.PaymentFormResponseDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.common.config.JwtUtil;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SigninResponseDto login(SigninRequestDto signinRequest) {
        String bearerToken = createBearerToken(signinRequest.getEmail(),
                signinRequest.getPassword());
        return new SigninResponseDto(bearerToken);
    }

    public void joinMember(MemberCreateRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateDataException(ErrorCode.DUPLICATE_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = Member.builder().name(requestDto.getName())
                .address(requestDto.getAddress())
                .memberRole(requestDto.getMemberRole()).email(requestDto.getEmail())
                .password(encodedPassword).build();

        memberRepository.save(member);
    }

    public PaymentFormResponseDto paymentFormLogin(PaymentFormRequestDto dto) {
        String bearerToken = createBearerToken(dto.getEmail(), dto.getPassword());
        return new PaymentFormResponseDto(bearerToken, dto.getAmount());
    }

    private String createBearerToken(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.LOGIN_FAILED, null));

        if (member.isDeleted()) {
            throw new DataNotFoundException(ErrorCode.LOGIN_FAILED, null);
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new DataNotMatchException(ErrorCode.LOGIN_FAILED, null);
        }

        return jwtUtil.createToken(member.getId(), member.getMemberRole());
    }
}
