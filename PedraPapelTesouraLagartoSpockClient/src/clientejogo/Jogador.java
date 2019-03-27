package clientejogo;

import static clientejogo.Jogadas.*;
import java.awt.HeadlessException;
import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 *
 * @author 020937
 */
public class Jogador implements Serializable{

    private  String nome;
    private int contador;//conta as rodadas ganhas

    public Jogador(String nome) {
        this.nome = nome;
        contador = 0;
    }

    public String getNome() {
        return this.nome;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public Jogadas joga() {//método para realizar a jogada

        while (true) {
            try {
                String jogada = JOptionPane.showInputDialog("Digite:"
                        + "\n1-Para Pedra"
                        + "\n2-Para Papel"
                        + "\n3-Para Tesoura"
                        + "\n4-Para Lagarto"
                        + "\n5-Para Spock ");
                int opcao = Integer.parseInt(jogada);
                switch (opcao) {
                    case 1:
                        return PEDRA;
                    case 2:
                        return PAPEL;
                    case 3:
                        return TESOURA;
                    case 4:
                        return LAGARTO;
                    case 5:
                        return SPOCK;
                    default:
                        JOptionPane.showMessageDialog(null, "Opção inválida");
                }
            } catch (HeadlessException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Não é um numero");
            }
        }
    }

    public void ganhaRodada() {
        if (contador < 3) {
            contador++;
        }

    }

}
