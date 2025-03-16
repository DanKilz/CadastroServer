package servidor;

import controller.MovimentoJpaController;
import controller.PessoaFisicaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Servidor {

    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ServidorPU");
        ProdutoJpaController ctrl = new ProdutoJpaController(emf);
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        MovimentoJpaController ctrlMov = new MovimentoJpaController(emf);
        PessoaFisicaJpaController ctrlPessoa = new PessoaFisicaJpaController(emf);
        
        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Servidor aguardando conexões na porta 4321...");
            
            while (true) {
                // Aguardando conexão com o cliente.
                Socket socket = serverSocket.accept();
                
                CadastroThread cadastroThread = new CadastroThread(ctrl, ctrlUsu, ctrlMov, ctrlPessoa, socket);
                // Inicia um nova thread diante de uma requisição de um cliente.
                cadastroThread.start();
            }
        }
        catch (IOException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        }
    }
    
}
