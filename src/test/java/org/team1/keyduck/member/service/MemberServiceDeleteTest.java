package org.team1.keyduck.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ADDRESS1;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER_ROLE1;
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
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.service.JwtBlacklistService;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceDeleteTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    JwtBlacklistService jwtBlacklistService;

    @Mock
    AuctionRepository auctionRepository;

    @Test
    @DisplayName("멤버 삭제 성공")
    void memberDeleteSuccess() {

        Member member = new Member(TEST_NAME1, TEST_EMAIL1, TEST_PASSWORD1, TEST_MEMBER_ROLE1,
                TEST_ADDRESS1);

        when(memberRepository.findByIdAndIsDeleted(any(Long.class), eq(false))).thenReturn(
                Optional.of(member));
        when(auctionRepository.existsByKeyboard_Member_IdAndAuctionStatus(any(Long.class),
                eq(AuctionStatus.IN_PROGRESS))).thenReturn(false);

        memberService.deleteMember(1L, TEST_TOKEN);

        assertTrue(member.isDeleted());
    }

    @Test
    @DisplayName("멤버 삭제 실패 : 진행 중인 경매가 있음")
    void memberDeleteFailInProgressAuction() {
        Member member = mock(Member.class);

        when(memberRepository.findByIdAndIsDeleted(any(Long.class), eq(false))).thenReturn(
                Optional.ofNullable(member));
        when(Objects.requireNonNull(member).getMemberRole()).thenReturn(MemberRole.SELLER);
        when(auctionRepository.existsByKeyboard_Member_IdAndAuctionStatus(any(Long.class),
                eq(AuctionStatus.IN_PROGRESS))).thenReturn(true);

        //when&then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class,
                () -> {
                    memberService.deleteMember(1L, TEST_TOKEN);
                });

        assertEquals("진행 중인 경매가 있기 때문에 삭제가 불가능 합니다.", exception.getMessage());

    }

}
