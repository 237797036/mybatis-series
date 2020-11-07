package com.zj.chat05.demo9;

import com.zj.chat05.demo9.mapper.UserMapper;
import com.zj.chat05.demo9.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 结果自动映射各种案例
 */
@Slf4j
public class Demo9Test {
    private SqlSessionFactory sqlSessionFactory;

    public void before(String mybatisConfig) throws IOException {
        //读取全局配置文件
        InputStream inputStream = Resources.getResourceAsStream(mybatisConfig);
        //构建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 一级缓存测试
     *
     * @throws IOException
     */
    @Test
    public void level1CacheTest1() throws IOException {
        String mybatisConfig = "demo9/mybatis-config.xml";
        this.before(mybatisConfig);
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //第一次查询（会访问数据库）
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
            //第二次查询（直接从1级缓存中获取）
            List<UserModel> userModelList2 = mapper.getList1(null);
            log.info("{}", userModelList2);
            log.info("{}", userModelList1 == userModelList2); // true,是同一个对象
        }
    }

    /**
     * 增删改使一级缓存失效
     *
     * @throws IOException
     */
    @Test
    public void level1CacheTest2() throws IOException {
        String mybatisConfig = "demo9/mybatis-config.xml";
        this.before(mybatisConfig);
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //第一次查询
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
            //新增一条数据
            mapper.insert1(UserModel.builder().id(Integer.valueOf(System.currentTimeMillis() / 1000 + "")).name("路人").age(30).build());
            //第二次查询
            List<UserModel> userModelList2 = mapper.getList1(null);
            log.info("{}", userModelList2);
            log.info("{}", userModelList1 == userModelList2); //false
        }
    }

    /**
     * 调用sqlSession.clearCache()清理一级缓存
     *
     * @throws IOException
     */
    @Test
    public void level1CacheTest3() throws IOException {
        String mybatisConfig = "demo9/mybatis-config.xml";
        this.before(mybatisConfig);
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //第一次查询
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
            //调用clearCache方法清理当前SqlSession中的缓存
            sqlSession.clearCache();
            //第二次查询
            List<UserModel> userModelList2 = mapper.getList1(null);
            log.info("{}", userModelList2);
            log.info("{}", userModelList1 == userModelList2); // false
        }
    }

    /**
     * 将Mapper xml中select元素的flushCache属性置为true时，
     * 每次执行这个select元素对应的查询之前，mybatis会将当前SqlSession中一级缓存中的所有数据都清除
     *
     * @throws IOException
     */
    @Test
    public void level1CacheTest4() throws IOException {
        String mybatisConfig = "demo9/mybatis-config.xml";
        this.before(mybatisConfig);
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            //查询1：getList1   （访问db去拿数据，然后将其丢到一级缓存中）
            log.info("查询1 start");
            log.info("查询1：getList1->{}", mapper.getList1(null));

            System.out.println();

            //查询2：getList1   （从一级缓存中拿数据）
            log.info("查询2 start");
            log.info("查询2：getList1->{}", mapper.getList1(null));

            System.out.println();

            //查询3：getList2   （getList2的select元素，flushCache为true，先清空一级缓存中数据，即此时查询1放入缓存的数据会被清理掉，会访问db获取数据）
            log.info("查询3 start");  //（db）
            log.info("查询3：getList2->{}", mapper.getList2(null));

            System.out.println();

            //查询3-2：getList1   （db）
            log.info("查询3-2 start");
            log.info("查询3-2：getList1->{}", mapper.getList1(null));

            System.out.println();

            //查询4：getList2   （db）
            log.info("查询4 start");
            log.info("查询4：getList2->{}", mapper.getList2(null));

            System.out.println();

            //查询5：getList1   （db）
            log.info("查询5 start");
            log.info("查询5：getList1->{}", mapper.getList1(null));
        }
    }


    /**
     * 二级缓存测试
     *
     * 1去这个namespace对应的二级缓存中去查询数据，没有查询到
     * 2然后去db中访问数据，会将db中返回的数据放在一级缓存中
     * 3第一次运行完毕之后会自动调用SqlSession的close方法，调用此方法会将db中返回的数据会被丢到二级缓存中
     * 4第二次查询时就直接从二级缓存中获取到数据
     */
    @Test
    public void level2CacheTest1() throws IOException {
        String mybatisConfig = "demo9/mybatis-config1.xml";
        this.before(mybatisConfig);
        // Cache Hit Ratio [mapper xml的namespace值] xx
        // 第一次命中率：0   日志：Cache Hit Ratio [com.zj.chat05.demo9.mapper.UserMapper]: 0.0
        // 第二次命中率：0.5(两次查询命中了一次) 日志：Cache Hit Ratio [com.zj.chat05.demo9.mapper.UserMapper]: 0.5
        for (int i = 0; i < 2; i++) {
            try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true)) {
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                List<UserModel> userModelList1 = mapper.getList1(null);
                log.info("{}", userModelList1);
            }
        }

        try {

        } catch (Exception e) {

        }
    }

    /**
     * 增删改会清除二级缓存中的数据
     *
     * @throws IOException
     */
    @Test
    public void level2CacheTest2() throws IOException {
        String mybatisConfig = "demo9/mybatis-config1.xml";
        this.before(mybatisConfig);
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
        }

        System.out.println();

        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //新增一条数据
            mapper.insert1(UserModel.builder().id(Integer.valueOf(System.nanoTime() % 100000 + "")).name("路人").age(30).build());
        }

        System.out.println();

        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
        }
    }

    /**
     * select元素的flushCache属性置为true，会先清空二级缓存中的数据，然后再去db中查询数据
     *
     * @throws IOException
     */
    @Test
    public void level2CacheTest3() throws IOException {
        String mybatisConfig = "demo9/mybatis-config1.xml";
        this.before(mybatisConfig);
        //先查询2次getList1,getList1第二次会从二级缓存中拿到数据
        log.info("getList1查询");
        for (int i = 0; i < 2; i++) {
            try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                List<UserModel> userModelList1 = mapper.getList1(null);
                log.info("{}", userModelList1);
            }
        }

        System.out.println();

        //getList2的flushCache为true，所以查询之前会先将对应的二级缓存中的所有数据清空，所以二次都会访问db
        log.info("getList2查询");
        for (int i = 0; i < 2; i++) {
            try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                List<UserModel> userModelList1 = mapper.getList2(null);
                log.info("{}", userModelList1);
            }
        }

        System.out.println();

        //二级缓存中没有getList1需要查找的数据了，所以这次访问getList1会去访问db
        log.info("getList1查询");
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
        }
    }


    /**
     * select元素的useCache置为false，当前查询会跳过二级缓存，但不会清除二级缓存数据
     *
     * @throws IOException
     */
    @Test
    public void level2CacheTest4() throws IOException {
        String mybatisConfig = "demo9/mybatis-config1.xml";
        this.before(mybatisConfig);
        //第一次查询访问getList1，会将数据丢到二级缓存中
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
        }

        System.out.println();

        //getList3对应的select的useCache为false，会跳过二级缓存，所以会直接去访问db
        for (int i = 0; i < 2; i++) {
            try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                List<UserModel> userModelList1 = mapper.getList3(null);
                log.info("{}", userModelList1);
            }
        }

        System.out.println();

        //下面的查询又去执行了getList1，由于上面的第一次查询也是访问getList1会将数据放在二级缓存中，所以下面的查询会从二级缓存中获取到数据
        try (SqlSession sqlSession = this.sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<UserModel> userModelList1 = mapper.getList1(null);
            log.info("{}", userModelList1);
        }
    }
}