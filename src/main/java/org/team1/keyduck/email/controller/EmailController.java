package org.team1.keyduck.email.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.email.dto.GeneralEmailRequestDto;
import org.team1.keyduck.email.dto.MemberEmailRequestDto;
import org.team1.keyduck.email.dto.MultipleEmailRequestDto;
import org.team1.keyduck.email.service.EmailService;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody GeneralEmailRequestDto generalEmailRequestDto) {
        emailService.sendMail(generalEmailRequestDto);
        return "OK";
    }

    @PostMapping("/sendbymember/{memberId}")
    public String sendEmailByMemberId(@PathVariable("memberId") Long memberId,
            @RequestBody MemberEmailRequestDto memberEmailRequestDto) {
        emailService.sendMemberEmail(memberId, memberEmailRequestDto);
        return "OK";
    }

    @PostMapping("/sendtomultiple")
    public String sendEmailToMultiple(
            @RequestBody MultipleEmailRequestDto multipleEmailRequestDto) {
        emailService.sendMultipleEmails(multipleEmailRequestDto);
        return "OK";
    }
}
