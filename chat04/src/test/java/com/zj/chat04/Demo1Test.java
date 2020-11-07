package com.zj.chat04;

import com.zj.chat04.demo1.mapper.UserMapper;
import com.zj.chat04.demo1.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;

@Slf4j
public class Demo1Test {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        //指定mybatis全局配置文件
        String resource = "demo1/mybatis-config.xml";
        //读取全局配置文件
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //构建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Test
    public void insertUser() {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //创建UserModel对象
            UserModel userModel = UserModel.builder().name("郭富城").age(30).salary(50000D).sex(1).build();
            //执行插入操作
            int insert = mapper.insertUser(userModel);
            log.info("影响行数：{}", insert);
            log.info("{}", userModel);
        }
    }

    @Test
    public void updateUser() {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //创建UserModel对象
            UserModel userModel = UserModel.builder().id(1L).name("路人甲Java，你好").age(18).salary(5000D).sex(0).build();
            //执行更新操作
            long result = mapper.updateUser(userModel);
            log.info("影响行数：{}", result);
        }
    }

    @Test
    public void deleteUser() {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //定义需要删除的用户id
            Long userId = 1L;
            //执行删除操作
            boolean result = mapper.deleteUser(userId);
            log.info("第1次删除:id={},返回值:{}", userId, result);
            result = mapper.deleteUser(userId);
            log.info("第2次删除:id={},返回值:{}", userId, result);
        }
    }

    private String jdbcDriver = "com.mysql.jdbc.Driver";
    private String jdbcUrl = "jdbc:mysql://localhost:3306/zj?characterEncoding=UTF-8";
    private String jdbcUserName = "root";
    private String jdbcPassword = "root";

    @Test
    public void jdbcInsertUser1() throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet generatedKeys = null;
        try {

            UserModel userModel = UserModel.builder().name("黎明").age(30).salary(50000D).sex(1).build();
            //执行jdbc插入数据操作
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
            //注意创建PreparedStatement的时候，使用prepareStatement方法的第二个参数需要指定Statement.RETURN_GENERATED_KEYS
            preparedStatement = connection.prepareStatement("INSERT INTO t_user (name,age,salary,sex) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            int parameterIndex = 1;
            preparedStatement.setString(parameterIndex++, userModel.getName());
            preparedStatement.setInt(parameterIndex++, userModel.getAge());
            preparedStatement.setDouble(parameterIndex++, userModel.getSalary());
            preparedStatement.setInt(parameterIndex++, userModel.getSex());
            int count = preparedStatement.executeUpdate();
            log.info("影响行数：{}", count);

            //获取自增值
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys != null && generatedKeys.next()) {
                log.info("自增值为：{}", generatedKeys.getInt(1));
            }
        } finally {
            if (generatedKeys != null && generatedKeys.isClosed()) {
                generatedKeys.close();
            }
            if (preparedStatement != null && preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Test
    public void jdbcInsertUser2() throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            UserModel userModel = UserModel.builder().name("梁朝伟").age(30).salary(50000D).sex(1).build();
            //执行jdbc插入数据操作
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
            //注意创建PreparedStatement的时候，使用prepareStatement方法的第二个参数需要指定Statement.RETURN_GENERATED_KEYS
            preparedStatement = connection.prepareStatement("INSERT INTO t_user (name,age,salary,sex) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            int parameterIndex = 1;
            preparedStatement.setString(parameterIndex++, userModel.getName());
            preparedStatement.setInt(parameterIndex++, userModel.getAge());
            preparedStatement.setDouble(parameterIndex++, userModel.getSalary());
            preparedStatement.setInt(parameterIndex++, userModel.getSex());
            int count = preparedStatement.executeUpdate();
            log.info("影响行数：{}", count);

            //通过查询获取自增值
            rs = connection.prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
            if (rs != null && rs.next()) {
                log.info("自增值为：{}", rs.getInt(1));
            }
        } finally {
            if (rs != null && rs.isClosed()) {
                rs.close();
            }
            if (preparedStatement != null && preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Test
    public void insertUser1() {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //创建UserModel对象
            UserModel userModel = UserModel.builder().name("陈宝国").age(30).salary(50000D).sex(1).build();
            //执行插入操作
            int insert = mapper.insertUser1(userModel);
            log.info("影响行数：{}", insert);
            log.info("{}", userModel);
        }
    }

    @Test
    public void insertUser2() {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //创建UserModel对象
            UserModel userModel = UserModel.builder().name("周润发").age(30).salary(50000D).sex(1).build();
            //执行插入操作
            int insert = mapper.insertUser2(userModel);
            log.info("影响行数：{}", insert);
            log.info("{}", userModel);
        }
    }
    @Test
    public void total() {
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            for (int i = 0; i < 2; i++) {
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                Object total = mapper.total();
                System.out.println(Collection.class.isAssignableFrom(total.getClass()));
                log.info("{}", total);
                sqlSession.clearCache();
            }
        }
    }
}
