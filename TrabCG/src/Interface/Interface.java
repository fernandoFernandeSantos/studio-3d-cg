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
    private double vrpX;
    private double vrpY;
    private double vrpZ;
    private double pontoX;
    private double pontoY;
    private double pontoZ;
    private double distancia;
    private int visualizacaoAtual;//identifica tipo de visualização | 1 -> Wireframe | 2 -> Wireframe com ocultação | 3 -> Sombreamento costante |
    private int cliqueAtual; // identifica clique do mouse 1 - selecionar; 2 - transladar; 3 - rotacionar; 4 - escala; 5 - esfera; 6 - piramide; 7 - prisma; 8 - cizalhar
    private ArrayList<Poligono> poligonos;
    private ArrayList<Poligono> poligonosTransformados;
    private Camera camera;
    private int Xanteior;
    private int Yanteior;
    private boolean vetores;
    private boolean pontos;
    private int tamanhoPanelFrenteX;
    private int tamanhoPanelFrenteY;
    private int tamanhoPanelLateralX;
    private int tamanhoPanelLateralY;
    private int tamanhoPanelTopoX;
    private int tamanhoPanelTopoY;
    private int tamanhoPanelFundoX;
    private int tamanhoPanelFundoY;
    public int prismas;
    public int piramides;
    public int esferas;
    private static int maximized = 0;
    /* 0 = nenhum
     * 1 = frente
     * 2 = lateral
     * 3 = topo
     * 4 = perspectiva
     */
    boolean jaSalvo = false;
