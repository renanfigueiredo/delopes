package br.com.delopes.config;

import br.com.delopes.model.Usuario;
import br.com.delopes.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Define as roles
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRole()) // A role pode ser "ADMIN" ou "USER"
        );

        // Retorna um objeto UserDetails com as roles
        return User.builder()
                .username(usuario.getEmail()) // Pode ser também CPF ou CNPJ, conforme necessário
                .password(usuario.getSenha()) // Senha já deve estar codificada
                .roles(usuario.getRole()) // "ADMIN" ou "USER"
                .authorities(authorities)
                .build();
    }
}
