package br.com.bootcamp.gestaoemprestimos;

import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipamentoTest {

    @Test
    void deveFicarIndisponivelAoEmprestar() {
        Equipamento equipamento = new Equipamento();

        equipamento.setDisponivel(true);
        equipamento.emprestar();
        assertFalse(equipamento.isDisponivel());
    }

    @Test
    void deveLancarExcecaoAoEmprestarIndisponivel() {
        Equipamento equipamento = new Equipamento();

        equipamento.setDisponivel(false);
        assertThrows(IllegalStateException.class, () -> equipamento.emprestar());
    }

    @Test
    void deveFicarDisponivelAoDevolver(){
        Equipamento equipamento = new Equipamento();

        equipamento.setDisponivel(false);
        equipamento.devolver();

        assertTrue(equipamento.isDisponivel());
    }
}
