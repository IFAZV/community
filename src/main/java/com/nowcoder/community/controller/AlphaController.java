//网页控制器
package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller//标记为控制器
@RequestMapping("/alpha")//基础路径
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    @RequestMapping("/http")
    @ResponseBody
    public void http(HttpServletRequest request, HttpServletResponse response) {
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name =  enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8");//网页类型文本
        try (PrintWriter writer = response.getWriter()){
            writer.write("<hl>牛客网<hl>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //GET请求获取

    // /students?current=1&limit=20
    @RequestMapping(path = "/student",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /studnet/123
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){//id可不加
        System.out.println("id");
        return "student";
    }

    //POST请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应html数据

    //方法1
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    //不加@ResponseBody默认返回html
    public ModelAndView getTeacher() {//返回model和view两份数据
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", "30");
        mav.setViewName("/demo/view");//不用加html，原为view.htm
        return mav;
    }

    //方法2 更简单
    //注释中的路径为客户端访问路径，即URL后的路径
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){//model从参数响应
        model.addAttribute("name", "浙江中医药大学");
        model.addAttribute("age", "72");
        return "/demo/view";//返回view的路径，此路径为服务端查找文件所用，与客户端无关
    }

    //响应JSON数据（异步请求，即当前网页不刷新而返回的结果，如用户名被占用）
    //JSON：具有特定格式的字符串，负责转化Java对象为JS对象，起到衔接作用，不仅可以转化为JS,所有都可以

    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", "30");
        emp.put("salary", "8000.00");
        return emp;
    }

    //JSON集合
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps(){
        List<Map<String, Object>> emps = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", "30");
        emp.put("salary", "8000.00");
        emps.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", "24");
        emp.put("salary", "9000.00");
        emps.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", "25");
        emp.put("salary", "10000.00");
        emps.add(emp);

        return emps;
    }
}
