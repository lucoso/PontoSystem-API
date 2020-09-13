package br.com.mmw.pontosystem.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mmw.pontosystem.domain.model.Falta;
import br.com.mmw.pontosystem.domain.model.Funcionario;

@Repository
public interface FaltaRepository extends JpaRepository<Falta, Long> {
	
	List<Falta> findByFuncionario(Funcionario f);
	List<Falta> findByDataAndFuncionario(LocalDate data, Funcionario f);
	List<Falta> findByFuncionarioAndDataAfterAndDataBefore(Funcionario f, LocalDate datai, LocalDate dataf);

}
