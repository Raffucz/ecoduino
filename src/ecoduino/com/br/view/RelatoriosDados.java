package ecoduino.com.br.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;

public class RelatoriosDados {

    public int papel;
    public int plastico;
    public int vidro;
    public int metal;

    public RelatoriosDados() {
        try {
            String json = FirebaseClient.get("lixeiras.json");
            carregarLixeira(json);
        } catch (Exception e) {
            System.err.println("Erro ao buscar dados do Firebase: " + e.getMessage());
            e.printStackTrace();
            // Valores ficam 0 por padrão
        }
    }

    private void carregarLixeira(String json) {
        if (json == null || json.trim().isEmpty() || json.equals("null")) {
            System.out.println("JSON vazio ou null.");
            return;
        }

        try {
            // Mude para Map<String, Object> para aceitar campos mistos (strings, números, objetos)
            Map<String, Object> mapa = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
            if (mapa == null || mapa.isEmpty()) {
                System.out.println("Mapa vazio.");
                return;
            }

            System.out.println("Processando " + mapa.size() + " chaves no mapa raiz.");
            for (Map.Entry<String, Object> entry : mapa.entrySet()) {
                // Pula se o valor não for um mapa (ex.: campos como "data_hora_ultimo_descarte" ou "quantidade_descarte")
                if (!(entry.getValue() instanceof Map)) {
                    System.out.println("Pulando chave não-mapa: " + entry.getKey() + " (tipo: " + entry.getValue().getClass().getSimpleName() + ")");
                    continue;
                }

                Map<String, Object> item = (Map<String, Object>) entry.getValue();
                if (item == null) continue;

                String tipo = (String) item.get("tipo_reciclagem");
                Object qtdObj = item.get("quantidade_descarte");
                int quantidade = 0;

                if (qtdObj instanceof Number) {
                    quantidade = ((Number) qtdObj).intValue();
                } else if (qtdObj instanceof String) {
                    try {
                        quantidade = Integer.parseInt((String) qtdObj);
                    } catch (NumberFormatException e) {
                        System.err.println("Quantidade inválida (string não numérica): " + qtdObj);
                    }
                } else {
                    System.out.println("Tipo de quantidade inesperado: " + (qtdObj != null ? qtdObj.getClass() : "null"));
                }

                if (tipo == null || tipo.trim().isEmpty()) {
                    System.out.println("Tipo de reciclagem null ou vazio para item: " + entry.getKey());
                    continue;
                }

                System.out.println("Item: " + entry.getKey() + " - Tipo: " + tipo + " - Quantidade: " + quantidade);
                switch (tipo.toLowerCase()) {  // Torna case-insensitive
                    case "papel": this.papel += quantidade; break;
                    case "plástico":
                    case "plastico": this.plastico += quantidade; break;
                    case "vidro": this.vidro += quantidade; break;
                    case "metal": this.metal += quantidade; break;
                    default: System.out.println("Tipo desconhecido: " + tipo); break;
                }
            }
            System.out.println("Totais: Papel=" + papel + ", Plástico=" + plastico + ", Vidro=" + vidro + ", Metal=" + metal);
        } catch (Exception e) {
            System.err.println("Erro ao parsear JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}