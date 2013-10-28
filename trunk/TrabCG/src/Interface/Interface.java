/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Classes.Aresta;
import Classes.Camera;
import Classes.Face;
import Classes.FileType;
import Classes.Iluminacao;
import Classes.Matriz;
import Classes.Poligono;
import Classes.Ponto;
import Classes.Vetor;
import Panels.MyColorChooser;
import Panels.PanelFrente;
import Panels.PanelLateral;
import Panels.PanelPerspectiva;
import Panels.PanelTopo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Classes.MyFileHandler;
import Main.Init;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alienware
 */
public class Interface extends javax.swing.JFrame {

    /**
     * Creates new form Interface
     */
    ZBuffer zbuffer;
    //coordenadas do vrp
    private double vrpX;
    private double vrpY;
    private double vrpZ;
    private double pontoX;
    private double pontoY;
    private double pontoZ;
    private double distancia;
    //identifica tipo de visualização | 1 -> Wireframe | 2 -> Wireframe com ocultação | 
    //3 -> Sombreamento costante | 4 - phong
    private int visualizacaoAtual;
    // identifica clique do mouse 1 - selecionar; 2 - transladar; 3 - rotacionar; 4 - escala;
    //5 - esfera; 6 - piramide; 7 - prisma; 8 - cizalhar
    private int cliqueAtual;
    private ArrayList<Poligono> poligonos;
    private ArrayList<Poligono> poligonosTransformados;
    private Camera camera;
    private int Xanteior;
    private int Yanteior;
    private boolean vetores;
    private boolean pontos;
    //variaveis que gardão o tamanho dos paineis
    //usados para fazer as escalas dos paineis
    private int tamanhoPanelFrenteX;
    private int tamanhoPanelFrenteY;
    private int tamanhoPanelLateralX;
    private int tamanhoPanelLateralY;
    private int tamanhoPanelTopoX;
    private int tamanhoPanelTopoY;
    private int tamanhoPanelFundoX;
    private int tamanhoPanelFundoY;
    //é a quantidade de cada objeto
    public int prismas;
    public int piramides;
    public int esferas;
    //qual está maximizado
    private static int maximized = 0;
    /* 0 = nenhum
     * 1 = frente
     * 2 = lateral
     * 3 = topo
     * 4 = perspectiva
     */
    //se foi salvo ou não
    boolean jaSalvo = false;
    String nomeArquivo;
    Init i;
    Iluminacao luzAmbiente;
    Iluminacao luzFundo;


    public Interface(Init _i) {

        initComponents();
        //inicializa a luz do ambiente
        luzAmbiente = new Iluminacao(new Ponto("localI", 0, 0, 100), 0.5, 0.5,
                0.5);
        //define uma iluminação de fundo
        luzFundo = new Iluminacao(new Ponto("localI", 0, 0, 0), 0.5, 0.5, 0.5);
        i = _i;
        setLocationRelativeTo(null);
        this.setResizable(false);
        //pega os valores iniciais dos spiner do vrp
        vrpX = Double.valueOf(svrpX.getValue().toString());
        vrpY = Double.valueOf(svrpY.getValue().toString());
        vrpZ = Double.valueOf(svrpZ.getValue().toString());
        //pega o valores do ponto de foco iniciais
        pontoX = Double.valueOf(spontoX.getValue().toString());
        pontoY = Double.valueOf(spontoY.getValue().toString());
        pontoZ = Double.valueOf(spontoZ.getValue().toString());
        //valores dos spiners
        distancia = Double.valueOf(sdistancia.getValue().toString());
        BaseSpinner.setValue(4);
        RaioSpinner.setValue(20);
        AlturaSpinner.setValue(20);

        this.poligonos = new ArrayList<>();
        this.poligonosTransformados = new ArrayList<>();
        this.camera = new Camera(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ,
                distancia);
        this.camera.GerarIntermediarios();
        this.visualizacaoAtual = 1;

        this.colorPanel.add(new JColorChooser(Color.black),
                BorderLayout.PAGE_END);

        vetores = false;
        //clique atual recebe o do painel da frente
        cliqueAtual = 1;
        //tamanho sem maximizar
        tamanhoPanelFrenteX = panelFrente.getWidth();
        tamanhoPanelLateralX = panelLateral.getWidth();
        tamanhoPanelTopoX = panelTopo.getWidth();
        tamanhoPanelFrenteY = panelFrente.getHeight();
        tamanhoPanelLateralY = panelLateral.getHeight();
        tamanhoPanelTopoY = panelTopo.getHeight();
        tamanhoPanelFundoX = panelFundo.getWidth();
        tamanhoPanelFundoY = panelFundo.getHeight();

        prismas = 0;
        piramides = 0;
        esferas = 0;
        jaSalvo = false;
        //nome do arquivo
        this.nomeArquivo = "/nothing";

    }

    /**
     * Adiciona o poligono já transformado para todos os paineis
     *
     * @param p poligono
     */
    public void addPoligono(Poligono p) {
        this.poligonos.add(p);
        this.poligonosTransformados.add(p.Transformar(true));

    }

    /**
     * Nome do arquivo
     *
     * @return
     */
    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    /**
     * Seta o nome do arquivo
     *
     * @param nome
     */
    public void setNomeArquivo(String nome) {
        this.nomeArquivo = nome;
    }

    /**
     * Variavel Maximized
     *
     * @return
     */
    public static int getMaximized() {
        return maximized;
    }

    public static void setMaximized(int maximized) {
        Interface.maximized = maximized;
    }

    /**
     * Verifica se o mostrar pontos está selecionado
     *
     * @return boolean pontos
     */
    public boolean isMostrarPontos() {
        return pontos;
    }

    /**
     * identifica tipo de visualização | 1 -> Wireframe | 2 -> Wireframe com
     * ocultação | 3 -> Sombreamento costante | 4 - phong
     *
     * @return
     */
    public int getVizualizacaoAtual() {
        return visualizacaoAtual;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public ArrayList<Poligono> getPoligonos() {
        return this.poligonos;
    }

    public ArrayList<Poligono> getPoligonosTransformados() {
        return this.poligonosTransformados;
    }

    /**
     * Seta os spinner do vrp e do ponto focal e a camera
     *
     * @param camera
     */
    public void setCamera(Camera camera) {
        //vrp
        svrpX.setValue((int) camera.getVx());
        svrpY.setValue((int) camera.getVy());
        svrpZ.setValue((int) camera.getVz());
        //ponto focal
        spontoX.setValue((int) camera.getFPx());
        spontoY.setValue((int) camera.getFPy());
        spontoZ.setValue((int) camera.getFPz());

        sdistancia.setValue((int) camera.getDistancia());

        if (zbuffer == null) {
            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
                    getWidth(), this.panelPerspectiva.getHeight());
            zbuffer.setVisible(true);
            zbuffer.repaint();
        } else {
            setZBuffer();
        }



    }

