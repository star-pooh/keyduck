package org.team1.keyduck.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auth.dto.request.SigninRequest;
import org.team1.keyduck.auth.dto.response.SigninResponse;
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

    public SigninResponse signin(SigninRequest signinRequest) {
        Member member = memberRepository.findByEmail(signinRequest.getEmail())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(signinRequest.getPassword(), member.getPassword())) {
            throw new DataNotMatchException(ErrorCode.LOGIN_FAILED);
        }

        String bearerToken = jwtUtil.createToken(member.getId(), member.getMemberRole());

        return new SigninResponse(bearerToken);
    }
}
