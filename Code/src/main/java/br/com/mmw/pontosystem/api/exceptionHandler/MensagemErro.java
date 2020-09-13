package br.com.mmw.pontosystem.api.exceptionHandler;

import java.time.OffsetDateTime;

public class MensagemErro {
	
	private int status;
	
	private OffsetDateTime datahora;
	
	private String titulo;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public OffsetDateTime getDatahora() {
		return datahora;
	}

	public void setDatahora(OffsetDateTime datahora) {
		this.datahora = datahora;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	

}
