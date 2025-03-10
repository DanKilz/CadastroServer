package aplicacao;

import controller.UsuarioJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Usuario;

public class ThreadServer extends Thread {

    private final UsuarioJpaController ctrlUsu;
    private final Socket s1;

    public ThreadServer(UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    @Override
    public void run() {
        // Ouvindo
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream()); ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {

            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            String mensagem = (String) in.readObject();
            System.out.println("login=" + login + "   senha=" + senha);
            System.out.println("mensagem=" + mensagem);
            out.writeObject("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
            out.flush();

            // Interagindo com o banco de dados.
            List<Usuario> usuariosList = ctrlUsu.findUsuarioEntities();
            int tamanhoLista = usuariosList.size();
            System.out.println("tamnho=" + tamanhoLista);
            for (Usuario usuario : usuariosList) {
                System.out.println("login=" + usuario.getLogin());
            }

            System.out.println("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
            Usuario userTeste = new Usuario((tamanhoLista + 1));
            userTeste.setLogin(login + (tamanhoLista + 1));
            userTeste.setSenha(senha + (tamanhoLista + 1));

            ctrlUsu.create(userTeste);
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
