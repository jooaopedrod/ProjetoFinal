package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Funcao;
import application.model.Servidor;

public class ServidorDAO {

	/**
	 * Método para recuperar a funcao do servidor (ENUM) para salvar
	 * 
	 * @param servidor
	 * @return
	 */
	private Integer getFuncao(Servidor servidor) {
		Integer funcao;
		switch (servidor.getFuncao()) {
		case CONCURSADO:
			funcao = Funcao.SERVIDOR_CONCURSADO;
			break;

		case CONTRATADO:
			funcao = Funcao.SERVIDOR_CONTRATADO;
			break;

		default:
			funcao = 0;
			break;

		}

		return funcao;
	}

	/**
	 * Método para setar a funcao do servidor (ENUM) vindo do banco de dados
	 * 
	 * @param servidor
	 * @return
	 */
	private Funcao setFuncao(Integer funcao) {
		Funcao funcaoEnum;
		switch (funcao) {
		case 0:
			funcaoEnum = Funcao.CONTRATADO;
			break;

		case 1:
			funcaoEnum = Funcao.CONCURSADO;
			break;
		default:
			funcaoEnum = Funcao.CONTRATADO;
			break;
		}

		return funcaoEnum;
	}

	/**
	 * Método que cadastra um novo servidor na base de dados
	 * 
	 * @param servidor
	 * @return
	 */
	public Servidor cadastrarServidor(Servidor servidor) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_servidor = "'" + servidor.getNome() + "'";
				String siape_servidor = "'" + servidor.getSiape() + "'";
				String data_nasc_servidor = (servidor.getDataNasc() != null ? ("'" + servidor.getDataNasc() + "'")
						: null);
				String codigo_cliente_servidor = "'" + servidor.getCodigoCliente() + "'";
				String sql = "insert into tb_servidor  "
						+ "(nome_servidor, funcao_servidor, siape_servidor, data_nasc_servidor, codigo_cliente_servidor) "
						+ "values (" + nome_servidor + ", " + this.getFuncao(servidor) + ", " + siape_servidor + ", "
						+ data_nasc_servidor + "," + codigo_cliente_servidor + ") returning id_servidor";
				stmt.execute(sql);
				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_servidor = stmt.getResultSet();
				if (last_insert_servidor.next()) {
					servidor.setIdCliente(last_insert_servidor.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return servidor;
	}

	/**
	 * Método que atualiza o registro de um servidor no banco de dados
	 * 
	 * @param servidor
	 * @return
	 */
	public Servidor atualizarServidor(Servidor servidor) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String nome_servidor = "'" + servidor.getNome() + "'";
				String siape_servidor = "'" + servidor.getSiape();
				String data_nasc_servidor = (servidor.getDataNasc() != null ? ("'" + servidor.getDataNasc() + "'")
						: null);
				String codigo_cliente_servidor = "'" + servidor.getCodigoCliente();

				stmt.executeUpdate("update tb_servidor set " + "nome_servidor = " + nome_servidor
						+ ", funcao_servidor = " + this.getFuncao(servidor) + ", siape_servidor = " + siape_servidor
						+ ", data_nasc_servidor = " + data_nasc_servidor + ", codigo_cliente_servidor = "
						+ codigo_cliente_servidor + " where id_servidor = " + servidor.getIdCliente());
			} catch (SQLException e) {
				System.out.println("Erro ao autlizar tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return servidor;
	}

	/**
	 * Método que remove um registro de servidor do banco de dados
	 * 
	 * @param servidorId
	 */
	public void removerServidor(Integer servidorId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_servidor where codigo = " + servidorId);
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
	 * Método que busca todos os registros de servidores do banco de dados
	 * 
	 * @return
	 */
	public List<Servidor> consultarServidores() {

		List<Servidor> listaServidores = new ArrayList<Servidor>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select s.id_servidor, s.nome_servidor, s.funcao_servidor, s.siape_servidor, s.data_nasc_servidor, s.codigo_cliente_servidor "
					+ "from tb_servidor s";
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {
				Servidor servidor = new Servidor();

				servidor.setIdCliente(rset.getInt(1));
				servidor.setNome(rset.getString(2));
				servidor.setFuncao(this.setFuncao(rset.getInt(3)));
				servidor.setSiape(rset.getString(4));
				LocalDate data_nasc_aluno = null;
				if (rset.getDate(5) != null) {
					data_nasc_aluno = rset.getDate(5).toLocalDate();
				}
				servidor.setDataNasc(data_nasc_aluno);
				servidor.setCodigoCliente(rset.getString(6));

				listaServidores.add(servidor);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaServidores;
	}

	/**
	 * Método que busca o registro de um servidor do banco de dados através do seu
	 * codigo
	 * 
	 * @param servidorId
	 * @return
	 */
	public Servidor detalharServidor(Integer servidorId) {

		Servidor servidor = new Servidor();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select s.id_servidor, s.nome_servidor, s.funcao_servidor, s.siape_servidor, s.data_nasc_servidor, s.codigo_cliente_servidor "
					+ "from tb_servidor s where s.id_servidor = " + servidorId;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				servidor.setIdCliente(rset.getInt(1));
				servidor.setNome(rset.getString(2));
				servidor.setFuncao(this.setFuncao(rset.getInt(3)));
				servidor.setSiape(rset.getString(4));
				LocalDate data_nasc_aluno = null;
				if (rset.getDate(5) != null) {
					data_nasc_aluno = rset.getDate(5).toLocalDate();
				}
				servidor.setDataNasc(data_nasc_aluno);
				servidor.setCodigoCliente(rset.getString(6));
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return servidor;
	}

}
