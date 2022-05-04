package br.com.geovan.Ponto.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Usuario {

	
	@Id
	@GeneratedValue
	private Long id;
	
	private String email;
	
	private String senha;
	
	private String nome;
	
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	

	public Usuario(Long id, String email, String senha, String nome) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.nome = nome;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
