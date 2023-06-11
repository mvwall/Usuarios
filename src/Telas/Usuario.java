package Telas;

import model.ModuloConexao;

import java.sql.*;
import java.util.Scanner;

public class Usuario {
    private int id;
    private String nome;
    private String fone;

    private String login;

    private String senha;

    private String perfil;

    Connection conexao;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Metodo construtor para criar um usuario
     * @param nome uma string que representa o nome do usuario
     * @param fone uma string que representa o telefone de contato.
     * @param login uma string que representa o login.
     * @param senha uma string que representa a senha
     * @param perfil uma string que representa o perfil
     */
    public Usuario(String nome,String fone, String login, String senha, String perfil){
        this.nome = nome;
        this.fone = fone;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    /**
     * Construtor padrão
     */
    public Usuario() {
    }

    /**
     * Medoto para adicionar um novo usuário no banco
     */
    public void adicionarUsuario() {
        Scanner console = new Scanner(System.in);
        String sql = "INSERT INTO tbusuarios (usuario, fone, login, senha, perfil) VALUES (?,?,?,?,?);";
        String nome, telefone, login, senha, perfil;

        System.out.println("=========================================");
        System.out.println("|         Adicionar usuário             |");
        System.out.println("=========================================");
        System.out.println("\nDigite o nome do usuário:");
        nome = console.next();

        ValidarTelefone validarTelefone = new ValidarTelefone();
        boolean validar = false;

        do {
            System.out.println("\nDigite o telefone válido:");
            telefone = console.next();
            validar = validarTelefone.validarTelefone(telefone);
        } while (!validar);

        System.out.println("\nDigite o seu login:");
        login = console.next();
        System.out.println("\nDigite sua senha:");
        senha = console.next();

        if (senha.length() < 6) {
            System.out.println("Digite uma senha com pelo menos 6 dígitos:");
            senha = console.next();
        }

        System.out.println("\nSelecione um perfil:");
        System.out.println("Digite 1 para Administrador");
        System.out.println("Digite 2 para Usuário");
        int opcao = console.nextInt();

        if (opcao == 1) {
            perfil = "Admin";
        } else {
            perfil = "viewer.Usuario";
        }

        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setString(2, telefone);
            pst.setString(3, login);
            pst.setString(4, senha);
            pst.setString(5, perfil);
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                System.out.println("\n=========================================");
                System.out.println("|   Usuário adicionado com sucesso      |");
                System.out.println("=========================================");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    /**
     * Metodo para consultar um usuário no banco, retorna um objeto Usuário.
     * @param id um parametro de filtro para retornar apenas um usuário na consulta
     * @return
     */

    public Usuario consultarUsuario(String id) {
        Scanner console = new Scanner(System.in);
        Usuario pessoa = new Usuario();
        String sql = "SELECT * FROM tbusuarios WHERE iduser = ?";


        System.out.println("=========================================");
        System.out.println("|        Consultar usuário              |");
        System.out.println("=========================================");


        try {
            conexao = ModuloConexao.conectar();
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet resultado = pst.executeQuery();

            if (resultado.next()) {
                String idUsuario = resultado.getString("iduser");
                String usuario = resultado.getString("usuario");
                String fone = resultado.getString("fone");
                String login = resultado.getString("login");
                String senha = resultado.getString("senha");
                String perfil = resultado.getString("perfil");

                System.out.println("\n=========================================");
                System.out.println("|           Dados atuais do usuário            |");
                System.out.println("=========================================");
                System.out.println("ID: "+ idUsuario);
                System.out.println("Nome: " + usuario);
                System.out.println("Telefone: " + fone);
                System.out.println("Login: " + login);
                System.out.println("Senha: " + senha);
                System.out.println("Perfil: " + perfil);
                pessoa.setId(Integer.parseInt(idUsuario));
                pessoa.setNome(usuario);
                pessoa.setFone(fone);
                pessoa.setLogin(login);
                pessoa.setSenha(senha);
                pessoa.setPerfil(perfil);

            } else {
                System.out.println("\n=========================================");
                System.out.println("|     Usuário não encontrado            |");
                System.out.println("=========================================");
            }

            resultado.close();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return pessoa;
    }

    /**
     * Metodo para atualizar um usuário no banco.
     */
    public void atualizarUsuario() {
        Scanner console = new Scanner(System.in);

        String sqlUpdate = "UPDATE tbusuarios SET usuario = ?, fone = ?, login = ?, senha = ?, perfil = ? WHERE iduser = ?";

        System.out.println("=========================================");
        System.out.println("|       Atualizar dados do usuário       |");
        System.out.println("=========================================");

        System.out.println("Digite o ID do usuário que deseja atualizar:");
        String id = console.next();

        try {
                Usuario pessoa = consultarUsuario(id);
                if (pessoa.getId()>1) {

                    // Solicitar campos a serem atualizados
                    System.out.println("\nSelecione o campo que deseja atualizar (Digite o número correspondente):");
                    System.out.println("1. Nome");
                    System.out.println("2. Telefone");
                    System.out.println("3. Login");
                    System.out.println("4. Senha");
                    System.out.println("5. Perfil");
                    System.out.println("6. Todos os campos");

                    int opcao = console.nextInt();

                    // Atualizar campos selecionados
                    String novoNome = pessoa.nome;
                    String novoFone = pessoa.fone;
                    String novoLogin = pessoa.login;
                    String novaSenha = pessoa.senha;
                    String novoPerfil = pessoa.perfil;

                    switch (opcao) {
                        case 1:
                            System.out.println("Digite o novo nome do usuário:");
                            novoNome = console.next();
                            break;
                        case 2:
                            System.out.println("Digite o novo telefone do usuário:");
                            novoFone = console.next();
                            break;
                        case 3:
                            System.out.println("Digite o novo login do usuário:");
                            novoLogin = console.next();
                            break;
                        case 4:
                            System.out.println("Digite a nova senha do usuário:");
                            novaSenha = console.next();
                            break;
                        case 5:
                            System.out.println("Digite o novo perfil do usuário:");
                            novoPerfil = console.next();
                            break;
                        case 6:
                            System.out.println("Digite o novo nome do usuário:");
                            novoNome = console.next();
                            System.out.println("Digite o novo telefone do usuário:");
                            novoFone = console.next();
                            System.out.println("Digite o novo login do usuário:");
                            novoLogin = console.next();
                            System.out.println("Digite a nova senha do usuário:");
                            novaSenha = console.next();
                            System.out.println("Digite o novo perfil do usuário:");
                            novoPerfil = console.next();
                            break;
                        default:
                            System.out.println("Opção inválida. Nenhum campo será atualizado.");
                            break;
                    }

                    // Atualizar usuário
                    conexao = ModuloConexao.conectar();
                    PreparedStatement pstUpdate = conexao.prepareStatement(sqlUpdate);
                    pstUpdate.setString(1, novoNome);
                    pstUpdate.setString(2, novoFone);
                    pstUpdate.setString(3, novoLogin);
                    pstUpdate.setString(4, novaSenha);
                    pstUpdate.setString(5, novoPerfil);
                    pstUpdate.setString(6, id);

                    int registrosAfetados = pstUpdate.executeUpdate();

                    if (registrosAfetados > 0) {
                        System.out.println("\n=========================================");
                        System.out.println("|   Usuário atualizado com sucesso!     |");
                        System.out.println("=========================================");
                        System.out.println("ID: " + id);
                        System.out.println("Nome: " + novoNome);
                        System.out.println("Telefone: " + novoFone);
                        System.out.println("Login: " + novoLogin);
                        System.out.println("Senha: " + novaSenha);
                        System.out.println("Perfil: " + novoPerfil);
                    }}
                else{
                    System.out.println("\n=========================================");
                    System.out.println("|   Nenhum usuário encontrado para atualização.   |");
                    System.out.println("=========================================");}

                    conexao.close();
                 } catch (SQLException e) {
                    System.out.println(e);

        }
    }

    /**
     * Medoto para listar todos os usuários do banco.
     */
    public void listarUsuarios() {
        String sql = "SELECT * FROM tbusuarios";

        try {
            Connection conexao = ModuloConexao.conectar();
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery(sql);

            System.out.println("=========================================");
            System.out.println("|        Lista de Usuários               |");
            System.out.println("=========================================");

            while (resultado.next()) {
                String idUsuario = resultado.getString("iduser");
                String usuario = resultado.getString("usuario");
                String fone = resultado.getString("fone");
                String login = resultado.getString("login");
                String senha = resultado.getString("senha");
                String perfil = resultado.getString("perfil");

                System.out.println("ID: " + idUsuario);
                System.out.println("Nome: " + usuario);
                System.out.println("Telefone: " + fone);
                System.out.println("Login: " + login);
                System.out.println("Senha: " + senha);
                System.out.println("Perfil: " + perfil);
                System.out.println("-----------------------------------------");
            }

            resultado.close();
            conexao.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Metodo para deletar um usuário no banco
     * @param id um parametro de filtro para deletar apenas um usuário.
     */
    public void deletarUsuario(String id) {
        String sqlDelete = "DELETE FROM tbusuarios WHERE iduser = ?";
        Usuario consulta = consultarUsuario(id);

        try {
            conexao = ModuloConexao.conectar();

            // Verificar se o usuário existe
            if (consulta.getId() > 1) {
                // Solicitar confirmação
                Scanner console = new Scanner(System.in);
                System.out.println("\nTem certeza de que deseja excluir este usuário? (S/N)");
                String confirmacao = console.next();

                if (confirmacao.equalsIgnoreCase("S")) {
                    // Deletar usuário
                    PreparedStatement pstDelete = conexao.prepareStatement(sqlDelete);
                    pstDelete.setString(1, id);
                    int registrosAfetados = pstDelete.executeUpdate();

                    if (registrosAfetados > 0) {
                        System.out.println("\n=========================================");
                        System.out.println("|   Usuário excluído com sucesso!       |");
                        System.out.println("=========================================");
                        System.out.println("Nome: "+consulta.getNome());
                        System.out.println("ID: "+ consulta.getId());
                        System.out.println("Nome: " + consulta.getNome());
                        System.out.println("Telefone: " + consulta.getFone());
                        System.out.println("Login: " + consulta.getLogin());
                        System.out.println("Senha: " + consulta.getSenha());
                        System.out.println("Perfil: " + consulta.getPerfil());

                    } else {
                        System.out.println("\n=========================================");
                        System.out.println("|   Falha ao excluir o usuário.         |");
                        System.out.println("=========================================");
                    }
                } else {
                    System.out.println("\n=========================================");
                    System.out.println("|   Operação cancelada pelo usuário.     |");
                    System.out.println("=========================================");
                }
            } else {
                System.out.println("\n=========================================");
                System.out.println("|   Nenhum usuário encontrado com o ID informado.   |");
                System.out.println("=========================================");
            }

            conexao.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public boolean login(String log, String pass) throws SQLException{        
        
        String sql = "SELECT * FROM tbusuarios WHERE login = ? ";        
        Connection conexao = null;            

        try {
            conexao = ModuloConexao.conectar();
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1,log);
            ResultSet resultado = pst.executeQuery();

            if (resultado.next()) {
                String idUsuario = resultado.getString("iduser");
                String nome = resultado.getString("usuario");
                String fone = resultado.getString("fone");
                String login = resultado.getString("login");
                String senha = resultado.getString("senha");
                String perfil = resultado.getString("perfil");

                if (log.equals(login)&& pass.equals(senha)){
                    return true;
                }}
                
                
                  } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return false;
    }
                
                
                
      
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }


}
