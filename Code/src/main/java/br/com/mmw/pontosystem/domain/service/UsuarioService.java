package br.com.mmw.pontosystem.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mmw.pontosystem.domain.exception.EntidadeNaoEncontradaException;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;
import br.com.mmw.pontosystem.domain.model.Usuario;
import br.com.mmw.pontosystem.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Metodos
	 * @author Lucas Manhães
	 */

	public Usuario Cadastrar(Usuario u) {

		Usuario user = usuarioRepository.findByLogin(u.getLogin());

		if (user != null) {
			throw new ErroDoUsuarioException("Esse Login Já Existe");
		}

		return usuarioRepository.save(u);
	}

	public List<Usuario> BuscarTodos() {
		return usuarioRepository.findAll();
	}

	public Usuario BuscarPorID(Long id) {

		Optional<Usuario> u = usuarioRepository.findById(id);

		if (!u.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário Não Encontrado");
		}

		return u.get();
	}

	public Usuario BuscarPorLogin(String login) {

		Usuario u = usuarioRepository.findByLogin(login);

		if (u == null) {
			throw new EntidadeNaoEncontradaException("Usuário Não Encontrado");
		}

		return u;
	}

	public Usuario Atualizar(Long id, Usuario u) {

		Optional<Usuario> user = usuarioRepository.findById(id);

		if (!user.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário Não Encontrado! Não é possivel Atualizar!");
		}

		u.setId(id);
		return usuarioRepository.save(u);
	}

	public void Excluir(Long id) {

		Optional<Usuario> u = usuarioRepository.findById(id);

		if (!u.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário Não Encontrado! Não é possivel Excluir");
		}

		usuarioRepository.delete(u.get());
	}

}
