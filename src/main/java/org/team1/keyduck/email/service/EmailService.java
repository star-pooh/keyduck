package org.team1.keyduck.email.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.EmailSendErrorException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.email.dto.GeneralEmailRequestDto;
import org.team1.keyduck.member.repository.MemberRepository;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;

    // 이메일 발신자 주소 설정
    @Value("${spring.mail.username}")
    private String senderEmail;

    private final SpringTemplateEngine templateEngine;

    public void sendMail(GeneralEmailRequestDto generalEmailRequestDto) {
        try {
            //이메일 정보 가져오기
            String recipientEmail = generalEmailRequestDto.getRecipientEmail();
            String emailTitle = generalEmailRequestDto.getEmailTitle();
            String emailContent = generalEmailRequestDto.getEmailContent();
            //새로운 이메일 메세지 객체
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //메세지에 정보를 설정할 수 있는 객체
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail); //발신자
            helper.setTo(recipientEmail); //수신자
            helper.setSubject(emailTitle);

            //Thymeleaf를 사용하여 동적 HTML 생성
            Context context = new Context();
            context.setVariable("emailTitle", emailTitle);
            context.setVariable("emailContent", emailContent);

            // emailContent 값이 Thymeleaf에 정상적으로 들어갔는지 확인하는 로그 추가
            log.info("📌 Thymeleaf Context emailContent 확인: '{}'",
                    context.getVariable("emailContent"));

            String htmlContent = templateEngine.process("email_template", context); //템플릿 이름
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("📌 emailContent 값 확인: '{}'", emailContent);
            log.info("이메일 전송완료: {}, 내용:{}", recipientEmail, emailContent);
        } catch (MailSendException e) {
            log.error("이메일 전송 실패 - 네트워크 문제 또는 잘못된 이메일 주소", e);
            throw new EmailSendErrorException(ErrorCode.EMAIL_SERVER_ERROR, "네트워크 오류");
        } catch (MailAuthenticationException e) {
            log.error("이메일 전송 실패 - 차단 등 인증 오류");
            throw new EmailSendErrorException(ErrorCode.EMAIL_SENDING_FORBIDDEN, "차단 등의 인증오류");
        } catch (MailException e) {
            log.error("이메일 전송 실패 - SMTP 서버 문제", e);
            throw new EmailSendErrorException(ErrorCode.EMAIL_SERVER_ERROR, "SMTP 서버 오류");
        } catch (MessagingException e) {
            log.error("이메일 전송 실패 - 메세징 서버 오류");
            throw new EmailSendErrorException(ErrorCode.EMAIL_SERVER_ERROR, "메세징 서버 오류");
        }
    }
}
