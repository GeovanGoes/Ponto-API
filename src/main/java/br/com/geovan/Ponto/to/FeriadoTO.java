package br.com.geovan.Ponto.to;

public class FeriadoTO {
	private String tipo;
	private String nome;
	
	public FeriadoTO(String tipo, String nome) {
		this.tipo = tipo;
		this.nome = nome;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
