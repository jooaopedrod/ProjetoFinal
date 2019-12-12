package application.model;

import java.time.LocalDate;

public class Servidor extends Cliente {

	private String siape;
	private Funcao funcao;

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcao == null) ? 0 : funcao.hashCode());
		result = prime * result + ((siape == null) ? 0 : siape.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servidor other = (Servidor) obj;
		if (funcao != other.funcao)
			return false;
		if (siape == null) {
			if (other.siape != null)
				return false;
		} else if (!siape.equals(other.siape))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id do servidor [" + getIdCliente() + "], nome: " + getNome() + ", siape: " + getSiape() + ", função: "
				+ getFuncao();
	}

	public Servidor(Integer idCliente, String nome, LocalDate dataNasc, String codigoCliente, String siape,
			Funcao funcao) {
		super(idCliente, nome, dataNasc, codigoCliente);
		this.siape = siape;
		this.funcao = funcao;
	}

	public Servidor() {
	}

}
