package br.com.mmw.pontosystem.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mmw.pontosystem.domain.exception.EntidadeNaoEncontradaException;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.model.Ponto;
import br.com.mmw.pontosystem.domain.repository.PontoRepository;

@Service
public class PontoService {

	@Autowired
	private PontoRepository pontoRepository;

	@Autowired
	private FuncionarioService funcService;

	/**
	 * Métodos
	 * @author Lucas Manhães
	 */

	public Ponto BaterPonto(Ponto ponto) {

		return pontoRepository.save(ponto);
	}

	public List<Ponto> BuscarTodos() {

		return pontoRepository.findAll();
	}

	public Ponto BuscarPorID(Long pontoId) {

		return pontoRepository.findById(pontoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ponto não encontrado"));
	}
	
	public Ponto BuscarPorIDDoFuncionario(Long funcID, Long pontoId) {
		
		Funcionario f = funcService.BuscarPorID(funcID);
		Optional<Ponto> p = null;
		
		if(f == null) {
			throw new ErroDoUsuarioException("Funcionário Não Existe!");
		}else {
			p = pontoRepository.findById(pontoId);
			if(p.isPresent()) {
				if(!p.get().getFuncionario().equals(f)) {
					throw new ErroDoUsuarioException("Este Ponto não pertence a este Funcionario!");
				}
			}else {
				throw new EntidadeNaoEncontradaException("Ponto não encontrado");
			}
		}

		return p.get();
				
	}

	public List<Ponto> BuscarPorFuncionario(Long funcID) {
		
		Funcionario f = funcService.BuscarPorID(funcID);
		
		if(f == null) {
			throw new ErroDoUsuarioException("Funcionário Não Existe!");
		}
		
		List<Ponto> pontos = pontoRepository.findByFuncionarioId(funcID);

		if (pontos.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Nenhum Ponto Encontrado para esse Funcionario");
		}

		return pontos;
	}

	public List<Ponto> BuscarPorFuncionarioEData(Long funcID, LocalDate data) {
		
		Funcionario f = funcService.BuscarPorID(funcID);
		
		if(f == null) {
			throw new ErroDoUsuarioException("Funcionário Não Existe!");
		}

		List<Ponto> pontos = pontoRepository.findByFuncionarioIdAndData(funcID, data);

		if (pontos.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Nenhum Ponto Encontrado para esse Funcionario");
		}

		return pontos;
	}
	
	public List<Ponto> BuscarPorFuncionarioEDataOrdenado(Long funcID, LocalDate data) {
		
		List<Ponto> pontos = pontoRepository.findByFuncionarioIdAndDataOrderByDataAsc(funcID, data);

		if (pontos.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Nenhum Ponto Encontrado para esse Funcionario");
		}

		return pontos;
	}

	public List<Ponto> BuscarPorFuncionarioEDataParaCadastro(Long funcID, LocalDate data) {

		Funcionario f = funcService.BuscarPorID(funcID);
		
		if(f == null) {
			throw new ErroDoUsuarioException("Funcionário Não Existe!");
		}
		
		return pontoRepository.findByFuncionarioIdAndData(funcID, data);

	}
	
	public List<Ponto> BuscarEntreDatas(Funcionario f, LocalDate dataInicial, LocalDate dataFinal){
		
		return pontoRepository.findByFuncionarioIdAndDataAfterAndDataBefore(f.getId(), dataInicial, dataFinal);
	}

	public Ponto Atualizar(Long pontoID, Ponto ponto) {

		ponto.setId(pontoID);
		return pontoRepository.save(ponto);
	}

	public void Remover(Long funcID, Long pontoID) {

		Funcionario f = funcService.BuscarPorID(funcID);
		Optional<Ponto> pontoExistente = pontoRepository.findById(pontoID);

		if (!pontoExistente.isPresent()) {

			throw new EntidadeNaoEncontradaException("Ponto Não Existe! Não é possivel Remover");

		} else {
			
			if(pontoExistente.get().getFuncionario().equals(f)) {
				pontoRepository.deleteById(pontoID);
			}else {
				throw new ErroDoUsuarioException("Este Ponto Não Pertence a este Funcionário! Não é Possível Excluir!");
			}
		}
	}

}
