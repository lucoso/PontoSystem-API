package br.com.mmw.pontosystem.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.mmw.pontosystem.api.assembler.FaltaModelAssembler;
import br.com.mmw.pontosystem.api.model.FaltaInputDTO;
import br.com.mmw.pontosystem.api.model.FaltaOutputDTO;
import br.com.mmw.pontosystem.domain.model.Falta;
import br.com.mmw.pontosystem.domain.model.Ponto;
import br.com.mmw.pontosystem.domain.service.FaltaService;
import br.com.mmw.pontosystem.domain.service.PontoService;

@RestController
@RequestMapping("/funcionarios/{funcID}/faltas")
public class FaltaController {

	@Autowired
	private FaltaService faltaService;

	@Autowired
	private PontoService pontoService;

	@Autowired
	private final FaltaModelAssembler assembler;

	public FaltaController(FaltaService faltaService, PontoService pontoService, FaltaModelAssembler assembler) {
		super();
		this.faltaService = faltaService;
		this.pontoService = pontoService;
		this.assembler = assembler;
	}

	private FaltaOutputDTO fromModelToOutputDTO(Falta f) {

		FaltaOutputDTO faltaOutputDto = new FaltaOutputDTO();

		if (f != null) {
			faltaOutputDto.setId(f.getId());
			faltaOutputDto.setData(f.getData());
			faltaOutputDto.setMotivo(f.getMotivo());
			faltaOutputDto.setFuncionarioID(f.getFuncionario().getId());
			faltaOutputDto.setFuncionario(f.getFuncionario().getNome());
		}

		return faltaOutputDto;
	}

	/**
	 * Métodos
	 * 
	 * @author Lucas Manhães
	 */

	/**
	 * Este método foi feito para que o consumidor da API possa executar
	 * automaticamente todo dia no final do dia. Assim não é necessário o usuário da
	 * aplicação ficar verificando se o funcionario bateu ou não algum ponto para
	 * registrar uma falta. O sistema deve fazer isso automaticamente e assim
	 * cadastrar uma nova falta para esse funcionario ou não. Porém, o moitvo da
	 * falta deve ser preenchido pelo usuario da aplicação, por isso esse método
	 * será feito no POST abaixo dessa API
	 * 
	 * @author Lucas Manhães
	 */
	@PostMapping("/automaticas")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void CadastrarAutomaticamente(@PathVariable Long funcID) {

		List<Ponto> pontos = pontoService.BuscarPorFuncionarioEDataParaCadastro(funcID, LocalDate.now());
		if (pontos.isEmpty()) {
			faltaService.Cadastrar(funcID, null, null);
		}
	}

	@PostMapping
	public ResponseEntity<?> CadastrarManualmente(@PathVariable Long funcID, @RequestBody FaltaInputDTO faltaDto) {

		Falta f = faltaService.Cadastrar(funcID, faltaDto.getFaltaID(), faltaDto.getMotivo());

		FaltaOutputDTO faltaOutputDto = fromModelToOutputDTO(f);
		
		EntityModel<FaltaOutputDTO> entityModel = assembler.toModel(faltaOutputDto);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		
	}

	@GetMapping
	public CollectionModel<EntityModel<FaltaOutputDTO>> BuscarFaltasDoFuncionario(@PathVariable Long funcID) {

		List<Falta> faltas = faltaService.BuscarFaltasDoFuncionario(funcID);
		List<FaltaOutputDTO> faltasDtos = new ArrayList<FaltaOutputDTO>();

		if (!faltas.isEmpty()) {
			for (Falta f : faltas) {
				FaltaOutputDTO faltaOutputDto = fromModelToOutputDTO(f);
				faltasDtos.add(faltaOutputDto);
			}
		}
		
		List<EntityModel<FaltaOutputDTO>> faltasModel = faltasDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(faltasModel,
				linkTo(methodOn(FaltaController.class).BuscarFaltasDoFuncionario(funcID)).withSelfRel());
	}

	@GetMapping("/{faltaID}")
	public ResponseEntity<?> BuscarFaltaDoFuncionario(@PathVariable Long funcID, @PathVariable Long faltaID) {

		Falta falta = faltaService.BuscarFaltaDoFuncionarioPorID(funcID, faltaID);
		FaltaOutputDTO faltaOutputDto = null;

		if (falta != null) {
			faltaOutputDto = fromModelToOutputDTO(falta);
		} 
		
		return ResponseEntity.status(200).body(assembler.toModel(faltaOutputDto));

	}
	
	@GetMapping("/datas")
	public CollectionModel<EntityModel<FaltaOutputDTO>> BuscarFaltasDoFuncionarioPorData(@PathVariable Long funcID, @RequestParam("data") String data) {

		LocalDate date = LocalDate.parse(data);
		List<Falta> faltas = faltaService.BuscarFaltasDoFuncionarioPorData(funcID, date);
		List<FaltaOutputDTO> faltasDtos = new ArrayList<FaltaOutputDTO>();

		if (!faltas.isEmpty()) {
			for (Falta f : faltas) {
				FaltaOutputDTO faltaOutputDto = fromModelToOutputDTO(f);
				faltasDtos.add(faltaOutputDto);
			}
		}
		
		List<EntityModel<FaltaOutputDTO>> faltasModel = faltasDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(faltasModel,
				linkTo(methodOn(FuncionarioController.class).buscarTodos()).withSelfRel());

	}

	@PutMapping("/{faltaID}")
	public ResponseEntity<?> AtualizarFaltaDoFuncionario(@PathVariable Long funcID, @PathVariable Long faltaID,
			@RequestBody FaltaInputDTO faltaDto) {

		Falta falta = new Falta();
		falta.setData(faltaDto.getData());
		falta.setMotivo(faltaDto.getMotivo());

		Falta f = faltaService.Atualizar(funcID, faltaID, falta);
		FaltaOutputDTO faltaOutputDto = fromModelToOutputDTO(f);
		return ResponseEntity.status(200).body(assembler.toModel(faltaOutputDto));

	}

	@DeleteMapping("/{faltaID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void RemoverFaltaDoFuncionario(@PathVariable Long funcID, @PathVariable Long faltaID) {

		faltaService.Excluir(funcID, faltaID);
	}

}
