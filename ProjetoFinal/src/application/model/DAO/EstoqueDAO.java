package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Estoque;
import application.model.Fornecedor;

public class EstoqueDAO {

	/**
	 * Método que cadastra um novo estoque na base de dados
	 * 
	 * @param estoque
	 * @return
	 */
	public Estoque cadastrarEstoque(Estoque estoque) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String codigo_produto_estoque = "'" + estoque.getCodigoProduto() + "'";
				String nome_produto_estoque = "'" + estoque.getNomeProduto() + "'";
				String quantidade_estoque = "'" + estoque.getQuantidade() + "'";
				String fornecedor_estoque = "'" + estoque.getFornecedor().getIdFornecedor() + "'";
				String valor_estoque = "'" + estoque.getValorUnitario() + "'";

				stmt.execute("insert into tb_estoque   "
						+ "(codigo_produto_estoque, nome_produto_estoque, quantidade_estoque, fornecedor_estoque, valor_estoque ) "
						+ "values (" + codigo_produto_estoque + ", " + nome_produto_estoque + ", " + quantidade_estoque
						+ ", " + fornecedor_estoque + ", " + valor_estoque + ") returning id_estoque");
				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_estoque = stmt.getResultSet();
				if (last_insert_estoque.next()) {
					estoque.setIdEstoque(last_insert_estoque.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return estoque;
	}

	/**
	 * Método que atualiza o registro de um estoque no banco de dados
	 * 
	 * @param estoque
	 * @return
	 */
	public Estoque atualizarEstoque(Estoque estoque) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String codigo_produto_estoque = "'" + estoque.getCodigoProduto() + "'";
				String nome_produto_estoque = "'" + estoque.getNomeProduto() + "'";
				String quantidade_estoque = "'" + estoque.getQuantidade() + "'";
				String fornecedor_estoque = "'" + estoque.getFornecedor().getIdFornecedor() + "'";
				String valor_estoque = "'" + estoque.getValorUnitario() + "'";

				stmt.executeUpdate("update tb_estoque set " + "codigo_produto_estoque  = " + codigo_produto_estoque
						+ ", nome_produto_estoque  = " + nome_produto_estoque + ", quantidade_estoque  = "
						+ quantidade_estoque + ", fornecedor_estoque  = " + fornecedor_estoque + ", valor_estoque  = "
						+ valor_estoque + " where id_estoque = " + estoque.getIdEstoque());
			} catch (SQLException e) {
				System.out.println("Erro ao alterar tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return estoque;
	}

	/**
	 * Método que remove um registro de um estoque do banco de dados
	 * 
	 * @param estoqueId
	 */
	public void removerEstoque(Integer estoqueId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_estoque where id_estoque = " + estoqueId);
			} catch (SQLException e) {
				System.out.println("Erro ao remover tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

	/**
	 * Método que busca todos os registros de estoques do banco de dados
	 * 
	 * @return
	 */
	public List<Estoque> consultarEstoques() {

		List<Estoque> listaEstoques = new ArrayList<Estoque>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select e.id_estoque, e.codigo_produto_estoque, e.nome_produto_estoque, e.fornecedor_estoque, e.valor_estoque, e.quantidade_estoque "
					+ "from tb_estoque e";
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				Estoque estoque = new Estoque();

				estoque.setIdEstoque(rset.getInt(1));
				estoque.setCodigoProduto(rset.getString(2));
				estoque.setNomeProduto(rset.getString(3));
				// Recupera o fornecedor
				FornecedorDAO fornecedorDAO = new FornecedorDAO();
				Fornecedor fornecedor = fornecedorDAO.detalharFornecedor(rset.getInt(4));
				estoque.setFornecedor(fornecedor);
				estoque.setValorUnitario(rset.getDouble(5));
				estoque.setQuantidade(rset.getInt(6));

				listaEstoques.add(estoque);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaEstoques;
	}

	/**
	 * Método que busca o registro de um estoque do banco de dados através do seu
	 * codigo
	 * 
	 * @param estoqueId
	 * @return
	 */
	public Estoque detalharEstoque(Integer estoqueId) {

		Estoque estoque = new Estoque();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select e.id_estoque, e.codigo_produto_estoque, e.nome_produto_estoque, e.fornecedor_estoque, e.valor_estoque, e.quantidade_estoque "
					+ "from tb_estoque e where e.id_estoque = " + estoqueId;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				estoque = new Estoque();

				estoque.setIdEstoque(rset.getInt(1));
				estoque.setCodigoProduto(rset.getString(2));
				estoque.setNomeProduto(rset.getString(3));
				// Recupera o fornecedor
				FornecedorDAO fornecedorDAO = new FornecedorDAO();
				Fornecedor fornecedor = fornecedorDAO.detalharFornecedor(rset.getInt(4));
				estoque.setFornecedor(fornecedor);
				estoque.setValorUnitario(rset.getDouble(5));
				estoque.setQuantidade(rset.getInt(6));
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return estoque;
	}

}
