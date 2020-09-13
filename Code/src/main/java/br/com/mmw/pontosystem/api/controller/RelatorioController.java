package br.com.mmw.pontosystem.api.controller;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mmw.pontosystem.api.assembler.RelatorioModelAssembler;
import br.com.mmw.pontosystem.api.model.FaltaOutputDTO;
import br.com.mmw.pontosystem.api.model.PontoDTO;
import br.com.mmw.pontosystem.api.model.RelatorioInput;
import br.com.mmw.pontosystem.api.model.RelatorioOutput;
import br.com.mmw.pontosystem.domain.exception.EntidadeNaoEncontradaException;
import br.com.mmw.pontosystem.domain.exception.ErroDoUsuarioException;
import br.com.mmw.pontosystem.domain.model.Falta;
import br.com.mmw.pontosystem.domain.model.Funcionario;
import br.com.mmw.pontosystem.domain.model.Ponto;
import br.com.mmw.pontosystem.domain.model.TipoPonto;
import br.com.mmw.pontosystem.domain.service.FaltaService;
import br.com.mmw.pontosystem.domain.service.FuncionarioService;
import br.com.mmw.pontosystem.domain.service.PontoService;

@RestController
@RequestMapping("/funcionarios/{funcID}/relatorios")
public class RelatorioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private PontoService pontoService;
	
	@Autowired
	private FaltaService faltaService;
	
	@Autowired
	private final RelatorioModelAssembler assembler;
	
	/**
	 * Métodos
	 * @author Lucas Manhães
	 */
	
	public RelatorioController(FuncionarioService funcionarioService, PontoService pontoService,
			FaltaService faltaService, RelatorioModelAssembler assembler) {
		super();
		this.funcionarioService = funcionarioService;
		this.pontoService = pontoService;
		this.faltaService = faltaService;
		this.assembler = assembler;
	}

	/**
	 * Metodo para Calcular a quantodade de minutos extras que foi feito pelo funcionario, somente dos pontos extras batidos.
	 * Pontos extras, são todos os pontos que foram batidos fora do horário de trabalho normal do funcionario. 
	 * Exemplo: o funcionario bateu seu ultimo ponto e saiu do trabalho às 18h. Porém por algum motivo precisou trabalhar e bateu novos pontos as 19h (extra entrada) e às 20h (extra saida), 
	 * ou seja esse funcionario trabalhou 1h extra nesse dia.
	 * @param lista
	 * @return long
	 * @author Lucas Manhães
	 */
	private long calcularExtraDosPontosExtras(List<LocalTime> lista) {
		
		long total = 0;
		Iterator<LocalTime> it = lista.iterator();
		for(int i=0;i<lista.size();i++) {
			while(it.hasNext()) {
				LocalTime lt = it.next();
				long extra = lt.until(it.next(), ChronoUnit.MINUTES);
				total = total + extra;
			}
		}
		
		return total;
	}
	
	@PostMapping
	public ResponseEntity<?> GerarRelatorio(@PathVariable Long funcID, @RequestBody RelatorioInput relatorioInput){
		
		boolean b = relatorioInput.getDataInicial().isBefore(relatorioInput.getDataFinal());
		
		if(b == false) {
			throw new ErroDoUsuarioException("A Data Inicial deve ser antes da Data Final");
		}
		
		long atrasos = 0;
		long extras = 0;
		
		Funcionario f = funcionarioService.BuscarPorID(funcID);
		
		if(f == null) {
			return ResponseEntity.status(404).build();
		}
		
		List<Ponto> pontos = pontoService.BuscarEntreDatas(f, relatorioInput.getDataInicial(), relatorioInput.getDataFinal());
		
		if(pontos.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Nenhum Ponto Encontrado para esse Funcionario nessas Datas");
		}
		
		List<Falta> faltas = faltaService.BuscarFaltasDoFuncionarioEntreDatas(f, relatorioInput.getDataInicial(), relatorioInput.getDataFinal());
		List<Ponto> pontosEntradaDoIntervalo = new ArrayList<Ponto>();
		List<Ponto> pontosSaidaDoIntervalo = new ArrayList<Ponto>();
		List<Ponto> pontosExtras = new ArrayList<Ponto>();
		List<PontoDTO> pontosDtos = new ArrayList<PontoDTO>();
		List<FaltaOutputDTO> faltasOutputDtos = new ArrayList<FaltaOutputDTO>();
		
		for(Falta falta : faltas) {
			FaltaOutputDTO faltaDTO = new FaltaOutputDTO();
			faltaDTO.setData(falta.getData());
			faltaDTO.setMotivo(falta.getMotivo());
			
			faltasOutputDtos.add(faltaDTO);
		}
		
		for(Ponto p : pontos) {
			
			PontoDTO pontoDto = new PontoDTO();
			pontoDto.setId(p.getId());
			pontoDto.setData(p.getData());
			pontoDto.setHora(p.getHora());
			pontoDto.setTipo(p.getTipo().toString());
			
			pontosDtos.add(pontoDto);
			
			if(p.getTipo().equals(TipoPonto.Entrada)) {
				long atrasoOuExtra = p.calcularAtrasoOuExtraEntrada(p.getHora());
				if(atrasoOuExtra > 0) {
					extras = extras + atrasoOuExtra;
				}else {
					atrasos = atrasos + atrasoOuExtra;
				}
			}
			
			if(p.getTipo().equals(TipoPonto.Saida)) {
				long atrasoOuExtra = p.calcularAtrasoOuExtraSaida(p.getHora());
				if(atrasoOuExtra > 0) {
					extras = extras + atrasoOuExtra;
				}else {
					atrasos = atrasos + atrasoOuExtra;
				}
			}
			
			if(p.getTipo().equals(TipoPonto.Entrada_Intervalo)) {
				pontosEntradaDoIntervalo.add(p);
			}
			
			if(p.getTipo().equals(TipoPonto.Saida_Intervalo)) {
				pontosSaidaDoIntervalo.add(p);
			}
			
			if(p.getTipo().equals(TipoPonto.Extra)) {
				pontosExtras.add(p);
			}
		}
		
		for(Ponto x : pontosEntradaDoIntervalo) {
			for(Ponto y : pontosSaidaDoIntervalo) {
				if(x.getData().isEqual(y.getData())) {
					long atrasoOuExtra = x.calcularAtrasoOuExtraIntervalo(x.getHora(), y.getHora());
					if(atrasoOuExtra > 60) {
						long verdatraso = atrasoOuExtra - 60;
						atrasos = atrasos + verdatraso;
					}else {
						long verdextra = 60 - atrasoOuExtra;
						extras = extras + verdextra;
					}
				}
			}
		}
		
		List<LocalTime> listaDeHorariosExtras = new ArrayList<LocalTime>();
		for(Ponto z : pontosExtras) {
			listaDeHorariosExtras.add(z.getHora());
		}
		
		long totalDosPontosExtras = calcularExtraDosPontosExtras(listaDeHorariosExtras);
		extras = extras + totalDosPontosExtras;
		
		long totalDeDias = relatorioInput.getDataInicial().until(relatorioInput.getDataFinal(), ChronoUnit.DAYS);
		int totalDeFaltas = faltas.size();
		
		RelatorioOutput rel = new RelatorioOutput();
		rel.setMsg("Relatório Gerado com Sucesso! Abaixo Detalhes do Relatório");
		rel.setFuncionarioID(f.getId());
		rel.setFuncionario(f.getNome());
		rel.setPeriodo("De: " + relatorioInput.getDataInicial().toString() + " Até: " + relatorioInput.getDataFinal().toString());
		rel.setTotalDeDias(String.valueOf(totalDeDias));
		rel.setTotalDeFaltas(String.valueOf(totalDeFaltas));
		rel.setHorasAtrasos(String.valueOf(atrasos) + " minutos");
		rel.setHorasExtras(String.valueOf(extras) + " minutos");
		rel.setPontos(pontosDtos);
		rel.setFaltas(faltasOutputDtos);
		
		return ResponseEntity.status(201).body(assembler.toModel(rel));
	}

}
