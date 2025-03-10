package aplicacao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class ThreadClient extends Thread {

    private ObjectInputStream in;
    private final JTextArea textArea;
    private JFrame frame;

    public ThreadClient(ObjectInputStream in) {
        this.in = in;
        frame = new JFrame("Mensagens do Servidor");
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    @Override
    public void run() {
        try {
            while (true) {
                Object data = in.readObject();
                String mensagem = (String) data;
                SwingUtilities.invokeLater(() -> {
                    textArea.append(mensagem + "\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength()); // Rolagem automática
                });
            }
        }
        catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