    public void addPoligonosBox(String what, int value) {
        //adiciona o poligono ao combo box 
        this.PoligonosBox.addItem(what + " " + String.valueOf(value));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     * @SuppressWarnings("unchecked")
     */
    /**
     *
     * @param aux
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        VisualizacaoRadioButtonGroup = new javax.swing.ButtonGroup();
        panelFundo = new javax.swing.JPanel();
        panelFrente = new PanelFrente(this);
        jButtonFrente = new javax.swing.JButton();
        panelLateral = new PanelLateral(this);
        jButtonLateral = new javax.swing.JButton();
        panelTopo = new PanelTopo(this);
        jButtonTopo = new javax.swing.JButton();
        panelPerspectiva = new PanelPerspectiva(this);
        jButtonPerspectiva = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        abas = new javax.swing.JTabbedPane();
        abaFerramentas = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        buttonSelecionar = new javax.swing.JButton();
        buttonTransladar = new javax.swing.JButton();
        buttonRotacionar = new javax.swing.JButton();
        buttonEscalonar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        PoligonosBox = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        redimensionar = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        mostrarPontos = new javax.swing.JCheckBox();
        abaObjetos = new javax.swing.JPanel();
        buttonPiramide = new javax.swing.JPanel();
        buttonEsfera = new javax.swing.JButton();
        piramide = new javax.swing.JButton();
        buttonPrisma = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        BaseSpinner = new javax.swing.JSpinner();
        RaioSpinner = new javax.swing.JSpinner();
        AlturaSpinner = new javax.swing.JSpinner();
        abaCamera = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        svrpX = new javax.swing.JSpinner();
        svrpY = new javax.swing.JSpinner();
        svrpZ = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        spontoX = new javax.swing.JSpinner();
        spontoY = new javax.swing.JSpinner();
        spontoZ = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        sdistancia = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        WireframeRadioButton = new javax.swing.JRadioButton();
        WireframeOcultacaoRadioButton = new javax.swing.JRadioButton();
        SombreamentoRadioButton = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        Ir = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        Ig = new javax.swing.JSpinner();
        jLabel23 = new javax.swing.JLabel();
        Ib = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        FLX = new javax.swing.JSpinner();
        FLY = new javax.swing.JSpinner();
        jLabel29 = new javax.swing.JLabel();
        FLZ = new javax.swing.JSpinner();
        jLabel30 = new javax.swing.JLabel();
        FLB = new javax.swing.JSpinner();
        FLR = new javax.swing.JSpinner();
        jLabel31 = new javax.swing.JLabel();
        FLG = new javax.swing.JSpinner();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        geral = new javax.swing.JPanel();
        colorPanel = new javax.swing.JPanel();
        ArestaPanelColorChooser = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        FacePanelColorChooser = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        KaR = new javax.swing.JSpinner();
        KaG = new javax.swing.JSpinner();
        KaB = new javax.swing.JSpinner();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        KdR = new javax.swing.JSpinner();
        KdG = new javax.swing.JSpinner();
        KdB = new javax.swing.JSpinner();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        KsR = new javax.swing.JSpinner();
        KsG = new javax.swing.JSpinner();
        KsB = new javax.swing.JSpinner();
        nPol = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        kt = new javax.swing.JSpinner();
        jButton4 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        panelFundo.setMaximumSize(new java.awt.Dimension(600, 600));
        panelFundo.setMinimumSize(new java.awt.Dimension(600, 600));
        panelFundo.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panelFundoComponentResized(evt);
            }
        });

        panelFrente.setPreferredSize(new java.awt.Dimension(300, 300));
        panelFrente.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                panelFrenteMouseWheelMoved(evt);
            }
        });
        panelFrente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelFrenteMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelFrenteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelFrenteMouseEntered(evt);
            }
        });
        panelFrente.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panelFrenteComponentResized(evt);
            }
        });
        panelFrente.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelFrenteMouseDragged(evt);
            }
        });

        jButtonFrente.setText("Maximiza");
        jButtonFrente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFrenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFrenteLayout = new javax.swing.GroupLayout(panelFrente);
        panelFrente.setLayout(panelFrenteLayout);
        panelFrenteLayout.setHorizontalGroup(
            panelFrenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFrenteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonFrente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelFrenteLayout.setVerticalGroup(
            panelFrenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFrenteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonFrente)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelLateral.setPreferredSize(new java.awt.Dimension(300, 300));
        panelLateral.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                panelLateralMouseWheelMoved(evt);
            }
        });
        panelLateral.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelLateralMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelLateralMouseClicked(evt);
            }
        });
        panelLateral.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelLateralMouseDragged(evt);
            }
        });

        jButtonLateral.setText("Maximizar");
        jButtonLateral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLateralActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLateralLayout = new javax.swing.GroupLayout(panelLateral);
        panelLateral.setLayout(panelLateralLayout);
        panelLateralLayout.setHorizontalGroup(
            panelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLateralLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonLateral, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelLateralLayout.setVerticalGroup(
            panelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonLateral, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTopo.setPreferredSize(new java.awt.Dimension(300, 300));
        panelTopo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelTopoMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelTopoMouseClicked(evt);
            }
        });
        panelTopo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelTopoMouseDragged(evt);
            }
        });

        jButtonTopo.setText("Maximizar");
        jButtonTopo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTopoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTopoLayout = new javax.swing.GroupLayout(panelTopo);
        panelTopo.setLayout(panelTopoLayout);
        panelTopoLayout.setHorizontalGroup(
            panelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTopoLayout.createSequentialGroup()
                .addContainerGap(255, Short.MAX_VALUE)
                .addComponent(jButtonTopo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelTopoLayout.setVerticalGroup(
            panelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonTopo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPerspectiva.setPreferredSize(new java.awt.Dimension(300, 300));

        jButtonPerspectiva.setText("Maximizar");
        jButtonPerspectiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPerspectivaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPerspectivaLayout = new javax.swing.GroupLayout(panelPerspectiva);
        panelPerspectiva.setLayout(panelPerspectivaLayout);
        panelPerspectivaLayout.setHorizontalGroup(
            panelPerspectivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPerspectivaLayout.createSequentialGroup()
                .addContainerGap(255, Short.MAX_VALUE)
                .addComponent(jButtonPerspectiva, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelPerspectivaLayout.setVerticalGroup(
            panelPerspectivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPerspectivaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonPerspectiva)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFundoLayout = new javax.swing.GroupLayout(panelFundo);
        panelFundo.setLayout(panelFundoLayout);
        panelFundoLayout.setHorizontalGroup(
            panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFundoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTopo, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(panelFrente, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPerspectiva, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(panelLateral, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelFundoLayout.setVerticalGroup(
            panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFundoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLateral, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(panelFrente, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTopo, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(panelPerspectiva, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Opções"));

        buttonSelecionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/cursor.png"))); // NOI18N
        buttonSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelecionarActionPerformed(evt);
            }
        });

        buttonTransladar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/move.png"))); // NOI18N
        buttonTransladar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTransladarActionPerformed(evt);
            }
        });

        buttonRotacionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/arrow_rotate_anticlockwise.png"))); // NOI18N
        buttonRotacionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRotacionarActionPerformed(evt);
            }
        });

        buttonEscalonar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/image_resize.png"))); // NOI18N
        buttonEscalonar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEscalonarActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jButton1.setText("SH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonSelecionar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonTransladar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonRotacionar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonEscalonar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(45, 45, 45))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonSelecionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonEscalonar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonRotacionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonTransladar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Poligono"));

        PoligonosBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PoligonosBoxItemStateChanged(evt);
            }
        });
        PoligonosBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PoligonosBoxActionPerformed(evt);
            }
        });

        jButton3.setText("Deletar Poligono");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("Redimensionar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButton3))
                    .addComponent(PoligonosBox, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(redimensionar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PoligonosBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(redimensionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)))
        );

        mostrarPontos.setText("Mostrar Pontos");
        mostrarPontos.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mostrarPontosStateChanged(evt);
            }
        });
        mostrarPontos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarPontosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout abaFerramentasLayout = new javax.swing.GroupLayout(abaFerramentas);
        abaFerramentas.setLayout(abaFerramentasLayout);
        abaFerramentasLayout.setHorizontalGroup(
            abaFerramentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(abaFerramentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(abaFerramentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, abaFerramentasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mostrarPontos, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104))
        );
        abaFerramentasLayout.setVerticalGroup(
            abaFerramentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(abaFerramentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mostrarPontos)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        jPanel6.getAccessibleContext().setAccessibleName("tyeste");

        abas.addTab("Ferramentas", abaFerramentas);

        buttonPiramide.setBorder(javax.swing.BorderFactory.createTitledBorder("Objetos"));

        buttonEsfera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/esfera.png"))); // NOI18N
        buttonEsfera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEsferaActionPerformed(evt);
            }
        });

        piramide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/piramide.png"))); // NOI18N
        piramide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                piramideActionPerformed(evt);
            }
        });

        buttonPrisma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/prisma.png"))); // NOI18N
        buttonPrisma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrismaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPiramideLayout = new javax.swing.GroupLayout(buttonPiramide);
        buttonPiramide.setLayout(buttonPiramideLayout);
        buttonPiramideLayout.setHorizontalGroup(
            buttonPiramideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPiramideLayout.createSequentialGroup()
                .addContainerGap(67, Short.MAX_VALUE)
                .addComponent(buttonEsfera, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(piramide, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPrisma, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        buttonPiramideLayout.setVerticalGroup(
            buttonPiramideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPiramideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPiramideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(piramide, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonEsfera, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(buttonPrisma, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Construção"));

        jLabel8.setText("Base/Pontos:");

        jLabel9.setText("Raio:");

        jLabel10.setText("Altura:");

        BaseSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                BaseSpinnerStateChanged(evt);
            }
        });

        RaioSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                RaioSpinnerStateChanged(evt);
            }
        });

        AlturaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AlturaSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BaseSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RaioSpinner)
                            .addComponent(AlturaSpinner))))
                .addGap(18, 18, 18))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(BaseSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(RaioSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(AlturaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout abaObjetosLayout = new javax.swing.GroupLayout(abaObjetos);
        abaObjetos.setLayout(abaObjetosLayout);
        abaObjetosLayout.setHorizontalGroup(
            abaObjetosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, abaObjetosLayout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addGroup(abaObjetosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonPiramide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );
        abaObjetosLayout.setVerticalGroup(
            abaObjetosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(abaObjetosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonPiramide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        abas.addTab("Objetos", abaObjetos);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Opções Câmera"));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("VRP"));

        svrpX.setModel(new javax.swing.SpinnerNumberModel(0, null, null, 1));
        svrpX.setModel(new javax.swing.SpinnerNumberModel());
        svrpX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                svrpXStateChanged(evt);
            }
        });

        svrpY.setModel(new javax.swing.SpinnerNumberModel(30, null, null, 1));
        svrpY.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(400), null, null, Integer.valueOf(1)));
        svrpY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                svrpYStateChanged(evt);
            }
        });

        svrpZ.setModel(new javax.swing.SpinnerNumberModel(100, null, null, 1));
        svrpZ.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(-300), null, null, Integer.valueOf(1)));
        svrpZ.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                svrpZStateChanged(evt);
            }
        });

        jLabel1.setText("x:");

        jLabel2.setText("y:");

        jLabel3.setText("z:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(svrpX, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addComponent(svrpY, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(svrpZ, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(svrpX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svrpZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svrpY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Ponto Foco"));

        svrpX.setValue(0);
        spontoX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spontoXStateChanged(evt);
            }
        });

        svrpX.setValue(0);
        spontoY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spontoYStateChanged(evt);
            }
        });

        svrpX.setValue(0);
        spontoZ.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spontoZStateChanged(evt);
            }
        });

        jLabel4.setText("x:");

        jLabel5.setText("y:");

        jLabel6.setText("z:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spontoX, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(6, 6, 6)
                .addComponent(spontoY, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spontoZ, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spontoX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spontoZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spontoY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Distância"));

        sdistancia.setModel(new javax.swing.SpinnerNumberModel(8, null, null, 1));
        sdistancia.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2), null, null, Integer.valueOf(1)));
        sdistancia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sdistanciaStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(sdistancia, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(sdistancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout abaCameraLayout = new javax.swing.GroupLayout(abaCamera);
        abaCamera.setLayout(abaCameraLayout);
        abaCameraLayout.setHorizontalGroup(
            abaCameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(abaCameraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        abaCameraLayout.setVerticalGroup(
            abaCameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(abaCameraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        abas.addTab("Opções Câmera", abaCamera);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        VisualizacaoRadioButtonGroup.add(WireframeRadioButton);
        WireframeRadioButton.setSelected(true);
        WireframeRadioButton.setText("Wireframe");
        WireframeRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WireframeRadioButtonMouseClicked(evt);
            }
        });
        WireframeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WireframeRadioButtonActionPerformed(evt);
            }
        });

        VisualizacaoRadioButtonGroup.add(WireframeOcultacaoRadioButton);
        WireframeOcultacaoRadioButton.setText("Wireframe com ocultação");
        WireframeOcultacaoRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WireframeOcultacaoRadioButtonMouseClicked(evt);
            }
        });
        WireframeOcultacaoRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WireframeOcultacaoRadioButtonActionPerformed(evt);
            }
        });

        VisualizacaoRadioButtonGroup.add(SombreamentoRadioButton);
        SombreamentoRadioButton.setText("Sombreamento Constante");
        SombreamentoRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SombreamentoRadioButtonMouseClicked(evt);
            }
        });
        SombreamentoRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SombreamentoRadioButtonActionPerformed(evt);
            }
        });

        VisualizacaoRadioButtonGroup.add(jRadioButton1);
        jRadioButton1.setText("Sombreamento Phong");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(SombreamentoRadioButton)
                    .addComponent(WireframeOcultacaoRadioButton)
                    .addComponent(WireframeRadioButton))
                .addContainerGap(190, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WireframeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(WireframeOcultacaoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SombreamentoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(117, Short.MAX_VALUE))
        );

        abas.addTab("Visualização", jPanel7);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Ambiente"));

        Ir.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        Ir.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IrStateChanged(evt);
            }
        });

        jLabel7.setText("Ir:");

        Ig.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        Ig.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IgStateChanged(evt);
            }
        });

        jLabel23.setText("Ig:");

        Ib.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        Ib.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IbStateChanged(evt);
            }
        });

        jLabel24.setText("Ib:");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(10, 10, 10)
                .addComponent(Ir, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Ig, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addComponent(Ib, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Ib, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)
                        .addComponent(Ig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23))
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Ir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Fonte de Luz"));

        jLabel25.setText("X:");

        FLX.setModel(new javax.swing.SpinnerNumberModel());
        FLX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLXStateChanged(evt);
            }
        });

        FLY.setModel(new javax.swing.SpinnerNumberModel());
        FLY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLYStateChanged(evt);
            }
        });

        jLabel29.setText("Y:");

        FLZ.setModel(new javax.swing.SpinnerNumberModel());
        FLZ.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLZStateChanged(evt);
            }
        });

        jLabel30.setText("Z:");

        FLB.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        FLB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLBStateChanged(evt);
            }
        });

        FLR.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        FLR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLRStateChanged(evt);
            }
        });

        jLabel31.setText("Lr:");

        FLG.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        FLG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLGStateChanged(evt);
            }
        });

        jLabel32.setText("Lg:");

        jLabel33.setText("Lb:");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FLX, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FLY, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FLZ, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(FLR, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FLG, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FLB, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(FLZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30))
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(FLY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29))
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(FLX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25)))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(FLR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel31))
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(FLG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33)
                        .addComponent(FLB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        abas.addTab("Iluminação", jPanel16);

        geral.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        colorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cor"));

        ArestaPanelColorChooser.setBackground(new java.awt.Color(0, 0, 0));
        ArestaPanelColorChooser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ArestaPanelColorChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ArestaPanelColorChooserMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ArestaPanelColorChooserLayout = new javax.swing.GroupLayout(ArestaPanelColorChooser);
        ArestaPanelColorChooser.setLayout(ArestaPanelColorChooserLayout);
        ArestaPanelColorChooserLayout.setHorizontalGroup(
            ArestaPanelColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );
        ArestaPanelColorChooserLayout.setVerticalGroup(
            ArestaPanelColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel11.setText("Arestas");

        FacePanelColorChooser.setBackground(new java.awt.Color(255, 255, 255));
        FacePanelColorChooser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        FacePanelColorChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FacePanelColorChooserMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout FacePanelColorChooserLayout = new javax.swing.GroupLayout(FacePanelColorChooser);
        FacePanelColorChooser.setLayout(FacePanelColorChooserLayout);
        FacePanelColorChooserLayout.setHorizontalGroup(
            FacePanelColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        FacePanelColorChooserLayout.setVerticalGroup(
            FacePanelColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel12.setText("Faces");

        javax.swing.GroupLayout colorPanelLayout = new javax.swing.GroupLayout(colorPanel);
        colorPanel.setLayout(colorPanelLayout);
        colorPanelLayout.setHorizontalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ArestaPanelColorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FacePanelColorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        colorPanelLayout.setVerticalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorPanelLayout.createSequentialGroup()
                .addGroup(colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ArestaPanelColorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FacePanelColorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Propriedades"));

        jLabel13.setText("n:");

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Ka"));

        jLabel14.setText("R:");

        jLabel15.setText("G:");

        jLabel16.setText("B:");

        KaR.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KaR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KaRStateChanged(evt);
            }
        });

        KaG.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KaG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KaGStateChanged(evt);
            }
        });

        KaB.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KaB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KaBStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(5, 5, 5)
                        .addComponent(KaB, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(KaR)
                            .addComponent(KaG))))
                .addGap(0, 0, 0))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(KaR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(KaG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KaB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Kd"));

        jLabel17.setText("R:");

        jLabel18.setText("G:");

        jLabel19.setText("B:");

        KdR.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KdR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KdRStateChanged(evt);
            }
        });

        KdG.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KdG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KdGStateChanged(evt);
            }
        });

        KdB.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KdB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KdBStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(5, 5, 5)
                        .addComponent(KdB, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(KdR)
                            .addComponent(KdG))))
                .addGap(0, 0, 0))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(KdR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(KdG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KdB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Ks"));

        jLabel20.setText("R:");

        jLabel21.setText("G:");

        jLabel22.setText("B:");

        KsR.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KsR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KsRStateChanged(evt);
            }
        });

        KsG.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KsG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KsGStateChanged(evt);
            }
        });

        KsB.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KsB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KsBStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(5, 5, 5)
                        .addComponent(KsB, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(KsR)
                            .addComponent(KsG))))
                .addGap(0, 0, 0))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(KsR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(KsG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KsB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        nPol.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), null, Float.valueOf(0.1f)));
        nPol.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nPolStateChanged(evt);
            }
        });

        jLabel34.setText("Kt:");

        kt.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, Float.valueOf(1.0f), Float.valueOf(0.01f)));
        kt.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ktStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nPol, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel34)))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(kt, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 68, Short.MAX_VALUE))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(nPol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(kt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jScrollPane1.setViewportView(jPanel10);

        jButton4.setText("zbuffer meio zoado");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout geralLayout = new javax.swing.GroupLayout(geral);
        geral.setLayout(geralLayout);
        geralLayout.setHorizontalGroup(
            geralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(geralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        geralLayout.setVerticalGroup(
            geralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(geralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abas)
            .addComponent(geral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(abas, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(geral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Arquivo");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Novo");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Abrir ");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Salvar ");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Salvar Como");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setText("Fechar");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelFundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFundo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRotacionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRotacionarActionPerformed
        this.cliqueAtual = 3;
    }//GEN-LAST:event_buttonRotacionarActionPerformed

    private void buttonPrismaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrismaActionPerformed
        this.cliqueAtual = 7;
    }//GEN-LAST:event_buttonPrismaActionPerformed
    /**
     * Conforme o evento e o poligono selecionado desenha o objeto no painel
     * frente
     *
     * @param evt
     */
    private void panelFrenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelFrenteMouseClicked
        //Clicou no painel frente ve qual poligono está selecionado
        // e cria o poligono
        Poligono pol = new Poligono();
        switch (cliqueAtual) {
            case 1:
                if (this.poligonos.size() > 0) {
                    //cria um vetor com as cordenadas do clique
                    Vetor click = new Vetor(2);
                    click.set(0, evt.getX());
                    click.set(1, panelFrente.getHeight() - evt.getY());
                    int index = 0;
                    int iterator = 0;
                    double menor = 0;
                    for (Poligono p : this.poligonosTransformados) {
                        Ponto c = p.getCentro();
                        Vetor centro = new Vetor(2);
                        centro.set(0, c.getX());
                        centro.set(1, c.getY());
                        Vetor clickCentro = Vetor.subtracao(centro, click);
                        if (iterator == 0) {
                            menor = clickCentro.getModulo();
                        }
                        if (menor > clickCentro.getModulo()) {
                            menor = clickCentro.getModulo();
                            index = iterator;
                        }
                        iterator++;
                    }
                    this.PoligonosBox.setSelectedIndex(index);
                }
                break;

            case 7:
                pol.GerarPrisma((int) BaseSpinner.getValue(),
                        (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                        getValue());
                this.prismas++;
                this.PoligonosBox.addItem("Prisma " + String.valueOf(prismas));
                break;
            case 6:
                pol.GerarPiramide((int) BaseSpinner.getValue(),
                        (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                        getValue());
                this.piramides++;
                this.PoligonosBox.addItem("Piramides " + String.valueOf(
                        piramides));
                break;
            case 5:
                pol.GerarEsfera((int) BaseSpinner.getValue(), (int) RaioSpinner.
                        getValue());
                this.esferas++;
                this.PoligonosBox.addItem("Esferas " + String.valueOf(esferas));
                break;
        }

        if (cliqueAtual == 5 || cliqueAtual == 6 || cliqueAtual == 7) {
            pol.Transladar(evt.getX(), panelFrente.getHeight() - evt.getY(),
                    this.panelFrente.getWidth() / 2);
            this.poligonos.add(pol);
            this.poligonosTransformados.add(pol.Transformar(true));
        }

        repaint();

        jaSalvo = false;



    }//GEN-LAST:event_panelFrenteMouseClicked

    private void panelFrenteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelFrenteMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_panelFrenteMouseEntered
    /**
     * Se mudou o estado do spinner svrpx
     *
     * @param evt
     */
    private void svrpXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_svrpXStateChanged

        vrpX = Double.valueOf(svrpX.getValue().toString());
        if (this.camera != null) {
            this.camera.setVx(vrpX);
            this.camera.GerarIntermediarios();
        }
        repaint();
        jaSalvo = false;

    }//GEN-LAST:event_svrpXStateChanged
    /**
     * Se mudou o estado do spinner svrpy
     *
     * @param evt
     */
    private void svrpYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_svrpYStateChanged
        vrpY = Double.valueOf(svrpY.getValue().toString());
        this.camera.setVy(vrpY);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
    }//GEN-LAST:event_svrpYStateChanged
    /**
     * Se mudou o estado do spinner svrpz
     *
     * @param evt
     */
    private void svrpZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_svrpZStateChanged
        vrpZ = Double.valueOf(svrpZ.getValue().toString());
        this.camera.setVz(vrpZ);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;

    }//GEN-LAST:event_svrpZStateChanged
    /**
     * Se mudou o estado do spinner spontox (ponto focal)
     *
     * @param evt
     */
    private void spontoXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spontoXStateChanged
        pontoX = Double.valueOf(spontoX.getValue().toString());
        this.camera.setFPx(pontoX);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;

    }//GEN-LAST:event_spontoXStateChanged
    /**
     * Se mudou o estado do spinner spontoY (ponto focal)
     *
     * @param evt
     */
    private void spontoYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spontoYStateChanged
        pontoY = Double.valueOf(spontoY.getValue().toString());
        this.camera.setFPy(pontoY);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
    }//GEN-LAST:event_spontoYStateChanged
    /**
     * Se mudou o estado do spinner spontoz (ponto focal)
     *
     * @param evt
     */
    private void spontoZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spontoZStateChanged
        pontoZ = Double.valueOf(spontoZ.getValue().toString());
        this.camera.setFPz(pontoZ);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;

    }//GEN-LAST:event_spontoZStateChanged
    /**
     * Se mudou o estado do spinner distancia
     *
     * @param evt
     */
    private void sdistanciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sdistanciaStateChanged
        distancia = Double.valueOf(sdistancia.getValue().toString());
        this.camera.setDistancia(distancia);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
    }//GEN-LAST:event_sdistanciaStateChanged
    /**
     * Botão transladar
     *
     * @param evt
     */
    private void buttonTransladarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTransladarActionPerformed
        cliqueAtual = 2;
    }//GEN-LAST:event_buttonTransladarActionPerformed
    /**
     * Este metodo verifica qual opção foi selecionada transladar, sisalhar,
     * selecionar e escalonar para o panel frente e executa a opção desejada
     *
     * @param evt
     */
    private void panelFrenteMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelFrenteMouseDragged
        if (!this.poligonos.isEmpty()) {
            if (cliqueAtual == 2) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get(this.PoligonosBox.
                        getSelectedIndex());
                if (Xanteior != 0) {
                    aux.TransladarX(evt.getX() - Xanteior);
                }
                if (Yanteior != 0) {
                    aux.TransladarY(Yanteior - evt.getY());
                }

                aux2.setPontos(aux.getPontosTransformados());

                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();
            }
            if (cliqueAtual == 3) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());

                if (Xanteior != 0) {
                    aux.RotacionarY(Xanteior - evt.getX());
                }
                if (Yanteior != 0) {
                    aux.RotacionarX(Yanteior - evt.getY());
                }
                Poligono aux2 = this.poligonosTransformados.
                        get(this.PoligonosBox.getSelectedIndex());
                aux2.setPontos(aux.getPontosTransformados());

                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();

            }
            if (cliqueAtual == 4) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get((this.PoligonosBox.
                        getSelectedIndex()));
                if (Xanteior - evt.getX() < 0) {
                    aux.EscalarX(1.10);
                } else {
                    if (Xanteior - evt.getX() > 0) {
                        aux.EscalarX(0.90);
                    }
                }
                if (evt.getY() - Yanteior < 0) {
                    aux.EscalarY(1.10);
                } else {
                    if (evt.getY() - Yanteior > 0) {
                        aux.EscalarY(0.90);
                    }

                }
                aux2.setPontos(aux.getPontosTransformados());
                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();
            }
            if (cliqueAtual == 8) {

                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get((this.PoligonosBox.
                        getSelectedIndex()));
                if (Xanteior - evt.getX() < 0) {
                    aux.CizalharXonZ(0.2);
                } else {
                    if (Xanteior - evt.getX() > 0) {
                        aux.CizalharXonZ(-0.2);
                    }
                }
                if (evt.getY() - Yanteior < 0) {
                    aux.CizalharYonZ(0.2);
                } else {
                    if (evt.getY() - Yanteior > 0) {
                        aux.CizalharYonZ(-0.2);
                    }

                }
                aux2.setPontos(aux.getPontosTransformados());
                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();

            }

            repaint();

            jaSalvo = false;

        }
    }//GEN-LAST:event_panelFrenteMouseDragged
    /**
     * Botão da Esfera
     *
     * @param evt
     */
    private void buttonEsferaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEsferaActionPerformed

        cliqueAtual = 5;

    }//GEN-LAST:event_buttonEsferaActionPerformed
    /**
     * Botão da Piramide
     *
     * @param evt
     */
    private void piramideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_piramideActionPerformed
        cliqueAtual = 6;
    }//GEN-LAST:event_piramideActionPerformed
    /**
     * Conforme o evento e o poligono selecionado desenha o objeto no painel da
     * lateral
     *
     * @param evt
     */
    private void panelLateralMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelLateralMouseClicked

        Poligono pol = new Poligono();
        switch (cliqueAtual) {
            case 1:
                if (this.poligonos.size() > 0) {
                    Vetor click = new Vetor(2);
                    click.set(0, evt.getX());
                    click.set(1, panelLateral.getHeight() - evt.getY());
                    int index = 0;
                    int iterator = 0;
                    double menor = 0;
                    for (Poligono p : this.poligonosTransformados) {
                        Ponto c = p.getCentro();
                        Vetor centro = new Vetor(2);
                        centro.set(0, c.getZ());
                        centro.set(1, c.getY());
                        Vetor clickCentro = Vetor.subtracao(centro, click);
                        if (iterator == 0) {
                            menor = clickCentro.getModulo();
                        }
                        if (menor > clickCentro.getModulo()) {
                            menor = clickCentro.getModulo();
                            index = iterator;
                        }
                        iterator++;
                    }
                    this.PoligonosBox.setSelectedIndex(index);
                }
                break;

            case 7:
                pol.GerarPrisma((int) BaseSpinner.getValue(),
                        (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                        getValue());
                this.prismas++;
                this.PoligonosBox.addItem("Prisma " + String.valueOf(prismas));
                break;
            case 6:
                pol.GerarPiramide((int) BaseSpinner.getValue(),
                        (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                        getValue());
                this.piramides++;
                this.PoligonosBox.addItem("Piramides " + String.valueOf(
                        piramides));
                break;
            case 5:
                pol.GerarEsfera((int) BaseSpinner.getValue(), (int) RaioSpinner.
                        getValue());
                this.esferas++;
                this.PoligonosBox.addItem("Esferas " + String.valueOf(esferas));
                break;
        }

        if (cliqueAtual == 5 || cliqueAtual == 6 || cliqueAtual == 7) {
            pol.Transladar(150, panelLateral.getHeight() - evt.getY(), evt.
                    getX());
            this.poligonos.add(pol);
            this.poligonosTransformados.add(pol.Transformar(true));
        }
        repaint();
    }//GEN-LAST:event_panelLateralMouseClicked
    /**
     * Conforme o evento e o poligono selecionado desenha o objeto no painel da
     * topo
     *
     * @param evt
     */
    private void panelTopoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopoMouseClicked
        Poligono pol = new Poligono();
        switch (cliqueAtual) {
            case 1:
                if (this.poligonos.size() > 0) {
                    Vetor click = new Vetor(2);
                    click.set(0, evt.getX());
                    click.set(1, evt.getY());
                    int index = 0;
                    int iterator = 0;
                    double menor = 0;
                    for (Poligono p : this.poligonosTransformados) {
                        Ponto c = p.getCentro();
                        Vetor centro = new Vetor(2);
                        centro.set(0, c.getZ());
                        centro.set(1, c.getY());
                        Vetor clickCentro = Vetor.subtracao(centro, click);
                        if (iterator == 0) {
                            menor = clickCentro.getModulo();
                        }
                        if (menor > clickCentro.getModulo()) {
                            menor = clickCentro.getModulo();
                            index = iterator;
                        }
                        iterator++;
                    }
                    this.PoligonosBox.setSelectedIndex(index);
                }
                break;

            case 7:
                pol.GerarPrisma((int) BaseSpinner.getValue(),
                        (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                        getValue());
                this.prismas++;
                this.PoligonosBox.addItem("Prisma " + String.valueOf(prismas));
                break;
            case 6:
                pol.GerarPiramide((int) BaseSpinner.getValue(),
                        (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                        getValue());
                this.piramides++;
                this.PoligonosBox.addItem("Piramides " + String.valueOf(
                        piramides));
                break;
            case 5:
                pol.GerarEsfera((int) BaseSpinner.getValue(), (int) RaioSpinner.
                        getValue());
                this.esferas++;
                this.PoligonosBox.addItem("Esferas " + String.valueOf(esferas));
                break;
        }

        if (cliqueAtual == 5 || cliqueAtual == 6 || cliqueAtual == 7) {
            pol.Transladar(evt.getX(), 150, evt.getY());
            this.poligonos.add(pol);
            this.poligonosTransformados.add(pol.Transformar(true));
        }
        repaint();
    }//GEN-LAST:event_panelTopoMouseClicked
    /**
     * Este metodo verifica qual opção foi selecionada transladar, sisalhar,
     * selecionar e escalonar para o panel lateral e executa a opção desejada
     *
     * @param evt
     */
    private void panelLateralMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelLateralMouseDragged
        if (!this.poligonos.isEmpty()) {
            if (cliqueAtual == 2) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get(this.PoligonosBox.
                        getSelectedIndex());
                if (Xanteior != 0) {
                    aux.TransladarY(Yanteior - evt.getY());
                }
                if (Yanteior != 0) {
                    aux.TransladarZ(evt.getX() - Xanteior);
                }

                aux2.setPontos(aux.getPontosTransformados());

                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();
            }
            if (cliqueAtual == 3) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());

                if (Xanteior != 0) {
                    aux.RotacionarY(Xanteior - evt.getX());
                }
                if (Yanteior != 0) {
                    aux.RotacionarZ(evt.getY() - Yanteior);
                }
                Poligono aux2 = this.poligonosTransformados.
                        get(this.PoligonosBox.getSelectedIndex());
                aux2.setPontos(aux.getPontosTransformados());

                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();

            }
            if (cliqueAtual == 4) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get((this.PoligonosBox.
                        getSelectedIndex()));
                if (evt.getY() - Yanteior < 0) {
                    aux.EscalarY(1.10);
                } else {
                    if (evt.getY() - Yanteior > 0) {
                        aux.EscalarY(0.90);
                    }
                }
                if (Xanteior - evt.getX() < 0) {
                    aux.EscalarZ(1.10);
                } else {
                    if (Xanteior - evt.getX() > 0) {
                        aux.EscalarZ(0.90);
                    }

                }
                aux2.setPontos(aux.getPontosTransformados());
                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();
            }
            if (cliqueAtual == 8) {

                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get((this.PoligonosBox.
                        getSelectedIndex()));
                if (Xanteior - evt.getX() < 0) {
                    aux.CizalharZonX(0.2);
                } else {
                    if (Xanteior - evt.getX() > 0) {
                        aux.CizalharZonX(-0.2);
                    }
                }
                if (Yanteior - evt.getY() < 0) {
                    aux.CizalharYonX(0.2);
                } else {
                    if (Yanteior - evt.getY() > 0) {
                        aux.CizalharYonX(-0.2);
                    }

                }
                aux2.setPontos(aux.getPontosTransformados());
                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();

            }


            repaint();
