package br.com.mmw.pontosystem.api.model;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

public class PontoInputDTO {
	
	@NotNull
	private LocalTime hora;

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}
	
	

}
