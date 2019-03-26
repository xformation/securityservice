/**
 * 
 */
package com.synectiks.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.synectiks")
public class SynectiksSecurityApplication {

	private static final Logger logger = LoggerFactory
			.getLogger(SynectiksSecurityApplication.class);

	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		ctx = SpringApplication.run(SynectiksSecurityApplication.class, args);
		for (String bean : ctx.getBeanDefinitionNames()) {
			logger.info("Bean: " + bean);
		}
	}

	/**
	 * Method to return a bean from running config.
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(Class<T> cls) {
		if (!IUtils.isNull(ctx)) {
			return ctx.getBean(cls);
		}
		return null;
	}

}
