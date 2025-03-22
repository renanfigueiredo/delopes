package br.com.delopes.controller;

import br.com.delopes.exception.ValidacaoException;
import br.com.delopes.model.Usuario;
import br.com.delopes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarOuAtualizar(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (usuario.getId() != null && !usuario.getId().isEmpty()) {
                Usuario usuarioAtualizado = usuarioService.atualizarUsuario(usuario);
                return "redirect:/usuarios/detalhes/" + usuarioAtualizado.getId();
            } else {
                Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
                return "redirect:/usuarios/detalhes/" + usuarioSalvo.getId();
            }
        } catch (ValidacaoException e) {
            model.addAttribute("erros", e.getErros());
            model.addAttribute("usuario", usuario);
            return "usuarios/cadastro";
        }
    }

    @GetMapping("/detalhes/{id}")
    public String mostrarDetalhes(@PathVariable String id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuarios/detalhes";
        } else {
            model.addAttribute("erro", "Usuário não encontrado.");
            return "error";
        }
    }

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable String id) {
        usuarioService.excluirUsuario(id);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable String id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuarios/cadastro";
        } else {
            model.addAttribute("erro", "Usuário não encontrado.");
            return "error";
        }
    }
}