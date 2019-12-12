package application;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import application.model.Aluno;
import application.model.Estoque;
import application.model.Funcionario;
import application.model.ItemVenda;
import application.model.Servidor;
import application.model.TipoCliente;
import application.model.Venda;
import application.model.DAO.AlunoDAO;
import application.model.DAO.CursoDAO;
import application.model.DAO.EstoqueDAO;
import application.model.DAO.FornecedorDAO;
import application.model.DAO.FuncionarioDAO;
import application.model.DAO.ServidorDAO;
import application.model.DAO.VendaDAO;

public class AppMain {
	private static VendaDAO daoVenda;
	private static EstoqueDAO daoEstoque;
	private static FornecedorDAO daoFornecedor;
	private static CursoDAO daoCurso;
	private static AlunoDAO daoAluno;
	private static Scanner ler;
	private static ServidorDAO daoServidor;
	private static FuncionarioDAO daoFuncionario;

	public static void main(String[] args) {
		daoAluno = new AlunoDAO();
		daoVenda = new VendaDAO();
		daoEstoque = new EstoqueDAO();
		daoFornecedor = new FornecedorDAO();
		daoCurso = new CursoDAO();
		daoServidor = new ServidorDAO();
		daoFuncionario = new FuncionarioDAO();
		ler = new Scanner(System.in);

		Funcionario funcionario = new Funcionario();
		funcionario = daoFuncionario.detalharFuncionario(1);

		System.out.println("Seja Bem-vindo ao RU, identifique-se: ");
		System.out.println("Digite seu usuario: ");
		String user = ler.next();
		System.out.println("Digite sua senha: ");
		String password = ler.next();

		while (funcionario.autenticar(password, user) == false) {
			System.out.println("Usuario o senha incoreto, tente novamente. ");
			System.out.println("Digite seu usuario: ");
			user = ler.next();
			System.out.println("Digite sua senha: ");
			password = ler.next();
			funcionario.autenticar(password, user);
		}

		System.out.println();
		System.out.println("Bem-vindo ao menu incial: ");
		menuPrincipal();

	}

	public static void menuPrincipal() {

		System.out.println("Que menu você deseja acessar?");
		System.out.println("[1] - Venda");
		System.out.println("[2] - Sair");

		Integer opcaoProduto = ler.nextInt();

		switch (opcaoProduto) {
		case 1:
			menuDaVenda();
			break;
		case 2:
			System.out.println("Saindo...");
			break;
		default:
			menuPrincipal();
			break;
		}
	}

	public static void menuDaVenda() {

		System.out.println("O que deseja realizar na venda?");
		System.out.println("1- Listar vendas");
		System.out.println("2- Detalhar uma Venda");
		System.out.println("3- Cancelar uma venda");
		System.out.println("4- Realizar uma nova venda");
		System.out.println("5- Alterar uma venda");

		Integer opcaoVenda = ler.nextInt();

		switch (opcaoVenda) {
		case 1:
			listarVendas();
			break;
		case 2:
			detalharVenda();
			break;
		case 3:
			removerVenda();
			break;
		case 4:
			cadastrarVenda();
			break;
		case 5:
			editarVenda();
			break;
		default:
			System.out.println("Opcão Invalida.");
			menuPrincipal();
			break;
		}
	}

	public static void cadastrarVenda() {
		// Realizando venda
		System.out.println("===== REALIZAR VENDA =====");

		System.out.println("Realizar venda de: ");
		System.out.println("[1] - Aluno ");
		System.out.println("[2] - Funcionario");

		Integer escolha = ler.nextInt();
		switch (escolha) {
		case 1:
			cadastrarVenda(TipoCliente.ALUNO);
			break;
		case 2:
			cadastrarVenda(TipoCliente.SERVIDOR);
			break;
		default:
			cadastrarVenda();
		}

	}

	public static void listarVendas() {
		// Listando vendas realizadas
		System.out.println("===== LISTA DE VENDAS =====");
		List<Venda> vendasConsultadas = daoVenda.consultarVendas(null);

		for (Venda venda : vendasConsultadas) {
			System.out.println(venda);
			List<ItemVenda> itemVendaDetalhado = daoVenda.listarItensVenda(venda);
			for (ItemVenda items : itemVendaDetalhado) {
				System.out.println(items);
			}
			System.out.println("\n" + "----------------------------//-----------------------------" + "\n");
		}
		menuPrincipal();
	}

