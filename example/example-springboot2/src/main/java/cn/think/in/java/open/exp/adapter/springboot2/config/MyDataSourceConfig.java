package cn.think.in.java.open.exp.adapter.springboot2.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class MyDataSourceConfig implements WebMvcConfigurer {
    /**
     * 当向容器中添加了 Druid 数据源
     * 使用 @ConfigurationProperties 将配置文件中 spring.datasource 开头的配置与数据源中的属性进行绑定
     *
     * @return
     */
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("Cc123456_");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return druidDataSource;
    }
}