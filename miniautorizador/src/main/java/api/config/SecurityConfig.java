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
        http.csrf().disable() // Desabilita CSRF para APIs
                .authorizeRequests()
                // Protege todos os endpoints da API com autenticação básica (seja GET, POST, PUT, DELETE)
                .antMatchers("/cartoes/**", "/transacoes/**", "/**").authenticated() // Protege todos os endpoints /cartoes e /transacoes
                .and()
                .httpBasic() // Exige autenticação Basic Auth para todos os endpoints
                .and()
                .addFilterBefore(new SwaggerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Customização do filtro (se necessário)
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return username -> {
            // Usuário fixo para login (apenas para testes)
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
