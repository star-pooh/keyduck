package org.team1.keyduck.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class BiddingEventListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // 메시지 수신 처리 메소드 (MessageListener의 onMessage 메소드 구현)
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer()
                    .deserialize(message.getBody());
            BiddingResponseDto dto = objectMapper.readValue(publishMessage,
                    BiddingResponseDto.class);
            messagingTemplate.convertAndSend("/topic/auction/" + dto.getAuctionId(), dto);
        } catch (IOException e) {
            log.error("Deserialization error", e);
        }
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishBidding(BiddingResponseDto biddingResponseDto) {
        log.info("Publishing bidding event: {}", biddingResponseDto);
        redisTemplate.convertAndSend(topic.getTopic(), biddingResponseDto);
    }
}
