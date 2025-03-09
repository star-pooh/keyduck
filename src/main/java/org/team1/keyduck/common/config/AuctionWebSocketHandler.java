package org.team1.keyduck.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    // 경매 ID별 웹소켓 세션 관리
    private static final Map<Long, Set<WebSocketSession>> auctionSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long auctionId = getAuctionIdFromSession(session);
        if (auctionId == null) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        // 해당 경매 ID에 대한 세션 리스트에 추가
        auctionSessions.computeIfAbsent(auctionId,
                        k -> Collections.synchronizedSet(new HashSet<>()))
                .add(session);

        log.info("WebSocket 연결됨: auctionId={}, sessionId={}", auctionId, session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("WebSocket 메시지 수신: {}", message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        Long auctionId = getAuctionIdFromSession(session);
        if (auctionId != null) {
            Set<WebSocketSession> sessions = auctionSessions.get(auctionId);
            if (sessions != null) {
                sessions.remove(session);
                log.info("WebSocket 연결 종료: auctionId={}, sessionId={}", auctionId, session.getId());

                // 세션이 모두 종료되면 해당 경매 ID 제거
                if (sessions.isEmpty()) {
                    auctionSessions.remove(auctionId);
                }
            }
        }
    }

    // 새로운 입찰 발생 시 모든 클라이언트에게 업데이트 전송
    public void broadcastAuctionUpdate(Long auctionId, BiddingResponseDto bidding)
            throws IOException {
        Set<WebSocketSession> sessions = auctionSessions.getOrDefault(auctionId,
                Collections.emptySet());
        if (sessions.isEmpty()) {
            log.warn("WebSocket 전송 대상 없음: auctionId={}", auctionId);
            return;
        }

        // JSON 변환
        String message = objectMapper.writeValueAsString(bidding);
        TextMessage textMessage = new TextMessage(message);

        // 연결된 모든 세션에 메시지 전송
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            }
        }

        log.info("WebSocket 브로드캐스트: auctionId={}, price={}, biddingId={}",
                auctionId, bidding.getBiddingPrice(), bidding.getId());
    }


    // 세션에서 auctionId 추출
    private Long getAuctionIdFromSession(WebSocketSession session) {
        try {
            // URI를 UriComponents로 파싱
            UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();

            // 쿼리 파라미터에서 auctionId 값을 추출
            String auctionIdParam = uriComponents.getQueryParams().getFirst("auctionId");

            if (auctionIdParam != null) {
                return Long.parseLong(auctionIdParam);  // auctionId를 Long으로 변환
            }
        } catch (Exception e) {
            log.error("auctionId 파싱 오류", e);
        }
        return null;  // 파라미터가 없거나 오류가 발생한 경우 null 반환
    }

}
