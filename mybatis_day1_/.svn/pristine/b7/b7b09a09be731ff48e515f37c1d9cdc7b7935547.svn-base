package cn.itcast.test;

import cn.itcast.dao.UserDao;
import cn.itcast.dao.impl.UserDaoImpl;
import cn.itcast.domain.User;
import cn.itcast.util.JDBCUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * mybatis入门案例
 */
public class MybatisTest {
    /**
     * 入门案例
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // 1. 读取配置文件
        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2. 创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(is);
        // 3. 使用工厂生产一个SqlSession对象
        /*SqlSession session = factory.openSession();*/
        // 4. 使用SqlSession创建Dao接口的代理对象
        /*UserDao dao = session.getMapper(UserDao.class);*/
        // 4.5 使用工厂创建dao的对象
        UserDao dao = new UserDaoImpl(factory);
        // 5. 使用代理对象执行方法
        List<User> users = dao.findAll();
        users.forEach(System.out::println);
        // 6. 释放资源
        // session.close();
        is.close();
        /*SqlSession session = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("SQlMapConfig.xml")).
                openSession();*/
        /*SqlSession session = JDBCUtils.getSession();
        session.getMapper(UserDao.class).findAll().forEach(System.out::println);
        JDBCUtils.close(session);
		SVN版本控制系统
		修改2
		*/
    }

    @Test
    public void test1() {

    }
}