//    File file;
    String nomeArquivo;
    Init i;
    Iluminacao luzAmbiente;
    Iluminacao luzFundo;

    public Interface(Init _i) {

        initComponents();

        luzAmbiente = new Iluminacao(new Ponto("localI", 0, 0, 100), 0.5, 0.5, 0.5);
        luzFundo = new Iluminacao(new Ponto("localI", 0, 0, 0), 0.5, 0.5, 0.5);


        i = _i;
        setLocationRelativeTo(null);
        this.setResizable(false);
        vrpX = Double.valueOf(svrpX.getValue().toString());
        vrpY = Double.valueOf(svrpY.getValue().toString());
        vrpZ = Double.valueOf(svrpZ.getValue().toString());
        pontoX = Double.valueOf(spontoX.getValue().toString());
        pontoY = Double.valueOf(spontoY.getValue().toString());
        pontoZ = Double.valueOf(spontoZ.getValue().toString());
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

        cliqueAtual = 1;

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

        this.nomeArquivo = "/nothing";

    }

    public void addPoligono(Poligono p) {
        this.poligonos.add(p);
        this.poligonosTransformados.add(p.Transformar(true));
//        if (p.getTipo().equals("Prisma")) {
//            this.prismas++;
//            this.addPoligonosBox("Prisma",this.prismas );
//        }
//        if (p.getTipo().equals("Piramide")) {
//            this.prismas++;
//            this.addPoligonosBox("Piramide",this.piramides );
//        }
//        if (p.getTipo().equals("Esfera")) {
//            this.prismas++;
//            this.addPoligonosBox("Esfera",this.esferas );
//        }
    }

    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    public void setNomeArquivo(String nome) {
        this.nomeArquivo = nome;
    }

    public static int getMaximized() {
        return maximized;
    }

    public static void setMaximized(int maximized) {
        Interface.maximized = maximized;
    }

    public boolean isMostrarPontos() {
        return pontos;
    }

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

    public void setCamera(Camera camera) {

        svrpX.setValue((int) camera.getVx());
        svrpY.setValue((int) camera.getVy());
        svrpZ.setValue((int) camera.getVz());

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
        jButton2 = new javax.swing.JButton();
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
        jPanel14 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        KtR = new javax.swing.JSpinner();
        KtG = new javax.swing.JSpinner();
        KtB = new javax.swing.JSpinner();
        nPol = new javax.swing.JSpinner();
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
                    .addComponent(panelLateral, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(panelFrente, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTopo, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .addComponent(panelPerspectiva, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/piramide.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPrisma, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        buttonPiramideLayout.setVerticalGroup(
            buttonPiramideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPiramideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPiramideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        svrpX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                svrpXStateChanged(evt);
            }
        });

        svrpY.setModel(new javax.swing.SpinnerNumberModel(30, null, null, 1));
        svrpY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                svrpYStateChanged(evt);
            }
        });

        svrpZ.setModel(new javax.swing.SpinnerNumberModel(100, null, null, 1));
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

        Ir.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        Ir.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IrStateChanged(evt);
            }
        });

        jLabel7.setText("Ir:");

        Ig.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        Ig.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IgStateChanged(evt);
            }
        });

        jLabel23.setText("Ig:");

        Ib.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
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

        FLB.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        FLB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLBStateChanged(evt);
            }
        });

        FLR.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        FLR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FLRStateChanged(evt);
            }
        });

        jLabel31.setText("Lr:");

        FLG.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
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

        jPanel10.setLayout(null);

        jLabel13.setText("n:");
        jPanel10.add(jLabel13);
        jLabel13.setBounds(131, 17, 10, 14);

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
                            .addComponent(KaR, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel11);
        jPanel11.setBounds(0, 40, 80, 105);

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
                            .addComponent(KdR, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel12);
        jPanel12.setBounds(80, 40, 80, 105);

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
                            .addComponent(KsR, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel13);
        jPanel13.setBounds(160, 40, 80, 105);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Kt"));

        jLabel26.setText("R:");

        jLabel27.setText("G:");

        jLabel28.setText("B:");

        KtR.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KtR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KtRStateChanged(evt);
            }
        });

        KtG.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KtG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KtGStateChanged(evt);
            }
        });

        KtB.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        KtB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                KtBStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(5, 5, 5)
                        .addComponent(KtB, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(KtR)
                            .addComponent(KtG))))
                .addGap(0, 0, 0))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(KtR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(KtG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KtB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel14);
        jPanel14.setBounds(240, 40, 80, 105);

        nPol.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), null, Float.valueOf(0.1f)));
        nPol.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nPolStateChanged(evt);
            }
        });
        jPanel10.add(nPol);
        nPol.setBounds(159, 11, 43, 20);

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
                .addContainerGap())
        );
        geralLayout.setVerticalGroup(
            geralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(geralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
            .addGroup(layout.createSequentialGroup()
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

    private void panelFrenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelFrenteMouseClicked
        Poligono pol = new Poligono();
        switch (cliqueAtual) {
            case 1:
                if (this.poligonos.size() > 0) {
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

    private void svrpXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_svrpXStateChanged
        vrpX = Double.valueOf(svrpX.getValue().toString());
        if (this.camera != null) {
            this.camera.setVx(vrpX);
            this.camera.GerarIntermediarios();
        }
        repaint();
//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


    }//GEN-LAST:event_svrpXStateChanged

    private void svrpYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_svrpYStateChanged
        vrpY = Double.valueOf(svrpY.getValue().toString());
        this.camera.setVy(vrpY);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);
    }//GEN-LAST:event_svrpYStateChanged

    private void svrpZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_svrpZStateChanged
        vrpZ = Double.valueOf(svrpZ.getValue().toString());
        this.camera.setVz(vrpZ);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);
    }//GEN-LAST:event_svrpZStateChanged

    private void spontoXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spontoXStateChanged
        pontoX = Double.valueOf(spontoX.getValue().toString());
        this.camera.setFPx(pontoX);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);
    }//GEN-LAST:event_spontoXStateChanged

    private void spontoYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spontoYStateChanged
        pontoY = Double.valueOf(spontoY.getValue().toString());
        this.camera.setFPy(pontoY);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);
    }//GEN-LAST:event_spontoYStateChanged

    private void spontoZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spontoZStateChanged
        pontoZ = Double.valueOf(spontoZ.getValue().toString());
        this.camera.setFPz(pontoZ);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);
    }//GEN-LAST:event_spontoZStateChanged

    private void sdistanciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sdistanciaStateChanged
        distancia = Double.valueOf(sdistancia.getValue().toString());
        this.camera.setDistancia(distancia);
        this.camera.GerarIntermediarios();
        repaint();

        jaSalvo = false;
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


//        paint(vrpX, vrpY, vrpZ, pontoX, pontoY, pontoZ, distancia, poli);
    }//GEN-LAST:event_sdistanciaStateChanged

    private void buttonTransladarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTransladarActionPerformed
        cliqueAtual = 2;
    }//GEN-LAST:event_buttonTransladarActionPerformed

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
//                 if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


        }
    }//GEN-LAST:event_panelFrenteMouseDragged

    private void buttonEsferaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEsferaActionPerformed

        cliqueAtual = 5;

    }//GEN-LAST:event_buttonEsferaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cliqueAtual = 6;
    }//GEN-LAST:event_jButton2ActionPerformed

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


