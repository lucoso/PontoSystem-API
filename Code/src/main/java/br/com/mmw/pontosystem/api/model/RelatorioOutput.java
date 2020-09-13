package br.com.mmw.pontosystem.api.model;

import java.util.List;

public class RelatorioOutput {
	
	private String msg;
	
	private Long funcionarioID;
	
	private String funcionario;
	
	private String periodo;
	
	private String totalDeDias;
	
	private String totalDeFaltas;
	
	private String horasExtras;
	
	private String horasAtrasos;

	private List<PontoDTO> pontos;
	
	private List<FaltaOutputDTO> faltas;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getFuncionarioID() {
		return funcionarioID;
	}

	public void setFuncionarioID(Long funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public String getPeriodo() {
		return periodo;
	}

	public String getTotalDeDias() {
		return totalDeDias;
	}

	public void setTotalDeDias(String totalDeDias) {
		this.totalDeDias = totalDeDias;
	}

	public String getTotalDeFaltas() {
		return totalDeFaltas;
	}

	public void setTotalDeFaltas(String totalDeFaltas) {
		this.totalDeFaltas = totalDeFaltas;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public List<FaltaOutputDTO> getFaltas() {
		return faltas;
	}

	public void setFaltas(List<FaltaOutputDTO> faltas) {
		this.faltas = faltas;
	}

	public List<PontoDTO> getPontos() {
		return pontos;
	}

	public void setPontos(List<PontoDTO> pontos) {
		this.pontos = pontos;
	}

	public String getHorasExtras() {
		return horasExtras;
	}

	public void setHorasExtras(String horasExtras) {
		this.horasExtras = horasExtras;
	}

	public String getHorasAtrasos() {
		return horasAtrasos;
	}

	public void setHorasAtrasos(String horasAtrasos) {
		this.horasAtrasos = horasAtrasos;
	}
	
	
	

}
