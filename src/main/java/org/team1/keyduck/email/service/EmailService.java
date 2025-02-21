package org.team1.keyduck.email.service;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.team1.keyduck.email.dto.EmailRequestDto;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    // 이메일 발신자 주소 설정
    private static final String SENDER_EMAIL = "qa9377pl@gmail.com";
    private final SpringTemplateEngine templateEngine;

    /*
     * 이메일을 보내는 메서드
     * @param recipientEmail 수신자 이메일 주소
     * @param jobOpeningUrlList 채용 공고 목록
     */
    public void sendMail(EmailRequestDto emailRequestDto) {
        try {
            //이메일 정보 가져오기
            String recipientEmail = emailRequestDto.getRecipientEmail();
            String emailTitle = emailRequestDto.getEmailTitle();
            String emailContent = emailRequestDto.getEmailContent();
            //새로운 이메일 메세지 객체
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //메세지에 정보를 설정할 수 있는 객체
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(SENDER_EMAIL); //발신자
            helper.setTo(recipientEmail); //수신자
            helper.setSubject(emailTitle);

            //Thymeleaf를 사용하여 동적 HTML 생성
            Context context = new Context();
            context.setVariable("emailTitle", emailTitle);
            context.setVariable("emailContent", emailContent);

            // ✅ emailContent 값이 Thymeleaf에 정상적으로 들어갔는지 확인하는 로그 추가
            log.info("📌 Thymeleaf Context emailContent 확인: '{}'",
                    context.getVariable("emailContent"));

            String htmlContent = templateEngine.process("email_template", context); //템플릿 이름
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("📌 emailContent 값 확인: '{}'", emailContent);
            log.info("이메일 전송완료: {}, 내용:{}", recipientEmail, emailContent);
        } catch (Exception e) {
            log.error("이메일 전송 실패", e);
        }
    }
}