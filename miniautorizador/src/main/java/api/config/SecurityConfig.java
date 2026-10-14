package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()

                .antMatchers("/cartoes/**", "/transacoes/**", "/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(new SwaggerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return username -> {

            if ("username".equals(username)) {
                return User.builder()
                        .username("username")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .build();
            }
            throw new IllegalArgumentException("Usuário não encontrado");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
