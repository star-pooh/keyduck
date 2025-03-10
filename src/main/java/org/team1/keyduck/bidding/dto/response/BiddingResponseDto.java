package org.team1.keyduck.bidding.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.bidding.entity.Bidding;

@Getter
public class BiddingResponseDto implements Serializable {

    private Long id;
    private Long auctionId;
    private String memberName;
    private Long biddingPrice;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BiddingResponseDto(Long id, Long auctionId, String memberName, Long biddingPrice,
            LocalDateTime createdAt) {
        this.id = id;
        this.auctionId = auctionId;
        this.memberName = memberName;
        this.biddingPrice = biddingPrice;
        this.createdAt = createdAt;
    }

    public static BiddingResponseDto of(Bidding bidding) {
        return new BiddingResponseDto(
                bidding.getId(),
                bidding.getAuction().getId(),
                bidding.getMember().getName(),
                bidding.getPrice(),
                bidding.getCreatedAt()
        );
    }
}




