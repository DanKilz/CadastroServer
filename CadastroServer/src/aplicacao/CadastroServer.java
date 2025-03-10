package aplicacao;

import controller.UsuarioJpaController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CadastroServer {

    public static void main(String[] args) throws ClassNotFoundException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");

        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor aguardando conexoes na porta 1234...");

            while (true) {
                // Aguardando conexão.
                Socket socket = serverSocket.accept();

                ThreadServer teste = new ThreadServer(ctrlUsu, socket);
                teste.start();
                System.out.println("thread iniciado!");
            }
        } catch (IOException e) {
            System.err.println("Erro na conexão com o serviço: " + e.getMessage());
        }
    }

}
