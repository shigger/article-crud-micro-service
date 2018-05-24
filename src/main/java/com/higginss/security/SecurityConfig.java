package com.higginss.security;

import javafx.beans.binding.When;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Authentication : User --> Roles
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
                .withUser("user1").password("secret1")
                .roles("USER").and().withUser("admin1").password("secret1")
                .roles("USER", "ADMIN");
    }

    // Authorization : Role -> Access
    protected void configure(HttpSecurity http) throws Exception {
        // So by default a session is created holding authentication state so once validated basic auth details are
        // sticky and not required - in order to force basic auth on every request make sessions stateless which is
        // good design for REST APIs albeit at a little loss of performance. Aslo time token authentication to consider
        // which could be implemented (safer/stronger than sticky session cookies).
        // When should you use CSRF protection? Our recommendation is to use CSRF protection for any request that could
        // be processed by a browser by normal users. If you are only creating a service that is used by non-browser
        // clients, you will likely want to disable CSRF protection. CSRF protection makes Spring Security generate and
        // send a token to the browser. The browser has to send it back to prove that he, not someone else, is talking
        // to the server. This is an extra layer of protection that is enabled by default by Spring Security.

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/api/v1/article/**").hasRole("USER") // All API calls must be users.
                .antMatchers("/**").hasRole("ADMIN").and()         // Other resources must be admin.
                .headers().frameOptions().disable();
    }

}