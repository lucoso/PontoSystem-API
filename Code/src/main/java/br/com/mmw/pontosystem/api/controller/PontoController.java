package br.com.mmw.pontosystem.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import br.com.mmw.pontosystem.api.assembler.PontoModelAssembler;
import br.com.mmw.pontosystem.api.model.PontoDTO;
import br.com.mmw.pontosystem.api.model.PontoInputDTO;
import br.com.mmw.pontosystem.api.model.PontoOutputDTO;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.model.Ponto;
import br.com.mmw.pontosystem.domain.model.TipoPonto;
import br.com.mmw.pontosystem.domain.service.FuncionarioService;
import br.com.mmw.pontosystem.domain.service.PontoService;

@RestController
@RequestMapping("/funcionarios/{funcID}/pontos")
public class PontoController {

	@Autowired
	private PontoService pontoService;

	@Autowired
	private FuncionarioService funcService;

	@Autowired
	private final PontoModelAssembler assembler;

	public PontoController(PontoService pontoService, FuncionarioService funcService, PontoModelAssembler assembler) {
		super();
		this.pontoService = pontoService;
		this.funcService = funcService;
		this.assembler = assembler;
	}

	/**
	 * Métodos
	 * 
	 * @author Lucas Manhães
	 */

	private List<PontoOutputDTO> FromModelToDTO(List<Ponto> pontos) {

		List<PontoOutputDTO> pontosOutputDtos = new ArrayList<PontoOutputDTO>();

		if (!pontos.isEmpty()) {
			PontoOutputDTO pontoOutputDto = new PontoOutputDTO();
			pontoOutputDto.setFuncionarioID(pontos.get(0).getFuncionario().getId());
			pontoOutputDto.setFuncionarioNome(pontos.get(0).getFuncionario().getNome());
			List<PontoDTO> pontosDtos = new ArrayList<PontoDTO>();
			for (Ponto p : pontos) {

				PontoDTO pontoDto = new PontoDTO();
				pontoDto.setId(p.getId());
				pontoDto.setData(p.getData());
				pontoDto.setHora(p.getHora());
				pontoDto.setTipo(p.getTipo().toString());

				pontosDtos.add(pontoDto);

			}
			pontoOutputDto.setPontos(pontosDtos);
			pontosOutputDtos.add(pontoOutputDto);
		}

		return pontosOutputDtos;
	}

	@PostMapping
	public ResponseEntity<?> BaterPonto(@PathVariable Long funcID, @Valid @RequestBody PontoInputDTO pontoInputDto) {

		Funcionario f = funcService.BuscarPorID(funcID);
		PontoOutputDTO pontoOutput = new PontoOutputDTO();

		if (f != null) {
			Ponto p = new Ponto();
			p.setData(LocalDate.now());
			p.setHora(pontoInputDto.getHora());
			p.setFuncionario(f);

			List<Ponto> pontosDoFuncionario = pontoService.BuscarPorFuncionarioEDataParaCadastro(f.getId(),
					LocalDate.now());
			switch (pontosDoFuncionario.size()) {
			case 0:
				p.setTipo(TipoPonto.Entrada);
				break;
			case 1:
				p.setTipo(TipoPonto.Entrada_Intervalo);
				break;
			case 2:
				p.setTipo(TipoPonto.Saida_Intervalo);
				break;
			case 3:
				p.setTipo(TipoPonto.Saida);
				break;
			default:
				p.setTipo(TipoPonto.Extra);
			}

			Ponto pontoCadastrado = pontoService.BaterPonto(p);
			PontoDTO pontoDto = new PontoDTO();
			List<PontoDTO> pontosDtos = new ArrayList<PontoDTO>();
			pontoDto.setId(pontoCadastrado.getId());
			pontoDto.setData(pontoCadastrado.getData());
			pontoDto.setHora(pontoCadastrado.getHora());
			pontoDto.setTipo(pontoCadastrado.getTipo().toString());
			pontosDtos.add(pontoDto);

			pontoOutput.setFuncionarioID(pontoCadastrado.getFuncionario().getId());
			pontoOutput.setFuncionarioNome(pontoCadastrado.getFuncionario().getNome());
			pontoOutput.setPontos(pontosDtos);

		}

		EntityModel<PontoOutputDTO> entityModel = assembler.toModel(pontoOutput);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@GetMapping("/{pontoID}")
	public CollectionModel<EntityModel<PontoOutputDTO>> BuscarPontoDoFuncionario(@PathVariable Long funcID,
			@PathVariable Long pontoID) {

		Ponto p = pontoService.BuscarPorIDDoFuncionario(funcID, pontoID);
		List<PontoOutputDTO> pontosOutputDtos = null;

		if (p != null) {
			List<Ponto> ps = new ArrayList<Ponto>();
			ps.add(p);
			pontosOutputDtos = FromModelToDTO(ps);
		}

		List<EntityModel<PontoOutputDTO>> pontosModel = pontosOutputDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(pontosModel,
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(funcID)).withSelfRel());
	}

