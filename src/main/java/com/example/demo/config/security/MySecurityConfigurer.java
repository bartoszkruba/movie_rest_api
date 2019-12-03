package com.example.demo.config.security;

import com.example.demo.config.security.handler.MySavedRequestAwareAuthenticationSuccessHandler;
import com.example.demo.config.security.handler.MySimpleUrlAuthenticationFailureHandler;
import com.example.demo.config.security.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MySecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final MyUserDetailsService myUserDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
    private final MySimpleUrlAuthenticationFailureHandler myFailureHandler;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;

    public MySecurityConfigurer(MyUserDetailsService myUserDetailsService, RestAuthenticationEntryPoint restAuthenticationEntryPoint, MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler, MySimpleUrlAuthenticationFailureHandler myFailureHandler, BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource) {
        this.myUserDetailsService = myUserDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.mySuccessHandler = mySuccessHandler;
        this.myFailureHandler = myFailureHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests();
        http.headers().frameOptions().disable();
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .formLogin().permitAll()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                .loginProcessingUrl("/api/login")
                .and()
                .logout().logoutUrl("/api/logout").permitAll()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
