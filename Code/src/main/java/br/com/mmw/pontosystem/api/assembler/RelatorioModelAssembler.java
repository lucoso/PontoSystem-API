package br.com.mmw.pontosystem.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.mmw.pontosystem.api.controller.RelatorioController;
import br.com.mmw.pontosystem.api.model.RelatorioOutput;

@Component
public class RelatorioModelAssembler implements RepresentationModelAssembler<RelatorioOutput, EntityModel<RelatorioOutput>> {

	@Override
	public EntityModel<RelatorioOutput> toModel(RelatorioOutput relatorio) {
		
		return EntityModel.of(relatorio, //
				linkTo(methodOn(RelatorioController.class).GerarRelatorio(relatorio.getFuncionarioID(),null)).withSelfRel());
	}

}
