package br.com.mmw.pontosystem.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mmw.pontosystem.domain.exception.EntidadeNaoEncontradaException;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;
import br.com.mmw.pontosystem.domain.model.Falta;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.repository.FaltaRepository;

@Service
public class FaltaService {

	@Autowired
	private FaltaRepository faltaRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	/**
	 * Métodos
	 * @author Lucas Manhães
	 */

	/**
	 * Este Método foi feito para cadstrar tanto automaticamente como através do usuário (o cadastro do usuario, é mais um atualizar, 
	 * pois somente é necessário informar o motivo da falta
	 * @param funcID
	 * @param faltaID
	 * @param motivo
	 * @return Falta
	 * @author Lucas Manhães
	 * 
	 */
	public Falta Cadastrar(Long funcID, Long faltaID, String motivo) {
		
		Funcionario funcionario = funcionarioService.BuscarPorID(funcID);

		Falta falta = new Falta();

		/**
		 * Metodo do manual
		 */
		if ((faltaID != null) && (funcID != null)) {
			Optional<Falta> faltaOptional = faltaRepository.findById(faltaID);
			if (faltaOptional.isPresent()) {
				if (faltaOptional.get().getFuncionario().getId().equals(funcID)) {
					Falta f = faltaOptional.get();
					f.setMotivo(motivo);
					falta = faltaRepository.save(f);
				} else {
					throw new ErroDoUsuarioException("Esta Falta Não Pertence a este Funcionario");
				}
			} else {
				throw new EntidadeNaoEncontradaException("Falta Não Encontrada");
			}
		}

		/**
		 * Metodo do automatico
		 */
		if (funcID != null) {
			falta.setData(LocalDate.now());
			falta.setFuncionario(funcionario);
			faltaRepository.save(falta);
		}

		return falta;
	}
	
	public Falta BuscarFaltaDoFuncionarioPorID(Long funcID, Long faltaID) {

		Funcionario f = funcionarioService.BuscarPorID(funcID);
		Optional<Falta> faltaOpt = faltaRepository.findById(faltaID);
		Falta falta = null;

		if (f == null) {
			throw new ErroDoUsuarioException("Funcionario Não Existe");
		}else {
			if(faltaOpt.isPresent()) {
				Falta fal = faltaOpt.get();
				if(fal.getFuncionario().getId().equals(f.getId())) {
					falta = fal;
				}else {
					throw new ErroDoUsuarioException("Essa Falta Não Pertence a esse Funcionario");
				}
			}else {
				throw new EntidadeNaoEncontradaException("Falta Não Encontrada");
			}
		}
		return falta;
	}

	public List<Falta> BuscarFaltasDoFuncionario(Long funcID) {

		Funcionario f = funcionarioService.BuscarPorID(funcID);

		if (f == null) {
			throw new ErroDoUsuarioException("Funcionario Não Existe");
		}

		return faltaRepository.findByFuncionario(f);
	}

	public List<Falta> BuscarFaltasDoFuncionarioPorData(Long funcID, LocalDate data) {

		Funcionario f = funcionarioService.BuscarPorID(funcID);
		List<Falta> faltas = null;

		if (f == null) {
			throw new ErroDoUsuarioException("Funcionario Não Existe");
		}else {
			faltas = faltaRepository.findByDataAndFuncionario(data, f);
			if(faltas.isEmpty()) {
				throw new EntidadeNaoEncontradaException("Nenhuma Falta Encontrada para Este Funcionario!");
			}
		}

		return faltas;
	}
	
	public List<Falta> BuscarFaltasDoFuncionarioEntreDatas(Funcionario f, LocalDate dataInicial, LocalDate dataFinal){
		
		return faltaRepository.findByFuncionarioAndDataAfterAndDataBefore(f, dataInicial, dataFinal);
	}
	
	public Falta Atualizar(Long funcID, Long faltaID, Falta falta) {
		
		Funcionario f = funcionarioService.BuscarPorID(funcID);
		Optional<Falta> faltaBuscada = faltaRepository.findById(faltaID);
		Falta faltaEncontrada = null;
		
		if(f == null) {
			throw new ErroDoUsuarioException("Funcionario não encontrado!");
		}else {
			
			if(!faltaBuscada.isPresent()) {
				throw new EntidadeNaoEncontradaException("Falta Não Encontrada");
			}else {
				faltaEncontrada = faltaBuscada.get();
				if(!faltaEncontrada.getFuncionario().equals(f)) {
					throw new ErroDoUsuarioException("Esta Falta Não Pertence a este Funcionario");
				}else {
					falta.setFuncionario(f);
				}
			}
		}
		
		falta.setId(faltaEncontrada.getId());
		
		return faltaRepository.save(falta);	 
	}
	
	
	public void Excluir(Long funcID, Long faltaID) {
		
		Optional<Falta> faltaOptional = faltaRepository.findById(faltaID);
		
		if(faltaOptional.isPresent()) {
			Falta f = faltaOptional.get();
			if(f.getFuncionario().getId().equals(funcID)) {
				faltaRepository.delete(f);
			}else {
				throw new ErroDoUsuarioException("Esta Falta Não Pertence a Este Funcionario");
			}
		}else {
			throw new EntidadeNaoEncontradaException("Falta Não Encontrada");
		}
	}
	

}
