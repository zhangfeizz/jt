package com.jt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/**
 * 如果启动 报 数据源相关的错误 则springboot程序启动时 会根据porm.xml文件中的数据源的jar包加载相关配置
 *  ，但是jt-web项目只引用jar包但是不需要连接数据源
 * 所以yml配置文件没有该配置，导致错误
 *
 *解决：
 *	exclude
 */
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class SpringBootRun {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootRun.class, args);
	}

}
