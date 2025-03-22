package br.com.delopes.service;

import br.com.delopes.exception.ValidacaoException;
import br.com.delopes.model.Usuario;
import br.com.delopes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario) {
        ValidacaoException exception = new ValidacaoException();

        // Verifica se o email já está em uso
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            exception.adicionarErro("Email já está em uso.");
        }

        // Verifica se o CPF ou CNPJ já está em uso
        if (usuarioRepository.findByCpfOuCnpj(usuario.getCpfOuCnpj()).isPresent()) {
            exception.adicionarErro("CPF ou CNPJ já está em uso.");
        }

        if (!exception.getErros().isEmpty()) {
            throw exception;
        }

        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorCpfOuCnpj(String cpfOuCnpj) {
        return usuarioRepository.findByCpfOuCnpj(cpfOuCnpj);
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Object listarTodos() {
        return usuarioRepository.findAll();
    }

    public void excluirUsuario(String id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        ValidacaoException exception = new ValidacaoException();

        // Verifica se o email já está em uso por outro usuário
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
            exception.adicionarErro("Email já está em uso.");
        }

        // Verifica se o CPF ou CNPJ já está em uso por outro usuário
        usuarioExistente = usuarioRepository.findByCpfOuCnpj(usuario.getCpfOuCnpj());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
            exception.adicionarErro("CPF ou CNPJ já está em uso.");
        }

        if (!exception.getErros().isEmpty()) {
            throw exception;
        }

        // Codifica a senha antes de salvar, se a senha foi alterada
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            // Mantém a senha existente
            usuario.setSenha(usuarioRepository.findById(usuario.getId()).get().getSenha());
        }

        return usuarioRepository.save(usuario);
    }
}