//        jaSalvo = false;
//             if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


    }//GEN-LAST:event_panelLateralMouseClicked

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

//        jaSalvo = false;
//             if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }



    }//GEN-LAST:event_panelTopoMouseClicked

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
//        if (zbuffer == null) {
//            zbuffer = new ZBuffer(this.getZBuffer(), this.panelPerspectiva.
//                    getWidth(), this.panelPerspectiva.getHeight());
//            zbuffer.setVisible(true);
//            zbuffer.repaint();
//        } else {
//            setZBuffer();
//        }


    }//GEN-LAST:event_panelTopoMouseDragged

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
//                    selecionado.createSphere((int) RaioSpinner.getValue(),
//                            (int) BaseSpinner.getValue());
                }
                this.poligonosTransformados.set(PoligonosBox.
                        getSelectedIndex(), selecionado.Transformar(true));
//                Poligono p = poligonos.get(PoligonosBox.getSelectedIndex());
//                Poligono pt = poligonosTransformados.get(PoligonosBox.
//                        getSelectedIndex());
//                pt.setCorFace(p.getFaces().get(0).getCor());
            }
        }
        repaint();


        jaSalvo = false;



    }//GEN-LAST:event_BaseSpinnerStateChanged

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
//                    selecionado.createSphere((int) RaioSpinner.getValue(),
//                            (int) BaseSpinner.getValue());
                }
                this.poligonosTransformados.set(PoligonosBox.
                        getSelectedIndex(), selecionado.Transformar(true));
            }
        }
        repaint();


        jaSalvo = false;


    }//GEN-LAST:event_RaioSpinnerStateChanged

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
//                    selecionado.createSphere((int) RaioSpinner.getValue(),
//                            (int) BaseSpinner.getValue());
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

    private void WireframeRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_WireframeRadioButtonActionPerformed
    {//GEN-HEADEREND:event_WireframeRadioButtonActionPerformed
        this.visualizacaoAtual = 1;
        repaint();
    }//GEN-LAST:event_WireframeRadioButtonActionPerformed

    private void WireframeRadioButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_WireframeRadioButtonMouseClicked
    {//GEN-HEADEREND:event_WireframeRadioButtonMouseClicked
        this.visualizacaoAtual = 1;
        repaint();
    }//GEN-LAST:event_WireframeRadioButtonMouseClicked

    private void WireframeOcultacaoRadioButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_WireframeOcultacaoRadioButtonMouseClicked
    {//GEN-HEADEREND:event_WireframeOcultacaoRadioButtonMouseClicked
        this.visualizacaoAtual = 2;
        repaint();
    }//GEN-LAST:event_WireframeOcultacaoRadioButtonMouseClicked

    private void SombreamentoRadioButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_SombreamentoRadioButtonMouseClicked
    {//GEN-HEADEREND:event_SombreamentoRadioButtonMouseClicked
        this.visualizacaoAtual = 3;
        repaint();
    }//GEN-LAST:event_SombreamentoRadioButtonMouseClicked

    private void ArestaPanelColorChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ArestaPanelColorChooserMouseClicked
        MyColorChooser.createAndShowGUI(this, 1);

        jaSalvo = false;
    }//GEN-LAST:event_ArestaPanelColorChooserMouseClicked

    private void PoligonosBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_PoligonosBoxItemStateChanged
    {//GEN-HEADEREND:event_PoligonosBoxItemStateChanged
        if (this.poligonos.size() > 0) {
            this.ArestaPanelColorChooser.setBackground(this.poligonos.
                    get(this.PoligonosBox.getSelectedIndex()).getCor());
            this.FacePanelColorChooser.setBackground(this.poligonos.
                    get(this.PoligonosBox.getSelectedIndex()).getCorFace());
        }
    }//GEN-LAST:event_PoligonosBoxItemStateChanged

    private void FacePanelColorChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FacePanelColorChooserMouseClicked
        MyColorChooser.createAndShowGUI(this, 2);

        jaSalvo = false;
    }//GEN-LAST:event_FacePanelColorChooserMouseClicked

    private void mostrarPontosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mostrarPontosStateChanged
    }//GEN-LAST:event_mostrarPontosStateChanged

    private void mostrarPontosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarPontosActionPerformed

        if (mostrarPontos.isSelected()) {
            this.pontos = true;
        } else {
            this.pontos = false;
        }
        repaint();
    }//GEN-LAST:event_mostrarPontosActionPerformed

    private void panelFrenteMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelFrenteMouseReleased
    {//GEN-HEADEREND:event_panelFrenteMouseReleased
        Xanteior = 0;
        Yanteior = 0;
    }//GEN-LAST:event_panelFrenteMouseReleased

    private void panelLateralMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelLateralMouseReleased
    {//GEN-HEADEREND:event_panelLateralMouseReleased
        Xanteior = 0;
        Yanteior = 0;
    }//GEN-LAST:event_panelLateralMouseReleased

    private void panelTopoMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelTopoMouseReleased
    {//GEN-HEADEREND:event_panelTopoMouseReleased
        Xanteior = 0;
        Yanteior = 0;
    }//GEN-LAST:event_panelTopoMouseReleased

    private void WireframeOcultacaoRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_WireframeOcultacaoRadioButtonActionPerformed
    {//GEN-HEADEREND:event_WireframeOcultacaoRadioButtonActionPerformed
        this.visualizacaoAtual = 2;
        repaint();
    }//GEN-LAST:event_WireframeOcultacaoRadioButtonActionPerformed

    private void SombreamentoRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SombreamentoRadioButtonActionPerformed
    {//GEN-HEADEREND:event_SombreamentoRadioButtonActionPerformed
        this.visualizacaoAtual = 3;
        repaint();
    }//GEN-LAST:event_SombreamentoRadioButtonActionPerformed

    private void buttonEscalonarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonEscalonarActionPerformed
    {//GEN-HEADEREND:event_buttonEscalonarActionPerformed
        this.cliqueAtual = 4;
    }//GEN-LAST:event_buttonEscalonarActionPerformed

    private void buttonSelecionarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonSelecionarActionPerformed
    {//GEN-HEADEREND:event_buttonSelecionarActionPerformed
        this.cliqueAtual = 1;
    }//GEN-LAST:event_buttonSelecionarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.cliqueAtual = 8;
    }//GEN-LAST:event_jButton1ActionPerformed

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

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            this.abrir();
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

    private void jButtonFrenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFrenteActionPerformed
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
                    //System.exit(0);
                }
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Poligono selecionado = poligonos.get(PoligonosBox.
                getSelectedIndex());
        double val = Integer.parseInt(this.redimensionar.getText());
        selecionado.Escalar(val, val, val);
        this.poligonosTransformados.set(PoligonosBox.
                getSelectedIndex(), selecionado.Transformar(true));
        repaint();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void nPolStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nPolStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setN((float) nPol.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setN((float) nPol.getValue());

        repaint();
    }//GEN-LAST:event_nPolStateChanged

    private void KaRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KaRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKaR((float) KaR.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKaR((float) KaR.getValue());
        repaint();
    }//GEN-LAST:event_KaRStateChanged

    private void KaGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KaGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKaG((float) KaG.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKaG((float) KaG.getValue());
        repaint();
    }//GEN-LAST:event_KaGStateChanged

    private void KaBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KaBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKaB((float) KaB.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKaB((float) KaB.getValue());
        repaint();
    }//GEN-LAST:event_KaBStateChanged

    private void KdRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KdRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKdR((float) KdR.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKdR((float) KdR.getValue());
        repaint();
    }//GEN-LAST:event_KdRStateChanged

    private void KdGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KdGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKdG((float) KdG.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKdG((float) KdG.getValue());

        repaint();
    }//GEN-LAST:event_KdGStateChanged

    private void KdBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KdBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKdB((float) KdB.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKdB((float) KdB.getValue());
        repaint();
    }//GEN-LAST:event_KdBStateChanged

    private void KsRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KsRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKsR((float) KsR.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKsR((float) KsR.getValue());

        repaint();
    }//GEN-LAST:event_KsRStateChanged

    private void KsGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KsGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKsG((float) KsG.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKsG((float) KsG.getValue());

        repaint();
    }//GEN-LAST:event_KsGStateChanged

    private void KsBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KsBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKsB((float) KsB.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKsB((float) KsB.getValue());

        repaint();
    }//GEN-LAST:event_KsBStateChanged

    private void KtRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KtRStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKtR((float) KtR.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKtR((float) KtR.getValue());

        repaint();
    }//GEN-LAST:event_KtRStateChanged

    private void KtGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KtGStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKtG((float) KtG.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKtG((float) KtG.getValue());

        repaint();
    }//GEN-LAST:event_KtGStateChanged

    private void KtBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_KtBStateChanged
        poligonos.get(PoligonosBox.getSelectedIndex()).setKtB((float) KtB.getValue());
        poligonosTransformados.get(PoligonosBox.getSelectedIndex()).setKtB((float) KtB.getValue());

        repaint();
    }//GEN-LAST:event_KtBStateChanged

    private void IrStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IrStateChanged
        luzAmbiente.setIr((float) Ir.getValue());
        repaint();
    }//GEN-LAST:event_IrStateChanged

    private void IgStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IgStateChanged
        luzAmbiente.setIg((float) Ig.getValue());
        repaint();
    }//GEN-LAST:event_IgStateChanged

    private void IbStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IbStateChanged
        luzAmbiente.setIb((float) Ib.getValue());
        repaint();
    }//GEN-LAST:event_IbStateChanged

    private void FLXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLXStateChanged
        luzFundo.setLocal(new Ponto("", (int) FLX.getValue(), (int) FLY.getValue(), (int) FLZ.getValue()));
        repaint();
    }//GEN-LAST:event_FLXStateChanged

    private void FLYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLYStateChanged
        luzFundo.setLocal(new Ponto("", (int) FLX.getValue(), (int) FLY.getValue(), (int) FLZ.getValue()));
        repaint();
    }//GEN-LAST:event_FLYStateChanged

    private void FLZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLZStateChanged
        luzFundo.setLocal(new Ponto("", (int) FLX.getValue(), (int) FLY.getValue(), (int) FLZ.getValue()));
        repaint();
    }//GEN-LAST:event_FLZStateChanged

    private void FLRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLRStateChanged
        luzFundo.setIr((float) FLR.getValue());
        repaint();
    }//GEN-LAST:event_FLRStateChanged

    private void FLGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLGStateChanged
        luzFundo.setIg((float) FLG.getValue());
        repaint();
    }//GEN-LAST:event_FLGStateChanged

    private void FLBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FLBStateChanged
        luzFundo.setIb((float) FLB.getValue());
        repaint();
    }//GEN-LAST:event_FLBStateChanged

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        this.visualizacaoAtual = 4;
        repaint();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    public void setArestasCor(Color c) {
        if (this.poligonos.size() > 0) {
            this.poligonos.get(this.PoligonosBox.getSelectedIndex()).setCor(c);
            this.poligonosTransformados.
                    get(this.PoligonosBox.getSelectedIndex()).setCor(c);
            this.ArestaPanelColorChooser.setBackground(c);
            repaint();
        }
    }

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
        //scanlines
        if (!poligonos.isEmpty()) {
            for (Poligono p : poligonos) {
//            g2D.setColor(p.getCor());
                camera.GerarIntermediarios();
//                p.getMatrizPontos().print("P");
                Poligono Paux = camera.GerarPerspectiva(largura, altura, p);
//                Paux.getMatrizPontos().print("Paux");
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
//                zpol.getMatrizPontos().print("zpol");
                for (int i = 0; i < Paux.getFaces().size(); i++) {
                    Face f = Paux.getFaces().get(i);
                    Face f1 = ocultaFace.getFaces().get(i);
                    Face f2 = zpol.getFaces().get(i);
                    f1.gerarVetorPlano();
                    Vetor norma = f1.getVetorPlano();
                    norma.normalizar();
                    ArrayList<Ponto> pt = f.getPontos();
                    if (Vetor.produtoEscalar(camera.getVRPtoFP3(), norma) > 0) {
                        f2.gerarVetorPlano();
                        norma = f2.getVetorPlano();
                        Ponto pontoQualquer = f2.getPontos().get(0);
                        double d = -(norma.get(0) * pontoQualquer.getX())
                                - (norma.get(1) * pontoQualquer.getY())
                                - (norma.get(2) * pontoQualquer.getZ());
                        ArrayList<Aresta> arestas = f.getArestas();
                        //utilizo para encontrar os limites
                        double minimoY = arestas.get(0).getP1().getY();
                        double maximoY = arestas.get(0).getP1().getY();
                        //acho o limite das faces
                        for (Aresta a : arestas) {
                            Ponto p1 = a.getP1();
                            Ponto p2 = a.getP2();

                            if (minimoY > p1.getY() || minimoY > p2.getY()) {
                                if (minimoY > p1.getY()) {
                                    minimoY = p1.getY();
                                } else {
                                    minimoY = p2.getY();
                                }
                            }

                            if (maximoY < p1.getY() || maximoY < p2.getY()) {
                                if (maximoY < p1.getY()) {
                                    maximoY = p1.getY();
                                } else {
                                    maximoY = p2.getY();
                                }
                            }
                        }

                        ArrayList<Aresta> pintar = new ArrayList<>(arestas);
                        //verifico se aresta esta na vertical, se esta, já esta pintada, removo da lista de pintura.
                        for (int e = 0; e < pintar.size(); ++e) {
                            if (pintar.get(e).getP1().getY() == pintar.get(
                                    e).getP2().getY()) {
                                pintar.remove(e);
                            }
                        }
                        //faço a pintura linha por linha, calculando por meio do algoritmo de recorte se uma aresta esta contida na face ou não.
                        for (int y = (int) maximoY; (y > (int) minimoY) && (y
                                > 0); --y) {
                            double x1 = 0;
                            double x2 = 0;
                            boolean ok = true;
                            for (int e = 0; e < pintar.size(); ++e) {
                                Ponto p1 = pintar.get(e).getP1();

                                Ponto p2 = pintar.get(e).getP2();

                                double eqR = (y - p1.getY()) / (p2.getY() - p1.
                                        getY());

                                if ((eqR >= 0.0) && (eqR <= 1.0)) {
                                    if (ok) {
                                        x1 = eqR * (p2.getX() - p1.getX()) + p1.
                                                getX();
                                        ok = false;
                                    } else {
                                        x2 = eqR * (p2.getX() - p1.getX()) + p1.
                                                getX();
                                        break;
                                    }
                                }
                            }
//                             g.drawLine((int) (x1), y, (int) (x2), y);

                            int inicial = (int) x1;
                            int finall = (int) x2;

                            if (inicial > finall) {
                                int auxI = inicial;
                                inicial = finall;
                                finall = auxI;
                            }
                            double zA = -d - norma.get(0) * inicial - norma.get(
                                    1) * y;
//                          
                            if (zA < zBuffer[inicial + fator][y + fator]) {
                                zBuffer[inicial + fator][y + fator] = zA;
                                bufferImagem.setRGB(inicial + fator, y + fator,
                                        p.getCorFace().getRGB());
                            }
                            for (int j = inicial; j < finall; j++) {
                                zA = zA - norma.get(0) / norma.get(2);
//                                try {
                                if (zA < zBuffer[j + fator][y + fator]) {
                                    zBuffer[j + fator][y + fator] = zA;
                                    bufferImagem.setRGB(j + fator, y + fator, p.
                                            getCorFace().getRGB());
                                }
//                                } catch (ArrayIndexOutOfBoundsException a) {

//                                }
                            }



                        }
                    }
                }
            }
        }

        return cortaBuffer(bufferImagem, largura, altura, fator);
