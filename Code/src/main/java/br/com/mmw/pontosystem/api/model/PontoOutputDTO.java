package br.com.mmw.pontosystem.api.model;

import java.util.List;

public class PontoOutputDTO {
	
	private Long funcionarioID;
	
	private String funcionarioNome;
	
	private List<PontoDTO> pontos;

	public String getFuncionarioNome() {
		return funcionarioNome;
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionarioNome = funcionarioNome;
	}

	public Long getFuncionarioID() {
		return funcionarioID;
	}

	public void setFuncionarioID(Long funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	public List<PontoDTO> getPontos() {
		return pontos;
	}

	public void setPontos(List<PontoDTO> pontos) {
		this.pontos = pontos;
	}
	
	

}
