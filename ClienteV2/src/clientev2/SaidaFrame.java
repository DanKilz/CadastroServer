package clientev2;

import javax.swing.JDialog;
import javax.swing.JTextArea;

public class SaidaFrame extends JDialog {
    
    public JTextArea texto;

    public SaidaFrame() {
        texto = new JTextArea();
        setBounds(100, 100, 400, 300);        
        setModal(false);
        getContentPane().add(texto);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Mensagens do servidor");
    }
    
    
}
