package application.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.config.JDBCConnection;
import application.model.Aluno;
import application.model.Estoque;
import application.model.Funcionario;
import application.model.ItemVenda;
import application.model.Servidor;
import application.model.TipoCliente;
import application.model.Venda;

public class VendaDAO {

	public Venda cadastrarVenda(Venda venda, TipoCliente tipo) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {

				String cliente_alu_venda = "";
				String cliente_ser_venda = "";

				if (tipo.equals(TipoCliente.SERVIDOR)) {
					cliente_alu_venda = null;
					cliente_ser_venda = (venda.getCliente().getIdCliente() != null
							? ("'" + venda.getCliente().getIdCliente() + "'")
							: null);
				} else {
					cliente_ser_venda = null;
					cliente_alu_venda = (venda.getCliente().getIdCliente() != null
							? ("'" + venda.getCliente().getIdCliente() + "'")
							: null);
				}
				String data_expedicao_venda = "'" + venda.getDataExpedicao() + "'";
				String valor_venda = "'" + venda.getValor() + "'";
				String vendedor_venda = "'" + venda.getVedendor().getIdFuncionario() + "'";
				stmt.execute("insert into tb_venda   "
						+ "(data_expedicao_venda, valor_venda, vendedor_venda, cliente_ser_venda, cliente_alu_venda) "
						+ "values (" + data_expedicao_venda + ", " + valor_venda + ", " + vendedor_venda + ","
						+ cliente_ser_venda + ", " + cliente_alu_venda + ") returning id_venda");

				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_venda = stmt.getResultSet();
				if (last_insert_venda.next()) {
					venda.setIdVenda(last_insert_venda.getInt(1));
					// Inserindo os itens da venda no banco de dados e na lista de itens
					for (ItemVenda item : venda.getProdutosComprados()) {
						inserirItemVenda(item, venda.getIdVenda());
					}

				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return venda;
	}

	private ItemVenda inserirItemVenda(ItemVenda item, Integer idVenda) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.execute("insert into tb_item_venda   "
						+ "(venda_item_venda, estoque_item_venda, valor_uni_item_venda, quantidade_item_venda) "
						+ "values (" + idVenda + ", " + item.getEstoque().getIdEstoque() + ", "
						+ item.getValorUnitario() + ", " + item.getQuantidade() + ") returning id_item_venda");

				/*
				 * Por vezes é necessário recuperar o código que a inserção gerou Nesses casos
				 * acrescentamos "returning codigo" ao final da query, como acima E recuperamos
				 * criando um ResultSet (como quando é uma consulta) Isso é muito usado em casos
				 * de venda e item venda, onde é inserida uma venda e os itens venda precisam do
				 * codigo da venda inserida para setar na foreing key
				 */
				ResultSet last_insert_item = stmt.getResultSet();
				if (last_insert_item.next()) {
					item.setIdItemVenda(last_insert_item.getInt(1));
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return item;
	}

	public List<Venda> consultarVendas(TipoCliente tipo) {

		List<Venda> listaVendas = new ArrayList<Venda>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "";
			if (tipo == null) {
				sql = "select v.id_venda, v.data_expedicao_venda, v.valor_venda, v.vendedor_venda, v.cliente_ser_venda, v.cliente_alu_venda "
						+ "from tb_venda v";
			}

			else if (tipo.equals(TipoCliente.ALUNO)) {
				sql = "select v.id_venda, v.data_expedicao_venda, v.valor_venda, v.vendedor_venda, v.cliente_ser_venda, v.cliente_alu_venda "
						+ "from tb_venda v where v.cliente_ser_venda is null";
			} else {
				sql = "select v.id_venda, v.data_expedicao_venda, v.valor_venda, v.vendedor_venda, v.cliente_ser_venda, v.cliente_alu_venda "
						+ "from tb_venda v where v.cliente_alu_venda is null";
			}

			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {
				Venda venda = new Venda();

				venda.setIdVenda(rset.getInt(1));
				LocalDate data_expedicao_venda = null;
				if (rset.getDate(2) != null) {
					venda.setDataExpedicao(rset.getDate(2).toLocalDate());
				}
				venda.setValor(rset.getDouble(3));
				// recuperar o vendedor
				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				Funcionario funcionario = funcionarioDAO.detalharFuncionario(rset.getInt(4));
				venda.setVedendor(funcionario);
				// Recupera o servidor
				Integer cliente_ser_cliente = null;
				if (rset.getInt(5) != 0) {
					ServidorDAO servidorDAO = new ServidorDAO();
					Servidor servidor = servidorDAO.detalharServidor(rset.getInt(5));
					venda.setCliente(servidor);
				}

				// Recupera o aluno
				Integer cliente_alu_cliente = null;
				if (rset.getInt(6) != 0) {
					AlunoDAO alunoDAO = new AlunoDAO();
					Aluno aluno = alunoDAO.detalharAluno(rset.getInt(6));
					venda.setCliente(aluno);
				}

				venda.getProdutosComprados().addAll(this.listarItensVenda(venda));
				listaVendas.add(venda);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaVendas;
	}

	public List<ItemVenda> listarItensVenda(Venda venda) {
		List<ItemVenda> itens = new ArrayList<ItemVenda>();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select iv.id_item_venda, iv.estoque_item_venda, iv.valor_uni_item_venda, iv.quantidade_item_venda "
					+ "from tb_item_venda iv " + "where venda_item_venda = " + venda.getIdVenda();
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {
				ItemVenda item = new ItemVenda();

				item.setIdItemVenda(rset.getInt(1));
				// Recupera o produto
				EstoqueDAO estoqueDAO = new EstoqueDAO();
				Estoque estoque = estoqueDAO.detalharEstoque(rset.getInt(2));
				item.setEstoque(estoque);
				item.setValorUnitario(rset.getDouble(3));
				item.setQuantidade(rset.getInt(4));
				item.setVenda(venda);

				// recuperar itens da venda

				itens.add(item);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return itens;
	}

	public Venda detalharVenda(Integer vendaCodigo) {

		Venda venda = new Venda();
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();

			String sql = "select v.id_venda, v.data_expedicao_venda, v.valor_venda, v.vendedor_venda, v.cliente_ser_venda, v.cliente_alu_venda "
					+ "from tb_venda v " + "where v.id_venda = " + vendaCodigo;
			ResultSet rset = stmt.executeQuery(sql);

			while (rset.next()) {

				venda.setIdVenda(rset.getInt(1));
				LocalDate data_expedicao_venda = null;
				if (rset.getDate(2) != null) {
					venda.setDataExpedicao(rset.getDate(2).toLocalDate());
				}
				venda.setValor(rset.getDouble(3));
				// recuperar o vendedor
				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				Funcionario funcionario = funcionarioDAO.detalharFuncionario(rset.getInt(4));
				venda.setVedendor(funcionario);
				// Recupera o servidor
				Integer cliente_ser_cliente = null;
				if (rset.getInt(5) != 0) {
					ServidorDAO servidorDAO = new ServidorDAO();
					Servidor servidor = servidorDAO.detalharServidor(rset.getInt(5));
					venda.setCliente(servidor);
				}

				// Recupera o aluno
				Integer cliente_alu_cliente = null;
				if (rset.getInt(6) != 0) {
					AlunoDAO alunoDAO = new AlunoDAO();
					Aluno aluno = alunoDAO.detalharAluno(rset.getInt(6));
					venda.setCliente(aluno);
				}

			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return venda;
	}

	public void removerVenda(Integer vendaId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_venda where id_venda = " + vendaId);
			} catch (SQLException e) {
				System.out.println("Erro ao remover tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

	public void removerItemVenda(Integer vendaId) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				stmt.executeUpdate("delete from tb_item_venda where venda_item_venda = " + vendaId);
			} catch (SQLException e) {
				System.out.println("Erro ao remover tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

	public Venda atualizarVenda(Venda venda, TipoCliente tipo) {
		try {
			JDBCConnection.JDBCConnect();
			Statement stmt = JDBCConnection.conn.createStatement();
			try {
				String cliente_alu_venda = "";
				String cliente_ser_venda = "";

				if (tipo.equals(TipoCliente.SERVIDOR)) {
					cliente_alu_venda = null;
					cliente_ser_venda = (venda.getCliente().getIdCliente() != null
							? ("'" + venda.getCliente().getIdCliente() + "'")
							: null);
				} else {
					cliente_ser_venda = null;
					cliente_alu_venda = (venda.getCliente().getIdCliente() != null
							? ("'" + venda.getCliente().getIdCliente() + "'")
							: null);
				}
				String data_expedicao_venda = "'" + venda.getDataExpedicao() + "'";
				String valor_venda = "'" + venda.getValor() + "'";
				String vendedor_venda = "'" + venda.getVedendor().getIdFuncionario() + "'";

				stmt.executeUpdate("update tb_venda set " + " data_expedicao_venda = " + data_expedicao_venda
						+ ", valor_venda = " + valor_venda + ", vendedor_venda = " + vendedor_venda
						+ ", cliente_ser_venda = " + cliente_ser_venda + ", cliente_alu_venda = " + cliente_alu_venda
						+ " where id_venda = " + venda.getIdVenda());

				// Atualizar os itens da venda no banco de dados e na lista de itens
				for (ItemVenda item : venda.getProdutosComprados()) {
					inserirItemVenda(item, venda.getIdVenda());
				}

			} catch (SQLException e) {
				System.out.println("Erro ao inserir tupla " + e);
			}

			stmt.close();
			JDBCConnection.conn.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return venda;
	}

}
