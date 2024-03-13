package org.example.restapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        UserDetails userAdmin = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN")
                .build();
        UserDetails userPosts = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("POSTS")
                .build();

        manager.createUser(userAdmin);
        manager.createUser(userPosts);

        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/posts/**").hasAnyRole("POSTS", "ADMIN")
                .requestMatchers("/api/users/**").hasAnyRole("USERS", "ADMIN")
                .requestMatchers("/api/albums/**").hasAnyRole("ALBUMS", "ADMIN")
                .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);;
        return http.build();
    }
}
