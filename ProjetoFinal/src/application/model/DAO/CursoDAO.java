package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Curso;

public class CursoDAO {

	/**
	 * Método que cadastra um novo curso na base de dados
	 * 
	 * @param curso
	 * @return
	 */
	public Curso cadastrarCurso(Curso curso) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_curso = "'" + curso.getNomeCurso() + "'";

				stmt.execute("insert into tb_curso   " + "(nome_curso ) " + "values (" + nome_curso
						+ ") returning id_curso");
				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_curso = stmt.getResultSet();
				if (last_insert_curso.next()) {
					curso.setIdCurso(last_insert_curso.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return curso;
	}

	/**
	 * Método que atualiza o registro de um curso no banco de dados
	 * 
	 * @param curso
	 * @return
	 */
	public Curso atualizarCurso(Curso curso) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_curso = "'" + curso.getNomeCurso() + "'";

				stmt.executeUpdate("update tb_curso set " + "nome_curso  = " + nome_curso + " where codigo = "
						+ curso.getIdCurso());
			} catch (SQLException e) {
				System.out.println("Erro ao alterar tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return curso;
	}

	/**
	 * Método que remove um registro de curso do banco de dados
	 * 
	 * @param cursoId
	 */
	public void removerCurso(Integer cursoId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_curso where id_curso = " + cursoId);
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
	 * Método que busca todos os registros de cursos do banco de dados
	 * 
	 * @return
	 */
	public List<Curso> consultarCursos() {

		List<Curso> listaCursos = new ArrayList<Curso>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select c.id_curso, c.nome_curso " + "from tb_curso c";
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				Curso curso = new Curso(rset.getInt(1), rset.getString(2));
				listaCursos.add(curso);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaCursos;
	}

	/**
	 * Método que busca o registro de um curso do banco de dados através do seu
	 * código
	 * 
	 * @param cursoId
	 * @return
	 */
	public Curso detalharCurso(Integer cursoId) {

		Curso curso = new Curso();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select c.id_curso, c.nome_curso " + "from tb_curso c where c.id_curso = " + cursoId;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				curso = new Curso(rset.getInt(1), rset.getString(2));
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return curso;
	}

}
