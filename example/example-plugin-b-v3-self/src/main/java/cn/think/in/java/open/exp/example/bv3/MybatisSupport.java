package cn.think.in.java.open.exp.example.bv3;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Slf4j
@Component
public class MybatisSupport {


    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    static Set<Class<?>> cache = new HashSet<>();

    public MybatisSupport() {
    }

    /**
     * 获取 mapper
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T doGetMapper(Class<T> clazz) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(MybatisSupport.class.getClassLoader());
        sqlSessionTemplate.getConfiguration().addMapper(clazz);
        Thread.currentThread().setContextClassLoader(contextClassLoader);
        cache.add(clazz);
        return sqlSessionTemplate.getMapper(clazz);
    }

    /**
     * mybatis 有缓存, 需要删除.
     */
    @PreDestroy
    public void dest() {
        log.info("MybatisUtil PreDestroy------>>>>");
        if (sqlSessionTemplate.getConfiguration() instanceof MybatisConfiguration) {
            MybatisConfiguration mpc = (MybatisConfiguration) sqlSessionTemplate.getConfiguration();
            for (Class<?> aClass : cache) {
                mpc.removeMapper(aClass);
                log.info("removeMapper {} ------>>>>", aClass.getName());
            }
        }
    }
}
