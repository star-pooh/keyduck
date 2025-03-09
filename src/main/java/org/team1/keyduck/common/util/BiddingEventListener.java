package org.team1.keyduck.common.util;

import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.common.config.AuctionWebSocketHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class BiddingEventListener {

    private final AuctionWebSocketHandler auctionWebSocketHandler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void publishBidding(BiddingResponseDto biddingResponseDto) {
        try {
            auctionWebSocketHandler.broadcastAuctionUpdate(biddingResponseDto.getAuctionId(),
                    biddingResponseDto);
        } catch (IOException e) {
            log.error("WebSocket 메시지 전송 실패: auctionId={}, error={}",
                    biddingResponseDto.getAuctionId(), e.getMessage(), e);
        }
    }

}
