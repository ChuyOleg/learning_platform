package com.oleh.chui.learning_platform.config;

import com.oleh.chui.learning_platform.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class    SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonService personService;

    public SecurityConfig(@Lazy PersonService personService) {
        this.personService = personService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personService);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users").hasAuthority("ADMIN")
                .antMatchers("/course/all").permitAll()
                .antMatchers("/course/all/filter").permitAll()
                .antMatchers("/course/learning/*").hasAuthority("USER")
                .antMatchers("/course/new").hasAuthority("USER")
                .antMatchers("/course/material/new").hasAuthority("USER")
                .antMatchers("/course/question/new").hasAuthority("USER")
                .antMatchers("/course/createdCourses").hasAuthority("USER")
                .antMatchers("/course/purchased").hasAuthority("USER")
                .antMatchers("/course/buy/*").hasAuthority("USER")
                .antMatchers("/course/*").permitAll()
                .and()
            .exceptionHandling().accessDeniedPage("/course/all")
                .and()
            .formLogin(form -> form
                        .defaultSuccessUrl("/course/all")
                        .loginPage("/login")
                        .failureUrl("/login?error=true"))
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

    }

}
