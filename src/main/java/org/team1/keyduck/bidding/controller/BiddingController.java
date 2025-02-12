package org.team1.keyduck.bidding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.bidding.dto.request.BiddingRequestDto;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.service.BiddingService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@RestController
@RequestMapping("/api/auctions")
public class BiddingController {

    @Autowired
    private BiddingService biddingService;

    @PostMapping("/{auctionId}/biddings")
    public ResponseEntity<ApiResponse<Void>> createBidding(@PathVariable("auctionId")
            @RequestBody BiddingRequestDto biddingRequestDto) {
        biddingService.createBidding(biddingRequestDto);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());

    }
}
