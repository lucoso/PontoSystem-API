package br.com.mmw.pontosystem.api.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.mmw.pontosystem.domain.model.Papel;

public class UsuarioInputDTO {
	
	@NotEmpty
	private String nomeDeUsuario;
	
	@NotEmpty
	private String senha;
	
	@NotNull
	private Papel papel;
	
	@NotNull
	private boolean ativo;
	
	@NotNull
	private Long funcionarioID;

	public String getNomeDeUsuario() {
		return nomeDeUsuario;
	}

	public void setNomeDeUsuario(String nomeDeUsuario) {
		this.nomeDeUsuario = nomeDeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Long getFuncionarioID() {
		return funcionarioID;
	}

	public void setFuncionarioID(Long funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	
	
}
