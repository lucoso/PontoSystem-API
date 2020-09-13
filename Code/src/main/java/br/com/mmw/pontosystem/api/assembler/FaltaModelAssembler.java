package br.com.mmw.pontosystem.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.mmw.pontosystem.api.controller.FaltaController;
import br.com.mmw.pontosystem.api.model.FaltaOutputDTO;

@Component
public class FaltaModelAssembler implements RepresentationModelAssembler<FaltaOutputDTO, EntityModel<FaltaOutputDTO>> {

	@Override
	public EntityModel<FaltaOutputDTO> toModel(FaltaOutputDTO faltaDto) {
		
		return EntityModel.of(faltaDto, //
				linkTo(methodOn(FaltaController.class).BuscarFaltaDoFuncionario(faltaDto.getFuncionarioID(), faltaDto.getId())).withSelfRel(),
				linkTo(methodOn(FaltaController.class).BuscarFaltasDoFuncionario(faltaDto.getFuncionarioID())).withRel("faltas"));
	}

}