	public static void detalharVenda() {
		// Recuperando uma venda por código
		System.out.println("===== DETALHAR VENDA =====");
		System.out.println("Digite o id da venda: ");
		Integer numVenda = ler.nextInt();
		Venda vendaDetalhada = daoVenda.detalharVenda(numVenda);
		System.out.println(vendaDetalhada);
		List<ItemVenda> itemVendaDetalhado = daoVenda.listarItensVenda(vendaDetalhada);
		for (ItemVenda items : itemVendaDetalhado) {
			System.out.println(items);
		}
		System.out.println();

		menuPrincipal();
	}

	public static void editarVenda() {
		// Editando um produto
		System.out.println("EDITAR VENDA");

		System.out.println("Editar a venda de: ");
		System.out.println("[1] - Aluno ");
		System.out.println("[2] - Funcionario");

		Integer escolha = ler.nextInt();
		switch (escolha) {
		case 1:
			editarVenda(TipoCliente.ALUNO);
			break;
		case 2:
			editarVenda(TipoCliente.SERVIDOR);
			break;
		default:
			System.out.println("Opcão Invalida.");
			menuPrincipal();
			break;
		}

	}

	public static void removerVenda() {
		// Removendo um produto
		System.out.println(" REMOVER VENDA");
		List<Venda> vendasConsultadas = daoVenda.consultarVendas(null);
		for (Venda venda : vendasConsultadas) {
			System.out.println(venda);
		}
		System.out.println("Digite o id da venda que deseja remover: ");
		Integer removerVenda = ler.nextInt();
		daoVenda.removerItemVenda(removerVenda);
		daoVenda.removerVenda(removerVenda);
		System.out.println("Venda removida com sucesso.");
		menuPrincipal();
	}

