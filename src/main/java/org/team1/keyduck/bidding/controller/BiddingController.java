package org.team1.keyduck.bidding.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/biddings")
public class BiddingController {

    private final BiddingService biddingService;
    //생성
    @PostMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<Void>> createBidding(@PathVariable("auctionId") Long auctionId,
            @RequestParam("price")Long price,
            @AuthenticationPrincipal AuthMember authMember) {
        biddingService.createBidding(auctionId,price, authMember);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());

    }
    //조회(경매별 입찰내역)



}
