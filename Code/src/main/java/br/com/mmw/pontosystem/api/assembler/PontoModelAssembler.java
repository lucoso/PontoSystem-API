package br.com.mmw.pontosystem.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.mmw.pontosystem.api.controller.PontoController;
import br.com.mmw.pontosystem.api.model.PontoDTO;
import br.com.mmw.pontosystem.api.model.PontoOutputDTO;

@Component
public class PontoModelAssembler implements RepresentationModelAssembler<PontoOutputDTO, EntityModel<PontoOutputDTO>> {
	
	@Override
	public EntityModel<PontoOutputDTO> toModel(PontoOutputDTO pontoDto) {
		
		EntityModel<PontoOutputDTO> pontoModel = EntityModel.of(pontoDto, //
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(pontoDto.getFuncionarioID())).withRel("pontos"));
		
		List<PontoDTO> pontosDtos = pontoDto.getPontos();
		
		for(PontoDTO p : pontosDtos) {
			pontoModel.add(linkTo(methodOn(PontoController.class).BuscarPontoDoFuncionario(pontoDto.getFuncionarioID(), p.getId())).withSelfRel());
		}
		
		return pontoModel;
	}

}
