package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Aluno;
import application.model.Curso;

public class AlunoDAO {

	/**
	 * Método que cadastra um novo aluno na base de dados
	 * 
	 * @param aluno
	 * @return
	 */
	public Aluno cadastrarAluno(Aluno aluno) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_aluno = "'" + aluno.getNome() + "'";
				String curso_aluno = "'" + aluno.getCurso().getIdCurso() + "'";
				String numero_matricula_aluno = "'" + aluno.getNumeroMatricula() + "'";
				String data_nasc_aluno = (aluno.getDataNasc() != null ? ("'" + aluno.getDataNasc() + "'") : null);
				String codigo_cliente_aluno = "'" + aluno.getCodigoCliente() + "'";

				stmt.execute("insert into tb_aluno   "
						+ "(nome_aluno, curso_aluno, numero_matricula_aluno, data_nasc_aluno, codigo_cliente_aluno ) "
						+ "values (" + nome_aluno + ", " + curso_aluno + ", " + numero_matricula_aluno + ","
						+ data_nasc_aluno + "," + codigo_cliente_aluno + ") returning id_aluno");
				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_aluno = stmt.getResultSet();
				if (last_insert_aluno.next()) {
					aluno.setIdCliente(last_insert_aluno.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return aluno;
	}

	/**
	 * Método que atualiza o registro de um aluno no banco de dados
	 * 
	 * @param aluno
	 * @return
	 */
	public Aluno atualizarCurso(Aluno aluno) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_aluno = "'" + aluno.getNome() + "'";
				String curso_aluno = "'" + aluno.getCurso().getIdCurso() + "'";
				String numero_matricula_aluno = "'" + aluno.getNumeroMatricula() + "'";
				String data_nasc_aluno = (aluno.getDataNasc() != null ? ("'" + aluno.getDataNasc() + "'") : null);
				String codigo_cliente_aluno = "'" + aluno.getCodigoCliente() + "'";

				stmt.executeUpdate("update tb_aluno set " + "nome_aluno  = " + nome_aluno + "curso_aluno  = "
						+ curso_aluno + "numero_matricula_aluno  = " + numero_matricula_aluno + "data_nasc_aluno  = "
						+ data_nasc_aluno + "codigo_cliente_aluno  = " + codigo_cliente_aluno + " where codigo = "
						+ aluno.getIdCliente());
			} catch (SQLException e) {
				System.out.println("Erro ao alterar tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return aluno;
	}

	/**
	 * Método que remove um registro de um aluno do banco de dados
	 * 
	 * @param alunoId
	 */
	public void removerAluno(Integer alunoId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_aluno where id_aluno = " + alunoId);
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
	 * Método que busca todos os registros de alunos do banco de dados
	 * 
	 * @return
	 */
	public List<Aluno> consultarAlunos() {

		List<Aluno> listaAlunos = new ArrayList<Aluno>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select a.id_aluno, a.nome_aluno, a.curso_aluno, a.numero_matricula_aluno, a.data_nasc_aluno, a.codigo_cliente_aluno  "
					+ "from tb_aluno a";
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				Aluno aluno = new Aluno();

				aluno.setIdCliente(rset.getInt(1));
				aluno.setNome(rset.getString(2));
				// Recupera o curso
				CursoDAO cursoDAO = new CursoDAO();
				Curso curso = cursoDAO.detalharCurso(rset.getInt(3));
				aluno.setCurso(curso);
				aluno.setNumeroMatricula(rset.getString(4));
				LocalDate data_nasc_aluno = null;
				if (rset.getDate(5) != null) {
					data_nasc_aluno = rset.getDate(5).toLocalDate();
				}
				aluno.setDataNasc(data_nasc_aluno);
				aluno.setCodigoCliente(rset.getString(6));

				listaAlunos.add(aluno);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaAlunos;
	}

	/**
	 * Método que busca o registro de um aluno do banco de dados através do seu
	 * codigo
	 * 
	 * @param alunoId
	 * @return
	 */
	public Aluno detalharAluno(Integer alunoId) {

		Aluno aluno = new Aluno();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select a.id_aluno, a.nome_aluno, a.curso_aluno, a.numero_matricula_aluno, a.data_nasc_aluno, a.codigo_cliente_aluno "
					+ "from tb_aluno a where a.id_aluno = " + alunoId;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				aluno = new Aluno();

				aluno.setIdCliente(rset.getInt(1));
				aluno.setNome(rset.getString(2));
				// Recupera o curso
				CursoDAO cursoDAO = new CursoDAO();
				Curso curso = cursoDAO.detalharCurso(rset.getInt(3));
				aluno.setCurso(curso);
				aluno.setNumeroMatricula(rset.getString(4));
				LocalDate data_nasc_aluno = null;
				if (rset.getDate(5) != null) {
					data_nasc_aluno = rset.getDate(5).toLocalDate();
				}
				aluno.setDataNasc(data_nasc_aluno);
				aluno.setCodigoCliente(rset.getString(6));
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return aluno;
	}

}
