package com.zj.chat05.demo6;

import com.zj.chat05.demo6.mapper.OrderMapper;
import com.zj.chat05.demo6.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 鉴别器(discriminator)  & 继承(extends)
 */
@Slf4j
public class Demo6Test {
    private SqlSessionFactory sqlSessionFactory;

    private String mybatisConfig;

    @Before
    public void before() throws IOException {
        mybatisConfig = "demo6/mybatis-config.xml";
        //读取全局配置文件
        InputStream inputStream = Resources.getResourceAsStream(mybatisConfig);
        //构建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //鉴别器
    @Test
    public void getById1() throws IOException {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            //查询订单为1的
            OrderModel orderModel = mapper.getById1(1);
            log.info("{}", orderModel);
            log.info("------------------------------------------------------------");
            //查询订单为2的
            orderModel = mapper.getById1(2);
            log.info("{}", orderModel);
            log.info("------------------------------------------------------------");
            //查询订单为3的
            orderModel = mapper.getById1(3);
            log.info("{}", orderModel);
        }
    }

    //继承
    @Test
    public void getById2() throws IOException {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            //查询订单为1的
            OrderModel orderModel = mapper.getById2(1);
            log.info("{}", orderModel);
            log.info("------------------------------------------------------------");
            //查询订单为2的
            orderModel = mapper.getById2(2);
            log.info("{}", orderModel);
            log.info("------------------------------------------------------------");
            //查询订单为3的
            orderModel = mapper.getById2(3);
            log.info("{}", orderModel);
        }
    }

}