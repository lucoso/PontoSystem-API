package br.com.mmw.pontosystem.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Ponto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="Ponto_Hora", nullable=true)
	private LocalTime hora;
	
	@Column(name="Ponto_Data", nullable=true)
	private LocalDate data;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPonto tipo;
	
	@ManyToOne
	@JoinColumn(name = "Funcionario_ID")
	private Funcionario funcionario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public TipoPonto getTipo() {
		return tipo;
	}

	public void setTipo(TipoPonto tipo) {
		this.tipo = tipo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ponto other = (Ponto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Se o long for negativo, então atraso (chegou depois do horario).
	 * Se for positivo, então extra (cehgou antes do horario).
	 * @param primeiroPonto
	 * @return
	 */
	public long calcularAtrasoOuExtraEntrada(LocalTime primeiroPonto) {
		
		long atrasoOuExtra = primeiroPonto.until(getFuncionario().getHoraEntrada(), ChronoUnit.MINUTES);
		
		return atrasoOuExtra;
	}
	
	/**
	 * Se retornar maior que 60min, então o funcionario fez mais de 1h de intervalo (atraso).
	 * Se retornar menos de 60min, então ele fez menos de 1h de intervalo (extra).
	 * @param entradaIntervalo
	 * @param saidaIntervalo
	 * @return
	 */
	public long calcularAtrasoOuExtraIntervalo(LocalTime entradaIntervalo, LocalTime saidaIntervalo) {
		
		long atrasoOuExtra = entradaIntervalo.until(saidaIntervalo, ChronoUnit.MINUTES);
		
		return atrasoOuExtra;
		
		
	}

	/**
	 * Se for negativo, então o funcionario saiu antes do horario ("atraso" - trabalhou menos).
	 * Se for positivo, então o funcionario saiu depois do horario (extra - fez hora extra, trabalhou mais)
	 * @param ultimoPonto
	 * @return
	 */
	public long calcularAtrasoOuExtraSaida(LocalTime ultimoPonto) {
		
		long atrasoOuExtra = getFuncionario().getHoraSaida().until(ultimoPonto, ChronoUnit.MINUTES);
		
		return atrasoOuExtra;
		
	}
}