	public static void cadastrarVenda(TipoCliente tipo) {
		// Vendedor
		Funcionario funcionario = new Funcionario();
		funcionario = daoFuncionario.detalharFuncionario(1);

		Venda venda = new Venda();
		venda.setVedendor(funcionario);

		if (tipo.equals(TipoCliente.ALUNO)) {
			Aluno clienteAluno;
			Integer idAlunoConsult;
			do {
				List<Aluno> alunosConsultados = daoAluno.consultarAlunos();
				for (Aluno aluno : alunosConsultados) {
					System.out.println(aluno);
				}

				System.out.println("Digite o id do aluno: ");
				idAlunoConsult = ler.nextInt();
				clienteAluno = new Aluno();
				clienteAluno = daoAluno.detalharAluno(idAlunoConsult);

			} while (clienteAluno.getIdCliente() == null);

			venda.setCliente(clienteAluno);
		} else {
			Servidor clienteServidor;
			Integer idServidorConsult;
			do {
				List<Servidor> servidoresConsultados = daoServidor.consultarServidores();
				for (Servidor servidor : servidoresConsultados) {
					System.out.println(servidor);
				}

				System.out.println("Digite o id do servidor: ");
				idServidorConsult = ler.nextInt();
				clienteServidor = new Servidor();
				clienteServidor = daoServidor.detalharServidor(idServidorConsult);

			} while (clienteServidor.getIdCliente() == null);

			venda.setCliente(clienteServidor);
		}
		venda.setDataExpedicao(LocalDate.now());

		Estoque produto1;
		Integer idProd1;

		do {
			List<Estoque> estoquesConsultados = daoEstoque.consultarEstoques();
			for (Estoque estoque : estoquesConsultados) {
				System.out.println(estoque);
			}
			System.out.println("Digite o id do produto");
			idProd1 = ler.nextInt();

			produto1 = daoEstoque.detalharEstoque(idProd1);

		} while (produto1.getIdEstoque() == null);

		while (produto1.getQuantidade() <= 0) {
			System.out.println("!!Produto indisponivel!!" + "\n");
			System.out.println("Selecione outro");
			System.out.println("Digite o id do produto");
			idProd1 = ler.nextInt();

			produto1 = daoEstoque.detalharEstoque(idProd1);
		}

		ItemVenda item1 = new ItemVenda();
		item1.setEstoque(produto1);
		// valor calcular
		item1.setValorUnitario(produto1.getValorUnitario());

		System.out.println("Digite a quantidade: ");
		Integer quantidade = ler.nextInt();
		item1.setQuantidade(quantidade);

		while (produto1.getQuantidade() < quantidade) {
			System.out.println("!!Quantidade indisponivel!!" + "\n");
			System.out.println("Selecione um numero menor: ");
			System.out.println("Digite a quantidade: ");
			quantidade = ler.nextInt();
			item1.setQuantidade(quantidade);
		}

		item1.setVenda(venda);

		venda.getProdutosComprados().add(item1);

		System.out.println("Deseja comprar outro produto: ");
		System.out.println("[1] - Sim");
		System.out.println("[2] - Não");
		Integer selecao = ler.nextInt();

		if (selecao == 1) {

			Estoque produto2;
			Integer idProd2;

			do {
				List<Estoque> estoquesConsultados = daoEstoque.consultarEstoques();
				for (Estoque estoque : estoquesConsultados) {
					System.out.println(estoque);
				}
				System.out.println("Digite o id do produto");
				idProd2 = ler.nextInt();

				produto2 = daoEstoque.detalharEstoque(idProd2);

			} while (produto2.getIdEstoque() == null);

			while (produto2.getQuantidade() <= 0) {
				System.out.println("!!Produto indisponivel!!" + "\n");
				System.out.println("Selecione outro");
				System.out.println("Digite o id do produto");
				idProd2 = ler.nextInt();

				produto2 = daoEstoque.detalharEstoque(idProd2);
			}

			ItemVenda item2 = new ItemVenda();
			item2.setEstoque(produto2);
			// valor calcular
			item2.setValorUnitario(produto2.getValorUnitario());

			System.out.println("Digite a quantidade: ");
			Integer quantidade2 = ler.nextInt();

			while (produto2.getQuantidade() < quantidade2) {
				System.out.println("!!Quantidade indisponivel!!" + "\n");
				System.out.println("Selecione um numero menor: ");
				System.out.println("Digite a quantidade: ");
				quantidade2 = ler.nextInt();
				item2.setQuantidade(quantidade2);
			}

			item2.setQuantidade(quantidade2);

			item2.setVenda(venda);

			venda.getProdutosComprados().add(item2);

			produto2.atualizarEstoque(quantidade2);
			daoEstoque.atualizarEstoque(produto2);
		}

		venda.calcularValorTotal();
		daoVenda.cadastrarVenda(venda, tipo);
		System.out.println("Venda registrada com sucesso.");
		System.out.println("Valor total: " + venda.calcularValorTotal());
		produto1.atualizarEstoque(quantidade);
		daoEstoque.atualizarEstoque(produto1);

		menuPrincipal();
	}

