package com.example.backend;

import com.example.backend.shared.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration.class
})
@EnableCaching
public class BackendApplication extends SpringBootServletInitializer implements WebMvcConfigurer, CommandLineRunner {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")
                .allowedOrigins(Constant.CROSS_ORIGIN_ALLOW_LIST)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);
    }

    @Override
    public void run(String... args) throws Exception {
    }

    @Bean
    public String uploadFolder() {
        return environment.getProperty("upload-folder");
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendApplication.class);

    ;
    @Autowired
    public Environment environment;
//    @Autowired
//    private AzureService azureService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
