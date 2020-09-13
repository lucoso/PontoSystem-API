package br.com.mmw.pontosystem.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mmw.pontosystem.domain.model.Ponto;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {
	
	List<Ponto> findByFuncionarioId(Long id);
	List<Ponto> findByFuncionarioIdAndData(Long id, LocalDate data);
	List<Ponto> findByFuncionarioIdAndDataOrderByDataAsc(Long id, LocalDate data);
	List<Ponto> findByFuncionarioIdAndDataAfterAndDataBefore(Long id, LocalDate datai, LocalDate dataf);

}
