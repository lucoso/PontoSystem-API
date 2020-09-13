package br.com.mmw.pontosystem.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Funcionario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name="Funcionario_Nome", nullable=false)
	private String nome;
	
	@NotEmpty
	@Column(name="Funcionario_Celular", nullable=false)
	private String cel;
	
	@NotEmpty
	@Column(name="Funcionario_Email", nullable=false)
	private String email;
	
	@NotEmpty
	@CPF
	@Column(name="Funcionario_CPF", nullable=false)
	private String cpf;
	
	@NotEmpty
	@Column(name="Funcionario_sexo", nullable=false)
	private String sexo;
	
	@NotNull
	@Column(name="Funcionario_DataNascimento", nullable=false)
	private LocalDate datanasc;
	
	@NotEmpty
	@Column(name="Funcionario_Carteira_Trabalho", nullable=false)
	private String cartDeTrabalho;
	
	@Column(name="Funcionario_Funcao", nullable=true)
	private String funcao;
	
	@Column(name="Funcionario_HoraEntrada", nullable=true)
	private LocalTime horaEntrada;
	
	@Column(name="Funcionario_HoraSaida", nullable=true)
	private LocalTime horaSaida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCel() {
		return cel;
	}

	public void setCel(String cel) {
		this.cel = cel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCartDeTrabalho() {
		return cartDeTrabalho;
	}

	public void setCartDeTrabalho(String cartDeTrabalho) {
		this.cartDeTrabalho = cartDeTrabalho;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public LocalDate getDatanasc() {
		return datanasc;
	}

	public void setDatanasc(LocalDate datanasc) {
		this.datanasc = datanasc;
	}

	public LocalTime getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(LocalTime horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public LocalTime getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(LocalTime horaSaida) {
		this.horaSaida = horaSaida;
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
		Funcionario other = (Funcionario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
