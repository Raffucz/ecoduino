package ecoduino.com.br.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DadosGrafico {

    // Classe auxiliar para representar ano-semana (ex.: "2025-48")
    public static class YearWeek {
        private final int year;
        private final int week;

        public YearWeek(int year, int week) {
            this.year = year;
            this.week = week;
        }

        public int getYear() {
            return year;
        }

        public int getWeek() {
            return week;
        }

        public String toString() {
            return String.format("%04d-%02d", year, week);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            YearWeek yearWeek = (YearWeek) obj;
            return year == yearWeek.year && week == yearWeek.week;
        }

        @Override
        public int hashCode() {
            return year * 100 + week;
        }

        // Método para criar YearWeek a partir de LocalDate
        public static YearWeek from(LocalDate date) {
            int week = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int year = date.get(java.time.temporal.IsoFields.WEEK_BASED_YEAR);
            return new YearWeek(year, week);
        }
    }

    // Mapas para armazenar os dados agrupados
    private Map<LocalDate, Map<String, Integer>> dadosDiarios;
    private Map<YearWeek, Map<String, Integer>> dadosSemanais;
    private Map<YearMonth, Map<String, Integer>> dadosMensais;

    public DadosGrafico() {
        dadosDiarios = new HashMap<>();
        dadosSemanais = new HashMap<>();
        dadosMensais = new HashMap<>();
        try {
            String json = FirebaseClient.get("historicos.json");
            carregarHistoricos(json);
        } catch (Exception e) {
            System.err.println("Erro ao buscar dados do Firebase para gráfico: " + e.getMessage());
            e.printStackTrace();
            // Mapas ficam vazios por padrão
        }
    }

    private void carregarHistoricos(String json) {
        if (json == null || json.trim().isEmpty() || json.equals("null")) {
            System.out.println("JSON vazio ou null para historicos.");
            return;
        }

        try {
            // Parseia o JSON como Map<String, Map<String, Object>> (chave externa: ID do descarte)
            Map<String, Map<String, Object>> mapa = new Gson().fromJson(json, new TypeToken<Map<String, Map<String, Object>>>() {}.getType());
            if (mapa == null || mapa.isEmpty()) {
                System.out.println("Mapa vazio para historicos.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME; // Para parsear "2025-11-29T20:43:20Z"

            System.out.println("Processando " + mapa.size() + " descartes no historicos.");
            for (Map.Entry<String, Map<String, Object>> entry : mapa.entrySet()) {
                Map<String, Object> descarte = entry.getValue();
                if (descarte == null) continue;

                // Extrai campos
                String dataHoraStr = (String) descarte.get("data_hora_descarte");
                String tipo = (String) descarte.get("tipo_reciclagem");
                Object qtdObj = descarte.get("quantidade_descarte");
                int quantidade = 0;

                if (qtdObj instanceof Number) {
                    quantidade = ((Number) qtdObj).intValue();
                } else if (qtdObj instanceof String) {
                    try {
                        quantidade = Integer.parseInt((String) qtdObj);
                    } catch (NumberFormatException e) {
                        System.err.println("Quantidade inválida (string não numérica): " + qtdObj);
                    }
                }

                if (dataHoraStr == null || tipo == null || tipo.trim().isEmpty()) {
                    System.out.println("Dados incompletos para descarte: " + entry.getKey());
                    continue;
                }

                // Parseia a data
                LocalDateTime dataHora;
                try {
                    dataHora = LocalDateTime.parse(dataHoraStr, formatter);
                } catch (Exception e) {
                    System.err.println("Erro ao parsear data: " + dataHoraStr);
                    continue;
                }
                LocalDate data = dataHora.toLocalDate();
                YearWeek semana = YearWeek.from(data);
                YearMonth mes = YearMonth.from(data);

                // Normaliza tipo (case-insensitive, trata "plástico" como "plastico")
                String tipoNormalizado = tipo.toLowerCase();
                if ("plástico".equals(tipoNormalizado)) tipoNormalizado = "plastico";

                // Adiciona aos mapas diários
                dadosDiarios.computeIfAbsent(data, k -> new HashMap<>())
                           .merge(tipoNormalizado, quantidade, Integer::sum);

                // Adiciona aos mapas semanais
                dadosSemanais.computeIfAbsent(semana, k -> new HashMap<>())
                            .merge(tipoNormalizado, quantidade, Integer::sum);

                // Adiciona aos mapas mensais
                dadosMensais.computeIfAbsent(mes, k -> new HashMap<>())
                           .merge(tipoNormalizado, quantidade, Integer::sum);

                System.out.println("Descarte processado: Data=" + data + ", Tipo=" + tipoNormalizado + ", Quantidade=" + quantidade);
            }
            System.out.println("Dados carregados: " + dadosDiarios.size() + " dias, " + dadosSemanais.size() + " semanas, " + dadosMensais.size() + " meses.");
        } catch (Exception e) {
            System.err.println("Erro ao parsear JSON dos historicos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos para obter os dados
    public Map<LocalDate, Map<String, Integer>> getDadosDiarios() {
        return new HashMap<>(dadosDiarios); // Retorna cópia para evitar modificações externas
    }

    public Map<YearWeek, Map<String, Integer>> getDadosSemanais() {
        return new HashMap<>(dadosSemanais);
    }

    public Map<YearMonth, Map<String, Integer>> getDadosMensais() {
        return new HashMap<>(dadosMensais);
    }
}