//
//            jaSalvo = false;
//                 if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


        }
    }//GEN-LAST:event_panelLateralMouseDragged
    /**
     * Este metodo verifica qual opção foi selecionada transladar, sisalhar,
     * selecionar e escalonar para o panel topo e executa a opção desejada
     *
     * @param evt
     */
    private void panelTopoMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopoMouseDragged
        if (!this.poligonos.isEmpty()) {
            if (cliqueAtual == 2) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get(this.PoligonosBox.
                        getSelectedIndex());
                if (Xanteior != 0) {
                    aux.TransladarX(evt.getX() - Xanteior);
                }
                if (Yanteior != 0) {
                    aux.TransladarZ(evt.getY() - Yanteior);
                }

                aux2.setPontos(aux.getPontosTransformados());

                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();
            }
            if (cliqueAtual == 3) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());

                if (Xanteior != 0) {
                    aux.RotacionarX(Xanteior - evt.getX());
                }
                if (Yanteior != 0) {
                    aux.RotacionarZ(Yanteior - evt.getY());
                }
                Poligono aux2 = this.poligonosTransformados.
                        get(this.PoligonosBox.getSelectedIndex());
                aux2.setPontos(aux.getPontosTransformados());

                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();

            }
            if (cliqueAtual == 4) {
                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get((this.PoligonosBox.
                        getSelectedIndex()));
                if (Xanteior - evt.getX() < 0) {
                    aux.EscalarX(1.10);
                } else {
                    if (Xanteior - evt.getX() > 0) {
                        aux.EscalarX(0.90);
                    }
                }
                if (evt.getY() - Yanteior < 0) {
                    aux.EscalarZ(1.10);
                } else {
                    if (evt.getY() - Yanteior > 0) {
                        aux.EscalarZ(0.90);
                    }

                }
                aux2.setPontos(aux.getPontosTransformados());
                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();
            }
            if (cliqueAtual == 8) {

                Poligono aux = this.poligonos.get(this.PoligonosBox.
                        getSelectedIndex());
                Poligono aux2 = this.poligonosTransformados.
                        get((this.PoligonosBox.
                        getSelectedIndex()));
                if (Xanteior - evt.getX() < 0) {
                    aux.CizalharXonY(0.2);
                } else {
                    if (Xanteior - evt.getX() > 0) {
                        aux.CizalharXonY(-0.2);
                    }
                }
                if (evt.getY() - Yanteior < 0) {
                    aux.CizalharZonY(0.2);
                } else {
                    if (evt.getY() - Yanteior > 0) {
                        aux.CizalharZonY(-0.2);
                    }

                }
                aux2.setPontos(aux.getPontosTransformados());
                this.Xanteior = evt.getX();
                this.Yanteior = evt.getY();

            }

            repaint();
        }

        jaSalvo = false;
    }//GEN-LAST:event_panelTopoMouseDragged
    /**
     * Conforme o estado do spinner de base muda as configurações de base do
     * poligono
     *
     * @param evt
     */
    private void BaseSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_BaseSpinnerStateChanged
        if (this.poligonos != null) {
            if (!this.poligonos.isEmpty()) {
                Poligono selecionado = poligonos.get(PoligonosBox.
                        getSelectedIndex());
                if ("Prisma".equals(selecionado.getTipo())) {
                    selecionado.GerarPrisma((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                            getValue());
                }
                if ("Piramide".equals(selecionado.getTipo())) {
                    selecionado.GerarPiramide((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                            getValue());
                }
                if ("Esfera".equals(selecionado.getTipo())) {
                    selecionado.GerarEsfera((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue());
                }
                this.poligonosTransformados.set(PoligonosBox.
                        getSelectedIndex(), selecionado.Transformar(true));

            }
        }
        repaint();
        jaSalvo = false;
    }//GEN-LAST:event_BaseSpinnerStateChanged
    /**
     * Conforme o estado do spinner de raio muda as configurações de raio do
     * poligono
     *
     * @param evt
     */
    private void RaioSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_RaioSpinnerStateChanged
        if (this.poligonos != null) {
            if (!this.poligonos.isEmpty()) {
                Poligono selecionado = poligonos.get(PoligonosBox.
                        getSelectedIndex());
                if ("Prisma".equals(selecionado.getTipo())) {
                    selecionado.GerarPrisma((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                            getValue());
                }
                if ("Piramide".equals(selecionado.getTipo())) {
                    selecionado.GerarPiramide((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                            getValue());
                }
                if ("Esfera".equals(selecionado.getTipo())) {
                    selecionado.GerarEsfera((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue());
                }
                this.poligonosTransformados.set(PoligonosBox.
                        getSelectedIndex(), selecionado.Transformar(true));
            }
        }
        repaint();


        jaSalvo = false;


    }//GEN-LAST:event_RaioSpinnerStateChanged
    /**
     * Conforme o estado do spinner de altura muda as configurações de altura do
     * poligono
     *
     * @param evt
     */
    private void AlturaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AlturaSpinnerStateChanged
        if (this.poligonos != null) {
            if (!this.poligonos.isEmpty()) {
                Poligono selecionado = poligonos.get(PoligonosBox.
                        getSelectedIndex());
                if ("Prisma".equals(selecionado.getTipo())) {
                    selecionado.GerarPrisma((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                            getValue());
                }
                if ("Piramide".equals(selecionado.getTipo())) {
                    selecionado.GerarPiramide((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue(), (int) AlturaSpinner.
                            getValue());
                }
                if ("Esfera".equals(selecionado.getTipo())) {
                    selecionado.GerarEsfera((int) BaseSpinner.getValue(),
                            (int) RaioSpinner.getValue());
                }
                this.poligonosTransformados.set(PoligonosBox.
                        getSelectedIndex(), selecionado.Transformar(true));
            }
        }
        repaint();


        jaSalvo = false;



    }//GEN-LAST:event_AlturaSpinnerStateChanged

    private void panelFrenteMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_panelFrenteMouseWheelMoved
    }//GEN-LAST:event_panelFrenteMouseWheelMoved

    private void panelLateralMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_panelLateralMouseWheelMoved
    }//GEN-LAST:event_panelLateralMouseWheelMoved
    /**
     * Pega as rotações do botão do meio do mouse
     *
     * @param evt
     */
    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        if (evt.getWheelRotation() == 1) {
            if (((Integer) this.BaseSpinner.getValue() - 1) >= 3) {
                this.BaseSpinner.setValue((Integer) this.BaseSpinner.getValue()
                        - 1);
            }
        } else {
            this.BaseSpinner.setValue((Integer) this.BaseSpinner.getValue() + 1);
        }

    }//GEN-LAST:event_formMouseWheelMoved
    /**
     * Visualização em wireframe
     *
     * @param evt
     */
    private void WireframeRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_WireframeRadioButtonActionPerformed
    {//GEN-HEADEREND:event_WireframeRadioButtonActionPerformed
        this.visualizacaoAtual = 1;
        repaint();
    }//GEN-LAST:event_WireframeRadioButtonActionPerformed
    /**
     * Visualização em wireframe
     *
     * @param evt
     */
    private void WireframeRadioButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_WireframeRadioButtonMouseClicked
    {//GEN-HEADEREND:event_WireframeRadioButtonMouseClicked
        this.visualizacaoAtual = 1;
        repaint();
    }//GEN-LAST:event_WireframeRadioButtonMouseClicked
    /**
     * Visualização em wireframe com ocultação
     *
     * @param evt
     */
    private void WireframeOcultacaoRadioButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_WireframeOcultacaoRadioButtonMouseClicked
    {//GEN-HEADEREND:event_WireframeOcultacaoRadioButtonMouseClicked
        this.visualizacaoAtual = 2;
        repaint();
    }//GEN-LAST:event_WireframeOcultacaoRadioButtonMouseClicked
    /**
     * Visualização em sombreamento
     *
     * @param evt
     */
    private void SombreamentoRadioButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_SombreamentoRadioButtonMouseClicked
    {//GEN-HEADEREND:event_SombreamentoRadioButtonMouseClicked
        this.visualizacaoAtual = 3;
        repaint();
    }//GEN-LAST:event_SombreamentoRadioButtonMouseClicked
    /**
     * Opção de cores da aresta
     *
     * @param evt
     */
    private void ArestaPanelColorChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ArestaPanelColorChooserMouseClicked
        MyColorChooser.createAndShowGUI(this, 1);

        jaSalvo = false;
    }//GEN-LAST:event_ArestaPanelColorChooserMouseClicked
    /**
     * Evento para mudança do poligono selecionado
     *
     * @param evt
     */
    private void PoligonosBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_PoligonosBoxItemStateChanged
    {//GEN-HEADEREND:event_PoligonosBoxItemStateChanged
        if (this.poligonos.size() > 0) {
            this.ArestaPanelColorChooser.setBackground(this.poligonos.
                    get(this.PoligonosBox.getSelectedIndex()).getCor());
            this.FacePanelColorChooser.setBackground(this.poligonos.
                    get(this.PoligonosBox.getSelectedIndex()).getCorFace());
        }
    }//GEN-LAST:event_PoligonosBoxItemStateChanged
    /**
     * Panel de cor
     *
     * @param evt
     */
    private void FacePanelColorChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FacePanelColorChooserMouseClicked
        MyColorChooser.createAndShowGUI(this, 2);

        jaSalvo = false;
    }//GEN-LAST:event_FacePanelColorChooserMouseClicked
    private void mostrarPontosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mostrarPontosStateChanged
    }//GEN-LAST:event_mostrarPontosStateChanged
    /**
     * Evento para mostrar os pontos
     *
     * @param evt
     */
    private void mostrarPontosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarPontosActionPerformed

        if (mostrarPontos.isSelected()) {
            this.pontos = true;
        } else {
            this.pontos = false;
        }
        repaint();
    }//GEN-LAST:event_mostrarPontosActionPerformed
    /**
     * Zera o x e y (anterior) para não usar o mouse dragged de forma errada
     *
     * @param evt
     */
    private void panelFrenteMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelFrenteMouseReleased
    {//GEN-HEADEREND:event_panelFrenteMouseReleased
        Xanteior = 0;
        Yanteior = 0;
    }//GEN-LAST:event_panelFrenteMouseReleased
    /**
     * Zera o x e y (anterior) para não usar o mouse dragged de forma errada
     *
     * @param evt
     */
    private void panelLateralMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelLateralMouseReleased
    {//GEN-HEADEREND:event_panelLateralMouseReleased
        Xanteior = 0;
        Yanteior = 0;
    }//GEN-LAST:event_panelLateralMouseReleased
    /**
     * Zera o x e y (anterior) para não usar o mouse dragged de forma errada
     *
     * @param evt
     */
    private void panelTopoMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelTopoMouseReleased
    {//GEN-HEADEREND:event_panelTopoMouseReleased
        Xanteior = 0;
        Yanteior = 0;
    }//GEN-LAST:event_panelTopoMouseReleased
    /**
     * Visualização com wireframe com ocultação
     *
     * @param evt
     */
    private void WireframeOcultacaoRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_WireframeOcultacaoRadioButtonActionPerformed
    {//GEN-HEADEREND:event_WireframeOcultacaoRadioButtonActionPerformed
        this.visualizacaoAtual = 2;
        repaint();
    }//GEN-LAST:event_WireframeOcultacaoRadioButtonActionPerformed
    /**
     * Visualização sombreamento
     *
     * @param evt
     */
    private void SombreamentoRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SombreamentoRadioButtonActionPerformed
    {//GEN-HEADEREND:event_SombreamentoRadioButtonActionPerformed
        this.visualizacaoAtual = 3;
        repaint();
    }//GEN-LAST:event_SombreamentoRadioButtonActionPerformed
    /**
     * escalonar clicado
     *
     * @param evt
     */
    private void buttonEscalonarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonEscalonarActionPerformed
    {//GEN-HEADEREND:event_buttonEscalonarActionPerformed
        this.cliqueAtual = 4;
    }//GEN-LAST:event_buttonEscalonarActionPerformed
    /**
     * Selecionar clicado
     *
     * @param evt
     */
    private void buttonSelecionarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonSelecionarActionPerformed
    {//GEN-HEADEREND:event_buttonSelecionarActionPerformed
        this.cliqueAtual = 1;
    }//GEN-LAST:event_buttonSelecionarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.cliqueAtual = 8;
    }//GEN-LAST:event_jButton1ActionPerformed
    /**
     * Opção de salvar
     *
     * @param evt
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            this.salvar();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    /**
     * Opção de abriar de arquivo
     *
     * @param evt
     */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            this.i.criaNovo(this);
            this.abrir();
            for (Poligono p : poligonos) {
               if(p.getTipo().equals("Prisma")){
                     this.PoligonosBox.addItem(("Prisma "+prismas));
                     prismas++;
               }
               if(p.getTipo().equals("Esfera")){
                     this.PoligonosBox.addItem(("Esfera "+esferas));
                     esferas++;
               }
               if(p.getTipo().equals("Piramide")){
                     this.PoligonosBox.addItem(("Piramide "+piramides));
                     piramides++;
               }

            }

           this.poligonosTransformados.add(this.poligonos.get(0));
            KaR.setValue(this.poligonos.get(0).getKaR());
            KaG.setValue(this.poligonos.get(0).getKaG());
            KaB.setValue(this.poligonos.get(0).getKaB());
            KdR.setValue(this.poligonos.get(0).getKdR());
            KdG.setValue(this.poligonos.get(0).getKdG());
            KdB.setValue(this.poligonos.get(0).getKdB());
            KsR.setValue(this.poligonos.get(0).getKsR());
            KsG.setValue(this.poligonos.get(0).getKsG());
            KsB.setValue(this.poligonos.get(0).getKsB());
            kt.setValue(this.poligonos.get(0).getKt());
            svrpX.setValue(this.camera.getVx());
            svrpY.setValue(this.camera.getVy());
            svrpZ.setValue(this.camera.getVz());   
            spontoX.setValue(this.camera.getFPx());
            spontoY.setValue(this.camera.getFPy());
            spontoZ.setValue(this.camera.getFPz());
            sdistancia.setValue(this.camera.getDistancia());
            Ir.setValue(this.luzAmbiente.getIr());
            Ig.setValue(this.luzAmbiente.getIg());
            Ib.setValue(this.luzAmbiente.getIb());
            
            FLX.setValue(this.luzFundo.getLocal().getX());
            FLY.setValue(this.luzFundo.getLocal().getY());
            FLZ.setValue(this.luzFundo.getLocal().getZ());
            
            FLB.setValue(this.luzFundo.getIb());
            FLR.setValue(this.luzFundo.getIr());
            FLG.setValue(this.luzFundo.getIg());

            nPol.setValue(this.poligonos.get(0).getN());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
                    ex);
        }

        repaint();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void panelFundoComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelFundoComponentResized
    }//GEN-LAST:event_panelFundoComponentResized
    /**
     * Botão maximizar do panel frente
     *
     * @param evt
     */
    private void jButtonFrenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFrenteActionPerformed
        //verifica em qual situação esta
        if (Interface.getMaximized() == 0) {
            panelFrente.setSize(panelFundo.getWidth(), panelFundo.getHeight());
            panelFrente.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();
            for (int i = 0; i < poligonos.size(); i++) {
                Poligono aux = poligonos.get(i);
                Poligono aux2 = poligonosTransformados.get(i);
                aux.EscalarX(tamanhoPanelFundoX / tamanhoPanelFrenteX);
                aux.EscalarY(tamanhoPanelFundoY / tamanhoPanelFrenteY);
                aux.EscalarZ(tamanhoPanelFundoY / tamanhoPanelFrenteY);

                long novoX = (Math.round((tamanhoPanelFundoX * aux2.getCentro().
                        getX()) / tamanhoPanelFrenteX));
                long novoY = (Math.round((tamanhoPanelFundoY * aux2.getCentro().
                        getY()) / tamanhoPanelFrenteY));
                aux.TransladarX(Math.round(novoX - aux2.getCentro().getX()));
                aux.TransladarY(Math.round(novoY - aux2.getCentro().getY()));
                aux2.setPontos(aux.getPontosTransformados());
            }
            repaint();
            panelLateral.setVisible(false);
            panelTopo.setVisible(false);
            panelPerspectiva.setVisible(false);
            Interface.setMaximized(1);
        } else if (Interface.getMaximized() == 1) {
            //System.out.println("\n\n\n");
            panelFrente.setSize(300, 300);
            panelFrente.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();
            for (int i = 0; i < poligonos.size(); i++) {
                Poligono aux = poligonos.get(i);
                Poligono aux2 = poligonosTransformados.get(i);
                long novoX = (Math.round((tamanhoPanelFrenteX
                        * aux2.getCentro().
                        getX()) / tamanhoPanelFundoX));
                long novoY = (Math.round((tamanhoPanelFrenteY
                        * aux2.getCentro().
                        getY()) / tamanhoPanelFundoY));
                aux.TransladarX(Math.round(novoX - aux2.getCentro().getX()));
                aux.TransladarY(Math.round(novoY - aux2.getCentro().getY()));

                aux.EscalarX(0.5);
                aux.EscalarY(0.5);
                aux.EscalarZ(0.5);
                aux2.setPontos(aux.getPontosTransformados());
            }
            repaint();
            panelLateral.setVisible(true);
            panelTopo.setVisible(true);
            panelPerspectiva.setVisible(true);
            Interface.setMaximized(0);
        }
    }//GEN-LAST:event_jButtonFrenteActionPerformed
    private void jButtonLateralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLateralActionPerformed
        if (Interface.getMaximized() == 0) {
            panelLateral.setSize(panelFundo.getWidth(), panelFundo.getHeight());
            panelLateral.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();

            for (int i = 0; i < poligonos.size(); i++) {
                Poligono aux = poligonos.get(i);
                Poligono aux2 = poligonosTransformados.get(i);
                aux.EscalarY(tamanhoPanelFundoX / tamanhoPanelFrenteX);
                aux.EscalarZ(tamanhoPanelFundoY / tamanhoPanelFrenteY);
                long novoX = (Math.round((tamanhoPanelFundoX * aux2.getCentro().
                        getY()) / tamanhoPanelFrenteX));
                long novoY = (Math.round((tamanhoPanelFundoY * aux2.getCentro().
                        getZ()) / tamanhoPanelFrenteY));
                aux.TransladarY(Math.round(novoX - aux2.getCentro().getY()));
                aux.TransladarZ(Math.round(novoY - aux2.getCentro().getZ()));
                aux2.setPontos(aux.getPontosTransformados());
            }
            //
            panelFrente.setVisible(false);
            panelTopo.setVisible(false);
            panelPerspectiva.setVisible(false);
            Interface.setMaximized(2);
        } else if (Interface.getMaximized() == 2) {
            panelLateral.setSize(200, 200);
            panelLateral.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();

            for (int i = 0; i < poligonos.size(); i++) {
                Poligono aux = poligonos.get(i);
                Poligono aux2 = poligonosTransformados.get(i);
                long novoX = (Math.round((tamanhoPanelFrenteX
                        * aux2.getCentro().
                        getY()) / tamanhoPanelFundoX));
                long novoY = (Math.round((tamanhoPanelFrenteY
                        * aux2.getCentro().
                        getZ()) / tamanhoPanelFundoY));
                aux.TransladarY(Math.round(novoX - aux2.getCentro().getY()));
                aux.TransladarZ(Math.round(novoY - aux2.getCentro().getZ()));

                aux.EscalarY(0.5);
                aux.EscalarZ(0.5);
                aux2.setPontos(aux.getPontosTransformados());
            }
            //
            panelFrente.setVisible(true);
            panelTopo.setVisible(true);
            panelPerspectiva.setVisible(true);
            Interface.setMaximized(0);
        }
    }//GEN-LAST:event_jButtonLateralActionPerformed
    /**
     * botão maximizar do topo
     *
     * @param evt
     */
    private void jButtonTopoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTopoActionPerformed
        if (Interface.getMaximized() == 0) {
            panelTopo.setSize(panelFundo.getWidth(), panelFundo.getHeight());
            panelTopo.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();

            for (int i = 0; i < poligonos.size(); i++) {
                Poligono aux = poligonos.get(i);
                Poligono aux2 = poligonosTransformados.get(i);
                aux.EscalarX(tamanhoPanelFundoX / tamanhoPanelFrenteX);
                aux.EscalarZ(tamanhoPanelFundoY / tamanhoPanelFrenteY);
                long novoX = (Math.round((tamanhoPanelFundoX * aux2.getCentro().
                        getX()) / tamanhoPanelFrenteX));
                long novoY = (Math.round((tamanhoPanelFundoY * aux2.getCentro().
                        getZ()) / tamanhoPanelFrenteY));
                aux.TransladarX(Math.round(novoX - aux2.getCentro().getX()));
                aux.TransladarZ(Math.round(novoY - aux2.getCentro().getZ()));
                aux2.setPontos(aux.getPontosTransformados());
            }
            //
            panelFrente.setVisible(false);
            panelLateral.setVisible(false);
            panelPerspectiva.setVisible(false);
            Interface.setMaximized(3);
        } else if (Interface.getMaximized() == 3) {
            panelTopo.setSize(200, 200);
            panelTopo.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();

            for (int i = 0; i < poligonos.size(); i++) {
                Poligono aux = poligonos.get(i);
                Poligono aux2 = poligonosTransformados.get(i);
                long novoX = (Math.round((tamanhoPanelFrenteX
                        * aux2.getCentro().
                        getX()) / tamanhoPanelFundoX));
                long novoY = (Math.round((tamanhoPanelFrenteY
                        * aux2.getCentro().
                        getZ()) / tamanhoPanelFundoY));
                aux.TransladarX(Math.round(novoX - aux2.getCentro().getX()));
                aux.TransladarZ(Math.round(novoY - aux2.getCentro().getZ()));

                aux.EscalarX(0.5);
                aux.EscalarZ(0.5);
                aux2.setPontos(aux.getPontosTransformados());
            }
            //
            panelFrente.setVisible(true);
            panelLateral.setVisible(true);
            panelPerspectiva.setVisible(true);
            Interface.setMaximized(0);
        }
    }//GEN-LAST:event_jButtonTopoActionPerformed
    /**
     * botão Maximizar da perspectiva
     *
     * @param evt
     */
    private void jButtonPerspectivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPerspectivaActionPerformed
        if (Interface.getMaximized() == 0) {
            panelPerspectiva.setSize(panelFundo.getWidth(), panelFundo.
                    getHeight());
            panelPerspectiva.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();
            //
            panelFrente.setVisible(false);
            panelLateral.setVisible(false);
            panelTopo.setVisible(false);
            Interface.setMaximized(4);
        } else if (Interface.getMaximized() == 4) {
            panelPerspectiva.setSize(200, 200);
            panelPerspectiva.repaint();
            panelFundo.repaint();
            panelFundo.updateUI();
            //
            panelFrente.setVisible(true);
            panelLateral.setVisible(true);
            panelTopo.setVisible(true);
            Interface.setMaximized(0);
        }
    }//GEN-LAST:event_jButtonPerspectivaActionPerformed

    private void panelFrenteComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelFrenteComponentResized
        panelFrente.repaint();
    }//GEN-LAST:event_panelFrenteComponentResized
    /**
     * Deleta o poligono selecionado
     *
     * @param evt
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (!this.poligonos.isEmpty()) {
            int index = this.PoligonosBox.getSelectedIndex();
            this.poligonos.remove(index);
            this.poligonosTransformados.remove(index);
            this.PoligonosBox.removeItemAt(index);
            repaint();
        }

        jaSalvo = false;
    }//GEN-LAST:event_jButton3ActionPerformed
    /**
     * Evento para deletar o poligono (confirmação)
     *
     * @param evt
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            this.salvarComo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
    }//GEN-LAST:event_formComponentResized
    /**
     * Menu par criação de uma nova cena
     *
     * @param evt
     */
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (jaSalvo) {
            i.criaNovo(this);
        } else {
            int resp = JOptionPane.showConfirmDialog(null,
                    "Cena não esta Salva. Deseja Salvar?", "Aviso",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (resp == 0) {
                try {
                    this.salvarComo();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Interface.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            } else {
                if (resp == 1) {
                    i.criaNovo(this);
                }
            }
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    /**
     * Menu fechar
     *
     * @param evt
     */
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (jaSalvo) {
            i.criaNovo(this);
        } else {
            int resp = JOptionPane.showConfirmDialog(null,
                    "Cena não esta Salva. Deseja Salvar?", "Aviso",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (resp == 0) {
                try {
                    this.salvarComo();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Interface.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            } else {
                if (resp == 1) {
                    System.exit(0);
                }
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    /**
     * Botão do zBuffer
     *
     * @param evt
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (zbuffer == null) {
            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
                    getWidth(), this.panelPerspectiva.getHeight());
            zbuffer.setVisible(true);
            zbuffer.repaint();
        } else {
            setZBuffer();
        }


    }//GEN-LAST:event_jButton4ActionPerformed
    /**
     * Botão escalar
     *
     * @param evt
     */
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Poligono selecionado = poligonos.get(PoligonosBox.
                getSelectedIndex());
        double val = Integer.parseInt(this.redimensionar.getText());
        selecionado.Escalar(val, val, val);
        this.poligonosTransformados.set(PoligonosBox.
                getSelectedIndex(), selecionado.Transformar(true));
        repaint();
    }//GEN-LAST:event_jButton5ActionPerformed
    /**
     * n para o calculo de reflexão
     *
     * @param evt
     */
    private void nPolStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nPolStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setN((float) nPol.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setN((float) nPol.getValue());

        repaint();
    }//GEN-LAST:event_nPolStateChanged
    /**
     * Ka red
     *
     * @param evt
     */
    private void KaRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KaRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKaR((float) KaR.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKaR((float) KaR.getValue());
        repaint();
    }//GEN-LAST:event_KaRStateChanged
    /**
     * Ka green
     *
     * @param evt
     */
    private void KaGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KaGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKaG((float) KaG.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKaG((float) KaG.getValue());
        repaint();
    }//GEN-LAST:event_KaGStateChanged
    /**
     * Ka blue
     *
     * @param evt
     */
    private void KaBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KaBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKaB((float) KaB.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKaB((float) KaB.getValue());
        repaint();
    }//GEN-LAST:event_KaBStateChanged
    /**
     * Kd red
     *
     * @param evt
     */
    private void KdRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KdRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKdR((float) KdR.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKdR((float) KdR.getValue());
        repaint();
    }//GEN-LAST:event_KdRStateChanged
    /**
     * Kd green
     *
     * @param evt
     */
    private void KdGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KdGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKdG((float) KdG.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKdG((float) KdG.getValue());

        repaint();
    }//GEN-LAST:event_KdGStateChanged
    /**
     * Kd blue
     *
     * @param evt
     */
    private void KdBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KdBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKdB((float) KdB.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKdB((float) KdB.getValue());
        repaint();
    }//GEN-LAST:event_KdBStateChanged
    /**
     * Ks red
     *
     * @param evt
     */
    private void KsRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KsRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKsR((float) KsR.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKsR((float) KsR.getValue());

        repaint();
    }//GEN-LAST:event_KsRStateChanged
    /**
     * Ks green
     *
     * @param evt
     */
    private void KsGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KsGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKsG((float) KsG.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKsG((float) KsG.getValue());

        repaint();
    }//GEN-LAST:event_KsGStateChanged
    /**
     * Ks blue
     *
     * @param evt
     */
    private void KsBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KsBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKsB((float) KsB.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKsB((float) KsB.getValue());

        repaint();
    }//GEN-LAST:event_KsBStateChanged
    /**
     * I red
     *
     * @param evt
     */
    private void IrStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IrStateChanged
        luzAmbiente.setIr((float) Ir.getValue());
        repaint();
    }//GEN-LAST:event_IrStateChanged
    /**
     * I green
     *
     * @param evt
     */
    private void IgStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IgStateChanged
        luzAmbiente.setIg((float) Ig.getValue());
        repaint();
    }//GEN-LAST:event_IgStateChanged
    /**
     * I blue
     *
     * @param evt
     */
    private void IbStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IbStateChanged
        luzAmbiente.setIb((float) Ib.getValue());
        repaint();
    }//GEN-LAST:event_IbStateChanged
    /**
     * FL X
     *
     * @param evt
     */
    private void FLXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLXStateChanged
        luzFundo.setLocal(new Ponto("", (int) FLX.getValue(), (int) FLY.
                getValue(), (int) FLZ.getValue()));
        repaint();
    }//GEN-LAST:event_FLXStateChanged
    /**
     * FL Y
     *
     * @param evt
     */
    private void FLYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLYStateChanged
        luzFundo.setLocal(new Ponto("", (int) FLX.getValue(), (int) FLY.
                getValue(), (int) FLZ.getValue()));
        repaint();
    }//GEN-LAST:event_FLYStateChanged
    /**
     * FL Z
     *
     * @param evt
     */
    private void FLZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLZStateChanged
        luzFundo.setLocal(new Ponto("", (int) FLX.getValue(), (int) FLY.
                getValue(), (int) FLZ.getValue()));
        repaint();
    }//GEN-LAST:event_FLZStateChanged
    /**
     * FL Red
     *
     * @param evt
     */
    private void FLRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLRStateChanged
        luzFundo.setIr((float) FLR.getValue());
        repaint();
    }//GEN-LAST:event_FLRStateChanged
    /**
     * FL green
     *
     * @param evt
     */
    private void FLGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLGStateChanged
        luzFundo.setIg((float) FLG.getValue());
        repaint();
    }//GEN-LAST:event_FLGStateChanged
    /**
     * FL Blue
     *
     * @param evt
     */
    private void FLBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLBStateChanged
        luzFundo.setIb((float) FLB.getValue());
        repaint();
    }//GEN-LAST:event_FLBStateChanged

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        this.visualizacaoAtual = 4;
        repaint();
    }//GEN-LAST:event_jRadioButton1ActionPerformed
    /**
     * Kt spinner
     *
     * @param evt
     */
    private void ktStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ktStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKt((float) kt.
                getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).
                setKt((float) kt.getValue());
        repaint();
    }//GEN-LAST:event_ktStateChanged

    private void PoligonosBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PoligonosBoxActionPerformed
    }//GEN-LAST:event_PoligonosBoxActionPerformed
    /**
     * Seta a cor das arestas do(s) poligonos(s)
     *
     * @param c color
     */
    public void setArestasCor(Color c) {
        if (this.poligonos.size() > 0) {
            this.poligonos.get(this.PoligonosBox.getSelectedIndex()).setCor(c);
            this.poligonosTransformados.
                    get(this.PoligonosBox.getSelectedIndex()).setCor(c);
            this.ArestaPanelColorChooser.setBackground(c);
            repaint();
        }
    }

    /**
     * seta a cor das faces dos poligonos
     *
     * @param c
     */
    public void setFacesCor(Color c) {
        if (this.poligonos.size() > 0) {
            this.poligonos.get(this.PoligonosBox.getSelectedIndex()).setCorFace(
                    c);
            this.poligonosTransformados.
                    get(this.PoligonosBox.getSelectedIndex()).setCorFace(c);
            this.FacePanelColorChooser.setBackground(c);
            repaint();
        }
    }

    /**
     * Faz a abertura de um arquivo, utiliza a classe MyFileHandler
     *
     * @throws FileNotFoundException arquivo não existe
     * @throws IOException erro na leitura do arquivo
     */
    public void abrir() throws FileNotFoundException, IOException {
        JFileChooser fileChosser = new JFileChooser();
        fileChosser.setFileFilter(new FileType());
        fileChosser.showOpenDialog(fileChosser);

        File arquivo;

        MyFileHandler MFH = new MyFileHandler(this);


        if (fileChosser.getSelectedFile() != null) {
            arquivo = new File(fileChosser.getSelectedFile().toString());


            MFH.openFile(arquivo, "r");
            MFH.Load();
            this.setNomeArquivo(arquivo.toString());
        }

    }

    /**
     * Salva a cena atual
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void salvar() throws FileNotFoundException, IOException {

        if (this.nomeArquivo.equals("/nothing")) {
            this.salvarComo();
        } else {
            File arquivo = new File(nomeArquivo);
            MyFileHandler MFH = new MyFileHandler(this);
            MFH.openFile(arquivo, "rw");
            MFH.Save();
            this.setNomeArquivo(arquivo.toString());
            JOptionPane.showMessageDialog(null, "Cena salva com sucesso!");
        }
        jaSalvo = true;
    }

    /**
     * Escolha nome para salvar a cena
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void salvarComo() throws FileNotFoundException, IOException {

        File arquivo = new File("Cena.3DM");
        MyFileHandler MFH = new MyFileHandler(this);

        JFileChooser fileChosser = new JFileChooser();
        fileChosser.setFileFilter(new FileType());
        fileChosser.setSelectedFile(arquivo);
        fileChosser.setAcceptAllFileFilterUsed(false);
        fileChosser.showSaveDialog(fileChosser);
        int opt = 0;
        if (fileChosser.getSelectedFile() != null) {

            String aux = fileChosser.getSelectedFile().toString();

            if (!aux.endsWith(".3DM")) {
                aux += ".3DM";
            }

            while (aux.equals(nomeArquivo)) {
                opt = JOptionPane.showConfirmDialog(null,
                        "Deseja sobreescrever o arquivo?", "Confirmação", 1);
                if (opt != 1) {
                    break;
                }
                fileChosser.setSelectedFile(arquivo);
                fileChosser.showSaveDialog(fileChosser);
                aux = fileChosser.getSelectedFile().toString();
            }
            if (opt == 0) {
                arquivo = new File(aux);
            }

            if (opt == 0) {
                MFH.openFile(arquivo, "rw");
                MFH.Save();
                this.setNomeArquivo(aux);
                JOptionPane.showMessageDialog(null, "Cena salva com sucesso!");
            }

        }
        jaSalvo = true;
    }

    public BufferedImage getZBuffer() {
        BufferedImage bufferImagem = new BufferedImage(this.panelPerspectiva.
                getWidth() + 600, this.panelPerspectiva.getHeight() + 600,
                BufferedImage.TYPE_INT_RGB);
        double[][] zBuffer = new double[this.panelPerspectiva.getWidth() + 600][this.panelPerspectiva.
                getHeight() + 600];
        //inicio as parada
        int fator = 300;
        for (int j = 0; j < bufferImagem.getWidth(); j++) {
            for (int k = 0; k < bufferImagem.getHeight(); k++) {
                bufferImagem.setRGB(j, k, this.panelPerspectiva.getBackground().
                        getRGB());
            }
        }
        for (int j = 0; j < zBuffer.length; j++) {
            for (int k = 0; k < zBuffer[0].length; k++) {
                zBuffer[j][k] = Long.MAX_VALUE;
            }
        }


        int altura = this.panelPerspectiva.getHeight();
        int largura = this.panelPerspectiva.getWidth();
        int ymax = altura;
        int xmax = largura;

        Color matrizCores[][] = new Color[xmax][ymax];
        BufferedImage buffer = new BufferedImage(xmax, ymax, BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < xmax; j++) {
            for (int k = 0; k < ymax; k++) {
                buffer.setRGB(j, k, panelPerspectiva.getBackground().getRGB());
            }
        }
        double matrizProfundidade[][] = new double[xmax][ymax];
        for (int i = 0; i < xmax; i++) {
            for (int j = 0; j < ymax; j++) {
                matrizProfundidade[i][j] = Double.MAX_VALUE;
            }

        }
        //scanlines
        if (!poligonos.isEmpty()) {
            for (Poligono p : poligonos) {
                camera.GerarIntermediarios();
                Poligono Paux = camera.GerarPerspectiva(largura, altura, p);
                Matriz aux = camera.getMatrizAux();
                Poligono ocultaFace = Paux.copy();
                ocultaFace.setPontos(aux);
                Poligono zpol = Paux.copy();
                Matriz zmaux = new Matriz();
                for (int i = zpol.getPontos().size() - 1; i >= 0; i--) {
                    zmaux.set(0, i, zpol.getPontos().get(i).getX());
                    zmaux.set(1, i, zpol.getPontos().get(i).getY());
                    zmaux.set(2, i, aux.get(2, i));
                }
                zpol.setPontos(zmaux);
                for (int i = 0; i < Paux.getFaces().size(); i++) {
                    Face f = Paux.getFaces().get(i);
                    Face f1 = ocultaFace.getFaces().get(i);
                    f1.gerarVetorPlano();
                    Vetor norma = f1.getVetorPlano();
                    norma.normalizar();
                    if (Vetor.produtoEscalar(camera.getVRPtoFP3(), norma) > 0) {
                        Face f2 = zpol.getFaces().get(i);
                        for (int j = 0; j < f2.getPontos().size(); j++) {
                            double mediaX = 0;
                            double mediaY = 0;
                            double mediaZ = 0;
                            int counter = 1;
                            for (int k = 0; k < zpol.getFaces().size(); k++) {
                                for (int l = 0; l < zpol.getFaces().get(k).getPontos().size(); l++) {
                                    if (f2.getPontos().get(j).getNome() == zpol.getFaces().get(k).getPontos().get(l).getNome()) {
                                        zpol.getFaces().get(k).gerarVetorPlano();
                                        zpol.getFaces().get(k).getVetorPlano().normalizar();
                                        mediaX += zpol.getFaces().get(k).getVetorPlano().get(0);
                                        mediaY += zpol.getFaces().get(k).getVetorPlano().get(1);
                                        mediaZ += zpol.getFaces().get(k).getVetorPlano().get(2);
                                        counter++;
                                    }
                                }
                            }
                            f2.getPontos().get(j).setnX(mediaX / counter);
                            f2.getPontos().get(j).setnY(mediaY / counter);
                            f2.getPontos().get(j).setnZ(mediaZ / counter);
                            f2.getPontos().get(j).setCameraZ(mediaZ);
                        }
                        for (int j = 0; j < f2.getPontos().size(); j++) {
                            for (int k = 0; k < p.getPontos().size(); k++) {
                                if (p.getPontos().get(k).getNome() == f2.getPontos().get(j).getNome()) {
                                    f2.getPontos().get(j).setmX(p.getPontos().get(k).getX());
                                    f2.getPontos().get(j).setmY(p.getPontos().get(k).getY());
                                    f2.getPontos().get(j).setmZ(p.getPontos().get(k).getZ());
                                }
                            }
                            for (int k = 0; k < zpol.getPontos().size(); k++) {
                                if (f2.getPontos().get(j).getNome() == zpol.getPontos().get(k).getNome()) {
                                    f2.getPontos().get(j).setCameraZ(zpol.getPontos().get(k).getZ());
                                }
                            }
                        }


                        //get arestas
                        java.util.List list = f2.getArestas();
                        double yinf = ((Aresta) list.get(0)).getP1().getX();
                        double ysup = ((Aresta) list.get(0)).getP1().getY();
                        for (int j = 1; j < list.size(); j++) {
                            if (((Aresta) list.get(j)).getP1().getY() < yinf) {
                                yinf = ((Aresta) list.get(j)).getP1().getY();
                            }
                            if (((Aresta) list.get(j)).getP1().getY() > ysup) {
                                ysup = ((Aresta) list.get(j)).getP1().getY();
                            }
                            if (((Aresta) list.get(j)).getP2().getY() < yinf) {
                                yinf = ((Aresta) list.get(j)).getP2().getY();
                            }
                            if (((Aresta) list.get(j)).getP2().getY() > ysup) {
                                ysup = ((Aresta) list.get(j)).getP2().getY();
                            }
                        }

                        int y = (int) (ysup >= (double) ymax ? ymax - 1 : ysup);
                        while (y > (int) yinf && y > 0) {
                            double x1 = 0.0D;
                            double x2 = 0.0D;
                            double u1 = 0.0D;
                            double u2 = 0.0D;
                            boolean first = true;
                            Ponto pa = null;
                            Ponto pb = null;
                            Ponto pc = null;
                            Ponto pd = null;
                            for (int j = 0; j < list.size(); j++) {
                                Ponto p1 = ((Aresta) list.get(j)).getP1();
                                Ponto p2 = ((Aresta) list.get(j)).getP2();
                                if ((int) p1.getY() == (int) p2.getY()) {
                                    continue;
                                }
                                double u = ((double) y - p1.getY()) / (p2.getY() - p1.getY());
                                if (u < 0.0D || u > 1.0D || y == (int) (p1.getY() <= p2.getY() ? p1.getY() : p2.getY())) {
                                    continue;
                                }
                                if (first) {
                                    x1 = u * (p2.getX() - p1.getX()) + p1.getX();
                                    pa = p1;
                                    pb = p2;
                                    u1 = u;
                                    first = false;
                                    continue;
                                }
                                x2 = u * (p2.getX() - p1.getX()) + p1.getX();
                                pc = p1;
                                pd = p2;
                                u2 = u;
                                break;
                            }

                            if (x1 != 0.0D || x2 != 0.0D) {
                                if (x1 > x2) {
                                    double aux2 = x1;
                                    x1 = x2;
                                    x2 = aux2;
                                    Ponto paux = pa;
                                    pa = pc;
                                    pc = paux;
                                    paux = pb;
                                    pb = pd;
                                    pd = paux;
                                    aux2 = u1;
                                    u1 = u2;
                                    u2 = aux2;
                                }


                                double e1 = (u1 * (pb.getY() - pa.getY())) / (pb.getY() - pa.getY());
                                double e2 = ((1.0D - u1) * (pb.getY() - pa.getY())) / (pb.getY() - pa.getY());
                                double e3 = (u2 * (pd.getY() - pc.getY())) / (pd.getY() - pc.getY());
                                double e4 = ((1.0D - u2) * (pd.getY() - pc.getY())) / (pd.getY() - pc.getY());

                                //os gets x e y acima são da perspectiva

                                double Nxi = pb.getnX() * e1 + pa.getnX() * e2;
                                double Nyi = pb.getnY() * e1 + pa.getnY() * e2;
                                double Nzi = pb.getnZ() * e1 + pa.getnZ() * e2;
                                double Nxf = pd.getnX() * e3 + pc.getnX() * e4;
                                double Nyf = pd.getnY() * e3 + pc.getnY() * e4;
                                double Nzf = pd.getnZ() * e3 + pc.getnZ() * e4;

                                //get mZ, mX e mY sao do mundo (ou camera, nao tenho ctz)
                                //get cameraZ é o z em camera, aqui



                                // o que tem que ser feito verificar se os meus mX, mY, mZ e cameraZ
                                //sao realmente o que tem que ser
                                // também verificar se os vetores normais medios estao corretos.

                                double Zi = pb.getmZ() * e1 + pa.getmZ() * e2;
                                double Zf = pd.getmZ() * e3 + pc.getmZ() * e4;
                                double Yi = pb.getmY() * e1 + pa.getmY() * e2;
                                double Yf = pd.getmY() * e3 + pc.getmY() * e4;
                                double Xi = pb.getmX() * e1 + pa.getmX() * e2;
                                double Xf = pd.getmX() * e3 + pc.getmX() * e4;
                                double Zci = pb.getCameraZ() * e1 + pa.getCameraZ() * e2;
                                double Zcf = pd.getCameraZ() * e3 + pc.getCameraZ() * e4;
                                double deltaX = x2 - x1;
                                double deltaNx = (Nxf - Nxi) / deltaX;
                                double deltaNy = (Nyf - Nyi) / deltaX;
                                double deltaNz = (Nzf - Nzi) / deltaX;
                                double deltaZM = (Zf - Zi) / deltaX;
                                double deltaXM = (Xf - Xi) / deltaX;
                                double deltaYM = (Yf - Yi) / deltaX;
                                double deltaZC = (Zcf - Zci) / deltaX;
                                int x = x1 <= 0.0D ? 0 : (int) Math.floor(x1);
                                double Nx = Nxi;
                                double Ny = Nyi;
                                double Nz = Nzi;
                                double Z = Zi;
                                double X = Xi;
                                double Y = Yi;
                                double ZC = Zci;
                                if (x == 0) {
                                    Nx += deltaNx * -x1;
                                    Ny += deltaNy * -x1;
                                    Nz += deltaNz * -x1;
                                    Z += deltaZM * -x1;
                                    X += deltaXM * -x1;
                                    Y += deltaYM * -x1;
                                    ZC += deltaZC * -x1;
                                }
                                while ((double) x < x2 && x < xmax) {
                                    if (ZC < matrizProfundidade[x][y]) {
//                                        System.out.println("entrou aqui");
                                        matrizProfundidade[x][y] = ZC;
                                        Ponto ponto = new Ponto("", X, Y, Z);
                                        ponto.setnX(Nx);
                                        ponto.setnY(Ny);
                                        ponto.setnZ(Nz);
                                        phong(p, ponto);
                                        int red = (int) (ponto.getIr() * (double) p.getCorFace().getRed());
                                        int green = (int) (ponto.getIg() * (double) p.getCorFace().getGreen());
                                        int blue = (int) (ponto.getIb() * (double) p.getCorFace().getBlue());
                                        System.out.println(red + " " + green + " " + blue);
                                        matrizCores[x][y] = new Color(red <= 255 ? red >= 0 ? red : 0 : 255, green <= 255 ? green >= 0 ? green : 0 : 255, blue <= 255 ? blue >= 0 ? blue : 0 : 255);
                                        buffer.setRGB(x, y, (new Color(red <= 255 ? red >= 0 ? red : 0 : 255, green <= 255 ? green >= 0 ? green : 0 : 255, blue <= 255 ? blue >= 0 ? blue : 0 : 255)).getRGB());
                                    }
                                    Nx += deltaNx;
                                    Ny += deltaNy;
                                    Nz += deltaNz;
                                    x++;
                                    Z += deltaZM;
                                    X += deltaXM;
                                    Y += deltaYM;
                                    ZC += deltaZC;
                                }
                            }
                            y--;
                        }
                    }
                }
            }

        }
//        BufferedImage buffer = new BufferedImage(xmax, ymax, BufferedImage.TYPE_INT_RGB);
//        for (int j = 0; j < xmax; j++) {
//            for (int w = 0; w < ymax; w++) {
//                int rgb = 0;
//                try {
//                    rgb = matrizCores[j][w].getRGB();
//                } catch (NullPointerException e) {
//                }
//                if (rgb != 0) {
//                    System.out.println("coco");
//                }
//                buffer.setRGB(j, w, rgb);
//            }
//        }

        return buffer;
//        return bufferImagem;
    }

    public void phong(Poligono p, Ponto ponto) {
        ponto.setIr(getIr(p, ponto));
        ponto.setIg(getIg(p, ponto));
        ponto.setIb(getIb(p, ponto));
    }

    private double getIr(Poligono p, Ponto ponto) {

        Matriz src = getCamera().getSRC();

        Matriz local = new Matriz(4, 1);
        local.set(0, 0, getLuzFundo().getLocal().getX());
        local.set(1, 0, getLuzFundo().getLocal().getY());
        local.set(2, 0, getLuzFundo().getLocal().getZ());
        local.set(3, 0, 1);

        local = Matriz.multiplicacao(src, local);

        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));

        double ambiente = ambiente(getLuzAmbiente().getIr(), p.getKaR());

//        double difusa = difusa(inter.getLuzFundo().getIr(), p.getKdR(), ponto.
//                getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double difusa = difusa(getLuzFundo().getIr(), p.getKdR(), ponto.
                getNormal(), plocal, ponto);

//        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
//                getIr(), p.getKsR(), p.getN(), inter.getLuzFundo().getLocal(),
//                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
//                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(getLuzFundo().
                getIr(), p.getKsR(), p.getN(), plocal,
                ponto.getNormal(), new Ponto("", getCamera().getVx(),
                getCamera().getVy(), getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private double getIg(Poligono p, Ponto ponto) {

        Matriz src = getCamera().getSRC();

        Matriz local = new Matriz(4, 1);
        local.set(0, 0, getLuzFundo().getLocal().getX());
        local.set(1, 0, getLuzFundo().getLocal().getY());
        local.set(2, 0, getLuzFundo().getLocal().getZ());
        local.set(3, 0, 1);

        local = Matriz.multiplicacao(src, local);

        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));

        double ambiente = ambiente(getLuzAmbiente().getIg(), p.getKaG());

//        double difusa = difusa(inter.getLuzFundo().getIg(), p.getKdG(), ponto.
//                getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double difusa = difusa(getLuzFundo().getIg(), p.getKdG(), ponto.
                getNormal(), plocal, ponto);

//        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
//                getIg(), p.getKsG(), p.getN(), inter.getLuzFundo().getLocal(),
//                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
//                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(getLuzFundo().
                getIg(), p.getKsG(), p.getN(), plocal,
                ponto.getNormal(), new Ponto("", getCamera().getVx(),
                getCamera().getVy(), getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private double getIb(Poligono p, Ponto ponto) {

        Matriz src = getCamera().getSRC();

        Matriz local = new Matriz(4, 1);
        local.set(0, 0, getLuzFundo().getLocal().getX());
        local.set(1, 0, getLuzFundo().getLocal().getY());
        local.set(2, 0, getLuzFundo().getLocal().getZ());
        local.set(3, 0, 1);

        local = Matriz.multiplicacao(src, local);

        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));

        double ambiente = ambiente(getLuzAmbiente().getIb(), p.getKaB());

        double difusa = difusa(getLuzFundo().getIb(), p.getKdB(), ponto.
                getNormal(), getLuzFundo().getLocal(), ponto);

        double especular = difusa == 0.0D ? 0.0D : especular(getLuzFundo().
                getIb(), p.getKsB(), p.getN(), getLuzFundo().getLocal(),
                ponto.getNormal(), new Ponto("", getCamera().getVx(),
                getCamera().getVy(), getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private static double ambiente(double Ia, double Ka) {
        return Ia * Ka;
    }

    private static double difusa(double Il, double Kd, Ponto normal, Ponto L,
            Ponto pontoObservado) {
        Ponto l = new Ponto("", pontoObservado.getX() - L.getX(),
                pontoObservado.getY() - L.getY(), pontoObservado.getZ() - L.
                getZ());
        double normaL = norma(l);
        l.setX(l.getX() / normaL);
        l.setY(l.getY() / normaL);
        l.setZ(l.getZ() / normaL);
        double normaNormal = norma(normal);
        Ponto n = new Ponto("", normal.getX() / normaNormal, normal.getY()
                / normaNormal, normal.getZ() / normaNormal);
        double escalarNL = escalar(n, l);
        if (escalarNL > 0.0D) {
            return Il * Kd * escalarNL;
        } else {
            return 0.0D;
        }
    }

    private static double especular(double Il, double Ks, double expoenteN,
            Ponto L, Ponto N, Ponto VRP, Ponto A) {
        Ponto l = new Ponto("", A.getX() - L.getX(), A.getY() - L.getY(), A.
                getZ() - L.getZ());
        double normaL = norma(l);
        l.setX(l.getX() / normaL);
        l.setY(l.getY() / normaL);
        l.setZ(l.getZ() / normaL);
        double normaN = norma(N);
        Ponto n = new Ponto("", N.getX() / normaN, N.getY() / normaN, N.getZ()
                / normaN);
        Ponto r = new Ponto();
        double DoisLN = 2D * escalar(l, n);
        r.setX(l.getX() - DoisLN * n.getX());
        r.setY(l.getY() - DoisLN * n.getY());
        r.setZ(l.getZ() - DoisLN * n.getZ());
        Ponto s = new Ponto("", VRP.getX() - A.getX(), VRP.getY() - A.getY(),
                VRP.getZ() - A.getZ());
        double normaS = norma(s);
        s.setX(s.getX() / normaS);
        s.setY(s.getY() / normaS);
        s.setZ(s.getZ() / normaS);
        double escalarRS = escalar(r, s);
        if (escalarRS > 0.0D) {
            return Il * Ks * Math.pow(escalarRS, expoenteN);
        } else {
            return 0.0D;
        }
    }

    public static double norma(Ponto p) {
        return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ()
                * p.getZ());
    }

    public static double escalar(Ponto p1, Ponto p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY() + p1.getZ() * p2.
                getZ();
    }

//    public BufferedImage getZBuffer() {
//        BufferedImage bufferImagem = new BufferedImage(this.panelPerspectiva.
//                getWidth() + 600, this.panelPerspectiva.getHeight() + 600,
//                BufferedImage.TYPE_INT_RGB);
//        double[][] zBuffer = new double[this.panelPerspectiva.getWidth() + 600][this.panelPerspectiva.
//                getHeight() + 600];
//        //inicio as parada
//        int fator = 300;
//        for (int j = 0; j < bufferImagem.getWidth(); j++) {
//            for (int k = 0; k < bufferImagem.getHeight(); k++) {
//                bufferImagem.setRGB(j, k, this.panelPerspectiva.getBackground().
//                        getRGB());
//            }
//        }
//        for (int j = 0; j < zBuffer.length; j++) {
//            for (int k = 0; k < zBuffer[0].length; k++) {
//                zBuffer[j][k] = Long.MAX_VALUE;
//            }
//        }
//
//        int altura = this.panelPerspectiva.getHeight();
//        int largura = this.panelPerspectiva.getWidth();
//        //scanlines
//        if (!poligonos.isEmpty()) {
//            for (Poligono p : poligonos) {
////            g2D.setColor(p.getCor());
//                camera.GerarIntermediarios();
////                p.getMatrizPontos().print("P");
//                Poligono Paux = camera.GerarPerspectiva(largura, altura, p);
////                Paux.getMatrizPontos().print("Paux");
//                Matriz aux = camera.getMatrizAux();
//                Poligono ocultaFace = Paux.copy();
//                ocultaFace.setPontos(aux);
//                Poligono zpol = Paux.copy();
//                Matriz zmaux = new Matriz();
//                for (int i = zpol.getPontos().size() - 1; i >= 0; i--) {
//                    zmaux.set(0, i, zpol.getPontos().get(i).getX());
//                    zmaux.set(1, i, zpol.getPontos().get(i).getY());
//                    zmaux.set(2, i, aux.get(2, i));
//                }
//                zpol.setPontos(zmaux);
////                zpol.getMatrizPontos().print("zpol");
//                for (int i = 0; i < Paux.getFaces().size(); i++) {
//                    Face f = Paux.getFaces().get(i);
//                    Face f1 = ocultaFace.getFaces().get(i);
//                    Face f2 = zpol.getFaces().get(i);
//                    f1.gerarVetorPlano();
//                    Vetor norma = f1.getVetorPlano();
//                    norma.normalizar();
//                    ArrayList<Ponto> pt = f.getPontos();
//                    if (Vetor.produtoEscalar(camera.getVRPtoFP3(), norma) > 0) {
//                        f2.gerarVetorPlano();
//                        norma = f2.getVetorPlano();
//                        Ponto pontoQualquer = f2.getPontos().get(0);
//                        double d = -(norma.get(0) * pontoQualquer.getX())
//                                - (norma.get(1) * pontoQualquer.getY())
//                                - (norma.get(2) * pontoQualquer.getZ());
//                        ArrayList<Aresta> arestas = f.getArestas();
//                        //utilizo para encontrar os limites
//                        double minimoY = arestas.get(0).getP1().getY();
//                        double maximoY = arestas.get(0).getP1().getY();
//                        //acho o limite das faces
//                        for (Aresta a : arestas) {
//                            Ponto p1 = a.getP1();
//                            Ponto p2 = a.getP2();
//
//                            if (minimoY > p1.getY() || minimoY > p2.getY()) {
//                                if (minimoY > p1.getY()) {
//                                    minimoY = p1.getY();
//                                } else {
//                                    minimoY = p2.getY();
//                                }
//                            }
//
//                            if (maximoY < p1.getY() || maximoY < p2.getY()) {
//                                if (maximoY < p1.getY()) {
//                                    maximoY = p1.getY();
//                                } else {
//                                    maximoY = p2.getY();
//                                }
//                            }
//                        }
//
//                        ArrayList<Aresta> pintar = new ArrayList<>(arestas);
//                        //verifico se aresta esta na vertical, se esta, já esta pintada, removo da lista de pintura.
//                        for (int e = 0; e < pintar.size(); ++e) {
//                            if (pintar.get(e).getP1().getY() == pintar.get(
//                                    e).getP2().getY()) {
//                                pintar.remove(e);
//                            }
//                        }
//                        //faço a pintura linha por linha, calculando por meio do algoritmo de recorte se uma aresta esta contida na face ou não.
//                        for (int y = (int) maximoY; (y > (int) minimoY) && (y
//                                > 0); --y) {
//                            double x1 = 0;
//                            double x2 = 0;
//                            boolean ok = true;
//                            for (int e = 0; e < pintar.size(); ++e) {
//                                Ponto p1 = pintar.get(e).getP1();
//
//                                Ponto p2 = pintar.get(e).getP2();
//
//                                double eqR = (y - p1.getY()) / (p2.getY() - p1.
//                                        getY());
//
//                                if ((eqR >= 0.0) && (eqR <= 1.0)) {
//                                    if (ok) {
//                                        x1 = eqR * (p2.getX() - p1.getX()) + p1.
//                                                getX();
//                                        ok = false;
//                                    } else {
//                                        x2 = eqR * (p2.getX() - p1.getX()) + p1.
//                                                getX();
//                                        break;
//                                    }
//                                }
//                            }
////                             g.drawLine((int) (x1), y, (int) (x2), y);
//
//                            int inicial = (int) x1;
//                            int finall = (int) x2;
//
//                            if (inicial > finall) {
//                                int auxI = inicial;
//                                inicial = finall;
//                                finall = auxI;
//                            }
//                            double zA = -d - norma.get(0) * inicial - norma.get(
//                                    1) * y;
////                          
//                            if (zA < zBuffer[inicial + fator][y + fator]) {
//                                zBuffer[inicial + fator][y + fator] = zA;
//                                bufferImagem.setRGB(inicial + fator, y + fator,
//                                        p.getCorFace().getRGB());
//                            }
//                            for (int j = inicial; j < finall; j++) {
//                                zA = zA - norma.get(0) / norma.get(2);
////                                try {
//                                if (zA < zBuffer[j + fator][y + fator]) {
//                                    zBuffer[j + fator][y + fator] = zA;
//                                    bufferImagem.setRGB(j + fator, y + fator, p.
//                                            getCorFace().getRGB());
//                                }
////                                } catch (ArrayIndexOutOfBoundsException a) {
//
////                                }
//                            }
//
//
//
//                        }
//                    }
//                }
//            }
//        }
//
//        return cortaBuffer(bufferImagem, largura, altura, fator);
////        return bufferImagem;
//    }
    private void setZBuffer() {
        if (zbuffer != null) {
            zbuffer.setZbuffer(this.getZBuffer(), this.panelPerspectiva.
                    getWidth(), this.panelPerspectiva.getHeight());
            zbuffer.setVisible(true);
            zbuffer.repaint();
        }
    }

    private BufferedImage cortaBuffer(BufferedImage bufferImagem, int largura,
            int altura, int fator) {
        BufferedImage buffer = new BufferedImage(largura, altura,
                BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < largura; j++) {
            for (int k = 0; k < altura; k++) {
                buffer.setRGB(j, k, bufferImagem.getRGB(j + fator, k + fator));
            }
        }

        return buffer;
    }

    /**
     * Luz ambiente
     *
     * @return Iluminacao
     */
    public Iluminacao getLuzAmbiente() {
        return luzAmbiente;
    }

    /**
     * Seta luz ambiente
     *
     * @param luzAmbiente
     */
    public void setLuzAmbiente(Iluminacao luzAmbiente) {
        this.luzAmbiente = luzAmbiente;
    }

    /**
     * Retorna a luz de fundo
     *
     * @return Iluminacao
     */
    public Iluminacao getLuzFundo() {
        return luzFundo;
    }

    /**
     * seta a luz de fundo
     *
     * @param luzFundo
     */
    public void setLuzFundo(Iluminacao luzFundo) {
        this.luzFundo = luzFundo;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner AlturaSpinner;
    private javax.swing.JPanel ArestaPanelColorChooser;
    private javax.swing.JSpinner BaseSpinner;
    private javax.swing.JSpinner FLB;
    private javax.swing.JSpinner FLG;
    private javax.swing.JSpinner FLR;
    private javax.swing.JSpinner FLX;
    private javax.swing.JSpinner FLY;
    private javax.swing.JSpinner FLZ;
    private javax.swing.JPanel FacePanelColorChooser;
    private javax.swing.JSpinner Ib;
    private javax.swing.JSpinner Ig;
    private javax.swing.JSpinner Ir;
    private javax.swing.JSpinner KaB;
    private javax.swing.JSpinner KaG;
    private javax.swing.JSpinner KaR;
    private javax.swing.JSpinner KdB;
    private javax.swing.JSpinner KdG;
    private javax.swing.JSpinner KdR;
    private javax.swing.JSpinner KsB;
    private javax.swing.JSpinner KsG;
    private javax.swing.JSpinner KsR;
    private javax.swing.JComboBox PoligonosBox;
    private javax.swing.JSpinner RaioSpinner;
    private javax.swing.JRadioButton SombreamentoRadioButton;
    private javax.swing.ButtonGroup VisualizacaoRadioButtonGroup;
    private javax.swing.JRadioButton WireframeOcultacaoRadioButton;
    private javax.swing.JRadioButton WireframeRadioButton;
    private javax.swing.JPanel abaCamera;
    private javax.swing.JPanel abaFerramentas;
    private javax.swing.JPanel abaObjetos;
    private javax.swing.JTabbedPane abas;
    private javax.swing.JButton buttonEscalonar;
    private javax.swing.JButton buttonEsfera;
    private javax.swing.JPanel buttonPiramide;
    private javax.swing.JButton buttonPrisma;
    private javax.swing.JButton buttonRotacionar;
    private javax.swing.JButton buttonSelecionar;
    private javax.swing.JButton buttonTransladar;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JPanel geral;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonFrente;
    private javax.swing.JButton jButtonLateral;
    private javax.swing.JButton jButtonPerspectiva;
    private javax.swing.JButton jButtonTopo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner kt;
    private javax.swing.JCheckBox mostrarPontos;
    private javax.swing.JSpinner nPol;
    public javax.swing.JPanel panelFrente;
    public javax.swing.JPanel panelFundo;
    public javax.swing.JPanel panelLateral;
    public javax.swing.JPanel panelPerspectiva;
    public javax.swing.JPanel panelTopo;
    private javax.swing.JButton piramide;
    private javax.swing.JTextField redimensionar;
    private javax.swing.JSpinner sdistancia;
    private static javax.swing.JSpinner spontoX;
    private static javax.swing.JSpinner spontoY;
    private static javax.swing.JSpinner spontoZ;
    private static javax.swing.JSpinner svrpX;
    private static javax.swing.JSpinner svrpY;
    private static javax.swing.JSpinner svrpZ;
    // End of variables declaration//GEN-END:variables
}
