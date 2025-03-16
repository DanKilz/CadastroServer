package clientev2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import model.Produto;

public class ThreadCliente extends Thread {
    
    private ObjectInputStream entrada;
    private JTextArea textArea;

    public ThreadCliente(ObjectInputStream entrada, JTextArea textArea) {
        this.entrada = entrada;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        
        try {
            while (true) {
                Object mensagem = entrada.readObject();
                
                if (mensagem instanceof String) {
                    SwingUtilities.invokeLater(() -> textArea.append((String) mensagem + "\n"));
                }
                else if (mensagem instanceof List) {
                    SwingUtilities.invokeLater(() -> {
                        for (Object item : (List<?>) mensagem) {
                            if (item instanceof Produto) {
                                Produto produto = (Produto) item;
                                textArea.append(produto.getNome() + " - Quantidade: " + produto.getQuantidade() + "\n");
                            }
                        }
                    });
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro na thread cliente: " + e.getMessage());
            SwingUtilities.invokeLater(() -> textArea.append("Conexão com servidor perdida."));
        }
    }
}
