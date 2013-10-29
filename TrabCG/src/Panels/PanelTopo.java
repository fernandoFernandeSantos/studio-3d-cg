/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Classes.Aresta;
import Classes.Face;
import Classes.Poligono;
import Classes.Ponto;
import Classes.Vetor;
import Interface.Interface;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Mateus
 */
public class PanelTopo extends javax.swing.JPanel {

    private Interface inter;
    private Vetor observador;
//possui uma referencia a interface principal

    public PanelTopo(Interface _i) {
        initComponents();
        inter = _i;
        observador = new Vetor(0, 1, 0);
    }
    /*
     * O método paintComponent pinta os objetos no panel, 
     * o metodo abaixo sobreescreveu o paintComponent do panel atual
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //identifica tipo de visualização | 1 -> Wireframe | 2 -> Wireframe com ocultação |
        //3 -> Sombreamento costante |4 - phong
        int viusalizacao = inter.getVizualizacaoAtual();
        //pega os poligonos transformados que foram criados na interface    
        ArrayList<Poligono> poligonosOrganizados = (ArrayList<Poligono>) this.inter.
                getPoligonosTransformados().clone();
        //ordena os poligonos
        for (int c = 0; c < (poligonosOrganizados.size() - 1); c++) {
            for (int d = 0; d < poligonosOrganizados.size() - c - 1; d++) {
                if (poligonosOrganizados.get(d).getCentro().getY()
                        > poligonosOrganizados.get(d + 1).getCentro().getY()) /* For descending order use < */ {
                    Collections.swap(poligonosOrganizados, d, d + 1);
                }
            }
        }

        for (Poligono p : poligonosOrganizados) {
            Poligono pol = p.copy();
            pol.usarjpv(this.getWidth(), this.getHeight());
            //quando vai desenhar escolhe o tipo de visualização
            //identifica tipo de visualização | 1 -> Wireframe | 2 -> Wireframe com ocultação | 
            //3 -> Sombreamento costante |4 - phong

            switch (viusalizacao) {
                case 1:
                    g2D.setColor(pol.getCor());

                    for (Aresta a : pol.getArestas()) {
                        this.drawline(g2D, a);
                    }
                    break;
                case 2:
                    for (Face f : pol.getFaces()) {
                        f.gerarVetorPlano();
                        Vetor normal = f.getVetorPlano();
                        g2D.setColor(pol.getCor());
                        //faz a verificação se é visível ou não
                        if (Vetor.produtoEscalar(normal, observador) > 0) {
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }
                    }
                    break;
                case 3:
                    for (Face f : pol.getFaces()) {
                        f.gerarVetorPlano();
                        Vetor normal = f.getVetorPlano();
                        if (Vetor.produtoEscalar(normal, observador) > 0) {
                            //faz o preenchimento se for visível
                            g.setColor(p.getCorFace());
                            preenchimento(f, g2D);
                            g2D.setColor(p.getCor());
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }
                    }
                    break;
                case 4:
                    for (Face f : pol.getFaces()) {
                        f.gerarVetorPlano();
                        Vetor normal = f.getVetorPlano();
                        //se visível obtem as variaveis necessárias para o phong
                        if (Vetor.produtoEscalar(normal, observador) > 0) {
                            Ponto origem = new Ponto("", 0, 100, 0);
                            //chama os métodos que fazem o calculo de ir, ig e ib
                            double Ir = getIr(p, f, origem);
                            double Ig = getIg(p, f, origem);
                            double Ib = getIb(p, f, origem);
                            //faz o calculo de cada cor de acordo com o ir, ig e ib já calculado
                            int red = (int) (Ir * (double) p.getCorFace().getRed());
                            int green = (int) (Ig * (double) p.getCorFace().getGreen());
                            int blue = (int) (Ib * (double) p.getCorFace().getBlue());
                            int transparencia = (int) (255 * (1 - p.getKt()));
//para a verificação das variáveis acima
                            int auxRed = 0, auxGreen = 0, auxBlue = 0;
                            if ((red <= 255) && (red >= 0)) {
                                auxRed = red;
                            } else {
                                if (red > 255) {
                                    auxRed = 255;
                                }
                                if (red < 0) {
                                    auxRed = 0;
                                }
                            }
                            //----------------------------
                            if ((green <= 255) && (green >= 0)) {
                                auxGreen = green;
                            } else {
                                if (green > 255) {
                                    auxGreen = 255;
                                }
                                if (green < 0) {
                                    auxGreen = 0;
                                }
                            }
                            //----------------------------
                            if ((blue <= 255) && (blue >= 0)) {
                                auxBlue = blue;
                            } else {
                                if (red > 255) {
                                    auxBlue = 255;
                                }
                                if (red < 0) {
                                    auxBlue = 0;
                                }
                            }
                            Color cor = new Color(auxRed, auxGreen, auxBlue, transparencia);
                            //seta a cor antes do preenchimento
                            g.setColor(cor);
                            preenchimento(f, g2D);
                            g2D.setColor(p.getCor());
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }
                    }
                    break;
            }
            //se o mostrar pontos está selecionado pinta os pontos do poligono
            if (inter.isMostrarPontos()) {
                paintPointNumbers(pol, g2D);
            }
        }
        drawAxis(g2D);


    }

    /**
     * Desenha os eixos na interface
     *
     * @param g2D o painel grafic onde vai desenhar
     */
    public void drawAxis(Graphics2D g2D) {
        g2D.setColor(Color.black);
        g2D.drawLine(20, 20, 20, 80);
        g2D.fillOval(18, 80, 5, 5);
        g2D.drawString("Z", 18, 95);

        g2D.drawLine(20, 20, 80, 20);
        g2D.fillOval(80, 18, 5, 5);
        g2D.drawString("X", 88, 25);

    }

    /**
     * Faz o desenho da linha que recebe por parâmetro
     *
     * @param g grapics onde ira desenhar
     * @param a ares que vai desenhar
     */
    public void drawline(Graphics2D g, Aresta a) {
        g.drawLine((int) Math.round(a.getP1().getX()), (int) Math.round(a.getP1().getZ()),
                (int) Math.round(a.getP2().getX()), (int) Math.round(a.getP2().getZ()));
    }

    /**
     * Pinta os pontos do poligono
     *
     * @param p um poligono
     * @param g2D Graphics2D onde vai desenhar
     */
    public void paintPointNumbers(Poligono p, Graphics2D g2D) {

        for (Ponto pT : p.getPontos()) {
            if (!pT.getNome().equals("centro")) {
                g2D.setColor(Color.yellow);
                g2D.fillOval((int) pT.getX() - 1, (int) pT.getZ() - 1, 3, 3);
                g2D.drawString(pT.getNome(), (int) pT.getX() - 3, (int) pT.
                        getZ() - 3);
            }
        }
    }

    /**
     * Faz o calcúlo de Ir
     *
     * @param p o poligono que a face está
     * @param f a face que
     * @param origem
     * @return retorna um double com o valor de ir
     */
    private double getIr(Poligono p, Face f, Ponto origem) {
        f.gerarVetorPlano();
        Ponto normal = new Ponto("normal", f.getVetorPlano().get(0), f.getVetorPlano().get(1), f.getVetorPlano().get(2));
        double ambiente = luzAmbiente(inter.getLuzAmbiente().getIr(), p.getKaR());
        double difusa = reflexaoDifusa(inter.getLuzFundo().getIr(), p.getKdR(), normal, inter.getLuzFundo().getLocal(), ((Aresta) f.getArestas().get(0)).getP1());

        double especular = 0;
        if (difusa == 0.0) {
            especular = 0.0;
        } else {
            especular = reflexaoEspecular(inter.getLuzFundo().getIr(), p.getKsR(), p.getN(),
                    inter.getLuzFundo().getLocal(), normal, origem, ((Aresta) f.getArestas().get(0)).getP1());
        }
        return ambiente + difusa + especular;
    }

    /**
     * Faz o calcúlo de Ig
     *
     * @param p o poligono que a face está
     * @param f a face que
     * @param origem
     * @return retorna um double com o valor de ig
     */
    private double getIg(Poligono p, Face f, Ponto origem) {
        f.gerarVetorPlano();
        Ponto normal = new Ponto("normal", f.getVetorPlano().get(0), f.getVetorPlano().get(1), f.getVetorPlano().get(2));
        double ambiente = luzAmbiente(inter.getLuzAmbiente().getIg(), p.getKaG());
        double difusa = reflexaoDifusa(inter.getLuzFundo().getIg(), p.getKdG(), normal, inter.getLuzFundo().getLocal(), ((Aresta) f.getArestas().get(0)).getP1());
        double especular = 0;
        if (difusa == 0.0) {
            especular = 0.0;
        } else {
            especular = reflexaoEspecular(inter.getLuzFundo().
                    getIg(), p.getKsG(), p.getN(), inter.getLuzFundo().getLocal(),
                    normal, origem, ((Aresta) f.getArestas().get(0)).getP1());
        }
        return ambiente + difusa + especular;
    }

    /**
     * Faz o calcúlo de Ib
     *
     * @param p o poligono que a face está
     * @param f a face que
     * @param origem
     * @return retorna um double com o valor de ib
     */
    private double getIb(Poligono p, Face f, Ponto origem) {
        f.gerarVetorPlano();
        Ponto normal = new Ponto("normal", f.getVetorPlano().get(0), f.getVetorPlano().get(1), f.getVetorPlano().get(2));
        double ambiente = luzAmbiente(inter.getLuzAmbiente().getIb(), p.getKaB());
        double difusa = reflexaoDifusa(inter.getLuzFundo().getIb(), p.getKdB(), normal, inter.getLuzFundo().getLocal(), ((Aresta) f.getArestas().get(0)).getP1());
        double especular;
        if (difusa == 0.0) {
            especular = 0.0;
        } else {
            especular = reflexaoEspecular(inter.getLuzFundo().
                    getIb(), p.getKsB(), p.getN(), inter.getLuzFundo().getLocal(),
                    normal, origem, ((Aresta) f.getArestas().get(0)).getP1());
        }
        return ambiente + difusa + especular;
    }

    /**
     * Calculo da luz ambiente de acordo com o Ia e Ka passados
     *
     * @param Ia intencidade da luz ambiente
     * @param Kaquantidade de luz ambiente
     * @return double
     */
    private static double luzAmbiente(double Ia, double Ka) {
        return Ia * Ka;
    }

    /**
     * Cálculo da reflexão difusa
     *
     * @param Il
     * @param Kd
     * @param normal vetor normal
     * @param L vetor L
     * @param pontoObservado
     * @return double
     */
    private static double reflexaoDifusa(double Il, double Kd, Ponto normal, Ponto L, Ponto pontoObservado) {
        Ponto l = new Ponto("", pontoObservado.getX() - L.getX(), pontoObservado.getY() - L.getY(), pontoObservado.getZ() - L.getZ());
        double normaL = norma(l);
        l.setX(l.getX() / normaL);
        l.setY(l.getY() / normaL);
        l.setZ(l.getZ() / normaL);
        double normaNormal = norma(normal);
        Ponto n = new Ponto("", normal.getX() / normaNormal, normal.getY() / normaNormal, normal.getZ() / normaNormal);
        double escalarNL = escalar(n, l);
        if (escalarNL > 0.0D) {
            return Il * Kd * escalarNL;
        } else {
            return 0.0D;
        }
    }

    /**
     * Calculo da reflexão expecular
     *
     * @param Il
     * @param Ks
     * @param expoenteN
     * @param L Luz incidente
     * @param N normal a superficie
     * @param VRP
     * @param A
     * @return valor da reflexão especular
     */
    private static double reflexaoEspecular(double Il, double Ks, double expoenteN, Ponto L, Ponto N, Ponto VRP, Ponto A) {
        Ponto l = new Ponto("", A.getX() - L.getX(), A.getY() - L.getY(), A.getZ() - L.getZ());
        double normaL = norma(l);
        l.setX(l.getX() / normaL);
        l.setY(l.getY() / normaL);
        l.setZ(l.getZ() / normaL);
        double normaN = norma(N);
        Ponto n = new Ponto("", N.getX() / normaN, N.getY() / normaN, N.getZ() / normaN);
        Ponto r = new Ponto();
        double DoisLN = 2D * escalar(l, n);
        r.setX(l.getX() - DoisLN * n.getX());
        r.setY(l.getY() - DoisLN * n.getY());
        r.setZ(l.getZ() - DoisLN * n.getZ());
        Ponto s = new Ponto("", VRP.getX() - A.getX(), VRP.getY() - A.getY(), VRP.getZ() - A.getZ());
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

    /**
     * Norma de um ponto
     *
     * @param p
     * @return double
     */
    public static double norma(Ponto p) {
        return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ());
    }

    /**
     * Produto escalar de dois pontos
     *
     * @param p1
     * @param p2
     * @return o resultado
     */
    public static double escalar(Ponto p1, Ponto p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY() + p1.getZ() * p2.getZ();
    }
