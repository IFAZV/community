//工具类
package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    public String from;

    public void sendMail(String to, String subject, String content) {//发给谁，主题，内容

        try {
            MimeMessage message = mailSender.createMimeMessage();//MimeMessage用于表示和处理 MIME 格式的电子邮件消息,此步为创建一个MimeMessage类
            MimeMessageHelper helper = new MimeMessageHelper(message);//用于构建更详细的message内容的工具
            helper.setFrom(from);//设置发件人
            helper.setTo(to);//设置收件人
            helper.setSubject(subject);//设置主题
            helper.setText(content, true);//设置内容，支持html文本
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败：" + e.getMessage());
        }
    }

}
