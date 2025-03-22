package br.com.delopes.controller;

import br.com.delopes.model.Processo;
import br.com.delopes.model.Usuario;
import br.com.delopes.service.ProcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Controller
@RequestMapping("/processos")
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService processoService;

    // Para ADMIN, lista todos os processos
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String listarProcessos(Model model) {
        List<Processo> processos = processoService.listarTodos(); // Lista todos os processos (Admin pode ver todos)
        model.addAttribute("processos", processos);
        return "processos/lista";
    }

    // Para USER, lista apenas os processos do usuário logado
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/meus")
    public String listarProcessosDoUsuario(@AuthenticationPrincipal Usuario usuario, Model model) {
        // Aqui, usa-se o método listarTodos() que já filtra os processos com base no CPF/CNPJ do usuário
        List<Processo> processos = processoService.listarTodos(); // Processos filtrados já no serviço
        model.addAttribute("processos", processos);
        return "processos/lista";
    }

    // Página para criar um novo processo
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/novo")
    public String novoProcesso(Model model) {
        model.addAttribute("processo", new Processo());
        return "processos/form";
    }

    // Salva ou atualiza um processo
    @PostMapping("/salvar")
    public String salvarProcesso(@ModelAttribute Processo processo) {
        processoService.salvarProcesso(processo);
        return "redirect:/processos";
    }

    // Página para editar um processo existente
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/editar/{id}")
    public String editarProcesso(@PathVariable String id, Model model) {
        model.addAttribute("processo", processoService.buscarPorId(id).orElse(new Processo()));
        return "processos/form";
    }

    // Excluir um processo
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/excluir/{id}")
    public String excluirProcesso(@PathVariable String id) {
        processoService.excluirProcesso(id);
        return "redirect:/processos";
    }
}
