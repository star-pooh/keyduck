package org.team1.keyduck.auction.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class AuctionUpdateRequestDto {

    @NotBlank(message = ValidationErrorMessage.TITLE_IS_NOT_NULL)
    private String title;

    @NotNull(message = ValidationErrorMessage.START_PRICE_IS_NOT_NULL)
    @Min(value = 1000L, message = ValidationErrorMessage.START_PRICE_IS_NOT_VALID)
    private Long startPrice;

    @Nullable
    @Min(value = 1000L, message = ValidationErrorMessage.IMMEDIATE_PURCHASE_PRICE_IS_NOT_VALID)
    private Long immediatePurchasePrice;

    @NotNull(message = ValidationErrorMessage.BIDDING_UNIT_IS_NOT_NULL)
    @Min(value = 10L, message = ValidationErrorMessage.BIDDING_UNIT_IS_NOT_VALID)
    private Long biddingUnit;

    @NotNull(message = ValidationErrorMessage.AUCTION_START_DATE_IS_NOT_NULL)
    @Future(message = ValidationErrorMessage.AUCTION_START_DATE_IS_NOT_BEFORE_NOW)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartDate;

    @NotNull(message = ValidationErrorMessage.AUCTION_END_DATE_IS_NOT_NULL)
    @Future(message = ValidationErrorMessage.AUCTION_END_DATE_IS_NOT_BEFORE_NOW)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndDate;
}
