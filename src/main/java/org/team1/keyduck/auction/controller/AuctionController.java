package org.team1.keyduck.auction.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionSearchSliceResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
import org.team1.keyduck.auction.service.AuctionService;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@Controller
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<AuctionCreateResponseDto>> createAuction(
            @AuthenticationPrincipal AuthMember authMember,
            @Valid @RequestBody AuctionCreateRequestDto requestDto) {

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

    // 경매 단건 조회 API
    @GetMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<AuctionReadResponseDto>> findAuctionAPI(
            @PathVariable Long auctionId) {

        AuctionReadResponseDto response = auctionService.findAuction(auctionId);

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.READ_SUCCESS, response),
                SuccessCode.READ_SUCCESS.getStatus());
    }

    // 경매 다건 조회 API
    @GetMapping
    public ResponseEntity<ApiResponse<AuctionSearchSliceResponseDto>> findAllAuctionAPI(
            @RequestParam(required = false) Long lastId,
            Pageable pageable, @RequestParam(required = false) String keyboardName,
            @RequestParam(required = false) String auctionTitle,
            @RequestParam(required = false) String sellerName,
            @RequestParam(required = false) String auctionStatus,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") String endDate) {

        Slice<AuctionSearchResponseDto> response = auctionService.findAllAuction(lastId, pageable,
                keyboardName, auctionTitle, sellerName, auctionStatus, startDate, endDate);

        AuctionSearchSliceResponseDto responseDto = new AuctionSearchSliceResponseDto(response);

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.READ_SUCCESS, responseDto),
                SuccessCode.READ_SUCCESS.getStatus());
    }

    @GetMapping("/main")
    public String findAllAuctionWithHtml(Pageable pageable, Model model) {

        Slice<AuctionSearchResponseDto> response = auctionService.findAllAuction(null, pageable,
                null, null, null, null, null, null);

        model.addAttribute("auctions", response);
        return "main";
    }
}
