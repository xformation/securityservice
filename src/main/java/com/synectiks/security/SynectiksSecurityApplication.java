/**
 * 
 */
package com.synectiks.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import com.synectiks.commons.utils.IUtils;
import com.synectiks.security.config.Constants;

import io.github.jhipster.config.JHipsterConstants;

/**
 * @author Rajesh
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.synectiks")
public class SynectiksSecurityApplication implements InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(SynectiksSecurityApplication.class);

	private static ConfigurableApplicationContext ctx;

	private final Environment env;

	public SynectiksSecurityApplication(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) {
		ctx = SpringApplication.run(SynectiksSecurityApplication.class, args);
		logApplicationStartup(ctx.getEnvironment());
		for (String bean : ctx.getBeanDefinitionNames()) {
			logger.info("Bean: " + bean);
		}
	}

	/**
	 * Initializes search.
	 * <p>
	 * Spring profiles can be configured with a program argument
	 * --spring.profiles.active=your-active-profile
	 * <p>
	 * You can find more information on how profiles work with JHipster on
	 * <a href=
	 * "https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<String> activeProfiles = Arrays
				.asList(env.getActiveProfiles());
		if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
				&& activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
			logger.error("You have misconfigured your application! It should not run "
					+ "with both the 'dev' and 'prod' profiles at the same time.");
		}
		if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
				&& activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
			logger.error("You have misconfigured your application! It should not "
					+ "run with both the 'dev' and 'cloud' profiles at the same time.");
		}
	}

	private static void logApplicationStartup(Environment env) {
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		String serverPort = env.getProperty("server.port");
		Constants.PORT = serverPort; 
		String contextPath = env.getProperty("server.servlet.context-path");
		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.warn(
					"The host name could not be determined, using `localhost` as fallback");
		}
		logger.info("\n----------------------------------------------------------\n\t"
				+ "Application '{}' is running! Access URLs:\n\t"
				+ "Local: \t\t{}://localhost:{}{}\n\t" + "External: \t{}://{}:{}{}\n\t"
				+ "Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"), protocol, serverPort,
				contextPath, protocol, hostAddress, serverPort, contextPath,
				env.getActiveProfiles());
		Constants.HOST = env.getProperty("security.server.ip");
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