	@GetMapping
	public CollectionModel<EntityModel<PontoOutputDTO>> BuscarTodosPontosDoFuncionario(@PathVariable Long funcID) {

		List<Ponto> pontos = pontoService.BuscarPorFuncionario(funcID);
		List<PontoOutputDTO> pontosOutputDtos = null;

		if (!pontos.isEmpty()) {
			pontosOutputDtos = FromModelToDTO(pontos);
		}

		List<EntityModel<PontoOutputDTO>> pontosModel = pontosOutputDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(pontosModel,
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(funcID)).withSelfRel());
	}

	@GetMapping("/hoje")
	public CollectionModel<EntityModel<PontoOutputDTO>> BuscarPontosDoDiaDoFuncionario(@PathVariable Long funcID) {

		List<Ponto> pontos = pontoService.BuscarPorFuncionarioEDataOrdenado(funcID, LocalDate.now());
		List<PontoOutputDTO> pontosOutputDtos = null;

		if (!pontos.isEmpty()) {
			pontosOutputDtos = FromModelToDTO(pontos);
		}

		List<EntityModel<PontoOutputDTO>> pontosModel = pontosOutputDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(pontosModel,
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(funcID)).withSelfRel());
	}

	@GetMapping("/data")
	public CollectionModel<EntityModel<PontoOutputDTO>> BuscarPontosDoFuncionarioPorData(@PathVariable Long funcID,
			@RequestParam("data") String data) {

		LocalDate date = LocalDate.parse(data);
		List<Ponto> pontos = pontoService.BuscarPorFuncionarioEDataOrdenado(funcID, date);
		List<PontoOutputDTO> pontosOutputDtos = null;

		if (!pontos.isEmpty()) {
			pontosOutputDtos = FromModelToDTO(pontos);
		}

		List<EntityModel<PontoOutputDTO>> pontosModel = pontosOutputDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(pontosModel,
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(funcID)).withSelfRel());
	}

	@PutMapping("/{pontoID}")
	public CollectionModel<EntityModel<PontoOutputDTO>> AtualizarPontoDoFuncionario(@PathVariable Long funcID,
			@PathVariable Long pontoID, @RequestBody PontoInputDTO pontoInputDto) {

		Funcionario f = funcService.BuscarPorID(funcID);
		List<PontoOutputDTO> pontosOutputDtos = null;

		if (f != null) {
			Ponto p = pontoService.BuscarPorID(pontoID);
			if (p != null) {
				if (p.getFuncionario().equals(f)) {
					p.setHora(pontoInputDto.getHora());
					Ponto pontoAtualizado = pontoService.Atualizar(p.getId(), p);
					List<Ponto> pontos = new ArrayList<Ponto>();
					pontos.add(pontoAtualizado);
					pontosOutputDtos = FromModelToDTO(pontos);
				} else {
					throw new ErroDoUsuarioException(
							"Este Ponto Não Pertence a este Funcionário! Não é Possível Atualizar!");
				}
			}
		}

		List<EntityModel<PontoOutputDTO>> pontosModel = pontosOutputDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(pontosModel,
				linkTo(methodOn(PontoController.class).BuscarTodosPontosDoFuncionario(funcID)).withSelfRel());
	}

	@DeleteMapping("/{pontoID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void Excluir(@PathVariable Long funcID, @PathVariable Long pontoID) {

		pontoService.Remover(funcID, pontoID);
	}

}
