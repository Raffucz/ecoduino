package ecoduino.com.br.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class TelaInicial extends JFrame {

    private Image logo;
    private Image iconSair;
    private Image logoEquipe; // Nova imagem para a logo da equipe

    // Variáveis para animação do logo (fade-in)
    private float logoOpacity = 0.0f;
    private Timer fadeInTimer;

    public TelaInicial() {
        setTitle("Ecoduino");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600)); // Define tamanho mínimo para evitar encolhimento excessivo
        setLayout(new BorderLayout());

        // Carregar imagens
        logo = new ImageIcon(getClass().getResource("/imgs/logoesc.png")).getImage();
        iconSair = new ImageIcon(getClass().getResource("/imgs/sairescuro.png")).getImage();
        logoEquipe = new ImageIcon(getClass().getResource("/imgs/logoequipe.png")).getImage(); // Carrega a logo da equipe

        // Painel principal com melhorias visuais
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Fundo com gradiente para um visual mais profissional
                GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#D8F6D3"), 0, getHeight(), Color.decode("#A8E6A3"));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Desenhar logo centralizada com redimensionamento automático e fade-in
                int maxLogoWidth = getWidth() / 3;
                int maxLogoHeight = getHeight() / 3;

                int originalW = logo.getWidth(null);
                int originalH = logo.getHeight(null);

                // Manter proporção
                double scale = Math.min((double) maxLogoWidth / originalW, (double) maxLogoHeight / originalH);

                int newW = (int) (originalW * scale);
                int newH = (int) (originalH * scale);

                int x = (getWidth() - newW) / 2;
                int y = getHeight() / 6;

                // Aplicar opacidade para fade-in
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, logoOpacity);
                g2d.setComposite(alphaComposite);
                g2d.drawImage(logo, x, y, newW, newH, this);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Resetar opacidade

                // Desenhar logo da equipe no canto inferior direito (pequena, como uma marca d'água)
                int equipeW = logoEquipe.getWidth(null) / 4; // Reduzir tamanho
                int equipeH = logoEquipe.getHeight(null) / 4;
                int equipeX = getWidth() - equipeW - 20; // 20px de margem
                int equipeY = getHeight() - equipeH - 20;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // Semi-transparente
                g2d.drawImage(logoEquipe, equipeX, equipeY, equipeW, equipeH, this);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Resetar
            }
        };
        mainPanel.setLayout(null);

        // Botão de sair com hover effect
        JButton btnSair = new JButton(new ImageIcon(iconSair));
        btnSair.setBackground(new Color(0,0,0,0));
        btnSair.setOpaque(false);
        btnSair.setBorderPainted(false);
        btnSair.setContentAreaFilled(false);
        btnSair.setFocusPainted(false);
        btnSair.setBounds(getWidth() - 70, 20, 48, 48);
        btnSair.addActionListener(e -> System.exit(0));

        // Adicionar hover effect ao botão sair
        btnSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnSair.setCursor(Cursor.getDefaultCursor());
            }
        });
        mainPanel.add(btnSair);

        // Texto "SEU AUXÍLIO NA RECICLAGEM" com sombra para profundidade
        JLabel subText = new JLabel("SEU AUXÍLIO NA RECICLAGEM", SwingConstants.CENTER);
        subText.setForeground(Color.decode("#20311B"));
        subText.setFont(new Font("Alexandria", Font.PLAIN, 22));
        subText.setBounds(0, (int)(getHeight()*0.55), getWidth(), 40);
        mainPanel.add(subText);

        // Botão Entrar com melhorias: hover effect e sombra
        JButton btnEntrar = new JButton("ENTRAR") {
            private boolean hovered = false;

            {
                // Inicializador para adicionar o listener dentro da classe anônima
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

                // Cor baseada no hover
                Color bgColor = hovered ? Color.decode("#3A5A2E") : Color.decode("#284021");
                g2.setColor(bgColor);

                // Desenhar retângulo arredondado com sombra
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(2, 2, getWidth() - 4, getHeight() - 4, 40, 40);
                g2.setColor(new Color(0, 0, 0, 50)); // Sombra
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 40, 40);
                g2.setColor(bgColor);
                g2.fill(roundedRect);

                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            public void setBounds(int x, int y, int width, int height) {
                super.setBounds(x, y, width, height);
            }
        };

        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Alexandria", Font.BOLD, 20));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.setBounds((getWidth()/2) - 90, (int)(getHeight()*0.65), 180, 50);

        btnEntrar.addActionListener(e -> {
            dispose(); // Fecha a tela inicial
            new TelaReciclagem(); // Abre a nova tela
        });
        mainPanel.add(btnEntrar);

        // Listener para redimensionamento melhorado
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                btnSair.setBounds(w - 70, 20, 48, 48);
                subText.setBounds(0, (int)(h * 0.55), w, 40);
                btnEntrar.setBounds((w/2) - 90, (int)(h * 0.65), 180, 50);
                mainPanel.repaint(); // Força repaint para ajustar elementos desenhados
            }
        });

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        // Iniciar animação de fade-in para o logo
        fadeInTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoOpacity += 0.05f;
                if (logoOpacity >= 1.0f) {
                    logoOpacity = 1.0f;
                    fadeInTimer.stop();
                }
                mainPanel.repaint();
            }
        });
        fadeInTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}
