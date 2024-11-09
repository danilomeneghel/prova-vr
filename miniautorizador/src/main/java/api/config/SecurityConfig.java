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
        // Autenticação básica
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated() // Protege o Swagger
                .anyRequest().permitAll() // Permite o acesso para outras URLs
                .and()
                .httpBasic() // Habilita autenticação básica
                .and()
                .addFilterBefore(new SwaggerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Customização do filtro
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return username -> {
            // Usuário fixo para login
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
