package ecoduino.com.br.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class TelaReciclagem extends JFrame {

    private static final Color BG_LIGHT = Color.decode("#D8F6D3");
    private static final Color TOP_BAR = Color.decode("#284021");
    private static final Color ICON_ACCENT = Color.decode("#8CA685");
    private static final Color TEXT_DARK = Color.decode("#20311B");
    private static final Font TITLE_FONT = new Font("Alexandria", Font.BOLD, 36);
    private static final Font MENU_FONT = new Font("Alexandria", Font.BOLD, 16);

    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JPanel dotsPanel;
    private List<JLabel> dotLabels = new ArrayList<>();
    private int totalSlides = 3;
    private int currentSlide = 0;
    private Timer carouselTimer;
    private List<JLabel> slideImageLabels = new ArrayList<>();

    // Para transições de fade no carrossel
    private float slideOpacity = 1.0f;
    private Timer fadeTimer;
    private boolean fadingOut = false;
    private int nextSlide = 0;

    // Para fade-in do título
    private float titleOpacity = 0.0f;
    private Timer titleFadeTimer;

    public TelaReciclagem() {
        super("Ecoduino - Reciclagem");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 700)); // Define um tamanho mínimo para evitar distorções excessivas ao minimizar
        initUI();
        setVisible(true);
        startCarousel();
        startTitleFade();
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());

        // ---------- TOP BAR ----------
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

        // Logo
        ImageIcon logoIcon = loadScaled("/imgs/logo.png", 120, 40);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);

        topBar.add(logoPanel, BorderLayout.WEST);

        // MENU CENTRAL (3 botões)
        JPanel menuCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        menuCenter.setOpaque(false);

        menuCenter.add(makeMenuButton("/imgs/reciclagem.png", "RECICLAGEM"));
        menuCenter.add(makeMenuButton("/imgs/relatorios.png", "RELATÓRIOS"));
        menuCenter.add(makeMenuButton("/imgs/graficos.png", "GRÁFICOS"));

        topBar.add(menuCenter, BorderLayout.CENTER);

        // ÍCONES À DIREITA (INFO e SAIR)
        JPanel menuRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        menuRight.setOpaque(false);

        menuRight.add(makeIconButton("/imgs/info.png"));
        menuRight.add(makeIconButton("/imgs/sair.png"));

        topBar.add(menuRight, BorderLayout.EAST);

        getContentPane().add(topBar, BorderLayout.NORTH);

        // ---------- CENTER ----------
        JPanel center = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente para profundidade
                GradientPaint gradient = new GradientPaint(0, 0, BG_LIGHT, 0, getHeight(), Color.decode("#A8E6A3"));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        center.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("RECICLAGEM?", SwingConstants.CENTER) {
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
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 24, 0));
        center.add(title, BorderLayout.NORTH);

        // slides container com fade
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, slideOpacity);
                g2.setComposite(alphaComposite);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        cardContainer.setOpaque(false);

        cardContainer.add(createSlide("/imgs/1.png", slideText1()), "0");
        cardContainer.add(createSlide("/imgs/2.png", slideText2()), "1");
        cardContainer.add(createSlide("/imgs/3.png", slideText3()), "2");

        JPanel carouselWrapper = new JPanel(new GridBagLayout()); // Use GridBagLayout for better control
        carouselWrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        JButton leftArrow = createArrowButton("◀", -1);
        gbc.weightx = 0.05; // Small weight for left arrow
        carouselWrapper.add(leftArrow, gbc);

        JPanel darkPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra leve
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(TOP_BAR);
                g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
            }
        };
        darkPanel.setOpaque(false);
        darkPanel.setPreferredSize(new Dimension(600, 300)); // Reduced size to prevent clutter and full-screen occupation
        darkPanel.add(cardContainer, BorderLayout.CENTER);

        gbc.weightx = 0.9; // Most weight for center
        carouselWrapper.add(darkPanel, gbc);

        JButton rightArrow = createArrowButton("▶", 1);
        gbc.weightx = 0.05; // Small weight for right arrow
        carouselWrapper.add(rightArrow, gbc);

        center.add(carouselWrapper, BorderLayout.CENTER);

        // DOTS CLICÁVEIS com hover melhorado
        dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        dotsPanel.setOpaque(false);

        for (int i = 0; i < totalSlides; i++) {
            final int index = i;  // Declare index as final to use in inner classes
            JLabel dot = new JLabel("●") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (index == currentSlide) {  // Use index instead of i
                        g2.setColor(TEXT_DARK);
                        g2.fillOval(0, 0, getWidth(), getHeight());
                    } else {
                        g2.setColor(ICON_ACCENT);
                        g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                    }
                    g2.dispose();
                }
            };
            dot.setPreferredSize(new Dimension(20, 20));
            dot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            dotLabels.add(dot);

            // final int index = i;  // Already declared above, remove duplicate if present
            dot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    startFadeToSlide(index);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    dot.repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    dot.repaint();
                }
            });

            dotsPanel.add(dot);
        }

        center.add(dotsPanel, BorderLayout.SOUTH);
        getContentPane().add(center, BorderLayout.CENTER);

        // handle resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() ->
                        rescaleSlides(darkPanel.getWidth(), darkPanel.getHeight()));
                center.repaint();
                topBar.repaint();
            }
        });
    }

    private JButton createArrowButton(String text, int direction) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Removed the white background painting to make it transparent
                // g2.setColor(new Color(255, 255, 255, 150));
                // g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setForeground(TEXT_DARK);
        button.setFont(new Font("Dialog", Font.BOLD, 32)); // Larger font for better visibility
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(60, 60)); // Larger size
        button.addActionListener(e -> startFadeToSlide((currentSlide + direction + totalSlides) % totalSlides));
        return button;
    }

    private JPanel createSlide(String imagePath, String htmlText) {
        JPanel slide = new JPanel();
        slide.setOpaque(false);
        slide.setLayout(new BoxLayout(slide, BoxLayout.Y_AXIS));
        slide.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon img = loadScaled(imagePath, 300, 220);
        imageLabel.setIcon(img);
        slideImageLabels.add(imageLabel);

        // Title
        JLabel title = new JLabel();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setText(extractTitle(htmlText));
        title.setForeground(BG_LIGHT);
        title.setFont(new Font("Dialog", Font.BOLD, 20)); 
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Body
        JLabel body = new JLabel();
        body.setForeground(BG_LIGHT);
        body.setFont(new Font("Dialog", Font.PLAIN, 12)); 
        body.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.setText(extractBodyAsLeftBlock(htmlText));

        JPanel bodyWrapper = new JPanel(new BorderLayout());
        bodyWrapper.setOpaque(false);
        bodyWrapper.add(body, BorderLayout.CENTER);
        bodyWrapper.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

        slide.add(Box.createVerticalGlue());
        slide.add(imageLabel);
        slide.add(title);
        slide.add(Box.createRigidArea(new Dimension(0, 8)));
        slide.add(bodyWrapper);
        slide.add(Box.createVerticalGlue());

        return slide;
    }

    private String extractTitle(String htmlText) {
        try {
            int start = htmlText.indexOf("<h2");
            if (start >= 0) {
                int openClose = htmlText.indexOf(">", start);
                int end = htmlText.indexOf("</h2>", openClose);
                if (openClose >= 0 && end >= 0) {
                    String inner = htmlText.substring(openClose + 1, end).trim();
                    return "<html><div style='text-align:center; color:#D8F6D3;'>" + inner + "</div></html>";
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    private String extractBodyAsLeftBlock(String htmlText) {
        try {
            int endH2 = htmlText.indexOf("</h2>");
            String rest = (endH2 >= 0) ? htmlText.substring(endH2 + 5) : htmlText;
            rest = rest.replaceAll("(?i)<(/?html|/?div)>", "");
            String body = "<html><div style='color:#D8F6D3; max-width:700px; margin:0 auto; text-align:left; font-size:12px;'>"; // Increased font-size for better readability
            body += rest;
            body += "</div></html>";
            return body;
        } catch (Exception e) {
            return "<html><div style='color:#D8F6D3; text-align:left;'>...</div></html>";
        }
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
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setForeground(new Color(210, 255, 210));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setForeground(ICON_ACCENT);
            }
        });

        b.addActionListener(e -> {
            if (text.equalsIgnoreCase("RECICLAGEM")) {
                startFadeToSlide(0);
            } else if (text.equalsIgnoreCase("RELATÓRIOS")) {
                new TelaRelatorios().setVisible(true);
                dispose();
            } else if (text.equalsIgnoreCase("GRÁFICOS")) {
                new TelaGraficos().setVisible(true);
                dispose();
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
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setIcon(loadScaled(iconPath, 30, 30));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setIcon(loadScaled(iconPath, 26, 26));
            }
        });

        if (iconPath.contains("info")) {
            b.addActionListener(e -> {
                new TelaAjuda().setVisible(true);
                dispose();
            });
        }

        if (iconPath.contains("sair")) {
            b.addActionListener(e -> {
                new TelaInicial().setVisible(true);
                dispose();
            });
        }

        return b;
    }

    private ImageIcon loadScaled(String resourcePath, int maxW, int maxH) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getResource(resourcePath));
            Image img = raw.getImage();
            int w = Math.max(1, raw.getIconWidth()), h = Math.max(1, raw.getIconHeight());
            double scale = Math.min((double) maxW / w, (double) maxH / h);
            int newW = Math.max(1, (int) (w * scale));
            int newH = Math.max(1, (int) (h * scale));
            Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            return new ImageIcon();
        }
    }

    private void rescaleSlides(int containerW, int containerH) {
        int targetW = Math.min(340, Math.max(220, containerW / 4));
        int targetH = Math.min(240, Math.max(140, containerH / 3));

        for (int i = 0; i < slideImageLabels.size(); i++) {
            JLabel lbl = slideImageLabels.get(i);
            String path = "/imgs/" + (i + 1) + ".png";
            lbl.setIcon(loadScaled(path, targetW, targetH));
        }
    }

    private void startCarousel() {
        carouselTimer = new Timer(6000, e -> startFadeToSlide((currentSlide + 1) % totalSlides));
        carouselTimer.start();
    }

    private void startFadeToSlide(int index) {
            if (fadeTimer != null && fadeTimer.isRunning()) return;
            nextSlide = index;
            fadingOut = true;
            fadeTimer = new Timer(30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (fadingOut) {
                        slideOpacity -= 0.05f;
                        if (slideOpacity <= 0.0f) {
                            slideOpacity = 0.0f;
                            fadingOut = false;
                            currentSlide = nextSlide;
                            cardLayout.show(cardContainer, String.valueOf(currentSlide));
                            updateDots();
                        }
                    } else {
                        slideOpacity += 0.05f;
                        if (slideOpacity >= 1.0f) {
                            slideOpacity = 1.0f;
                            fadeTimer.stop();
                        }
                    }
                    cardContainer.repaint();
                }
            });
            fadeTimer.start();
            if (carouselTimer != null) carouselTimer.restart();
        }

        private void startTitleFade() {
            titleFadeTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    titleOpacity += 0.05f;
                    if (titleOpacity >= 1.0f) {
                        titleOpacity = 1.0f;
                        titleFadeTimer.stop();
                    }
                    // Repaint the center panel to update the title
                    getContentPane().repaint();
                }
            });
            titleFadeTimer.start();
        }

        private void updateDots() {
            for (int i = 0; i < dotLabels.size(); i++) {
                dotLabels.get(i).repaint();
            }
        }

        // ---- SLIDE TEXTS ----
        private String slideText1() {
            return "<html><div style='color:#D8F6D3; padding:8px;'>"
                    + "<h2>O QUE É RECICLAGEM E SUA IMPORTÂNCIA</h2>"
                    + "<p>A reciclagem consiste no processo de reaproveitamento de materiais descartados, transformando-os em novos produtos. Esse procedimento contribui significativamente para a sustentabilidade ambiental ao:</p>"
                    + "<ul>"
                    + "<li>Reduzir a exploração de recursos naturais</li>"
                    + "<li>Economizar energia</li>"
                    + "<li>Minimizar a poluição do ar, solo e água</li>"
                    + "<li>Prolongar a vida útil dos aterros sanitários</li>"
                    + "<li>Promover a educação ambiental e hábitos sustentáveis</li>"
                    + "</ul>"
                    + "<p>Apesar de sua simplicidade, a correta separação de resíduos ainda representa um desafio para muitos, impactando diretamente a saúde do planeta.</p>"
                    + "</div></html>";
        }

        private String slideText2() {
            return "<html><div style='color:#D8F6D3; padding:8px;'>"
                    + "<h2>GUIA PARA SEPARAÇÃO DE MATERIAIS</h2>"
                    + "<p><b>Papel:</b><br>✓ Cadernos, jornais, folhas e revistas<br>✗ Papel molhado ou engordurado (não reciclável)</p>"
                    + "<p><b>Plástico:</b><br>✓ Garrafas PET, embalagens rígidas e frascos<br>✗ Copos descartáveis sujos e isopor contaminado</p>"
                    + "<p><b>Vidro:</b><br>✓ Garrafas, potes e frascos<br>✗ Lâmpadas, espelhos e vidros temperados</p>"
                    + "<p><b>Metal:</b><br>✓ Latas de alumínio, tampas metálicas e latas de conserva<br>✗ Objetos enferrujados ou contaminados com alimentos</p>"
                    + "</div></html>";
        }

        private String slideText3() {
            return "<html><div style='color:#D8F6D3; padding:8px;'>"
                    + "<h2>IMPACTO AMBIENTAL E BENEFÍCIOS</h2>"
                    + "<p>Com base nos dados coletados, a plataforma quantifica sua contribuição ambiental.</p>"
                    + "<p>Exemplos de benefícios:<br>• Uma lata de alumínio reciclada economiza energia equivalente a 3 horas de funcionamento de uma TV<br>• Um quilograma de plástico reciclado evita a emissão de até 1,5 kg de CO₂<br>• A reciclagem de papel preserva árvores e reduz o consumo de água</p>"
                    + "<p>Através da interface, é possível visualizar economias realizadas, resíduos desviados de aterros e sua pontuação ambiental.</p>"
                    + "</div></html>";
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(TelaReciclagem::new);
        }
        }
