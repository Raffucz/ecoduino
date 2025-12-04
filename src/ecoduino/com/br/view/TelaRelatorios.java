package ecoduino.com.br.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class TelaRelatorios extends JFrame {

    private static final Color BG_LIGHT = Color.decode("#D8F6D3");
    private static final Color TOP_BAR = Color.decode("#284021");
    private static final Color ICON_ACCENT = Color.decode("#8CA685");
    private static final Color TEXT_DARK = Color.decode("#20311B");

    private static final Font TITLE_FONT = new Font("Alexandria", Font.BOLD, 36);
    private static final Font REPORT_TITLE_FONT = new Font("Alexandria", Font.BOLD, 22);
    private static final Font REPORT_BODY_FONT = new Font("Alexandria", Font.PLAIN, 16);
    private static final Font CARD_FONT = new Font("Alexandria", Font.PLAIN, 14);

    public TelaRelatorios() {
        super("Ecoduino - Relatórios");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ---------------------- TOP BAR ----------------------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(TOP_BAR);
        topBar.setPreferredSize(new Dimension(0, 70));

        // LOGO
        ImageIcon logoIcon = loadScaled("/imgs/logo.png", 120, 40);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);
        topBar.add(logoPanel, BorderLayout.WEST);

        // MENU CENTRAL
        JPanel menuCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        menuCenter.setOpaque(false);

        menuCenter.add(makeMenuButton("/imgs/reciclagem.png", "RECICLAGEM"));
        menuCenter.add(makeMenuButton("/imgs/relatorios.png", "RELATÓRIOS"));
        menuCenter.add(makeMenuButton("/imgs/graficos.png", "GRÁFICOS"));

        topBar.add(menuCenter, BorderLayout.CENTER);

        // ÍCONES DIREITA
        JPanel menuRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        menuRight.setOpaque(false);
        menuRight.add(makeIconButton("/imgs/info.png"));
        menuRight.add(makeIconButton("/imgs/sair.png"));
        topBar.add(menuRight, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ---------------------- CENTER ----------------------
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG_LIGHT);
        center.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // TITLE
        JLabel title = new JLabel("RELATÓRIOS DE DESCARTES", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        center.add(title, BorderLayout.NORTH);

        // AREA ROLÁVEL DOS RELATÓRIOS
        JPanel reportsContainer = new JPanel();
        reportsContainer.setLayout(new BoxLayout(reportsContainer, BoxLayout.Y_AXIS));
        reportsContainer.setBackground(BG_LIGHT);

        // ------------ Construtor dados ------------
        RelatoriosDados dado = new RelatoriosDados();

        // Cards dos materiais lado a lado 
        JPanel materialsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        materialsPanel.setBackground(BG_LIGHT);
        materialsPanel.add(makeMaterialCard("Papel", dado.papel, "/imgs/papel.png"));
        materialsPanel.add(makeMaterialCard("Plástico", dado.plastico, "/imgs/plastico.png"));
        materialsPanel.add(makeMaterialCard("Vidro", dado.vidro, "/imgs/vidro.png"));
        materialsPanel.add(makeMaterialCard("Metal", dado.metal, "/imgs/metal.png"));
        reportsContainer.add(materialsPanel);

        // Espaçamento maior
        reportsContainer.add(Box.createVerticalStrut(40));

        // Título "META E IMPACTO AMBIENTAL" 
        JLabel impactTitle = new JLabel("META E IMPACTO AMBIENTAL");
        impactTitle.setFont(REPORT_TITLE_FONT);
        impactTitle.setForeground(TEXT_DARK);
        JPanel impactTitleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        impactTitleWrapper.setBackground(BG_LIGHT);
        impactTitleWrapper.add(impactTitle);
        reportsContainer.add(impactTitleWrapper);

        // Card de impacto 
        JPanel impactWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        impactWrapper.setBackground(BG_LIGHT);
        impactWrapper.add(makeImpactCard(dado));
        reportsContainer.add(impactWrapper);

        JScrollPane scroll = new JScrollPane(reportsContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_LIGHT);

        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    // --------------------------------------------------------
    // CARD PARA MATERIAL 
    // --------------------------------------------------------
    private RoundedPanel makeMaterialCard(String material, int quantidade, String iconPath) {
        RoundedPanel card = new RoundedPanel(15);  // Raio de 15px para arredondamento
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(TOP_BAR);  // Fundo escuro
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));  // Espaçamento interno
        card.setPreferredSize(new Dimension(160, 140));  // Maior para parecer profissional
        card.setMaximumSize(new Dimension(160, 140));

        // Ícone
        ImageIcon icon = loadScaled(iconPath, 40, 40);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        // Nome
        JLabel nameLabel = new JLabel(material);
        nameLabel.setFont(CARD_FONT);
        nameLabel.setForeground(Color.WHITE);  // Texto claro
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);

        // Valor
        JLabel valueLabel = new JLabel(String.valueOf(quantidade));
        valueLabel.setFont(new Font("Alexandria", Font.BOLD, 20));
        valueLabel.setForeground(ICON_ACCENT);  // Destaque
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);

        // Efeito hover 
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(TOP_BAR.darker());  // Escurece ao passar mouse
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(TOP_BAR);  // Volta ao normal
            }
        });

        return card;
    }

    // --------------------------------------------------------
    // CARD PARA IMPACTO AMBIENTAL 
    // --------------------------------------------------------
    private RoundedPanel makeImpactCard(RelatoriosDados dado) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(TOP_BAR);
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        card.setPreferredSize(new Dimension(500, 200));  // Maior para conter tudo

        // Cálculos (iguais)
        int totalDescartes = dado.papel + dado.plastico + dado.vidro + dado.metal;
        int metaMensal = 1000;
        double porcentagemMeta = (totalDescartes / (double) metaMensal) * 100;
        double arvoresSalvas = dado.papel * 0.01;
        double energiaEconomizada = (dado.plastico * 2) + (dado.vidro * 1.5) + (dado.metal * 3);
        double aguaEconomizada = (dado.papel * 5) + (dado.plastico * 3);

        // Campos
        card.add(makeImpactField("Total de descartes", totalDescartes + " unidades"));
        card.add(makeImpactField("Meta mensal", metaMensal + " unidades (" + String.format("%.1f", porcentagemMeta) + "% atingido)"));
        card.add(makeImpactField("Árvores salvas", String.format("%.1f", arvoresSalvas) + " árvores"));
        card.add(makeImpactField("Energia economizada", String.format("%.1f", energiaEconomizada) + " kWh"));
        card.add(makeImpactField("Água economizada", String.format("%.1f", aguaEconomizada) + " litros"));

        return card;
    }

    private JPanel makeImpactField(String label, String value) {
        JPanel field = new JPanel(new BorderLayout());
        field.setBackground(TOP_BAR);  // Fundo igual ao card
        field.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel labelComp = new JLabel(label + ": ");
        labelComp.setFont(CARD_FONT);
        labelComp.setForeground(Color.WHITE);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Alexandria", Font.BOLD, 14));
        valueComp.setForeground(ICON_ACCENT);

        field.add(labelComp, BorderLayout.WEST);
        field.add(valueComp, BorderLayout.EAST);

        return field;
    }

    // --------------------------------------------------------
    // CLASSE PARA PAINEL ARREDONDADO
    // --------------------------------------------------------
    private static class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);  // Permite desenhar o fundo customizado
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
        }
    }

    // --------------------------------------------------------
    // UTILS E BOTÕES
    // --------------------------------------------------------
    private JButton makeMenuButton(String iconPath, String text) {
        ImageIcon ic = loadScaled(iconPath, 22, 22);
        JButton b = new JButton(text, ic);

        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setForeground(ICON_ACCENT);
        b.setFont(new Font("Alexandria", Font.BOLD, 16));
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setIconTextGap(8);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setForeground(Color.white); }
            @Override public void mouseExited (MouseEvent e) { b.setForeground(ICON_ACCENT); }
        });

        b.addActionListener(e -> {
            switch (text.toUpperCase()) {
                case "RECICLAGEM":
                    new TelaReciclagem().setVisible(true);
                    dispose();
                    break;
                case "RELATÓRIOS": // já está aqui
                    break;
                case "GRÁFICOS":
                    new TelaGraficos().setVisible(true);
                    dispose();
                    break;
            }
        });

        return b;
    }

    private JButton makeIconButton(String iconPath) {
        ImageIcon ic = loadScaled(iconPath, 26, 26);
        JButton b = new JButton(ic);

        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setIcon(loadScaled(iconPath, 30, 30)); }
            @Override public void mouseExited (MouseEvent e) { b.setIcon(loadScaled(iconPath, 26, 26)); }
        });

        if (iconPath.contains("info"))
            b.addActionListener(e -> { new TelaAjuda().setVisible(true); dispose(); });

        if (iconPath.contains("sair"))
            b.addActionListener(e -> { new TelaInicial().setVisible(true); dispose(); });

        return b;
    }

    private ImageIcon loadScaled(String path, int maxW, int maxH) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getResource(path));
            Image img = raw.getImage();
            double scale = Math.min((double)maxW / raw.getIconWidth(), (double)maxH / raw.getIconHeight());
            int nw = (int)(raw.getIconWidth() * scale);
            int nh = (int)(raw.getIconHeight() * scale);
            return new ImageIcon(img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaRelatorios::new);
    }
}
