package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    //生成随机字符串
    public static String generateUuid(){
        return UUID.randomUUID().toString().replace("-", "");//去除’-‘
    }

    //MD5加密
    //hello abc123def456 易破解
    //hello + 3e4sB(salt) 更难破解
    public static String md5(String key){
        if(StringUtils.isBlank(key)){//key == null || key.length() == 0 ||key == ' '
            return "";
        }
        return DigestUtils.md5DigestAsHex(key.getBytes()/*数据转换为bytes形式*/);//返回md5加密结
    }

}
