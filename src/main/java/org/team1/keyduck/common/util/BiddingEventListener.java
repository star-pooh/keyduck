package org.team1.keyduck.common.util;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class BiddingEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void publishBidding(BiddingResponseDto biddingResponseDto) {
        messagingTemplate.convertAndSend("/topic/auction/" + biddingResponseDto.getAuctionId(),
                biddingResponseDto);
    }

}
