package com.polaris.lesscode.permission.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus Config
 *
 * @author roamer
 * @version v1.0
 * @date 2020-08-31 10:30
 */
@Configuration
@MapperScan(value = {"com.polaris.**.mapper*"})
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // 设置sql的limit为无限制，默认是500
        return new PaginationInterceptor().setLimit(-1);
    }

}
