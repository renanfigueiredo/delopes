package br.com.delopes.repository;

import br.com.delopes.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpfOuCnpj(String cpfOuCnpj);
}
