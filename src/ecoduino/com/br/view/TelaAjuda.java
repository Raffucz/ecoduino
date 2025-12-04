package ecoduino.com.br.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class TelaAjuda extends JFrame {

    private static final Color BG_LIGHT = Color.decode("#D8F6D3");
    private static final Color TOP_BAR = Color.decode("#284021");
    private static final Color BOX_BG = Color.decode("#E9FFE4");
    private static final Color TEXT_DARK = Color.decode("#20311B");
    private static final Color ICON_ACCENT = Color.decode("#8CA685");

    private static final Font TITLE_FONT = new Font("Alexandria", Font.BOLD, 36);
    private static final Font SUBTITLE_FONT = new Font("Alexandria", Font.BOLD, 22);
    private static final Font BODY_FONT = new Font("Alexandria", Font.PLAIN, 16);

    // Variáveis para animação do título (fade-in)
    private float titleOpacity = 0.0f;
    private Timer fadeInTimer;

    public TelaAjuda() {
        super("Ecoduino - Ajuda");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600)); // Define tamanho mínimo para evitar encolhimento excessivo
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ---------------------- TOP BAR ----------------------
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente sutil no top bar para profundidade
                GradientPaint gradient = new GradientPaint(0, 0, TOP_BAR, 0, getHeight(), Color.decode("#1E2F1A"));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
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
        JPanel center = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fundo com gradiente para consistência
                GradientPaint gradient = new GradientPaint(0, 0, BG_LIGHT, 0, getHeight(), Color.decode("#A8E6A3"));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        center.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        add(center, BorderLayout.CENTER);

        // TÍTULO com fade-in
        JLabel title = new JLabel("AJUDA", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleOpacity);
                g2.setComposite(alphaComposite);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        center.add(title, BorderLayout.NORTH);

        // Painel de conteúdo (2 colunas)
        JPanel content = new JPanel(new GridLayout(1, 2, 30, 0));
        content.setOpaque(false);

        content.add(createHelpBox(
                "🗑️  USANDO AS LIXEIRAS",
                "A lixeira inteligente foi feita para facilitar a reciclagem do dia a dia.\n\n"
                        + "Para utilizá-la corretamente, basta seguir estes passos:\n\n"
                        + "1. Escolha o lixo que você quer descartar (plástico, vidro, papel ou metal).\n"
                        + "2. Verifique se o material está limpo — isso ajuda no processo de reciclagem.\n"
                        + "3. Jogue o lixo no compartimento da lixeira.\n"
                        + "4. Logo depois, aperte o botão correspondente ao tipo de material:\n"
                        + "      • Plástico\n"
                        + "      • Papel\n"
                        + "      • Vidro\n"
                        + "      • Metal\n"
                        + "5. Quando o botão é pressionado, o LED acende e a lixeira registra a informação automaticamente.\n"
                        + "6. Pronto! Seu descarte foi contabilizado no sistema.\n\n"
                        + "Esse processo ajuda o aplicativo a registrar tudo corretamente e mostrar dados reais sobre seus hábitos sustentáveis."
        ));

        content.add(createHelpBox(
                "📱  USANDO O APLICATIVO",
                "O aplicativo foi criado para acompanhar o impacto das suas ações de forma simples e visual.\n\n"
                        + "1. Abra o app conectado à lixeira inteligente.\n"
                        + "2. Na tela inicial, você verá um resumo dos descartes do dia.\n"
                        + "3. No menu, acesse:\n"
                        + "      • Gráficos — para ver quanto você reciclou por tipo de material\n"
                        + "      • Histórico — para consultar descartes passados\n"
                        + "      • Metas — para acompanhar desafios e conquistas\n"
                        + "      • Dicas — com conteúdos educativos sobre reciclagem\n"
                        + "4. Toda vez que você aperta um botão na lixeira, o app atualiza automaticamente.\n"
                        + "5. Você pode acompanhar seu progresso, visualizar seu impacto ambiental e até comparar seu evolução ao longo do tempo.\n\n"
                        + "O aplicativo transforma cada descarte em informação — e cada informação em consciência ambiental."
        ));

        // Rolagem caso necessário
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(0,0,0,0)); // Transparente para mostrar gradiente

        center.add(scroll, BorderLayout.CENTER);

        // Listener para redimensionamento
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ajustes dinâmicos se necessário (ex.: reposicionar elementos, mas GridLayout cuida do layout)
                center.repaint();
                topBar.repaint();
            }
        });

        // Iniciar animação de fade-in para o título
        fadeInTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titleOpacity += 0.05f;
                if (titleOpacity >= 1.0f) {
                    titleOpacity = 1.0f;
                    fadeInTimer.stop();
                }
                title.repaint();
            }
        });
        fadeInTimer.start();
    }

    // --------------------------------------------------------
    // BOXS DE AJUDA
    // --------------------------------------------------------
    private JPanel createHelpBox(String titleText, String bodyText) {
        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra leve
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 18, 18);
                g2.setColor(BOX_BG);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 18, 18);
            }
        };

        box.setLayout(new BorderLayout());
        box.setOpaque(false);
        box.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel(titleText);
        title.setFont(SUBTITLE_FONT);
        title.setForeground(TEXT_DARK);

        JTextArea body = new JTextArea(bodyText);
        body.setFont(BODY_FONT);
        body.setForeground(TEXT_DARK);
        body.setOpaque(false);
        body.setEditable(false);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);

        box.add(title, BorderLayout.NORTH);
        box.add(body, BorderLayout.CENTER);

        return box;
    }

    // --------------------------------------------------------
    // BOTÕES E UTILIDADES
    // --------------------------------------------------------
    private JButton makeMenuButton(String icon, String text) {
        ImageIcon ic = loadScaled(icon, 22, 22);
        JButton b = new JButton(text, ic);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setForeground(ICON_ACCENT);
        b.setFont(new Font("Alexandria", Font.BOLD, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setIconTextGap(8);

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setForeground(Color.WHITE); }
            @Override public void mouseExited(MouseEvent e)  { b.setForeground(ICON_ACCENT); }
        });

        b.addActionListener(e -> {
            switch (text) {
                case "RECICLAGEM" -> { new TelaReciclagem().setVisible(true); dispose(); }
                case "RELATÓRIOS" -> { new TelaRelatorios().setVisible(true); dispose(); }
                case "GRÁFICOS"   -> { new TelaGraficos().setVisible(true); dispose(); }
            }
        });

        return b;
    }

    private JButton makeIconButton(String icon) {
        ImageIcon ic = loadScaled(icon, 26, 26);
        JButton b = new JButton(ic);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setIcon(loadScaled(icon, 30, 30)); }
            @Override public void mouseExited (MouseEvent e) { b.setIcon(loadScaled(icon, 26, 26)); }
        });

        if (icon.contains("info"))
            b.addActionListener(e -> {}); // já está nesta tela

        if (icon.contains("sair"))
            b.addActionListener(e -> { new TelaInicial().setVisible(true); dispose(); });

        return b;
    }

    private ImageIcon loadScaled(String path, int maxW, int maxH) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getResource(path));
            Image img = raw.getImage();
            double scale = Math.min((double)maxW / raw.getIconWidth(), (double)maxH / raw.getIconHeight());
            return new ImageIcon(img.getScaledInstance(
                    (int)(raw.getIconWidth() * scale),
                    (int)(raw.getIconHeight() * scale),
                    Image.SCALE_SMOOTH
            ));
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(TelaAjuda::new); }
}
