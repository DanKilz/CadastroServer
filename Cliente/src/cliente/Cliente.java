package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import model.Produto;

public class Cliente {

    public static void main(String[] args) {

        try (
                // Socket apontando para "localhost:4321".
                Socket socket = new Socket("localhost", 4321);
                
                // Encapsular os canais de entrada e saída.
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            ) {
            // Login e senha utilizando os dados da tabela Usuarios.
            String login = "op1";
            String senha = "op1";

            // Escrever o login e a senha na saída
            saida.writeObject(login);
            saida.writeObject(senha);
            saida.flush();
            
            System.out.println("Usuário conectado com sucesso!");
            
            // Enviar o comando L no canal de saída.
            saida.writeObject("L");
            saida.flush();
            
            // Receber a coleção de entidades no canal de entrada.
            Object reposta = entrada.readObject();
            
            // Processar a resposta recebida.
            if (reposta instanceof Collection<?>) {
                Collection<?> produtos = (Collection<Object>) reposta;
                for (Object produto : produtos) {
                    if (produto instanceof Produto) {
                        System.out.println("Produto: " + ((Produto) produto).getNome());
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.err.println("Erro ao obter dados: " + e.getMessage());
        }
    }

}