/**
 * Faz o preenchimento da face passada
 * @param f
 * @param g 
 */
    public static void preenchimento(Face f, Graphics g) {
        ArrayList<Aresta> arestaFaceAtual = f.getArestas();
        double yInferior = (arestaFaceAtual.get(0)).getP1().getX();
        double ySuperior = (arestaFaceAtual.get(0)).getP1().getZ();
        for (Aresta a : arestaFaceAtual) {
            if (a.getP1().getZ() < yInferior) {
                yInferior = a.getP1().getZ();
            }
            if (a.getP1().getZ() > ySuperior) {
                ySuperior = a.getP1().getZ();
            }
            if (a.getP2().getZ() < yInferior) {
                yInferior = a.getP2().getZ();
            }
            if (a.getP2().getZ() > ySuperior) {
                ySuperior = a.getP2().getZ();
            }
        }

        for (int it = (int) ySuperior; it > (int) yInferior && it > 0; it--) {
            double xFirst = 0.0;
            double xSecond = 0.0;
            boolean first = true;
            for (Aresta a : arestaFaceAtual) {
                Ponto p1 = a.getP1();
                Ponto p2 = a.getP2();
                if ((int) p1.getZ() == (int) p2.getZ()) {
                    continue;
                }
                double parametroU = ((double) it - p1.getZ()) / (p2.getZ() - p1.getZ());
                int auxCompara;
                if (p1.getZ() <= p2.getZ()) {
                    auxCompara = (int) p1.getZ();
                } else {
                    auxCompara = (int) p2.getZ();
                }
                if (parametroU < 0.0D || parametroU > 1.0D || it == auxCompara) {
                    continue;
                }
                if (first) {
                    xFirst = parametroU * (p2.getX() - p1.getX()) + p1.getX();
                    first = false;
                    continue;
                }
                xSecond = parametroU * (p2.getX() - p1.getX()) + p1.getX();
                break;
            }

            if (xFirst == 0.0 && xSecond == 0.0) {
                continue;
            }
            if (xFirst > xSecond) {
                double aux = xFirst;
                xFirst = xSecond;
                xSecond = aux;
            }
            g.drawLine((int) Math.floor(xFirst), it, (int) Math.ceil(xSecond), it);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder("Topo"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
