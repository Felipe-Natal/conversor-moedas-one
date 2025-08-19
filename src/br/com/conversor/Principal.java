package br.com.conversor;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws Exception {
        Scanner leitura = new Scanner(System.in);
        Gson gson = new Gson();

        String apiKey = "598f8dc8dbba625c44e55d4c"; // sua chave da API
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("""
                Escolha a conversão desejada:
                1 - Dólar → Real
                2 - Real → Dólar
                3 - Dólar → Euro
                4 - Euro → Dólar
                5 - Real → Euro
                6 - Euro → Real
                7 - Sair
                """);

            opcao = leitura.nextInt();
            if (opcao == 7) break;

            System.out.println("Digite o valor para conversão: ");
            double valor = leitura.nextDouble();

            String moedaOrigem = "";
            String moedaDestino = "";

            switch (opcao) {
                case 1 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
                case 2 -> { moedaOrigem = "BRL"; moedaDestino = "USD"; }
                case 3 -> { moedaOrigem = "USD"; moedaDestino = "EUR"; }
                case 4 -> { moedaOrigem = "EUR"; moedaDestino = "USD"; }
                case 5 -> { moedaOrigem = "BRL"; moedaDestino = "EUR"; }
                case 6 -> { moedaOrigem = "EUR"; moedaDestino = "BRL"; }
                default -> { System.out.println("Opção inválida!"); continue; }
            }

            // Chamada da API
            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + moedaOrigem;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            RespostaAPI resposta = gson.fromJson(response.body(), RespostaAPI.class);

            double taxa = resposta.conversion_rates.get(moedaDestino);
            double convertido = valor * taxa;

            System.out.printf("%.2f %s = %.2f %s%n%n", valor, moedaOrigem, convertido, moedaDestino);
        }

        System.out.println("Programa encerrado!");
        leitura.close();
    }
}
