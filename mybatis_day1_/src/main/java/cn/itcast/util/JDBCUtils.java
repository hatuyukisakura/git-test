package cn.itcast.util;

import java.io.IOException;
import java.io.InputStream;

public class JDBCUtils {

    private static SqlSessionFactory factory = null;

    private static InputStream is = null;

    static {
        // 1. 读取配置文件
        try {
            is = Resources.getResourceAsStream("SQlMapConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. 创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        factory = builder.build(is);
    }

    public static SqlSession getSession() {
        // 3. 使用工厂生产一个SqlSession对象
        return factory.openSession();
    }

    public static void close(SqlSession session) throws IOException {
        // 释放资源
        if (session != null)
            session.close();
        if (is != null)
            is.close();
    }
}
