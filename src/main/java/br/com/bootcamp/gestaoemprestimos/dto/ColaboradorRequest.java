package br.com.bootcamp.gestaoemprestimos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ColaboradorRequest(@NotBlank String nome, @NotBlank @Email String email, @NotBlank String cargo) {
}
