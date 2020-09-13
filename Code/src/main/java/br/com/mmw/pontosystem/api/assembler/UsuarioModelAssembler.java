package br.com.mmw.pontosystem.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.mmw.pontosystem.api.controller.UsuarioController;
import br.com.mmw.pontosystem.api.model.UsuarioOutputDTO;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioOutputDTO, EntityModel<UsuarioOutputDTO>> {

	@Override
	public EntityModel<UsuarioOutputDTO> toModel(UsuarioOutputDTO userDto) {
		
		return EntityModel.of(userDto, //
				linkTo(methodOn(UsuarioController.class).BuscarPorID(userDto.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioController.class).BuscarTodos()).withRel("usuarios"));
	}

}
