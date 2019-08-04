package com.shopping_list.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

public class ConfigWeb {

    @Order(1)
    @Configuration
    public static class ActuatorConfiguration  extends WebSecurityConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
            auth.
                    jdbcAuthentication()
                    .usersByUsernameQuery(usersQuery)
                    .authoritiesByUsernameQuery(rolesQuery)
                    .dataSource(dataSource)
                    .passwordEncoder(encoder);
        }


        @Value("${spring.queries.users-query}")
        private String usersQuery;

        @Value("${spring.queries.roles-query}")
        private String rolesQuery;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.
                    authorizeRequests()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/vendor/**").permitAll()
                    .antMatchers("/login","/user/registration","/user/save").permitAll()
                    .antMatchers("/shopping/**").hasAuthority("USER").anyRequest()
                    .authenticated().and().csrf().disable().formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                    .defaultSuccessUrl("/")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }
    }
}
