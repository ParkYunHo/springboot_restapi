package com.naver.capcha;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(value= {"com.naver.capcha.rest.mapper"})
public class CapchaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapchaApplication.class, args);
	}
	
	@Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception{
        SqlSessionFactoryBean sf = new SqlSessionFactoryBean();
        
        sf.setDataSource(ds);
        return sf.getObject();
    }
}
