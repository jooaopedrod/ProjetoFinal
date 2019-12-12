package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Funcionario;

public class FuncionarioDAO {

	/**
	 * Método que cadastra um novo funcionario na base de dados
	 * 
	 * @param funcionario
	 * @return
	 */
	public Funcionario cadastrarFuncionario(Funcionario funcionario) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_funcionario = "'" + funcionario.getNome() + "'";
				String usuario_funcionario = "'" + funcionario.getUsuario() + "'";
				String senha_funcionario = "'" + funcionario.getSenha() + "'";

				stmt.execute("insert into tb_funcionario  "
						+ "(nome_funcionario, usuario_funcionario, senha_funcionario) " + "values (" + nome_funcionario
						+ ", " + usuario_funcionario + ", " + senha_funcionario + ") returning id_funcionario");
				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_funcionario = stmt.getResultSet();
				if (last_insert_funcionario.next()) {
					funcionario.setIdFuncionario(last_insert_funcionario.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return funcionario;
	}

	/**
	 * Método que atualiza o registro de um funcionario no banco de dados
	 * 
	 * @param funcionario
	 * @return
	 */
	public Funcionario atualizarFuncionario(Funcionario funcionario) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_funcionario = "'" + funcionario.getNome() + "'";
				String usuario_funcionario = "'" + funcionario.getUsuario() + "'";
				String senha_funcionario = "'" + funcionario.getSenha() + "'";

				stmt.executeUpdate("update tb_funcionario set " + "nome_funcionario  = " + nome_funcionario
						+ "usuario_funcionario  = " + usuario_funcionario + "senha_funcionario  = " + senha_funcionario
						+ " where codigo = " + funcionario.getIdFuncionario());
			} catch (SQLException e) {
				System.out.println("Erro ao alterar tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return funcionario;
	}

	/**
	 * Método que remove um registro de um funcionario do banco de dados
	 * 
	 * @param alunoId
	 */
	public void removerFuncionario(Integer funcionarioId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_funcionario where id_funcionario = " + funcionarioId);
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
	 * Método que busca todos os registros de funcionarios do banco de dados
	 * 
	 * @return
	 */
	public List<Funcionario> consultarFuncionarios() {

		List<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select f.id_funcionario, f.nome_funcionario, f.usuario_funcionario, f.senha_funcionario"
					+ "from tb_funcionario f";
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				Funcionario funcionario = new Funcionario(rset.getInt(1), rset.getString(2), rset.getString(3),
						rset.getString(4));

				listaFuncionarios.add(funcionario);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaFuncionarios;
	}

	/**
	 * Método que busca o registro de um funcionario do banco de dados através do
	 * seu codigo
	 * 
	 * @param funcionarioId
	 * @return
	 */
	public Funcionario detalharFuncionario(Integer funcionarioId) {

		Funcionario funcionario = new Funcionario();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select f.id_funcionario, f.nome_funcionario, f.usuario_funcionario, f.senha_funcionario"
					+ " from tb_funcionario f where f.id_funcionario = " + funcionarioId;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				funcionario = new Funcionario(rset.getInt(1), rset.getString(2), rset.getString(3), rset.getString(4));

			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return funcionario;
	}

}
