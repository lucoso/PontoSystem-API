package br.com.mmw.pontosystem.api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FaltaInputDTO {
	
	private Long faltaID;
	
	private LocalDate data;
	
	private String motivo;

	public Long getFaltaID() {
		return faltaID;
	}

	public void setFaltaID(Long faltaID) {
		this.faltaID = faltaID;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
	
	

}
