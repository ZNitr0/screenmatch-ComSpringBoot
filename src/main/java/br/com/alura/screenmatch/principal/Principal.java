package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverterDados;

import java.util.*;
import java.util.stream.Collectors;

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

            List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream())
                    .collect(Collectors.toList());

        System.out.println("\n top 5 episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios =  temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite o trecho do titulo:");
        var trechoTitulos = scanner.nextLine();
         Optional<Episodio> episodioBuscado = episodios.stream()
                 .filter(e-> e.getTitulo().toUpperCase().contains(trechoTitulos.toUpperCase()))
                 .findFirst();

        if (episodioBuscado.isPresent()){
            System.out.println("Episodio econtrado!");
            System.out.println("Temporada:"  + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episodio não encontrado!");
        }
    }
}
