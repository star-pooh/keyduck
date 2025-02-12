package org.team1.keyduck.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.common.config.JwtUtil;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {
        Member member = memberRepository.findByEmail(signinRequestDto.getEmail())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(signinRequestDto.getPassword(), member.getPassword())) {
            throw new DataNotMatchException(ErrorCode.LOGIN_FAILED);
        }

        String bearerToken = jwtUtil.createToken(member.getId(), member.getMemberRole());

        return new SigninResponseDto(bearerToken);
    }
}
