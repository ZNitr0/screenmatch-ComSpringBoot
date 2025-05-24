package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json =  consumoApi.obterDados("https://www.omdbapi.com/?t=Prison+Break&apikey=bd4571b8");
		System.out.println(json);
		ConverterDados conversor = new ConverterDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		List<DadosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <=dados.totalTemporadas(); i++){
			json = consumoApi.obterDados("https://www.omdbapi.com/?t=Prison+Break&season=" + i + "&apikey=bd4571b8");
			DadosTemporada dadoTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadoTemporada);
		}
		temporadas.forEach(System.out::println);
	}
}
