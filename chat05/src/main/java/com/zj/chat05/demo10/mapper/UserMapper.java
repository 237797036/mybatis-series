package com.zj.chat05.demo10.mapper;

import com.zj.chat05.demo10.model.UserModel;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<UserModel> getList1(Map<String, Object> paramMap);

    int insert1(Map<String, Object> map);
}
