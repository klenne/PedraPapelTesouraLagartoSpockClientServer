/*
 * Classe servidor para jogo pedra papel tesoura lagarto e spock
 * 
 * 
 */
package jogoservidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import static jogoservidor.Jogadas.*;

/**
 *
 * @author KLENNE
 */
public class Servidor {

    //vetores com as jogadas possíveis
    Jogadas[] pedraVence = {LAGARTO, TESOURA},
            papelVence = {PEDRA, SPOCK},
            tesouraVence = {PAPEL, LAGARTO},
            lagartoVence = {PAPEL, SPOCK},
            spockVence = {PEDRA, TESOURA};

    ObjectInputStream ois;
    ObjectOutputStream oos;
    Jogador p1;
    Jogador p2;

    public void startServer() {

        try {
            ServerSocket socketServidor = new ServerSocket(12345);
            while (true) {

                Socket cliente = socketServidor.accept();
                oos = new ObjectOutputStream(
                        cliente.getOutputStream());
                ois = new ObjectInputStream(
                        cliente.getInputStream());

                p1 = new Jogador((String) ois.readObject());
                p2 = new Jogador((String) ois.readObject());

                while (checarJogo(p1) && checarJogo(p2)) {
                    //enviando mensagem para cliente
                    enviaMensagem(p1.getNome() + " jogue:");
                    //recebendo jogada do jogador 1
                    Jogadas j1 = traduzirJogadaRecebida((String) ois.readObject());
                    //enviando mensagem para cliente
                    enviaMensagem(p2.getNome() + " jogue:");
                    //recebendo jogada do jogador 2
                    Jogadas j2 = traduzirJogadaRecebida((String) ois.readObject());

                    if (j2.equals(j1)) {
                        //se os dois jogadores jogarem a mesma opção o loop recomeça
                        enviaMensagem("Jogadas iguais, joguem novamente");
                        sincronizaPontuacao();
                        continue;
                    }
                    //verificando quem venceu a rodada
                    switch (j1) {
                        case PEDRA:
                            if (checarJogada(pedraVence, j2)) {

                                p1.ganhaRodada();
                                jogadorVenceRodada(j1, j2, p1, p2);
                            } else {
                                p2.ganhaRodada();
                                jogadorVenceRodada(j2, j1, p2, p1);
                            }
                            break;

                        case PAPEL:
                            if (checarJogada(papelVence, j2)) {
                                p1.ganhaRodada();
                                jogadorVenceRodada(j1, j2, p1, p2);
                            } else {
                                p2.ganhaRodada();
                                jogadorVenceRodada(j2, j1, p2, p1);
                            }
                            break;
                        case TESOURA:
                            if (checarJogada(tesouraVence, j2)) {
                                p1.ganhaRodada();
                                jogadorVenceRodada(j1, j2, p1, p2);
                            } else {
                                p2.ganhaRodada();
                                jogadorVenceRodada(j2, j1, p2, p1);
                            }
                            break;
                        case LAGARTO:
                            if (checarJogada(lagartoVence, j2)) {
                                p1.ganhaRodada();
                                jogadorVenceRodada(j1, j2, p1, p2);
                            } else {
                                p2.ganhaRodada();
                                jogadorVenceRodada(j2, j1, p2, p1);
                            }
                            break;
                        case SPOCK:
                            if (checarJogada(spockVence, j2)) {
                                p1.ganhaRodada();
                                jogadorVenceRodada(j1, j2, p1, p2);
                            } else {
                                p2.ganhaRodada();
                                jogadorVenceRodada(j2, j1, p2, p1);
                            }
                            break;

                    }
                    //sincronizando pontuação com o cliente
                    sincronizaPontuacao();

                }
                //enviando o vencedor
                enviaMensagem(checarVencedor());
                oos.close();
                ois.close();

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(String.format("Erro: %s",
                    e.getLocalizedMessage()));
        }
    }

    public boolean checarJogo(Jogador p) {//checando a pontuação o jogador que faz 3 pontos primeiro vence
        return p.getContador() < 3;
    }

    public boolean checarJogada(Jogadas[] vetorParaComparar, Jogadas valor) {//verifica  quem venceu a rodada
        for (int i = 0; i < 2; i++) {
            if (vetorParaComparar[i] == valor) {
                return true;
            }
        }
        return false;
    }

    private String checarVencedor() {//verifica quem venceu o jogo
        String vencedor = "";
        if (p1.getContador() == 3) {
            vencedor = p1.getNome();
        }
        if (p2.getContador() == 3) {
            vencedor = p2.getNome();
        }
        return "Parabéns " + vencedor + " você ganhou";
    }

    private void jogadorVenceRodada(Jogadas jogada1, Jogadas jogada2, Jogador jogadorVencedor, Jogador jogadorPerdedor) throws IOException {//envia mensagem falando qual jogador venceu a rodada
        enviaMensagem("Jogador " + jogadorVencedor.getNome() + " ganhou\n Pois: " + jogada1 + " vence " + jogada2);

    }

    public void enviaMensagem(String mensagem) throws IOException {//envia mensagens para o cliente

        oos.flush();
        oos.writeObject(mensagem);
        oos.reset();
    }

    public void sincronizaPontuacao() throws IOException {//sincroniza a pontuação com cliente
        oos.flush();
        oos.writeInt(p1.getContador());
        oos.reset();
        oos.writeInt(p2.getContador());
        oos.reset();

    }

    public Jogadas traduzirJogadaRecebida(String jogada) {//traduz a jogada recebida em String para a enum Jogadas
        switch (jogada) {
            case "PEDRA":
                return PEDRA;
            case "PAPEL":
                return PAPEL;
            case "TESOURA":
                return TESOURA;
            case "LAGARTO":
                return LAGARTO;
            case "SPOCK":
                return SPOCK;
        }
        return null;
    }

}
