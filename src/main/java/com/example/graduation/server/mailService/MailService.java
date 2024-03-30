package com.example.graduation.server.mailService;

import com.example.graduation.utils.RandomCodeUtils;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class MailService {
    @Autowired
    private MailServiceConfig mailServiceConfig;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送邮箱验证码
     * @param emailAddress 收件人邮箱
     */
    public Res<Boolean> sendMailCode(String emailAddress) {
        String code = RandomCodeUtils.createSmsCode(mailServiceConfig.getLength());
        long smsValidTimeInterval = mailServiceConfig.getSmsValidTimeInterval();

        Context context = new Context();
        // 设置html中的变量（分割验证码）
        context.setVariable("verifyCode", Arrays.asList(code.split("")));
        context.setVariable("ValidTimeInterval", smsValidTimeInterval/60);

        String process = templateEngine.process("MailVerificationCode.html", context); // 这里不用写全路径

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(mailServiceConfig.getTitle()); // 邮件的标题
            helper.setFrom(mailServiceConfig.getUsername()); // 发送者
            helper.setTo(emailAddress); // 接收者
            helper.setSentDate(new Date()); // 时间
            helper.setText(process, true);
            mailSender.send(mimeMessage);
            //将验证码存入redis中
            stringRedisTemplate.opsForValue().set(emailAddress, code, smsValidTimeInterval, TimeUnit.SECONDS);
            logger.info("验证邮件发送成功。");
            return Res.Success(true);
        } catch (MessagingException ex) {
            logger.error("验证邮件发送时发生了意外: " + ex.getMessage());
            return Res.Error(status.mailServiceError);
        }
    }

    /**
     * 验证安全码
     * @param emailAddress
     * @param code
     * @return captchaExpiration/captchaError/
     */
    public Res<Boolean> verify(@RequestParam String emailAddress, @RequestParam String code) {
        if(Boolean.FALSE.equals(stringRedisTemplate.hasKey(emailAddress))){
            return Res.Error(status.captchaExpiration);
        }
        String s = stringRedisTemplate.opsForValue().get(emailAddress);
        if(code.equals(s)){
            stringRedisTemplate.delete(emailAddress);
            return Res.Success(true);
        }
        return Res.Error(status.captchaError);
    }
}
