package org.team1.keyduck.bidding.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.dto.response.SuccessBiddingResponseDto;
import org.team1.keyduck.bidding.service.BiddingService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/biddings")
public class BiddingController {

    private final BiddingService biddingService;

    //생성
    @PostMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<Void>> createBidding(
            @PathVariable("auctionId") Long auctionId,
            @RequestParam(value = "price", required = true) Long price,
            @AuthenticationPrincipal AuthMember authMember) {
        biddingService.createBidding(auctionId, price, authMember);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());

    }

    //조회(경매별 입찰내역)
    @GetMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<List<BiddingResponseDto>>> getBiddingList(
            @PathVariable Long auctionId) {
        List<BiddingResponseDto> biddingResponseDtos = biddingService.getBiddingByAuction(
                auctionId);
        return new ResponseEntity<>(
                ApiResponse.success(SuccessCode.READ_SUCCESS, biddingResponseDtos),
                SuccessCode.READ_SUCCESS.getStatus());
    }

    //낙찰내역 조회
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<Page<SuccessBiddingResponseDto>>> getSuccessBidding(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestParam(defaultValue = "1") int page) {
        Page<SuccessBiddingResponseDto> successBiddingResponseDtoPage = biddingService.getSuccessBidding(
                authMember.getId(), page);
        return new ResponseEntity<>(
                ApiResponse.success(SuccessCode.READ_SUCCESS, successBiddingResponseDtoPage),
                SuccessCode.READ_SUCCESS.getStatus());
    }
}
