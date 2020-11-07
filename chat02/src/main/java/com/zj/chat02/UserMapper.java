package com.zj.chat02;

import java.util.List;

public interface UserMapper {

    int insertUser(UserModel model);

    int updateUser(UserModel model);

    int deleteUser(Long userId);

    List<UserModel> getUserList();

    List<UserModel> getUserByUserIdAndName(long id, String name);
}
