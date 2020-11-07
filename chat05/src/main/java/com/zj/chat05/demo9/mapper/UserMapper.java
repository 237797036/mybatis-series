package com.zj.chat05.demo9.mapper;

import com.zj.chat05.demo9.model.UserModel;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<UserModel> getList1(Map<String, Object> paramMap);

    int insert1(UserModel userModel);

    List<UserModel> getList2(Map<String, Object> paramMap);

    List<UserModel> getList3(Map<String, Object> paramMap);
}
