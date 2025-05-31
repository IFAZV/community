package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;//thymeleaf模板引擎核心类

    @Test
    public void testTextMail() {
        mailClient.sendMail("A1181030337@163.com", "TEST", "Hello World");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();//选thymeleaf版
        context.setVariable("username", "lmx");//传给模板引擎的变量

        String content = templateEngine.process("/mail/demo", context);//模板文件文件路径，无需.html。另一个是数据
        System.out.println(content);

        mailClient.sendMail("A1181030337@qq.com","TEST_html",content);

    }

}
