package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String APIKEY = "&apikey=bd4571b8";

    public void exbiriMenu() {
        System.out.println("Digite o nome da Série: ");
        var NomeSerie = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + NomeSerie.replace(" ", "+") + APIKEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <=dados.totalTemporadas(); i++){

            json = consumo.obterDados(ENDERECO + NomeSerie.replace(" ", "+")+ "&season=" + i + APIKEY);
            DadosTemporada dadoTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadoTemporada);
        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    }
}
