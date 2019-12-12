package application.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venda {

	private Integer idVenda;
	private LocalDate dataExpedicao;
	private Funcionario vedendor;
	private List<ItemVenda> produtosComprados = new ArrayList<ItemVenda>();
	private Double valor;
	private Cliente cliente;

	public Integer getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(Integer idVenda) {
		this.idVenda = idVenda;
	}

	public LocalDate getDataExpedicao() {
		return dataExpedicao;
	}

	public void setDataExpedicao(LocalDate dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	public Funcionario getVedendor() {
		return vedendor;
	}

	public void setVedendor(Funcionario vedendor) {
		this.vedendor = vedendor;
	}

	public List<ItemVenda> getProdutosComprados() {
		return produtosComprados;
	}

	public void setProdutosComprados(List<ItemVenda> produtosComprados) {
		this.produtosComprados = produtosComprados;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((dataExpedicao == null) ? 0 : dataExpedicao.hashCode());
		result = prime * result + ((idVenda == null) ? 0 : idVenda.hashCode());
		result = prime * result + ((produtosComprados == null) ? 0 : produtosComprados.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((vedendor == null) ? 0 : vedendor.hashCode());
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
		Venda other = (Venda) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (dataExpedicao == null) {
			if (other.dataExpedicao != null)
				return false;
		} else if (!dataExpedicao.equals(other.dataExpedicao))
			return false;
		if (idVenda == null) {
			if (other.idVenda != null)
				return false;
		} else if (!idVenda.equals(other.idVenda))
			return false;
		if (produtosComprados == null) {
			if (other.produtosComprados != null)
				return false;
		} else if (!produtosComprados.equals(other.produtosComprados))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (vedendor == null) {
			if (other.vedendor != null)
				return false;
		} else if (!vedendor.equals(other.vedendor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id da venda [" + idVenda + "], dataExpedicao: " + dataExpedicao + ", vedendor: "
				+ getVedendor().getNome() + ", produtosComprados=" + produtosComprados + ", valor=" + valor
				+ ", cliente=" + cliente + "]";
	}

	public Venda(Integer idVenda, LocalDate dataExpedicao, Funcionario vedendor, List<ItemVenda> produtosComprados,
			Double valor, Cliente cliente) {
		super();
		this.idVenda = idVenda;
		this.dataExpedicao = dataExpedicao;
		this.vedendor = vedendor;
		this.produtosComprados = produtosComprados;
		this.valor = valor;
		this.cliente = cliente;
	}

	public Venda() {
	}

	public Double calcularValorTotal() {
		this.valor = 0.0;
		for (ItemVenda itemVenda : produtosComprados) {
			this.valor = this.valor + (itemVenda.getValorUnitario() * itemVenda.getQuantidade());
		}
		return valor;
	}

}