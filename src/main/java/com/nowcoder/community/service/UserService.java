package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    //邮件客户端
    @Autowired
    private MailClient mailClient;

    //模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    //注入域名
    @Value("${community.path.domain}")
    private String domain;

    //注入项目名，来自a.a配置文件
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //通过id查用户名
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    //注册业务,返回值以错误信息为主，由于可能存在多个，所以返回map
    public Map<String,Object> register(User user) {
        Map<String,Object> map = new HashMap<String,Object>();//map用于装载错误信息

        //空置处理
        if(user == null) {//全都是空的，根本没返回
            throw new IllegalArgumentException("参数不能为空");
        }

        if(StringUtils.isBlank(user.getUsername())) {//账号为空
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())) {//密码为空
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())) {//邮箱为空
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u != null) {//如果账号已存在在数据库中
            map.put("usernameMsg", "账号已存在");
            return map;
        }

        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null) {//如果邮箱已存在在数据库中
            map.put("emailMsg", "邮箱已被使用");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUuid().substring(0, 5));//生成salt,截取0-5位字符
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));//加密密码
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUuid());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000))); //在一千之内随机%d
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//传入数据库并自动创建id，自动生成id配置在a.a配置文件中

        //激活邮件
        Context context = new Context();//thymeleaf包下的context
        context.setVariable("email", user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);//process将模板与传入的数据结合生成最终版输出内容，第一个参数是模板位置,第二个是要传入的数据
        mailClient.sendMail(user.getEmail(),"账号激活",content);

        return map;//如果无异常则返回空值
    }

    //激活方法
    public int activation(int userId,String code) {
        User user = userMapper.selectById(userId);
        if(user.getStatus()==1) {
            return ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)) {//比较激活码是否与数据库内的相等
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }

    }


}
