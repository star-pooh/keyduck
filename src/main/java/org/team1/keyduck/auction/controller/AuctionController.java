package org.team1.keyduck.auction.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;
import org.team1.keyduck.auction.service.AuctionService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    // 경매 다건 조회 API
    @GetMapping
    public ResponseEntity<ApiResponse<List<AuctionReadAllResponseDto>>> findAllAuctionAPI() {
        List<AuctionReadAllResponseDto> response = auctionService.findAllAuction();

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.READ_SUCCESS, response),
                SuccessCode.READ_SUCCESS.getStatus());
    }

}
