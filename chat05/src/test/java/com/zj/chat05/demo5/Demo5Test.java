package com.zj.chat05.demo5;

import com.zj.chat05.demo5.mapper.OrderMapper;
import com.zj.chat05.demo5.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 懒加载
 */
@Slf4j
public class Demo5Test {
    private SqlSessionFactory sqlSessionFactory;

    private String mybatisConfig;

    public void before() throws IOException {

        //读取全局配置文件
        InputStream inputStream = Resources.getResourceAsStream(mybatisConfig);
        //构建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Test
    public void getById1() throws IOException {
        //指定mybatis全局配置文件
        mybatisConfig = "demo5/mybatis-config.xml";
        this.before();
        OrderModel orderModel = null;
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            orderModel = mapper.getById1(1);
        }
        log.info("-------分割线--------");
        log.info("{}", orderModel.getUserModel());
    }

    @Test
    public void getById1_0() throws IOException {
        //指定mybatis全局配置文件
        mybatisConfig = "demo5/mybatis-config1.xml";
        this.before();
        OrderModel orderModel = null;
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            orderModel = mapper.getById1(1);
        }
        log.info("-------分割线--------");
        log.info("{}", orderModel.getUserModel());
    }


    @Test
    public void getById2() throws IOException {
        //指定mybatis全局配置文件
        mybatisConfig = "demo5/mybatis-config2.xml";
        this.before();
        OrderModel orderModel = null;
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            orderModel = mapper.getById2(1);
        }
        log.info("-------分割线--------");
        log.info("{}", orderModel.getOrderDetailModelList());
    }

}