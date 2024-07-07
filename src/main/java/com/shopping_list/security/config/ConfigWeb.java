package com.shopping_list.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class ConfigWeb {

    @Order(2)
    @Configuration
    public static class ActuatorConfiguration {

        @Autowired
        private DataSource dataSource;

        @Value("${spring.queries.users-query}")
        private String usersQuery;

        @Value("${spring.queries.roles-query}")
        private String rolesQuery;

        @Bean(name = "actuatorPasswordEncoder")
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean(name = "actuatorAuthenticationManager")
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .usersByUsernameQuery(usersQuery)
                    .authoritiesByUsernameQuery(rolesQuery)
                    .passwordEncoder(passwordEncoder());
            return auth.build();
        }

        @Bean(name = "actuatorSecurityFilterChain")
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/css/**", "/vendor/**", "/login", "/user/registration", "/user/save").permitAll()
                            .requestMatchers("/shopping/**").hasAuthority("USER")
                            .anyRequest().authenticated())
                    .formLogin(form -> form
                            .loginPage("/login")
                            .failureUrl("/login?error=true")
                            .defaultSuccessUrl("/")
                            .usernameParameter("username")
                            .passwordParameter("password"))
                    .logout(logout -> logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")))
                    .csrf(csrf -> csrf.disable());

            return http.build();
            
        }

        @Bean(name = "actuatorWebSecurityCustomizer")
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring()
                    .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }
    }
}
