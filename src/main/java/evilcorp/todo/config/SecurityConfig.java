package evilcorp.todo.config;

import evilcorp.todo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/api/users/register", "/login").permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(formLogin -> formLogin.loginPage("/login").permitAll())
                .logout(LogoutConfigurer::permitAll);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
