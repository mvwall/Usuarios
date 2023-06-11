package model;

import java.sql.*;

/**
 * Conexão com o banco de dados
 *
 * @author Wallysson
 * @version 1.0
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ModuloConexao {
    //jdbc 6.4.0.jre8
    //jdk-19.0.2


    public static Connection conectar() {
        Connection conexao;
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";


        String url = "jdbc:sqlserver://localhost:1433;databaseName=gestaoPessoas";//alterar
        String user = "sa";//alterar
        String password = "dba1234";//alterar

        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }
}