package br.com.delopes.service;

import br.com.delopes.model.Processo;
import br.com.delopes.model.Usuario;
import br.com.delopes.repository.ProcessoRepository;
import br.com.delopes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessoService {

    private final ProcessoRepository processoRepository;
    private final UsuarioRepository usuarioRepository;

    // Lista todos os processos (Admin pode ver todos, User só os seus)
    public List<Processo> listarTodos() {
        // Pega o nome do usuário logado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if ("ADMIN".equals(usuario.getRole())) {
            // Admin pode ver todos os processos
            return processoRepository.findAll();
        } else {
            // Usuário pode ver apenas seus próprios processos
            return processoRepository.findByClienteCpfOuCnpj(usuario.getCpfOuCnpj());
        }
    }

    // Busca um processo pelo ID
    public Optional<Processo> buscarPorId(String id) {
        // Pega o nome do usuário logado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Optional<Processo> processo = processoRepository.findById(id);

        // Verifica se o processo pertence ao usuário
        if ("USER".equals(usuario.getRole()) && processo.isPresent() && !processo.get().getClienteCpfOuCnpj().equals(usuario.getCpfOuCnpj())) {
            throw new RuntimeException("Usuário não autorizado a visualizar este processo.");
        }

        return processo;
    }

    // Salva ou atualiza um processo
    public void salvarProcesso(Processo processo) {
        // Pega o nome do usuário logado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o usuário está tentando modificar um processo que não é dele
        if ("USER".equals(usuario.getRole()) && !processo.getClienteCpfOuCnpj().equals(usuario.getCpfOuCnpj())) {
            throw new RuntimeException("Usuário não autorizado a modificar este processo.");
        }

        // Gera um novo ID se estiver vazio
        if (processo.getId() == null || processo.getId().isEmpty()) {
            processo.setId(UUID.randomUUID().toString());
        }

        processoRepository.save(processo);
    }

    // Exclui um processo pelo ID
    public void excluirProcesso(String id) {
        // Pega o nome do usuário logado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Optional<Processo> processo = processoRepository.findById(id);

        // Verifica se o processo pertence ao usuário
        if ("USER".equals(usuario.getRole()) && processo.isPresent() && !processo.get().getClienteCpfOuCnpj().equals(usuario.getCpfOuCnpj())) {
            throw new RuntimeException("Usuário não autorizado a excluir este processo.");
        }

        processoRepository.deleteById(id);
    }

    // Método para buscar processos por status (ex: "Em andamento", "Concluído")
    public List<Processo> buscarPorStatus(String status) {
        return processoRepository.findByStatus(status);
    }

    // Método para buscar processos por tipo de ação (ex: "Ação de cobrança")
    public List<Processo> buscarPorTipoAcao(String tipoAcao) {
        return processoRepository.findByTipoAcao(tipoAcao);
    }

    // Método para buscar processos por advogado
    public List<Processo> buscarPorAdvogado(String advogado) {
        return processoRepository.findByAdvogado(advogado);
    }
}
