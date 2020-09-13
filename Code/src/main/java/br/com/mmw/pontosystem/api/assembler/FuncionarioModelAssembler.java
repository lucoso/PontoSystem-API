package br.com.mmw.pontosystem.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.mmw.pontosystem.api.controller.FaltaController;
import br.com.mmw.pontosystem.api.controller.FuncionarioController;
import br.com.mmw.pontosystem.api.controller.PontoController;
import br.com.mmw.pontosystem.api.controller.RelatorioController;
import br.com.mmw.pontosystem.domain.model.Funcionario;

@Component
public class FuncionarioModelAssembler implements RepresentationModelAssembler<Funcionario, EntityModel<Funcionario>> {

	@Override
	public EntityModel<Funcionario> toModel(Funcionario funcionario) {
		
		return EntityModel.of(funcionario, //
				linkTo(methodOn(FuncionarioController.class).buscarPorID(funcionario.getId())).withSelfRel(),
				linkTo(methodOn(FaltaController.class).BuscarFaltasDoFuncionario(funcionario.getId())).withRel("faltas"),
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(funcionario.getId())).withRel("pontos"),
				linkTo(methodOn(RelatorioController.class).GerarRelatorio(funcionario.getId(),null)).withRel("relatorio"),
				linkTo(methodOn(FuncionarioController.class).buscarTodos()).withRel("funcionarios"));
	}
	
	

}
