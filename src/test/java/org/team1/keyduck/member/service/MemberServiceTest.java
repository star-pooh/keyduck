package org.team1.keyduck.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ADDRESS1;
import static org.team1.keyduck.testdata.TestData.TEST_ADDRESS2;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL1;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL2;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER2;
import static org.team1.keyduck.testdata.TestData.TEST_NAME1;
import static org.team1.keyduck.testdata.TestData.TEST_NAME2;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD3;
import static org.team1.keyduck.testdata.TestData.TEST_WRONG_EMAIL1;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.service.JwtBlacklistService;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.service.CommonService;
import org.team1.keyduck.member.dto.request.MemberUpdatePasswordRequestDto;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberReadResponseDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CommonService commonService;

    @Mock
    JwtBlacklistService jwtBlacklistService;

    @Mock
    AuctionRepository auctionRepository;

    @Mock
    PaymentDepositRepository paymentDepositRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    void 성공_케이스_멤버_업데이트() {
        //given

        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);
        MemberUpdateResponseDto expectMember = MemberUpdateResponseDto.of(TEST_MEMBER2);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_MEMBER1));
        when(requestDto.getName()).thenReturn(TEST_NAME2);
        when(requestDto.getEmail()).thenReturn(TEST_EMAIL2);
        when(requestDto.getAddress()).thenReturn(TEST_ADDRESS2);

        //when
        MemberUpdateResponseDto actualMember = memberService.updateMember(requestDto, TEST_ID1);

        //then
        assertThat(actualMember).usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("memberRole")
                .ignoringFields("createdAt")
                .ignoringFields("modifiedAt")
                .ignoringFields("password")
                .isEqualTo(expectMember);

    }

    @Test
    void 실패_케이스_멤버_업데이트_이메일_형식_불일치() {

        //given
        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_MEMBER1));
        when(requestDto.getName()).thenReturn(TEST_NAME1);
        when(requestDto.getEmail()).thenReturn(TEST_WRONG_EMAIL1);

        //when&then
        assertThrows(DataNotMatchException.class, () -> {
            memberService.updateMember(requestDto, TEST_ID1);
        });

    }

    @Test
    void 성공_케이스_비밀번호_업데이트() {
        //given
        Member beforeMember = new Member(TEST_NAME1, TEST_EMAIL1, TEST_PASSWORD1,
                MemberRole.CUSTOMER,
                TEST_ADDRESS1);

        MemberUpdatePasswordRequestDto requestDto = mock(MemberUpdatePasswordRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeMember));
        when(requestDto.getBeforePassword()).thenReturn(beforeMember.getPassword());
        when(requestDto.getModifyPassword()).thenReturn(TEST_PASSWORD3);
        when(passwordEncoder.encode(any(String.class))).thenReturn(TEST_PASSWORD3);

        //when
        memberService.updatePassword(requestDto, TEST_ID1);

        //then
        assertThat(beforeMember.getPassword()).isEqualTo(TEST_PASSWORD3);

    }


    @Test
    void 성공_케이스_멤버_조회() {
        //given
        MemberReadResponseDto expectMember = MemberReadResponseDto.of(TEST_MEMBER1, 0L);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_MEMBER1));

        //when
        MemberReadResponseDto actualMember = memberService.getMember(TEST_ID1);

        //then
        assertThat(actualMember).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(expectMember);
    }

}