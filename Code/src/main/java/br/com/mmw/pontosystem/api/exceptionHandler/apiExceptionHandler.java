package br.com.mmw.pontosystem.api.exceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.mmw.pontosystem.domain.exception.EntidadeNaoEncontradaException;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;

@ControllerAdvice
public class apiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(ErroDoUsuarioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		MensagemErro erro = new MensagemErro();
		erro.setStatus(status.value());
		erro.setTitulo(ex.getMessage());
		erro.setDatahora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ErroDoUsuarioException.class)
	public ResponseEntity<Object> handleNegocio(ErroDoUsuarioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		MensagemErro erro = new MensagemErro();
		erro.setStatus(status.value());
		erro.setTitulo(ex.getMessage());
		erro.setDatahora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Validacao.Campo> campos = new ArrayList<Validacao.Campo>();
		
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new Validacao.Campo(nome, mensagem));
		}
		
		Validacao valid = new Validacao();
		valid.setStatus(status.value());
		valid.setTitulo("Um ou mais campos estão inválidos. "
				+ "Faça o preenchimento correto e tente novamente");
		valid.setDataHora(OffsetDateTime.now());
		valid.setCampos(campos);
		
		return super.handleExceptionInternal(ex, valid, headers, status, request);
	}
	

}
