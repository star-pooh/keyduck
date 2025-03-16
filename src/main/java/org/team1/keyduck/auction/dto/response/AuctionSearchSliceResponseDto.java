package org.team1.keyduck.auction.dto.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class AuctionSearchSliceResponseDto {

    private final List<AuctionSearchResponseDto> content;

    private final boolean hasNext;

    public AuctionSearchSliceResponseDto(Slice<AuctionSearchResponseDto> slice) {
        this.content = slice.getContent();
        this.hasNext = slice.hasNext();
    }

}
