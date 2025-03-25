package br.com.delopes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "processos")
public class  Processo {
    @Id
    private String id;
    private String numero;                        // Número do processo
    private String descricao;                     // Descrição do processo
    private String status;                        // Status do processo (ex: Em andamento, Concluído)
    private String clienteCpfOuCnpj;              // CPF ou CNPJ do cliente
    private String vara;                          // Vara em que o processo tramita
    private String comarca;                       // Comarca onde o processo está

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInicio;                      // Data de início do processo

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataJulgamento;                  // Data de julgamento do processo

    private String tipoAcao;                      // Tipo de ação (ex: Ação de cobrança)
    private List<String> partes;                  // Partes envolvidas no processo (autor, réu, etc.)
    private String advogado;                      // Nome do advogado responsável
    private String natureza;                      // Público ou privado
    private String sentenca;                     // Sentença do processo (ex: Sentença de mérito)
    private List<String> andamentos;              // Histórico de andamentos do processo
}
