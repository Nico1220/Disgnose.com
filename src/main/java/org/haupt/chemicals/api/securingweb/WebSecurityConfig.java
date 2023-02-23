package org.haupt.chemicals.api.securingweb;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/contact.html", "/addWarenkorb", "/deleteWarenkorb").hasAnyRole("USER" ,"ADMIN", "MITARBEITER", "CUSTOMER")
                        .antMatchers("/","/register","process_register").permitAll()
                        .antMatchers("/users", "/bestellungen", "/deleteProduct", "/addProduct", "/saveProduct", "/saveOrder", "/showUpdateUser", "/saveUser", "/showSpecificOrder").hasAnyRole("MITARBEITER","ADMIN")
                        .antMatchers("/api/**", "/users").hasRole("ADMIN")
                        .antMatchers("/bestellen").hasAnyRole("ADMIN", "MITARBEITER", "CUSTOMER")
//                        .and()

                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )

                .logout((logout) -> logout
                        .permitAll()
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
