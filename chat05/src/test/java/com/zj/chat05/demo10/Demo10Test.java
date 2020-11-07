package com.zj.chat05.demo10;

import com.zj.chat05.demo10.mapper.UserMapper;
import com.zj.chat05.demo10.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型转换器
 */
@Slf4j
public class Demo10Test {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        String mybatisConfig = "demo10/mybatis-config.xml";
        //读取全局配置文件
        InputStream inputStream = Resources.getResourceAsStream(mybatisConfig);
        //构建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Test
    public void getModelList() throws IOException {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> paramMap = new HashMap<>();
            List<UserModel> userModelList = mapper.getList1(paramMap);
            log.info("{}", userModelList);
        }
    }

    @Test
    public void insert1() throws IOException {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> map = new HashMap<>();
            Integer id = Integer.valueOf(Calendar.getInstance().getTime().getTime() / 1000 + "");
            map.put("id", id);
            map.put("name", id.toString());
            map.put("age", 30);
            map.put("sex", null);
            int result = mapper.insert1(map);
            log.info("{}", result);
        }
    }

}