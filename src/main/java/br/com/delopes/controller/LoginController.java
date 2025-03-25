package br.com.delopes.controller;

import br.com.delopes.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            return "redirect:/processos" + usuario.getId();
        }
        return "login";  // Retorna a página de login (login.html) se o usuário não estiver autenticado.
    }
}