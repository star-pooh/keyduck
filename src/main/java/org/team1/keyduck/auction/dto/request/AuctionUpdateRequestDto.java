package org.team1.keyduck.auction.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuctionUpdateRequestDto {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "시작가는 필수 입력값입니다.")
    private Long startPrice;

    @Nullable
    private Long immediatePurchasePrice;

    @NotNull(message = "입찰 단위는 필수 입력 값입니다.")
    private int biddingUnit;

    @Future(message = "경매 시작일은 현재보다 이전일 수 없습니다.")
    @NotNull(message = "경매 시작일은 필수 입력 값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartDate;

    @Future(message = "경매 종료일은 현재보다 이전일 수 없습니다.")
    @NotNull(message = "경매 종료일은 필수 입력 값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndDate;
}
