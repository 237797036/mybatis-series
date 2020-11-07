package com.zj.chat05.demo8.mapper;

import com.zj.chat05.demo8.model.UserModel;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<UserModel> getList1(Map<String, Object> paramMap);
    List<UserModel> getList2(Map<String, Object> paramMap);
    int update1(UserModel userModel);
    int insertBatch(List<UserModel> userModelList);
}
