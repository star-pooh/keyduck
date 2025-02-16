package org.team1.keyduck.auction.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuctionCreateRequestDto {

    @NotNull(message = "제품을 등록해주세요")
    private Long keyboardId;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "시작가 필수 입력 값입니다.")
    private Long startPrice;

    @Nullable
    private Long immediatePurchasePrice;

    @NotNull(message = "입찰 단위는 필수 입력 값입니다.")
    private int biddingUnit;

    @NotNull(message = "경매 시작일은 필수 입력 값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartDate;

    @NotNull(message = "경매 종료일은 필수 입력 값입니다.")
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndDate;

}
