package org.team1.keyduck.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL1;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL2;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER_ROLE2;
import static org.team1.keyduck.testdata.TestData.TEST_NAME1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;
import static org.team1.keyduck.testdata.TestData.TEST_TOKEN;

import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.common.config.JwtUtil;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.service.CommonService;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    CommonService commonService;

    @Test
    @DisplayName(value = "회원가입 성공")
    void memberJoinSuccess() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL1);
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(false);

        when(requestDto.getPassword()).thenReturn(TEST_PASSWORD1);
        when(passwordEncoder.encode(any(String.class))).thenReturn(TEST_PASSWORD1);

        when(requestDto.getName()).thenReturn(TEST_NAME1);
        when(memberRepository.save(any(Member.class))).thenReturn(TEST_MEMBER1);

        Member member = mock(Member.class);

        //when
        authService.joinMember(requestDto, MemberRole.SELLER);
        Member actualMember = memberRepository.save(member);

        //then
        assertThat(actualMember)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("createdAt")
                .ignoringFields("modifiedAt")
                .isEqualTo(TEST_MEMBER1);

    }

    @Test
    @DisplayName(value = "멤버 회원가입 실패 : 이메일 중복")
    void memberJoinFailEmailDuplicate() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL1);
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(true);

        //when&then
        DataDuplicateException exception = assertThrows(DataDuplicateException.class, () -> {
            authService.joinMember(requestDto, MemberRole.CUSTOMER);
        });

        assertEquals("해당 이메일은(는) 이미 사용 중입니다.", exception.getMessage());

    }

    @Test
    @DisplayName(value = "로그인 성공")
    void loginSuccess() {
        //given
        SigninRequestDto requestDto = mock(SigninRequestDto.class);
        Member member = mock(Member.class);
        SigninResponseDto expectDto = new SigninResponseDto(TEST_TOKEN);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL2);
        when(memberRepository.findByEmail(any(String.class))).thenReturn(
                Optional.ofNullable(member));
        when(Objects.requireNonNull(member).isDeleted()).thenReturn(false);
        when(member.getId()).thenReturn(1L);
        when(member.getMemberRole()).thenReturn(TEST_MEMBER_ROLE2);
        when(jwtUtil.createToken(any(Long.class), any(MemberRole.class))).thenReturn(TEST_TOKEN);

        //when
        SigninResponseDto dto = authService.login(requestDto);

        //then
        assertEquals(expectDto.getBearerToken(), dto.getBearerToken());
    }

    @Test
    @DisplayName(value = "로그인 실패 : 삭제된 멤버")
    void loginFailDeletedMember() {

        //given
        SigninRequestDto requestDto = mock(SigninRequestDto.class);
        Member member = mock(Member.class);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL2);
        when(memberRepository.findByEmail(any(String.class))).thenReturn(
                Optional.ofNullable(member));
        when(Objects.requireNonNull(member).isDeleted()).thenReturn(true);

        //when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            authService.login(requestDto);
        });

        assertEquals("이미 삭제된 멤버 입니다.", exception.getMessage());

    }
}