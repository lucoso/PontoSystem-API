package br.com.mmw.pontosystem.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.mmw.pontosystem.api.assembler.FuncionarioModelAssembler;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.service.FuncionarioService;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcService;
	
	@Autowired
	private final FuncionarioModelAssembler assembler;
	
	public FuncionarioController(FuncionarioService funcService, FuncionarioModelAssembler assembler) {
		super();
		this.funcService = funcService;
		this.assembler = assembler;
	}

	/**
	 * Métodos
	 * @author Lucas Manhães
	 */
	
	@PostMapping
	public ResponseEntity<?> Cadastrar(@Valid @RequestBody Funcionario func) {
		
		Funcionario f = funcService.CadastrarFuncionario(func);
		
		EntityModel<Funcionario> entityModel = assembler.toModel(f);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public CollectionModel<EntityModel<Funcionario>> buscarTodos(){
		
		List<Funcionario> funcionarios = funcService.buscarTodos();
		
		List<EntityModel<Funcionario>> funcionariosModel = funcionarios.stream().map(assembler::toModel).collect(Collectors.toList());
		
		return CollectionModel.of(funcionariosModel, linkTo(methodOn(FuncionarioController.class).buscarTodos()).withSelfRel());
		
	}
	
	@GetMapping("/{funcID}")
	public ResponseEntity<?> buscarPorID(@PathVariable Long funcID){
		
		Funcionario func = funcService.BuscarPorID(funcID);
		
		if(func == null) {
			return ResponseEntity.status(404).build();
		}
		
		return ResponseEntity.ok(assembler.toModel(func));
	}
	
	@PutMapping("/{funcID}")
	public ResponseEntity<?> Atualizar(@PathVariable Long funcID, @RequestBody Funcionario funcionario){
		
		Funcionario f = funcService.Atualizar(funcID, funcionario);
		
		return ResponseEntity.ok(assembler.toModel(f)); 
		
	}
	
	@DeleteMapping("/{funcID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void Excluir(@PathVariable Long funcID) {
		
		funcService.Remover(funcID);
	}

}
