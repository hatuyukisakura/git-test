package cn.itcast.dao.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private SqlSessionFactory factory;

    public UserDaoImpl(SqlSessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<User> findAll() {
        // 1. 获取session对象
        SqlSession session = factory.openSession();
        // SqlSession session = JDBCUtils.getSession();
        // 2. 执行session查询所有方法
        List<User> users = session.selectList("cn.itcast.dao.UserDao.findAll");
        // 2.5 关闭session,释放资源;
        session.close();
        int a=0;
        // 3. 返回查询结果
        return users;
    }
}
