/*
 * Classe cliente para jogo pedra papel tesoura lagarto spock
 * 
 * 
 */
package clientejogo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author KLENNE
 */
public class ClienteJogo {

   
    public static void main(String[] args) throws ClassNotFoundException {
        //perguntando os nomes dos jogadores
        Jogador p1 = new Jogador(JOptionPane.showInputDialog("Jogador 1 Digite seu nome:"));
        Jogador p2 = new Jogador(JOptionPane.showInputDialog("Jogador 2 Digite seu nome:"));
        try {

            ObjectOutputStream oos;
            ObjectInputStream ois;
            Socket cliente = new Socket("127.0.0.1", 12345);
            oos = new ObjectOutputStream(
                    cliente.getOutputStream());
            ois = new ObjectInputStream(
                    cliente.getInputStream());
            //Enviando os nomes dos jogadores
            oos.flush();
            oos.writeObject(p1.getNome());
            oos.flush();
            oos.writeObject(p2.getNome());
          

            while (p1.getContador() < 3 && p2.getContador() < 3) {
                //lendo mensagem do servidor
                JOptionPane.showMessageDialog(null,ois.readObject());
                //realizando jogada jogador 1
                Jogadas j1 = p1.joga();
                oos.flush();
                //enviando jogada para o servidor
                oos.writeObject(j1.toString());
                oos.reset();
                //realizando jogada jogador 2
                JOptionPane.showMessageDialog(null,ois.readObject());
                Jogadas j2 = p2.joga();
                oos.flush();
                 //enviando jogada para o servidor
                oos.writeObject(j2.toString());
                oos.reset();
                //recebendo mensagem com o vencedor da rodada
                JOptionPane.showMessageDialog(null,ois.readObject());
                //recebendo a pontuação
                JOptionPane.showMessageDialog(null,ois.readObject());
                //sincronizando com o servidor os contadores de rodadas ganhas de cada jogador
                int contadorP1=ois.readInt();
                int contadorP2=ois.readInt();
               p1.setContador(contadorP1);
               p2.setContador(contadorP2);
            }
            //lendo quem foi o vencedor da partida
             JOptionPane.showMessageDialog(null,ois.readObject());
            oos.close();
            ois.close();
            
            System.exit(0);
        } catch (IOException e) {
            System.out.println(String.format("Erro: %s",
                    e.getLocalizedMessage()));
        }
    }

}
