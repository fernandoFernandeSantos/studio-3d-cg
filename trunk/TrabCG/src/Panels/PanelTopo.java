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
public class PanelTopo extends javax.swing.JPanel {

private Interface i;
    private Vetor observador;

    public PanelTopo(Interface _i) {
        initComponents();
        i = _i;
        observador = new Vetor(0, 1, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int viusalizacao = i.getVizualizacaoAtual();
        for (Poligono p : this.i.getPoligonosTransformados()) {
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
                            //                        fillPolygon(f.getPontos(), g2D, f.getCor());
                            fillPolygon(f.getPontos(), g2D, pol.getCorFace());
                            g2D.setColor(p.getCor());
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }
                    }
                    break;
            }
            
            if(i.isMostrarPontos()){
                paintPointNumbers(pol, g2D);
            }
        }
    drawAxis(g2D);
        
        
    }
    
    public void drawAxis(Graphics2D g2D){
        
//        Graphics2D g2D = (Graphics2D) this.getGraphics();
//        g2D.setRenderingHint(
//                RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
g2D.setColor(Color.black);
        g2D.drawLine(20,  20, 20,  80);
        g2D.fillOval(18,  80, 5, 5);
        g2D.drawString("Z", 18,  95);
        
        g2D.drawLine(20,  20, 80,  20);
        g2D.fillOval(80,  18, 5, 5);
        g2D.drawString("X", 88,  25);
        
    }

    public void drawline(Graphics2D g, Aresta a) {
        g.drawLine((int) Math.round(a.getP1().getX()), (int) Math.round(a.getP1().getZ()),
                (int) Math.round(a.getP2().getX()), (int) Math.round(a.getP2().getZ()));
    }

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

    //Metodo que preenche o polígono através de uma scanline que percorre a area de desenho e o poligono encontrando os pontos de intersecção
    public void fillPolygon(ArrayList<Ponto> p, Graphics2D g, Color cor) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(cor);
        double xl1, xl2, yl1, yl2;
        for (int i = 0; i < this.getHeight(); i++) { //percorro toda a altura
            ArrayList<Double> pontos = new ArrayList<>();
            Line2D.Double scanline = new Line2D.Double(-1000, i + 0.5, this.getWidth() + 1000, i + 0.5); //defino a linha da scanline
            for (int j = 0; j < p.size(); j++) { //faço a montagem dos pontos "X" e "Y" para formação da Scanline de comparação
                if (j == p.size() - 1) {
                    xl1 = p.get(j).getX();
                    yl1 = p.get(j).getZ();
                    xl2 = p.get(0).getX();
                    yl2 = p.get(0).getZ();
                } else {
                    xl1 = p.get(j).getX();
                    yl1 = p.get(j).getZ();
                    xl2 = p.get(j + 1).getX();
                    yl2 = p.get(j + 1).getZ();
                }
                Line2D.Double l = new Line2D.Double(xl1, yl1, xl2, yl2); //defino scanLine
                if (l.intersectsLine(scanline)) {//calculo interseçao pela equacao da reta
                    if (l.x1 == l.x2) {
                        pontos.add(l.ptLineDist(0, i + 0.5));
                    } else {
                        double m = (l.y1 - l.y2) / (l.x1 - l.x2);
                        pontos.add((((i + 0.5) - l.y1) / m) + l.x1);

                    }
                }
            }
            while (!pontos.isEmpty()) {//se ocorreu interseçao pinta
                // Recupera o valor de x1
                double auxx1;
                auxx1 = (pontos.get(0));
                pontos.remove(0);
                double auxx2 = pontos.get(0) + 1;
                pontos.remove(0);
                g.drawLine((int) auxx1, i, (int) auxx2, i);
            }
        }
        repaint();
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
