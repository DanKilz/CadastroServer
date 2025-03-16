package clientev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteV2 {

    public static void main(String[] args) {

        try {
            // Socket apontando para "localhost:4321".
            Socket socket = new Socket("localhost", 4321);

            // Encapsular os canais de entrada e saída.
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            // Encapsular a leitura do teclado.
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            // Login e senha utilizando os dados da tabela Usuarios.
            String login = "op1";
            String senha = "op1";

            // Escrever o login e a senha na saída
            saida.writeObject(login);
            saida.writeObject(senha);
            saida.flush();

            System.out.println("Usuário conectado com sucesso!");

            // Instanciar janela e thread assíncrona
            SaidaFrame saidaFrame = new SaidaFrame();
            saidaFrame.setVisible(true);
            ThreadCliente threadCliente = new ThreadCliente(entrada, saidaFrame.texto);
            threadCliente.start();

            String comando;
            do {
                System.out.println("\nMENU:");
                System.out.println("L - Lista | X - Finalizar | E - Entrada | S - Saída");
                System.out.print("Opção: ");
                comando = teclado.readLine().toUpperCase();

                saida.writeObject(comando);

                switch (comando) {
                    case "E":
                    case "S":
                        System.out.print("ID da pessoa: ");
                        saida.writeObject(Integer.parseInt(teclado.readLine()));
                        System.out.print("ID do produto: ");
                        saida.writeObject(Integer.parseInt(teclado.readLine()));
                        System.out.print("Quantidade: ");
                        saida.writeObject(Integer.parseInt(teclado.readLine()));
                        System.out.print("Valor unitário: ");
                        saida.writeObject(Double.parseDouble(teclado.readLine()));
                        break;
                    case "L":
                        saida.writeObject("L");
                        saida.flush();
                        break;
                    case "X":
                        System.out.println("Encerrando a conexão.");
                        break;
                    default:
                        System.out.println("Comando inválido.");
                        break;
                }

            } while (!comando.equalsIgnoreCase("X"));
            
            teclado.close();
            entrada.close();
            saida.close();
            socket.close();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

}
