package servidor;

import controller.MovimentoJpaController;
import controller.PessoaFisicaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Movimento;
import model.Pessoa;
import model.Produto;
import model.Usuario;

public class CadastroThread extends Thread {
    
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;
    private MovimentoJpaController ctrlMov;
    private PessoaFisicaJpaController ctrlPessoa;
    private Socket s1;

    public CadastroThread(ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu, MovimentoJpaController ctrlMov, PessoaFisicaJpaController ctrlPessoa, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
        this.s1 = s1;
    }

    public CadastroThread(ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    @Override
    public void run() {
        try (
            // A ordem primeiro ObjectOutputStream e depois ObjectInputStream evita deadlocks.
            ObjectOutputStream saida = new ObjectOutputStream(s1.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(s1.getInputStream());
        ) {
            // Obter login e senha.
            String login = (String) entrada.readObject();
            String senha = (String) entrada.readObject();
            
            // Verificar login.
            Usuario usuario = ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                saida.writeObject("Login inválido.");
                
                return; // Termina a conexão.
            }
            
            // Loop de resposta para comandos.
            while (true) {
                String comando = (String) entrada.readObject();
                
                // Se o comando fo "L", listar os produtos.
                if ("L".equalsIgnoreCase(comando)) {
                    List<Produto> produtos = ctrl.findProdutoEntities();
                    saida.writeObject(produtos);
                }
                else if ("E".equalsIgnoreCase(comando) || "S".equalsIgnoreCase(comando)) {
                    // Gerar um objeto Movimento.
                    Movimento movimento = new Movimento();
                    movimento.setIdUsuario(usuario);
                    movimento.setTipoMovimento(comando);
                    
                    Pessoa pessoa = (Pessoa) entrada.readObject();
                    movimento.setIdPessoa(pessoa);
                    
                    Produto idProduto = (Produto) entrada.readObject();
                    Produto produto = ctrl.findProduto(idProduto.getIdProduto());
                    movimento.setIdProduto(idProduto);
                    
                    
                    int quantidade = (int) entrada.readObject();
                    movimento.setQuantidade(quantidade);
                    
                    double valorUnitario = (double) entrada.readObject();
                    movimento.setValorUnitario(valorUnitario);
                    
                    // Persistir o movimento.
                    ctrlMov.create(movimento);
                    
                    // Atualizar estoque.
                    if (comando.equals("E")) {
                        produto.setQuantidade(produto.getQuantidade() + quantidade);
                    }
                    else {
                        produto.setQuantidade(produto.getQuantidade() - quantidade);
                    }
                    
                    ctrl.edit(produto);
                    
                    saida.writeObject("Movimento registrado com sucesso!");
                }
                else {
                    saida.writeObject("Comando inválido.");
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadastroThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CadastroThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                s1.close();
            }
            catch (IOException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }        
    }    
}
