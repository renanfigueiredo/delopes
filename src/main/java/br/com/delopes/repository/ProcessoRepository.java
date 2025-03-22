package br.com.delopes.repository;

import br.com.delopes.model.Processo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessoRepository extends MongoRepository<Processo, String> {

    // Busca processos por status
    List<Processo> findByStatus(String status);

    // Busca processos por tipo de ação
    List<Processo> findByTipoAcao(String tipoAcao);

    // Busca processos por advogado
    List<Processo> findByAdvogado(String advogado);

    // Buscar processos pelo CPF ou CNPJ do usuário
    List<Processo> findByClienteCpfOuCnpj(String clienteCpfOuCnpj);
}
