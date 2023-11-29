package com.example.ram.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.ram.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserService userService;

    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider());

    }
    
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(
                "/registration/**",
                "/conocenos",
                "/js/**",
                "/css/**",
                "/img/**",
                "/style.css",
                "/function.js",
                "/pa.js",
                "/K.png",
                "/con1.jpg",
                "/con2.jpg",
                "/main.gif",
                "/gif2.gif",
                "/gif3.gif",
                "/", 
                "/indexx",
                "/registrationAdmin",
                "/registrationAdmin/gesServicios/**",
                "/registrationAdmin/inputResetPassword/**",
                "/registrationAdmin/recuperarContra/**",
                "/registration/pagarServiciosDelCarritoo/**").permitAll()
            .antMatchers("/login/google", "/oauth2/authorization/google").permitAll()
            .antMatchers("/index").authenticated()
            .antMatchers("/gestionAdmin").authenticated()
            .antMatchers("/gestionAdmin").hasAuthority("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                        if (auth.getAuthority().equals("USER")) {
                            String email = authentication.getName();
                            response.sendRedirect("/index?email=" + email);
                            return;
                        } else if (auth.getAuthority().equals("ADMIN")) {
                        	String email = authentication.getName();
                        	request.getSession().setAttribute("email", email);
                            response.sendRedirect("/gestionAdmin");
                            return;
                        }
                    }
                })
                .permitAll()

            .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/login/oauth2/success", true)
                .userInfoEndpoint()
                .oidcUserService(oidcUserService());
    }
    
    
    
    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }


}