//        return bufferImagem;
    }

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
    private javax.swing.JSpinner KtB;
    private javax.swing.JSpinner KtG;
    private javax.swing.JSpinner KtR;
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
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
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
    private javax.swing.JPanel jPanel14;
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
    private javax.swing.JCheckBox mostrarPontos;
    private javax.swing.JSpinner nPol;
    public javax.swing.JPanel panelFrente;
    public javax.swing.JPanel panelFundo;
    public javax.swing.JPanel panelLateral;
    public javax.swing.JPanel panelPerspectiva;
    public javax.swing.JPanel panelTopo;
    private javax.swing.JTextField redimensionar;
    private javax.swing.JSpinner sdistancia;
    private static javax.swing.JSpinner spontoX;
    private static javax.swing.JSpinner spontoY;
    private static javax.swing.JSpinner spontoZ;
    private static javax.swing.JSpinner svrpX;
    private static javax.swing.JSpinner svrpY;
    private static javax.swing.JSpinner svrpZ;
    // End of variables declaration//GEN-END:variables

    public Iluminacao getLuzAmbiente() {
        return luzAmbiente;
    }

    public void setLuzAmbiente(Iluminacao luzAmbiente) {
        this.luzAmbiente = luzAmbiente;
    }

    public Iluminacao getLuzFundo() {
        return luzFundo;
    }

    public void setLuzFundo(Iluminacao luzFundo) {
        this.luzFundo = luzFundo;
    }
}
