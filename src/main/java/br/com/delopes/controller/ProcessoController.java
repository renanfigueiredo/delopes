package br.com.delopes.controller;

import br.com.delopes.model.Processo;
import br.com.delopes.model.Usuario;
import br.com.delopes.service.ProcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/processos")
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService processoService;

    @GetMapping
    public String listarProcessos(Model model, @AuthenticationPrincipal Usuario usuario) {
        List<Processo> processos;
        String role = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        processos = processoService.listarTodos();

        model.addAttribute("processos", processos);
        model.addAttribute("role", role);
        return "processos/listar";
    }

    // Página para criar um novo processo
    @GetMapping("/novo")
    public String novoProcesso(Model model) {
        model.addAttribute("processo", new Processo());
        return "processos/cadastro";
    }

    // Salva ou atualiza um processo
    @PostMapping("/salvar")
    public String salvarProcesso(@ModelAttribute Processo processo) {
        processoService.salvarProcesso(processo);
        return "redirect:/processos";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesProcesso(@PathVariable String id, Model model) {
        Processo processo = processoService.buscarPorId(id).orElse(null);
        model.addAttribute("processo", processo);
        return "processos/detalhes";
    }

    // Página para editar um processo existente
    @GetMapping("/editar/{id}")
    public String editarProcesso(@PathVariable String id, Model model) {
        model.addAttribute("processo", processoService.buscarPorId(id).orElse(new Processo()));
        return "processos/cadastro";
    }

    // Excluir um processo
    @GetMapping("/excluir/{id}")
    public String excluirProcesso(@PathVariable String id) {
        processoService.excluirProcesso(id);
        return "redirect:/processos";
    }
}
