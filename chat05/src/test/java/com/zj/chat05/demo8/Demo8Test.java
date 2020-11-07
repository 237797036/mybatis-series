package com.zj.chat05.demo8;

import com.zj.chat05.demo8.mapper.UserMapper;
import com.zj.chat05.demo8.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 结果自动映射各种案例
 */
@Slf4j
public class Demo8Test {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        String mybatisConfig = "demo8/mybatis-config.xml";
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
//            paramMap.put("id",1);
//            paramMap.put("name", "张学友");
            paramMap.put("idList", Arrays.asList(1, 2).stream().collect(Collectors.toList()));
//            paramMap.put("likeName","java");
            paramMap.put("orderSql","order by id asc,age desc");
            List<UserModel> userModelList = mapper.getList1(paramMap);
            log.info("{}", userModelList);
        }
    }

    @Test
    public void update1() throws IOException {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int count = mapper.update1(UserModel.builder().id(1).age(31).build());
            log.info("{}", count);
        }
    }

@Test
public void insertBatch() throws IOException {
    try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<UserModel> userModelList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            userModelList.add(UserModel.builder().id(10 + i).name("mybatis-" + i).age(20 + i).build());
        }
        int count = mapper.insertBatch(userModelList);
        log.info("{}", count);
    }
}


}