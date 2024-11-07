package api.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@WebFilter("/*")
public class SwaggerAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BASIC = "Basic ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Verifica se a requisição possui o cabeçalho de autorização
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BASIC)) {
            String base64Credentials = authHeader.substring(BASIC.length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            // Valida se o usuário e a senha estão corretos
            if ("username".equals(username) && "password".equals(password)) {
                // Cria o token de autenticação
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, password, null); // Não há roles nesse caso
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua com o filtro da requisição
        filterChain.doFilter(request, response);
    }
}
