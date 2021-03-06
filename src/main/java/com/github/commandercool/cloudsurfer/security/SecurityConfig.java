package com.github.commandercool.cloudsurfer.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "spring.security", value = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
                .antMatchers("/subject/v1/**", "/user/v1/**", "/mri/v1/**", "/container/v1/**").authenticated().and()
                .oauth2ResourceServer().jwt();
    }

}
