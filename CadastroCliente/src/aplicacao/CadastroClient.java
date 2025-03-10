package aplicacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CadastroClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Socket socket = new Socket("localhost", 1234);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        ThreadClient threadClient = new ThreadClient(in);
        threadClient.start();

        
        System.out.println("------------------------------");
        System.out.print("Login: ");
        String login = reader.readLine();
        System.out.print("Senha: ");
        String senha = reader.readLine();

        out.writeObject(login);
        out.writeObject(senha);
        out.writeObject("Mensagem do cliente para o servidor.");
        out.flush();
    }
    
}
