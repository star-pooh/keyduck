package org.team1.keyduck.email.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.team1.keyduck.email.dto.EmailEvent;
import org.team1.keyduck.email.service.EmailService;

@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailEvent(EmailEvent event) {
        emailService.sendMemberEmail(event.getMemberId(), event.getEmailRequestDto());
    }
}
