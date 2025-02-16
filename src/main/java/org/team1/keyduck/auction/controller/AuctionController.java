package org.team1.keyduck.auction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
import org.team1.keyduck.auction.service.AuctionService;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<AuctionCreateResponseDto>> createAuction(
        @AuthenticationPrincipal AuthMember authMember,
        @RequestBody AuctionCreateRequestDto requestDto) {

        AuctionCreateResponseDto responseDto = auctionService.createAuctionService(
            authMember.getId(), requestDto);

        ApiResponse<AuctionCreateResponseDto> response = ApiResponse.success(
            SuccessCode.CREATE_SUCCESS, responseDto);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<AuctionUpdateResponseDto>> auctionModification(
        @AuthenticationPrincipal AuthMember authMember,
        @PathVariable Long auctionId,
        @Valid @RequestBody AuctionUpdateRequestDto requestDto) {
        AuctionUpdateResponseDto responseDto = auctionService.auctionModification(
            authMember.getId(), auctionId, requestDto);

        ApiResponse<AuctionUpdateResponseDto> response = ApiResponse.success(
            SuccessCode.UPDATE_SUCCESS, responseDto);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/{auctionId}/open")
    public ResponseEntity<ApiResponse<Void>> openAuction(
        @AuthenticationPrincipal AuthMember authMember,
        @PathVariable Long auctionId
    ) {
        auctionService.openAuction(authMember.getId(), auctionId);
        ApiResponse<Void> response = ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
