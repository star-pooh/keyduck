package org.team1.keyduck.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.keyduck.member.dto.response.MemberReadResponseDto;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceGetTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PaymentDepositRepository paymentDepositRepository;


    @Test
    void 성공_케이스_멤버_조회() {
        //given
        MemberReadResponseDto expectMember = MemberReadResponseDto.of(TEST_MEMBER1, TEST_ID1);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_MEMBER1));

        //when
        MemberReadResponseDto actualMember = memberService.getMember(TEST_ID1);

        //then
        assertThat(actualMember).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(expectMember);
    }

}
