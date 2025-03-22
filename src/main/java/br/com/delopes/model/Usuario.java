package br.com.delopes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String nome;
    private String cpfOuCnpj;
    private String email;
    private String senha;
    private String role;
}