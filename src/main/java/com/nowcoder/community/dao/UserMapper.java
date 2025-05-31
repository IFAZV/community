package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//接口给SQL方法起一个本地的名字，便于在编译器里调用
@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    User selectByStatus(String email);

    int insertUser(User user);

    int updateStatus(@Param("id") int id, @Param("status") int status);//updata接口要加@Param注释，@Param("xxx")括号内名称与xml文件里的#{xxx}相同，即编译器里的驼峰命名规则

    int updateHeader(@Param("id")int id,@Param("headerUrl") String headUrl);

    int updatePassword(@Param("id")int id,@Param("password") String password);
}
