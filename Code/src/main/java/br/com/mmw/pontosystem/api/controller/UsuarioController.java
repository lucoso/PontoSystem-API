package br.com.mmw.pontosystem.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import br.com.mmw.pontosystem.api.assembler.UsuarioModelAssembler;
import br.com.mmw.pontosystem.api.model.UsuarioInputDTO;
import br.com.mmw.pontosystem.api.model.UsuarioOutputDTO;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.model.Usuario;
import br.com.mmw.pontosystem.domain.service.FuncionarioService;
import br.com.mmw.pontosystem.domain.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private final UsuarioModelAssembler assembler;
	
	public UsuarioController(UsuarioService usuarioService, FuncionarioService funcionarioService,
			UsuarioModelAssembler assembler) {
		super();
		this.usuarioService = usuarioService;
		this.funcionarioService = funcionarioService;
		this.assembler = assembler;
	}

	/**
	 * Métodos
	 * @author Lucas Manhães
	 */
	
	private UsuarioOutputDTO FromModelToDTO(Usuario u) {
		
		UsuarioOutputDTO userDto = new UsuarioOutputDTO();
		userDto.setId(u.getId());
		userDto.setNomeDeUsuario(u.getLogin());
		userDto.setSenha(u.getSenha());
		userDto.setPapel(u.getPapel());
		userDto.setAtivo(u.isAtivo());
		userDto.setFuncionario(u.getFuncionario().getNome());
		
		return userDto;
	}
	
	private Usuario FromDtoToModel(UsuarioInputDTO user) {
		
		Usuario usuario = new Usuario();
		usuario.setLogin(user.getNomeDeUsuario());
		usuario.setSenha(user.getSenha());
		usuario.setPapel(user.getPapel());
		usuario.setAtivo(user.isAtivo());
		
		Funcionario f = funcionarioService.BuscarPorID(user.getFuncionarioID());
		
		if(f != null) {
			usuario.setFuncionario(f);
		}
		
		return usuario;
	}
	
	@PostMapping
	public ResponseEntity<?> Cadastrar(@Valid @RequestBody UsuarioInputDTO user) {
		
		Usuario usuario = FromDtoToModel(user);
		
		Usuario userCadastrado = usuarioService.Cadastrar(usuario);
		
		UsuarioOutputDTO userDto = FromModelToDTO(userCadastrado);
		
		EntityModel<UsuarioOutputDTO> entityModel = assembler.toModel(userDto);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@GetMapping
	public CollectionModel<EntityModel<UsuarioOutputDTO>> BuscarTodos(){
		
		List<Usuario> users = usuarioService.BuscarTodos();
		List<UsuarioOutputDTO> usersDtos = new ArrayList<UsuarioOutputDTO>();
		
		for(Usuario u : users) {
			UsuarioOutputDTO userDto = FromModelToDTO(u);
			usersDtos.add(userDto);
		}
		
		List<EntityModel<UsuarioOutputDTO>> usersModel = usersDtos.stream().map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(usersModel,
				linkTo(methodOn(UsuarioController.class).BuscarTodos()).withSelfRel());
	}
	
	@GetMapping("/{userID}")
	public ResponseEntity<?> BuscarPorID(@PathVariable Long userID){
		
		Usuario u = usuarioService.BuscarPorID(userID);
		UsuarioOutputDTO userDto = new UsuarioOutputDTO();
		
		if(u == null) {
			return ResponseEntity.status(404).build();
		}else {
			userDto = FromModelToDTO(u);
		}
		
		return ResponseEntity.status(200).body(assembler.toModel(userDto));
	}
	
	/**
	 * COLOCAR METODO PARA BUSCAR PELO NOME USANDO A URI "usuarios?nome=lucoso"
	 * @param userID
	 * @param userDto
	 * @return
	 */
	
	@GetMapping("/username")
	public ResponseEntity<?> BuscarPorLogin(@RequestParam("nome") String username){
		
		Usuario u = usuarioService.BuscarPorLogin(username);
		UsuarioOutputDTO userDto = new UsuarioOutputDTO();
		
		if(u == null) {
			return ResponseEntity.status(404).build();
		}else {
			userDto = FromModelToDTO(u);
		}
		
		return ResponseEntity.status(200).body(assembler.toModel(userDto));
	}
	
	@PutMapping("/{userID}")
	public ResponseEntity<?> Atualizar(@PathVariable Long userID, @RequestBody UsuarioInputDTO userDto){
		
		Usuario usuario = FromDtoToModel(userDto);
		Usuario u = usuarioService.Atualizar(userID, usuario);
		
		if(u == null) {
			return ResponseEntity.status(404).build();
		}
		
		UsuarioOutputDTO userAtualizadoDto = FromModelToDTO(u);
		
		return ResponseEntity.status(200).body(assembler.toModel(userAtualizadoDto));
	}
	
	@DeleteMapping("/{userID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void Excluir(@PathVariable Long userID) {
		
		usuarioService.Excluir(userID);
		
	}

}
