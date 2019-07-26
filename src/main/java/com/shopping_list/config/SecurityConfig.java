package com.shopping_list.config;

import com.shopping_list.AuthTokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService customUserDetailsService;

    @Autowired
    @Qualifier("authTokenConfig")
    private AuthTokenConfig authTokenConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String [] methodSecured={"/users/*","/shopping/**", "/api/shopping/**"};
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.csrf().disable()
                .authorizeRequests().antMatchers( "/login","/","/login/authenticate", "/api/shopping/all").permitAll()
                .antMatchers("/users/signup").permitAll()
                //.antMatchers("/api/shopping/**").hasAuthority("USER")
                .antMatchers(methodSecured).authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error=true").permitAll()
                .and().logout().deleteCookies("JSESSIONID").logoutUrl("/logout").logoutSuccessUrl("/login");

        http.apply(authTokenConfig);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**","/static/**","/css/**","/js/**","/images/**");
    }
}
