package org.team1.keyduck.auction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.service.AuctionService;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    // 경매 단건 조회 API
    @GetMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<AuctionReadResponseDto>> findAuctionAPI(
            @PathVariable Long auctionId,
            @AuthenticationPrincipal AuthMember authMember) {

        AuctionReadResponseDto response = auctionService.findAuction(authMember.getId());

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.READ_SUCCESS, response),
                SuccessCode.READ_SUCCESS.getStatus());
    }

}
