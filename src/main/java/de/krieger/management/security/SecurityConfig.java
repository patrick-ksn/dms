package de.krieger.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
@SuppressWarnings("unused")
public class SecurityConfig {
    /**
     * Configures a UserDetailsManager bean with a JDBC implementation.
     *
     * @param dataSource The DataSource to be used by the JDBC implementation.
     * @return UserDetailsManager configured with the provided DataSource.
     */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource)
    {
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http HttpSecurity object to configure security settings.
     * @return SecurityFilterChain configured with the specified security settings.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configure ->
                configure
                        .requestMatchers(HttpMethod.POST, "/api/author").hasRole(Role.ADMIN.getRole()) // insert
                        .requestMatchers(HttpMethod.GET, "/api/authors").hasRole(Role.AUTHOR.getRole()) // get All
                        .requestMatchers(HttpMethod.PUT, "/api/author/**").hasRole(Role.AUTHOR.getRole()) // update
                        .requestMatchers(HttpMethod.DELETE, "/api/author/**").hasRole(Role.ADMIN.getRole()) // delete
                        .requestMatchers(HttpMethod.DELETE, "/api/author/queue/**").hasRole(Role.ADMIN.getRole()) // delete queue
                        .requestMatchers(HttpMethod.GET, "/api/author/**").hasRole(Role.AUTHOR.getRole()) // get

                        .requestMatchers(HttpMethod.POST, "/api/document").hasRole(Role.AUTHOR.getRole()) // create
                        .requestMatchers(HttpMethod.GET, "/api/documents").hasRole(Role.AUTHOR.getRole()) // get All
                        .requestMatchers(HttpMethod.PUT, "/api/document/**").hasRole(Role.AUTHOR.getRole()) // update
                        .requestMatchers(HttpMethod.DELETE, "/api/document/**").hasRole(Role.ADMIN.getRole()) // delete
                        .requestMatchers(HttpMethod.GET, "/api/document/**").hasRole(Role.AUTHOR.getRole()) // get
                        .anyRequest().permitAll() // for swagger
        );

        // use http basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}