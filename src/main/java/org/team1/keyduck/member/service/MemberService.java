package org.team1.keyduck.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.service.JwtBlacklistService;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.common.service.CommonService;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.dto.request.MemberUpdatePasswordRequestDto;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberReadResponseDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuctionRepository auctionRepository;
    private final PaymentDepositRepository paymentDepositRepository;

    private final JwtBlacklistService jwtBlacklistService;
    private final CommonService commonService;

    @Transactional
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto requestDto, Long id) {

        requestDto.isAllFieldsEmpty();

        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                ErrorCode.NOT_FOUND_MEMBER, ErrorMessageParameter.MEMBER));

        member.updateMember(requestDto);

        return MemberUpdateResponseDto.of(member);
    }

    @Transactional
    public void updatePassword(MemberUpdatePasswordRequestDto requestDto, Long id) {

        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                ErrorCode.NOT_FOUND_MEMBER, ErrorMessageParameter.MEMBER));

        commonService.comparePassword(requestDto.getBeforePassword(), member.getPassword());

        String encodedModifyPassword = passwordEncoder.encode(requestDto.getModifyPassword());
        member.updatePassword(encodedModifyPassword);
    }

    @Transactional
    public void deleteMember(Long id, String token) {
        Member member = memberRepository.findByIdAndIsDeleted(id, false);

        if (member == null) {
            throw new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER,
                    ErrorMessageParameter.MEMBER);
        }

        //현재 진행중인 경매가 있으면 탈퇴 불가능
        if (auctionRepository.existsByKeyboard_Member_IdAndAuctionStatus(id,
                AuctionStatus.IN_PROGRESS)) {
            throw new OperationNotAllowedException(ErrorCode.DELETE_FAIL_AUCTION_IN_PROGRESS, null);
        }

        jwtBlacklistService.addToBlacklist(token);

        member.deleteMember();
    }

    @Transactional(readOnly = true)
    public MemberReadResponseDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                ErrorCode.NOT_FOUND_MEMBER, ErrorMessageParameter.MEMBER));

        Long paymentDeposit = paymentDepositRepository.findPaymentDepositAmountMember_Id(id)
                .orElse(0L);

        return MemberReadResponseDto.of(member, paymentDeposit);
    }
}
