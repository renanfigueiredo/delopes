package br.com.delopes.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidacaoException extends RuntimeException {
    private final List<String> erros;

    public ValidacaoException() {
        this.erros = new ArrayList<>();
    }

    public void adicionarErro(String erro) {
        this.erros.add(erro);
    }

    public List<String> getErros() {
        return erros;
    }
}