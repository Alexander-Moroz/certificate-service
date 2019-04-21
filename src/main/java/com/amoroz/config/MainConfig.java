package com.amoroz.config;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@PropertySources(
        @PropertySource("classpath:/hibernate.properties")
)
@Import({DBConfig.class, JPAConfig.class, WebMvcConfig.class, JmsConfig.class})
@ComponentScan("com.amoroz")
@EnableWebMvc
@EnableJpaRepositories("com.amoroz.repository")
@EnableJms
@EnableScheduling
@EnableAsync
public class MainConfig {
}