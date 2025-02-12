package org.team1.keyduck.bidding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.dto.request.BiddingRequestDto;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.service.BiddingService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.member.entity.Member;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class BiddingController {

    private BiddingService biddingService;

    @PostMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<Bidding>> createBidding(@PathVariable("auctionId") Long auctionId,
            @RequestParam("price")Long price,
            @AuthenticationPrincipal AuthMember authmember) {
        biddingService.createBidding(auctionId,price, authmember);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());

    }

}
