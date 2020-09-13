package br.com.mmw.pontosystem.domain.exception;

public class EntidadeNaoEncontradaException extends ErroDoUsuarioException {

	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncontradaException(String message) {
		super(message);
	}
}
