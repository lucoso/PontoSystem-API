package br.com.mmw.pontosystem.api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FaltaOutputDTO {
	
	private Long id;
	
	private LocalDate data;
	
	private String motivo;
	
	private Long funcionarioID;
	
	private String funcionario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFuncionarioID() {
		return funcionarioID;
	}

	public void setFuncionarioID(Long funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

}
