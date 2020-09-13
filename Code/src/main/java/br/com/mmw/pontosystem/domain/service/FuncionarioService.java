package br.com.mmw.pontosystem.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mmw.pontosystem.domain.exception.EntidadeNaoEncontradaException;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcRepository;

	/**
	 * Métodos
	 * @author Lucas Manhães
	 */

	public Funcionario CadastrarFuncionario(Funcionario func) {

		Funcionario funcExistente = funcRepository.findByCpf(func.getCpf());

		if ((funcExistente != null) && (!funcExistente.equals(func))) {
			
			throw new ErroDoUsuarioException("Funcionário Já Existe com esse CPF!");

		}

		return funcRepository.save(func);
	}

	public List<Funcionario> buscarTodos() {

		return funcRepository.findAll();
	}


	public Funcionario BuscarPorID(Long funcionarioId) {
				
		return funcRepository.findById(funcionarioId).orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionario não encontrado"));
	}

	public Funcionario Atualizar(Long funcID, Funcionario funcionario) {

		Optional<Funcionario> funcExistente = funcRepository.findById(funcID);

		if (!funcExistente.isPresent()) {

			throw new EntidadeNaoEncontradaException("Funcionário Não Existe! Não é possivel Atualizar");
		} 
		
		funcionario.setId(funcID);
		return funcRepository.save(funcionario);
	}

	public void Remover(Long funcID) {

		Optional<Funcionario> funcExistente = funcRepository.findById(funcID);

		if (!funcExistente.isPresent()) {
			
			throw new EntidadeNaoEncontradaException("Funcionário Não Existe! Não é possivel Remover");

		} else {
			funcRepository.deleteById(funcID);
		}
	}

}
