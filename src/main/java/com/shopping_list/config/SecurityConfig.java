package com.shopping_list.config;


import com.shopping_list.security.jwt.JwtEntryPoint;
import com.shopping_list.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@DependsOn("passwordEncoder")
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    private JwtEntryPoint accessDenyHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static  final String[] PUBLIC_MATCHES = {
            "/api**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.

                authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/save").permitAll()
                .antMatchers(PUBLIC_MATCHES).permitAll().anyRequest().authenticated()
                .antMatchers("/api/shopping/**").access("hasAnyRole('USER')")
                .antMatchers("/shopping/**").hasAuthority("USER").anyRequest()
                .authenticated().and().csrf().disable().cors().disable().httpBasic().and().formLogin()
                .loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .exceptionHandling().authenticationEntryPoint(accessDenyHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
       /* http.cors().and().csrf().disable()
                .authorizeRequests()

                .antMatchers(PUBLIC_MATCHES).permitAll().anyRequest().authenticated()
                .antMatchers("/api/shopping/**").access("hasAnyRole('USER')")
                .anyRequest().permitAll()

                .and()
                .exceptionHandling().authenticationEntryPoint(accessDenyHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);*/
    }







    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**","/static/**","/css/**","/js/**","/images/**");
    }

}
