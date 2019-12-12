package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Fornecedor;

public class FornecedorDAO {
	/**
	 * Método que cadastra um novo fornecedor na base de dados
	 * 
	 * @param fornecedor
	 * @return
	 */
	public Fornecedor cadastrarFornecedor(Fornecedor fornecedor) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_fornecedor = "'" + fornecedor.getNome() + "'";

				stmt.execute("insert into tb_fornecedor   " + "(nome_fornecedor ) " + "values (" + nome_fornecedor
						+ ") returning id_fornecedor");

				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_fornecedor = stmt.getResultSet();
				if (last_insert_fornecedor.next()) {
					fornecedor.setIdFornecedor(last_insert_fornecedor.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return fornecedor;
	}

	/**
	 * MÃ©todo que atualiza o registro de um fornecedor no banco de dados
	 * 
	 * @param fornecedor
	 * @return
	 */
	public Fornecedor atualizarFornecedor(Fornecedor fornecedor) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_fornecedor = "'" + fornecedor.getIdFornecedor() + "'";

				stmt.executeUpdate("update tb_fornecedor set " + "nome_fornecedor  = " + nome_fornecedor
						+ " where id_fornecedor = " + fornecedor.getIdFornecedor());
			} catch (SQLException e) {
				System.out.println("Erro ao alterar tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return fornecedor;
	}

	/**
	 * Método que remove um registro de fornecedor do banco de dados
	 * 
	 * @param fornecedorId
	 */
	public void removerFornecedor(Integer fornecedorId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_fornecedor where id_fornecedor = " + fornecedorId);
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
	 * Método que busca todos os registros de fornecedores do banco de dados
	 * 
	 * @return
	 */
	public List<Fornecedor> consultarFornecedores() {

		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select f.id_fornecedor, f.nome_fornecedor  " + "from tb_fornecedor f";
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				Fornecedor fornecedor = new Fornecedor(rset.getInt(1), rset.getString(2));
				listaFornecedores.add(fornecedor);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaFornecedores;
	}

	/**
	 * Método que busca o registro de um fornecedor do banco de dados através do seu
	 * codigo
	 * 
	 * @param fornecedorId
	 * @return
	 */
	public Fornecedor detalharFornecedor(Integer fornecedorId) {

		Fornecedor fornecedor = new Fornecedor();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select f.id_fornecedor, f.nome_fornecedor " + "from tb_fornecedor f where f.id_fornecedor = "
					+ fornecedorId;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				fornecedor = new Fornecedor(rset.getInt(1), rset.getString(2));
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fornecedor;
	}

}