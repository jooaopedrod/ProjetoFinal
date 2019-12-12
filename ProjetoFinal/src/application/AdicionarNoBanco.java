package application;

import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

import application.model.Aluno;
import application.model.Curso;
import application.model.Estoque;
import application.model.Fornecedor;
import application.model.Funcao;
import application.model.Funcionario;
import application.model.Servidor;
import application.model.DAO.AlunoDAO;
import application.model.DAO.CursoDAO;
import application.model.DAO.EstoqueDAO;
import application.model.DAO.FornecedorDAO;
import application.model.DAO.FuncionarioDAO;
import application.model.DAO.ServidorDAO;
import application.model.DAO.VendaDAO;

public class AdicionarNoBanco {
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

		// Vendedor
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("João Carlos");
		funcionario.setSenha("0000");
		funcionario.setUsuario("vendedor");
		daoFuncionario.cadastrarFuncionario(funcionario);

		// Fornecedores
		Fornecedor pratsLtda = new Fornecedor();
		pratsLtda.setNome("Prats Ltda");
		daoFornecedor.cadastrarFornecedor(pratsLtda);

		Fornecedor ru = new Fornecedor();
		ru.setNome("Restaurante");
		daoFornecedor.cadastrarFornecedor(ru);

		// Estoques - Produtos
		Estoque pratsLaranja = new Estoque();
		pratsLaranja.setCodigoProduto("101010");
		pratsLaranja.setNomeProduto("Prats Laranja");
		pratsLaranja.setQuantidade(30);
		pratsLaranja.setFornecedor(pratsLtda);
		pratsLaranja.setValorUnitario(4.0);
		daoEstoque.cadastrarEstoque(pratsLaranja);

		Estoque pratsUva = new Estoque();
		pratsUva.setCodigoProduto("101020");
		pratsUva.setNomeProduto("Prats Uva");
		pratsUva.setQuantidade(30);
		pratsUva.setFornecedor(pratsLtda);
		pratsUva.setValorUnitario(4.0);
		daoEstoque.cadastrarEstoque(pratsUva);

		Estoque pratsLimao = new Estoque();
		pratsLimao.setCodigoProduto("101030");
		pratsLimao.setNomeProduto("Prats Limao");
		pratsLimao.setQuantidade(30);
		pratsLimao.setFornecedor(pratsLtda);
		pratsLimao.setValorUnitario(4.0);
		daoEstoque.cadastrarEstoque(pratsLimao);

		Estoque marmita = new Estoque();
		marmita.setCodigoProduto("101040");
		marmita.setNomeProduto("Marmita");
		marmita.setQuantidade(30);
		marmita.setFornecedor(ru);
		marmita.setValorUnitario(4.0);
		daoEstoque.cadastrarEstoque(marmita);

		Estoque buffet = new Estoque();
		buffet.setCodigoProduto("101050");
		buffet.setNomeProduto("Buffet");
		buffet.setQuantidade(30);
		buffet.setFornecedor(ru);
		buffet.setValorUnitario(5.0);
		daoEstoque.cadastrarEstoque(buffet);

		// Cursos
		Curso informatica = new Curso();
		informatica.setNomeCurso("Informática");
		daoCurso.cadastrarCurso(informatica);

		Curso aquicultura = new Curso();
		aquicultura.setNomeCurso("Aquicultura");
		daoCurso.cadastrarCurso(aquicultura);

		Curso meioAmbiente = new Curso();
		meioAmbiente.setNomeCurso("Meio Ambiente");
		daoCurso.cadastrarCurso(meioAmbiente);

		Curso edificacaoe = new Curso();
		edificacaoe.setNomeCurso("Edificações");
		daoCurso.cadastrarCurso(edificacaoe);

		// Clientes
		Aluno cliente1 = new Aluno();
		cliente1.setNome("Pedro Costa Alves");
		cliente1.setDataNasc(LocalDate.of(2004, Month.FEBRUARY, 5));
		((Aluno) cliente1).setCurso(informatica);
		((Aluno) cliente1).setNumeroMatricula("201929");
		cliente1.setCodigoCliente("102030");
		daoAluno.cadastrarAluno(cliente1);

		Aluno cliente2 = new Aluno();
		cliente2.setNome("Maria Lima Costa");
		cliente2.setDataNasc(LocalDate.of(2004, Month.NOVEMBER, 15));
		((Aluno) cliente2).setCurso(meioAmbiente);
		((Aluno) cliente2).setNumeroMatricula("201930");
		cliente2.setCodigoCliente("102040");
		daoAluno.cadastrarAluno(cliente2);

		Servidor cliente3 = new Servidor();
		cliente3.setNome("Manuela Melo Barbosa");
		cliente3.setDataNasc(LocalDate.of(1960, Month.DECEMBER, 02));
		cliente3.setFuncao(Funcao.CONCURSADO);
		cliente3.setSiape("2256759");
		cliente3.setCodigoCliente("102050");
		daoServidor.cadastrarServidor(cliente3);

		Servidor cliente4 = new Servidor();
		cliente4.setNome("Luiz Oliveira Fernandes");
		cliente4.setDataNasc(LocalDate.of(1980, Month.JULY, 11));
		cliente4.setFuncao(Funcao.CONTRATADO);
		cliente4.setSiape("2256760");
		cliente4.setCodigoCliente("102056");
		daoServidor.cadastrarServidor(cliente4);
	}

}
