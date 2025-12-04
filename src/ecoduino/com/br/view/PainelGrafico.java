package ecoduino.com.br.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public class PainelGrafico extends JPanel {

    // Constantes de cor do app (copiadas de TelaGraficos para consistência)
    private static final Color BG_LIGHT = Color.decode("#D8F6D3");
    private static final Color TOP_BAR = Color.decode("#284021");
    private static final Color ICON_ACCENT = Color.decode("#8CA685");
    private static final Color TEXT_DARK = Color.decode("#20311B");

    private DadosGrafico dados;
    private String periodo = "dia"; // padrão

    private TimeSeriesCollection dataset;
    private JFreeChart chart;

    public PainelGrafico(DadosGrafico dados) {
        this.dados = dados;
        setLayout(new BorderLayout());

        dataset = new TimeSeriesCollection();
        chart = ChartFactory.createTimeSeriesChart(
                "Coleta de Materiais - ECOduino",
                "Tempo",
                "Quantidade",
                dataset,
                true, // legend
                true, // tooltips
                false // urls
        );

        // Customizações visuais do gráfico para usar cores do app
        chart.setBackgroundPaint(BG_LIGHT);  // Fundo do gráfico
        chart.getTitle().setPaint(TEXT_DARK);  // Cor do título
        chart.getTitle().setFont(new java.awt.Font("Alexandria", java.awt.Font.BOLD, 18));  // Fonte do título

        // Customizar o plot (fundo, grades, etc.)
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);  // Fundo interno do plot (branco para contraste)
        plot.setDomainGridlinePaint(ICON_ACCENT);  // Linhas de grade horizontais
        plot.setRangeGridlinePaint(ICON_ACCENT);  // Linhas de grade verticais

        // Removido: Customizar as séries (agora usa cores padrão: azul, vermelho, amarelo, verde, etc.)
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        // Outros ajustes visuais
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));  // Espessura das linhas
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.0f));
        renderer.setSeriesStroke(3, new BasicStroke(2.0f));

        // Eixos
        plot.getDomainAxis().setLabelPaint(TEXT_DARK);
        plot.getRangeAxis().setLabelPaint(TEXT_DARK);
        plot.getDomainAxis().setTickLabelPaint(TEXT_DARK);
        plot.getRangeAxis().setTickLabelPaint(TEXT_DARK);

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        atualizarDataset();
    }

    public void setPeriodo(String p) {
        this.periodo = p.toLowerCase();
        atualizarDataset();
    }

    private void atualizarDataset() {
        dataset.removeAllSeries();

        switch (periodo) {
            case "semana":
                adicionarSeriesSemanais();
                break;
            case "mes":
                adicionarSeriesMensais();
                break;
            default: // dia
                adicionarSeriesDiarias();
                break;
        }
    }

    private void adicionarSeriesDiarias() {
        Map<LocalDate, Map<String, Integer>> diarios = dados.getDadosDiarios();

        // Cria uma série para cada tipo
        TimeSeries seriePapel = new TimeSeries("Papel");
        TimeSeries seriePlastico = new TimeSeries("Plástico");
        TimeSeries serieVidro = new TimeSeries("Vidro");
        TimeSeries serieMetal = new TimeSeries("Metal");

        for (Map.Entry<LocalDate, Map<String, Integer>> entry : diarios.entrySet()) {
            LocalDate data = entry.getKey();
            Map<String, Integer> tipos = entry.getValue();

            Day day = new Day(data.getDayOfMonth(), data.getMonthValue(), data.getYear());
            seriePapel.add(day, tipos.getOrDefault("papel", 0));
            seriePlastico.add(day, tipos.getOrDefault("plastico", 0));
            serieVidro.add(day, tipos.getOrDefault("vidro", 0));
            serieMetal.add(day, tipos.getOrDefault("metal", 0));
        }

        dataset.addSeries(seriePapel);
        dataset.addSeries(seriePlastico);
        dataset.addSeries(serieVidro);
        dataset.addSeries(serieMetal);
    }

    private void adicionarSeriesSemanais() {
        Map<DadosGrafico.YearWeek, Map<String, Integer>> semanais = dados.getDadosSemanais();

        TimeSeries seriePapel = new TimeSeries("Papel");
        TimeSeries seriePlastico = new TimeSeries("Plástico");
        TimeSeries serieVidro = new TimeSeries("Vidro");
        TimeSeries serieMetal = new TimeSeries("Metal");

        for (Map.Entry<DadosGrafico.YearWeek, Map<String, Integer>> entry : semanais.entrySet()) {
            DadosGrafico.YearWeek yw = entry.getKey();
            Map<String, Integer> tipos = entry.getValue();

            Week week = new Week(yw.getWeek(), yw.getYear());  // Usando getters
            seriePapel.add(week, tipos.getOrDefault("papel", 0));
            seriePlastico.add(week, tipos.getOrDefault("plastico", 0));
            serieVidro.add(week, tipos.getOrDefault("vidro", 0));
            serieMetal.add(week, tipos.getOrDefault("metal", 0));
        }

        dataset.addSeries(seriePapel);
        dataset.addSeries(seriePlastico);
        dataset.addSeries(serieVidro);
        dataset.addSeries(serieMetal);
    }

    private void adicionarSeriesMensais() {
        Map<YearMonth, Map<String, Integer>> mensais = dados.getDadosMensais();

        TimeSeries seriePapel = new TimeSeries("Papel");
        TimeSeries seriePlastico = new TimeSeries("Plástico");
        TimeSeries serieVidro = new TimeSeries("Vidro");
        TimeSeries serieMetal = new TimeSeries("Metal");

        for (Map.Entry<YearMonth, Map<String, Integer>> entry : mensais.entrySet()) {
            YearMonth ym = entry.getKey();
            Map<String, Integer> tipos = entry.getValue();

            Month month = new Month(ym.getMonthValue(), ym.getYear());
            seriePapel.add(month, tipos.getOrDefault("papel", 0));
            seriePlastico.add(month, tipos.getOrDefault("plastico", 0));
            serieVidro.add(month, tipos.getOrDefault("vidro", 0));
            serieMetal.add(month, tipos.getOrDefault("metal", 0));
        }

        dataset.addSeries(seriePapel);
        dataset.addSeries(seriePlastico);
        dataset.addSeries(serieVidro);
        dataset.addSeries(serieMetal);
    }
}