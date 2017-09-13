package com.avparamonov.checkers.configurations;

import com.avparamonov.checkers.controllers.Api;
import com.avparamonov.checkers.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new PlayerService();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceBean())
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers(Api.ROOT_PATH).permitAll()
                .antMatchers(Api.ROOT_PATH + Api.V1.LOGIN).permitAll()
                .antMatchers(Api.ROOT_PATH + Api.V1.REGISTRATION).permitAll()
                .antMatchers(Api.ROOT_PATH + Api.V1.ADMIN + "/**").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage(Api.ROOT_PATH + Api.V1.LOGIN).failureUrl(Api.ROOT_PATH + Api.V1.LOGIN + "?error=true")
                .defaultSuccessUrl(Api.ROOT_PATH + Api.V1.GAME)
                .usernameParameter("nickname")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(Api.ROOT_PATH + Api.V1.LOGOUT))
                .logoutSuccessUrl(Api.ROOT_PATH + Api.V1.HOME).and().exceptionHandling()
                .accessDeniedPage(Api.ROOT_PATH + Api.V1.ERROR);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(Api.ROOT_PATH + "/resources/**",
                        Api.ROOT_PATH + "/static/**",
                        Api.ROOT_PATH + "/css/**",
                        Api.ROOT_PATH + "/js/**",
                        Api.ROOT_PATH + "/images/**");
    }

}
