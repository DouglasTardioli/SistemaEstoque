package estoque.controller;

import estoque.database.Database;
import estoque.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoController {

    public void cadastrar(Produto produto) {

        String sql = """
                INSERT INTO produtos(nome, preco, quantidade)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection = Database.conectar();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());

            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao cadastrar: " + e.getMessage()
            );
        }
    }


    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produtos ORDER BY id DESC";

        try (
                Connection connection = Database.conectar();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet result =
                        statement.executeQuery()
        ) {

            while (result.next()) {

                Produto produto = new Produto();

                produto.setId(result.getInt("id"));
                produto.setNome(result.getString("nome"));
                produto.setPreco(result.getDouble("preco"));
                produto.setQuantidade(
                        result.getInt("quantidade")
                );

                produtos.add(produto);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao listar: " + e.getMessage()
            );
        }

        return produtos;
    }


    public void atualizar(Produto produto) {

        String sql = """
                UPDATE produtos
                SET nome = ?,
                    preco = ?,
                    quantidade = ?
                WHERE id = ?
                """;

        try (
                Connection connection = Database.conectar();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            statement.setInt(4, produto.getId());

            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao atualizar: " + e.getMessage()
            );
        }
    }


    public void excluir(int id) {

        String sql = "DELETE FROM produtos WHERE id = ?";

        try (
                Connection connection = Database.conectar();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao excluir: " + e.getMessage()
            );
        }
    }
}