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
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 *
 * @author Mateus
 */
public class PanelLateral extends javax.swing.JPanel {

    private Interface inter;
    private Vetor observador;

    public PanelLateral(Interface _i) {
        initComponents();
        inter = _i;
        observador = new Vetor(1, 0, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int viusalizacao = inter.getVizualizacaoAtual();
        for (Poligono p : this.inter.getPoligonosTransformados()) {
            Poligono pol = p.copy();
            pol.usarjpv();
//            pol.getMatrizPontos().print("pol lateral zoado");

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

                        if (Vetor.produtoEscalar(normal, observador) > 0) {
                            Ponto origem = new Ponto("", 100, 0, 0);
                            double Ir = getIr(p, f, origem);
                            double Ig = getIg(p, f, origem);
                            double Ib = getIb(p, f, origem);
                            int red = (int) (Ir * (double) p.getCorFace().getRed());
                            int green = (int) (Ig * (double) p.getCorFace().getGreen());
                            int blue = (int) (Ib * (double) p.getCorFace().getBlue());
                            Color cor = new Color(red <= 255 ? red >= 0 ? red : 0 : 255, green <= 255 ? green >= 0 ? green : 0 : 255, blue <= 255 ? blue >= 0 ? blue : 0 : 255);
                            g.setColor(cor);
                            preenchimento(f, g2D);
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }
                    }
                    break;
            }

            if (inter.isMostrarPontos()) {
                paintPointNumbers(pol, g2D);
            }
        }
        drawAxis(g2D);


    }

    public void drawAxis(Graphics2D g2D) {


        g2D.setColor(Color.black);
        g2D.drawLine(20, this.getHeight() - 20, 20, this.getHeight() - 80);
        g2D.fillOval(18, this.getHeight() - 84, 5, 5);
        g2D.drawString("Y", 18, this.getHeight() - 87);

        g2D.drawLine(20, this.getHeight() - 20, 80, this.getHeight() - 20);
        g2D.fillOval(80, this.getHeight() - 22, 5, 5);
        g2D.drawString("Z", 87, this.getHeight() - 17);

    }

    public void drawline(Graphics2D g, Aresta a) {
        g.drawLine((int) Math.round(a.getP1().getZ()), (int) Math.round(a.getP1().getY()),
                (int) Math.round(a.getP2().getZ()), (int) Math.round(a.getP2().getY()));
    }

    public void paintPointNumbers(Poligono p, Graphics2D g2D) {
        for (Ponto pT : p.getPontos()) {
            if (!pT.getNome().equals("centro")) {
                g2D.setColor(Color.yellow);
                g2D.fillOval((int) pT.getZ() - 1, (int) pT.getY() - 1, 3, 3);
                g2D.drawString(pT.getNome(), (int) pT.getZ() - 3, (int) pT.
                        getY() - 3);
            }
        }
    }

    private double getIr(Poligono p, Face f, Ponto origem) {
        f.gerarVetorPlano();
        Ponto normal = new Ponto("normal", f.getVetorPlano().get(0), f.getVetorPlano().get(1), f.getVetorPlano().get(2));
        double ambiente = ambiente(inter.getLuzAmbiente().getIr(), p.getKaR());
        double difusa = difusa(inter.getLuzFundo().getIr(), p.getKdR(), normal, inter.getLuzFundo().getLocal(), ((Aresta) f.getArestas().get(0)).getP1());
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().getIr(), p.getKsR(), p.getN(), inter.getLuzFundo().getLocal(), normal, origem, ((Aresta) f.getArestas().get(0)).getP1());
        return ambiente + difusa + especular;
    }

    private double getIg(Poligono p, Face f, Ponto origem) {
        f.gerarVetorPlano();
        Ponto normal = new Ponto("normal", f.getVetorPlano().get(0), f.getVetorPlano().get(1), f.getVetorPlano().get(2));
        double ambiente = ambiente(inter.getLuzAmbiente().getIg(), p.getKaG());
        double difusa = difusa(inter.getLuzFundo().getIg(), p.getKdG(), normal, inter.getLuzFundo().getLocal(), ((Aresta) f.getArestas().get(0)).getP1());
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().getIg(), p.getKsG(), p.getN(), inter.getLuzFundo().getLocal(), normal, origem, ((Aresta) f.getArestas().get(0)).getP1());
        return ambiente + difusa + especular;
    }

    private double getIb(Poligono p, Face f, Ponto origem) {
        f.gerarVetorPlano();
        Ponto normal = new Ponto("normal", f.getVetorPlano().get(0), f.getVetorPlano().get(1), f.getVetorPlano().get(2));
        double ambiente = ambiente(inter.getLuzAmbiente().getIb(), p.getKaB());
        double difusa = difusa(inter.getLuzFundo().getIb(), p.getKdB(), normal, inter.getLuzFundo().getLocal(), ((Aresta) f.getArestas().get(0)).getP1());
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().getIb(), p.getKsB(), p.getN(), inter.getLuzFundo().getLocal(), normal, origem, ((Aresta) f.getArestas().get(0)).getP1());
        return ambiente + difusa + especular;
    }

    private static double ambiente(double Ia, double Ka) {
        return Ia * Ka;
    }

    private static double difusa(double Il, double Kd, Ponto normal, Ponto L, Ponto pontoObservado) {
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

    private static double especular(double Il, double Ks, double expoenteN, Ponto L, Ponto N, Ponto VRP, Ponto A) {
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

    public static double norma(Ponto p) {
        return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ());
    }

    public static double escalar(Ponto p1, Ponto p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY() + p1.getZ() * p2.getZ();
    }

    public static void preenchimento(Face f, Graphics g) {
        java.util.List list = f.getArestas();
        double yinf = ((Aresta) list.get(0)).getP1().getZ();
        double ysup = ((Aresta) list.get(0)).getP1().getY();
        for (int i = 1; i < list.size(); i++) {
            if (((Aresta) list.get(i)).getP1().getY() < yinf) {
                yinf = ((Aresta) list.get(i)).getP1().getY();
            }
            if (((Aresta) list.get(i)).getP1().getY() > ysup) {
                ysup = ((Aresta) list.get(i)).getP1().getY();
            }
            if (((Aresta) list.get(i)).getP2().getY() < yinf) {
                yinf = ((Aresta) list.get(i)).getP2().getY();
            }
            if (((Aresta) list.get(i)).getP2().getY() > ysup) {
                ysup = ((Aresta) list.get(i)).getP2().getY();
            }
        }

        for (int y = (int) ysup; y > (int) yinf && y > 0; y--) {
            double x1 = 0.0D;
            double x2 = 0.0D;
            boolean first = true;
            for (int i = 0; i < list.size(); i++) {
                Ponto p1 = ((Aresta) list.get(i)).getP1();
                Ponto p2 = ((Aresta) list.get(i)).getP2();
                if ((int) p1.getY() == (int) p2.getY()) {
                    continue;
                }
                double u = ((double) y - p1.getY()) / (p2.getY() - p1.getY());
                if (u < 0.0D || u > 1.0D || y == (int) (p1.getY() <= p2.getY() ? p1.getY() : p2.getY())) {
                    continue;
                }
                if (first) {
                    x1 = u * (p2.getZ() - p1.getZ()) + p1.getZ();
                    first = false;
                    continue;
                }
                x2 = u * (p2.getZ() - p1.getZ()) + p1.getZ();
                break;
            }

            if (x1 == 0.0D && x2 == 0.0D) {
                continue;
            }
            if (x1 > x2) {
                double aux = x1;
                x1 = x2;
                x2 = aux;
            }
            g.drawLine((int) Math.floor(x1), y, (int) Math.ceil(x2), y);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder("Lateral"));

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
