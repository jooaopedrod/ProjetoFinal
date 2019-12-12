package application.model;

import java.time.LocalDate;

public class Aluno extends Cliente {

	private String numeroMatricula;
	private Curso curso;

	public String getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((numeroMatricula == null) ? 0 : numeroMatricula.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		if (numeroMatricula == null) {
			if (other.numeroMatricula != null)
				return false;
		} else if (!numeroMatricula.equals(other.numeroMatricula))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id do aluno [" + getIdCliente() + "], nome: " + getNome() + ", matricula: " + getNumeroMatricula()
				+ ", curso: " + getCurso().getNomeCurso();
	}

	public Aluno(Integer idCliente, String nome, LocalDate dataNasc, String codigoCliente, String numeroMatricula,
			Curso curso) {
		super(idCliente, nome, dataNasc, codigoCliente);
		this.numeroMatricula = numeroMatricula;
		this.curso = curso;
	}

	public Aluno() {
		super();
		// TODO Auto-generated constructor stub
	}

}