	public static void editarVenda(TipoCliente tipo) {
		if (tipo.equals(TipoCliente.ALUNO)) {
			List<Venda> vendasConsultadas = daoVenda.consultarVendas(tipo.ALUNO);
			for (Venda venda : vendasConsultadas) {
				System.out.println(venda);
				List<ItemVenda> itemVendaDetalhado = daoVenda.listarItensVenda(venda);
				for (ItemVenda items : itemVendaDetalhado) {
					System.out.println(items);
				}
				System.out.println("\n" + "----------------------------//-----------------------------" + "\n");
			}
		} else {
			List<Venda> vendasConsultadas = daoVenda.consultarVendas(tipo.SERVIDOR);
			for (Venda venda : vendasConsultadas) {
				System.out.println(venda);
				List<ItemVenda> itemVendaDetalhado = daoVenda.listarItensVenda(venda);
				for (ItemVenda items : itemVendaDetalhado) {
					System.out.println(items);
				}
				System.out.println("\n" + "----------------------------//-----------------------------" + "\n");
			}
		}

		System.out.println("Digite o id da venda: ");
		Integer numVenda = ler.nextInt();
		Venda vendaDetalhada = daoVenda.detalharVenda(numVenda);
		daoVenda.removerItemVenda(numVenda);

		if (tipo.equals(TipoCliente.ALUNO)) {
			Aluno clienteAluno;
			Integer idAlunoConsult;
			do {
				List<Aluno> alunosConsultados = daoAluno.consultarAlunos();
				for (Aluno aluno : alunosConsultados) {
					System.out.println(aluno);
				}

				System.out.println("Digite o id do aluno: ");
				idAlunoConsult = ler.nextInt();
				clienteAluno = new Aluno();
				clienteAluno = daoAluno.detalharAluno(idAlunoConsult);

			} while (clienteAluno.getIdCliente() == null);

			vendaDetalhada.setCliente(clienteAluno);
		} else {
			Servidor clienteServidor;
			Integer idServidorConsult;
			do {
				List<Servidor> servidoresConsultados = daoServidor.consultarServidores();
				for (Servidor servidor : servidoresConsultados) {
					System.out.println(servidor);
				}

				System.out.println("Digite o id do servidor: ");
				idServidorConsult = ler.nextInt();
				clienteServidor = new Servidor();
				clienteServidor = daoServidor.detalharServidor(idServidorConsult);

			} while (clienteServidor.getIdCliente() == null);

			vendaDetalhada.setCliente(clienteServidor);
		}

		vendaDetalhada.setDataExpedicao(LocalDate.now());

		List<Estoque> estoquesConsultados = daoEstoque.consultarEstoques();
		for (Estoque estoque : estoquesConsultados) {
			System.out.println(estoque);
		}
		System.out.println("Digite o id do produto");
		Integer idProd1 = ler.nextInt();

		Estoque produto1 = daoEstoque.detalharEstoque(idProd1);

		while (produto1.getQuantidade() <= 0) {
			System.out.println("!!Produto indisponivel!!" + "\n");
			System.out.println("Selecione outro");
			System.out.println("Digite o id do produto");
			idProd1 = ler.nextInt();

			produto1 = daoEstoque.detalharEstoque(idProd1);
		}

		ItemVenda item1 = new ItemVenda();
		item1.setEstoque(produto1);
		// valor calcular
		item1.setValorUnitario(produto1.getValorUnitario());

		System.out.println("Digite a quantidade: ");
		Integer quantidade = ler.nextInt();
		item1.setQuantidade(quantidade);

		while (produto1.getQuantidade() < quantidade) {
			System.out.println("!!Quantidade indisponivel!!" + "\n");
			System.out.println("Selecione um numero menor: ");
			System.out.println("Digite a quantidade: ");
			quantidade = ler.nextInt();
			item1.setQuantidade(quantidade);
		}

		item1.setVenda(vendaDetalhada);

		vendaDetalhada.getProdutosComprados().add(item1);

		System.out.println("Deseja comprar outro produto: ");
		System.out.println("[1] - Sim");
		System.out.println("[2] - Não");
		Integer selecao = ler.nextInt();

		if (selecao == 1) {
			List<Estoque> estoquesConsultados2 = daoEstoque.consultarEstoques();
			for (Estoque estoque : estoquesConsultados2) {
				System.out.println(estoque);
			}
			System.out.println("Digite o id do produto");
			Integer idProd2 = ler.nextInt();

			Estoque produto2 = daoEstoque.detalharEstoque(idProd2);

			while (produto2.getQuantidade() <= 0) {
				System.out.println("!!Produto indisponivel!!" + "\n");
				System.out.println("Selecione outro");
				System.out.println("Digite o id do produto");
				idProd2 = ler.nextInt();

				produto2 = daoEstoque.detalharEstoque(idProd2);
			}

			ItemVenda item2 = new ItemVenda();
			item2.setEstoque(produto2);
			// valor calcular
			item2.setValorUnitario(produto2.getValorUnitario());

			System.out.println("Digite a quantidade: ");
			Integer quantidade2 = ler.nextInt();

			while (produto2.getQuantidade() < quantidade2) {
				System.out.println("!!Quantidade indisponivel!!" + "\n");
				System.out.println("Selecione um numero menor: ");
				System.out.println("Digite a quantidade: ");
				quantidade2 = ler.nextInt();
				item2.setQuantidade(quantidade2);
			}

			item2.setQuantidade(quantidade2);

			item2.setVenda(vendaDetalhada);

			vendaDetalhada.getProdutosComprados().add(item2);

			produto2.atualizarEstoque(quantidade2);
			daoEstoque.atualizarEstoque(produto2);
		}

		vendaDetalhada.calcularValorTotal();
		daoVenda.atualizarVenda(vendaDetalhada, tipo);
		System.out.println("Valor total: " + vendaDetalhada.calcularValorTotal());
		produto1.atualizarEstoque(quantidade);
		daoEstoque.atualizarEstoque(produto1);
		System.out.println("Venda alterada com sucesso.");
		menuPrincipal();

	}
}
