package ecoduino.com.br.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class TelaGraficos extends JFrame {

    private static final Color BG_LIGHT = Color.decode("#D8F6D3");
    private static final Color TOP_BAR = Color.decode("#284021");
    private static final Color ICON_ACCENT = Color.decode("#8CA685");
    private static final Color TEXT_DARK = Color.decode("#20311B");
    private static final Font TITLE_FONT = new Font("Alexandria", Font.BOLD, 36);
    private static final Font MENU_FONT = new Font("Alexandria", Font.BOLD, 16);

    public TelaGraficos() {
        super("Ecoduino - Gráficos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());

        // ---------- TOP BAR (com gradiente, como na TelaReciclagem) ----------
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente sutil
                GradientPaint gradient = new GradientPaint(0, 0, TOP_BAR, 0, getHeight(), Color.decode("#1E2F1A"));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topBar.setPreferredSize(new Dimension(0, 70));

        // LOGO (ajustada para manter proporção e não esticar)
        ImageIcon logoIcon = loadScaled("/imgs/logo.png", 120, 40);  // Agora mantém proporção
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

        // ---------- CENTRO ----------
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setBackground(BG_LIGHT);
        center.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        DadosGrafico dados = new DadosGrafico();
        PainelGrafico grafico = new PainelGrafico(dados);
        center.add(grafico, BorderLayout.CENTER);
        
        // Botões customizados (arredondados, com cores do app, como na tela inicial)
        JButton diaBtn = makeCustomButton("Dia");
        diaBtn.addActionListener(e -> grafico.setPeriodo("dia"));

        JButton semanaBtn = makeCustomButton("Semana");
        semanaBtn.addActionListener(e -> grafico.setPeriodo("semana"));

        JButton mesBtn = makeCustomButton("Mês");
        mesBtn.addActionListener(e -> grafico.setPeriodo("mes"));
        
        // Painel de botões com fundo BG_LIGHT e espaçamento
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoes.setBackground(BG_LIGHT);
        botoes.add(diaBtn);
        botoes.add(semanaBtn);
        botoes.add(mesBtn);
        center.add(botoes, BorderLayout.NORTH);

        add(center, BorderLayout.CENTER);
    }

    // Método para botões customizados (arredondados, com hover, inspirado na tela inicial)
    private JButton makeCustomButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cor baseada no hover (usando cores do app)
                Color bgColor = hovered ? ICON_ACCENT : TOP_BAR;
                g2.setColor(bgColor);

                // Sombra leve
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);

                super.paintComponent(g);
                g2.dispose();
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Alexandria", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));  // Tamanho fixo para consistência

        return button;
    }

    private JButton makeMenuButton(String iconPath, String text) {
        ImageIcon ic = loadScaled(iconPath, 22, 22);
        JButton b = new JButton(text, ic);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setForeground(ICON_ACCENT);
        b.setFont(MENU_FONT);
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setIconTextGap(8);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setForeground(new Color(210,255,210)); }
            @Override public void mouseExited(MouseEvent e) { b.setForeground(ICON_ACCENT); }
        });
        b.addActionListener(e -> {
            switch (text) {
                case "RECICLAGEM" -> { new TelaReciclagem().setVisible(true); dispose(); }
                case "RELATÓRIOS" -> { new TelaRelatorios().setVisible(true); dispose(); }
                case "GRÁFICOS" -> {}
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
        if (iconPath.contains("info")) {
            b.addActionListener(e -> { new TelaAjuda().setVisible(true); dispose(); });
        }
        if (iconPath.contains("sair")) {
            b.addActionListener(e -> { new TelaInicial().setVisible(true); dispose(); });
        }
        return b;
    }

    // Método loadScaled ajustado para manter proporção (não estica a imagem)
    private ImageIcon loadScaled(String path, int maxW, int maxH) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getResource(path));
            Image img = raw.getImage();
            int w = raw.getIconWidth();
            int h = raw.getIconHeight();
            if (w == 0 || h == 0) return new ImageIcon();  // Evita divisão por zero

            // Calcula escala mantendo proporção
            double scale = Math.min((double) maxW / w, (double) maxH / h);
            int newW = (int) (w * scale);
            int newH = (int) (h * scale);

            Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaGraficos::new);
    }
}