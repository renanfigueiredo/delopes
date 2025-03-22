package br.com.delopes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desativa CSRF, caso não seja necessário
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/**").permitAll())

//                // Configuração de autorização de URLs
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        // Permite o acesso público à página inicial, login, cadastro e arquivos estáticos (CSS, JS, imagens)
//                        .requestMatchers("/", "/login", "/usuarios/cadastro", "/public/**", "/css/**", "/js/**", "/img/**").permitAll()
//                        // Usuários com ROLE_USER podem acessar URLs relacionadas a processos
//                        .requestMatchers("/processos/**").hasRole("USER")
//                        // Somente ADMIN pode acessar URLs relacionadas à administração
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        // Todas as outras URLs exigem autenticação
//                        .anyRequest().authenticated()
//                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/error")
                )

                // Configuração do formulário de login
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // Página de login personalizada
                        .permitAll()  // Permite acesso público à página de login
                )

                // Configuração de logout
                .logout(LogoutConfigurer::permitAll)  // Permite logout público

                // Configuração de tratamento de exceções
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usando o BCryptPasswordEncoder
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> response.sendRedirect("/access-denied");
    }
}