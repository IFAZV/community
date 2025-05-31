package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {//也是返回值
        // 方法调用钱,SpringMVC(DispatcherServlet)会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.

        //返回一些必要的用于分页的值给页面，剩下的值page类会自行计算
        page.setRows(discussPostService.findDiscussPostRows(0));//将行数信息存入page
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());//存储一系列用户数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();//存储用户名;
        if(list != null) {//根据id输出用户名,这个类查询的是帖子数据，只有用户id，用户名在user数据库里

            for (DiscussPost post : list) {
                Map<String,Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        //将map传入index.html
        model.addAttribute("discussPosts", discussPosts);
        //model.addAttribute("page", page); 依照方法开头注释，已自动执行
        return "/index";
    }


}
