/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Classes.Aresta;
import Classes.Camera;
import Classes.Face;
import Classes.Matriz;
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
public class PanelPerspectiva extends javax.swing.JPanel {

    private Interface inter;

    public PanelPerspectiva(Interface _i) {
        initComponents();
        inter = _i;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Camera c = inter.getCamera();


        if (this.inter.getVizualizacaoAtual() == 1) {
            for (Poligono p : this.inter.getPoligonos()) {
                g2D.setColor(p.getCor());
                Poligono Paux = c.GerarPerspectiva(this.getWidth(), this.
                        getHeight(), p);
                for (Aresta a : Paux.getArestas()) {
                    this.drawline(g2D, a);
                }
                if (inter.isMostrarPontos()) {
                    this.paintPointNumbers(Paux, g2D);
                }
            }
        }
        if (this.inter.getVizualizacaoAtual() == 2) {
            for (Poligono p : inter.getPoligonos()) {
                g2D.setColor(p.getCor());
                Poligono Paux = c.GerarPerspectiva(this.getWidth(), this.
                        getHeight(), p);
                Matriz aux = c.getMatrizAux();
                Poligono ocultaFace = Paux.copy();
                ocultaFace.setPontos(aux);
                for (int i = 0; i < Paux.getFaces().size(); i++) {
                    Face f = Paux.getFaces().get(i);
                    Face f1 = ocultaFace.getFaces().get(i);
                    f1.gerarVetorPlano();
                    Vetor norma = f1.getVetorPlano();
                    norma.normalizar();
                    if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
//                    if (Vetor.produtoEscalar(normal, c.getVRPtoFP()) > 0) {
//                        fillPolygon(f.getPontos(), g2D, f.getCor());
                        g2D.setColor(p.getCor());
                        for (Aresta a : f.getArestas()) {
                            this.drawline(g2D, a);
                        }
                    }

                }
                if (inter.isMostrarPontos()) {
                    this.paintPointNumbers(Paux, g2D);
                }
            }

        }
        if (this.inter.getVizualizacaoAtual() == 3) {
            for (Poligono p : inter.getPoligonos()) {
                g2D.setColor(p.getCor());
                Poligono Paux = c.GerarPerspectiva(this.getWidth(), this.
                        getHeight(), p);
                Matriz aux = c.getMatrizAux();
                Poligono ocultaFace = Paux.copy();
                ocultaFace.setPontos(aux);
                for (int i = 0; i < Paux.getFaces().size(); i++) {
                    Face f = Paux.getFaces().get(i);
                    Face f1 = ocultaFace.getFaces().get(i);
                    f1.gerarVetorPlano();
                    Vetor norma = f1.getVetorPlano();
                    norma.normalizar();
                    if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
//                        fillPolygon(f, g2D, p.getCorFace(),p);
                        PhongShading(p, f, g);
//                        preencherFrente(f, g2D,p.getCorFace());
//                    if (Vetor.produtoEscalar(normal, c.getVRPtoFP()) > 0) {
//                        fillPolygon(f.getPontos(), g2D, f.getCor());
                        g2D.setColor(p.getCor());
                        for (Aresta a : f.getArestas()) {
                            this.drawline(g2D, a);
                        }
                    }

                }
                if (inter.isMostrarPontos()) {
                    this.paintPointNumbers(Paux, g2D);
                }
            }

        }


    }

    public void paintPointNumbers(Poligono p, Graphics2D g2D) {
        for (Ponto pT : p.getPontos()) {
            if (!pT.getNome().equals("centro")) {
                g2D.setColor(Color.yellow);
                g2D.
                        fillOval((int) pT.getX() - 1, (int) pT.getY()
                        - 1, 3, 3);
                g2D.drawString(pT.getNome(), (int) pT.getX() - 3,
                        (int) pT.getY() - 3);
            }
        }

    }

    public void drawline(Graphics2D g, Aresta a) {
        g.drawLine((int) Math.round(a.getP1().getX()), (int) Math.round(a.getP1().getY()),
                (int) Math.round(a.getP2().getX()), (int) Math.round(a.getP2().getY()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder("Perspectiva"));

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

    public void fillPolygon(Face face, Graphics2D g, Color c, Poligono p) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(c);
        ArrayList<Aresta> arestas = face.getArestas();
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
        for (int i = 0; i < pintar.size(); ++i) {
            if (pintar.get(i).getP1().getY() == pintar.get(i).getP2().getY()) {
                pintar.remove(i);
            }
        }
        //faço a pintura linha por linha, calculando por meio do algoritmo de recorte se uma aresta esta contida na face ou não.
        for (int y = (int) maximoY; (y > (int) minimoY) && (y > 0); --y) {
            double x1 = 0;
            double x2 = 0;
            boolean ok = true;
            for (int i = 0; i < pintar.size(); ++i) {
                Ponto p1 = pintar.get(i).getP1();

                Ponto p2 = pintar.get(i).getP2();

                double eqR = (y - p1.getY()) / (p2.getY() - p1.getY());

                if ((eqR >= 0.0) && (eqR <= 1.0)) {
                    if (ok) {
                        x1 = eqR * (p2.getX() - p1.getX()) + p1.getX();
                        ok = false;
                    } else {
                        x2 = eqR * (p2.getX() - p1.getX()) + p1.getX();
                        break;
                    }
                }
            }

            g.drawLine((int) (x1), y, (int) (x2), y);
        }
    }
//   public void fillPolygon(Face face, Graphics2D g, Color c, Poligono p) {
//
//
////        ArrayList<Ponto> vetoresNormais = new ArrayList<>();
////        for (int j = 0; j < face.getPontos().size(); j++) {
////            for (int k = 0; k < p.getFaces().size(); k++) {
////                for (int l = 0; l < p.getFaces().get(k).getPontos().size(); l++) {
////                    if (face.getPontos().get(j).getNome() == p.getFaces().get(k).getPontos().get(l).getNome()) {
////                        Ponto pt = face.getPontos().get(j).copy();
////                        p.getFaces().get(k).gerarVetorPlano();
////                        p.getFaces().get(k).getVetorPlano().normalizar();
////                        pt.setNorma(p.getFaces().get(k).getVetorPlano());
////                        vetoresNormais.add(pt);
////                    }
////                }
////            }
////        }
//
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setColor(c);
//        ArrayList<Aresta> arestas = face.getArestas();
//        //utilizo para encontrar os limites
//        double minimoY = arestas.get(0).getP1().getY();
//        double maximoY = arestas.get(0).getP1().getY();
//        //acho o limite das faces
//        for (Aresta a : arestas) {
//            Ponto p1 = a.getP1();
//            Ponto p2 = a.getP2();
//
//            if (minimoY > p1.getY() || minimoY > p2.getY()) {
//                if (minimoY > p1.getY()) {
//                    minimoY = p1.getY();
//                } else {
//                    xl1 = p.get(j).getX();
//                    yl1 = p.get(j).getY();
//                    xl2 = p.get(j + 1).getX();
//                    yl2 = p.get(j + 1).getY();
//                }
//                Line2D.Double l = new Line2D.Double(xl1, yl1, xl2, yl2);//defino scanLine
//                if (l.intersectsLine(scanline)) {//calculo interseçao pela equacao da reta
//                    if (l.x1 == l.x2) {
//                        pontos.add(l.ptLineDist(0, iterator + 0.5));
//                    } else {
//                        double m = (l.y1 - l.y2) / (l.x1 - l.x2);
//                        pontos.add((((iterator + 0.5) - l.y1) / m) + l.x1);
//
//                    }
//                }
//            }
//        }
//
//        ArrayList<Aresta> pintar = new ArrayList<>(arestas);
//        //verifico se aresta esta na vertical, se esta, já esta pintada, removo da lista de pintura.
//        for (int i = 0; i < pintar.size(); ++i) {
//            if (pintar.get(i).getP1().getY() == pintar.get(i).getP2().getY()) {
//                pintar.remove(i);
//            }
//        }
//
//        //faço a pintura linha por linha, calculando por meio do algoritmo de recorte se uma aresta esta contida na face ou não.
//        for (int y = (int) maximoY; (y > (int) minimoY) && (y > 0); --y) {
//            double x1 = 0;
//            double x2 = 0;
//            boolean ok = true;
//            for (int i = 0; i < pintar.size(); ++i) {
//                Ponto p1 = pintar.get(i).getP1();
//
//                Ponto p2 = pintar.get(i).getP2();
//
//                double eqR = (y - p1.getY()) / (p2.getY() - p1.getY());
//
//                if ((eqR >= 0.0) && (eqR <= 1.0)) {
//                    if (ok) {
//                        x1 = eqR * (p2.getX() - p1.getX()) + p1.getX();
//                        ok = false;
//                    } else {
//                        x2 = eqR * (p2.getX() - p1.getX()) + p1.getX();
//                        break;
//                    }
//                }
//            }
////            for (int j = 0; j < pintar.size()-1; j++) {
////                // PASSO B - CALCULO DA VARIACAO DAS ARESTAS
////                Ponto pt1 = null;
////                Ponto pt2 = null;
////                double deltaX = pintar.get(j)..getX() - p1.getX();
////                double deltaY = p2.getY() - p1.getY();
////                double deltaZ = p2.getZ() - p1.getZ();
////                for (int j = 0; j < vetoresNormais.size(); j++) {
////                    if (p1.getNome() == vetoresNormais.get(j).getNome()) {
////                        pt1 = vetoresNormais.get(j).copy();
////                    }
////                    if (p2.getNome() == vetoresNormais.get(j).getNome()) {
////                        pt2 = vetoresNormais.get(j).copy();
////                    }
////                }
////                double deltaMediaX = pt2.getNorma().get(0) - pt1.getNorma().get(0);
////                double deltaMediaY = pt2.getNorma().get(1) - pt1.getNorma().get(1);
////                double deltaMediaZ = pt2.getNorma().get(2) - pt1.getNorma().get(2);
////
////                //PASSO C - CALCULO DA TAXA DE INTERPOLACAO DAS LINHAS
////                double taxaX = deltaX / deltaY;
////                double taxaZ = deltaZ / deltaY;
////                double taxaMediaX = deltaMediaX / deltaY;
////                double taxaMediaY = deltaMediaY / deltaY;
////                double taxaMediaZ = deltaMediaZ / deltaY;
////
////                //PASSO F 
////                double x = pt1.getX() + (y - pt1.getY()) * taxaX;
////                double z = pt1.getZ() + (y - pt1.getY()) * taxaZ;
////                double nx = pt1.getNorma().get(0) + (y - pt1.getY()) * taxaMediaX;
////                double ny = pt1.getNorma().get(1) + (y - pt1.getY()) * taxaMediaY;
////                double nz = pt1.getNorma().get(2) + (y - pt1.getY()) * taxaMediaZ;
////                if (x1 > x2) {
////                    double aux = x1;
////                    x1 = x2;
////                    x2 = x1;
////                }
////            }
//            g.drawLine((int) (x1), y, (int) (x2), y);
//
//        }
//    }

    public void PhongShading(Poligono p, Face f, Graphics g) {
//        System.out.println("entrou");
        java.util.List list = f.getArestas();
        double yinf = ((Aresta) list.get(0)).getP1().getX();
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
//            System.out.println("for 1");
            double x1 = 0.0D;
            double x2 = 0.0D;
            double u1 = 0.0D;
            double u2 = 0.0D;
            boolean first = true;
            Ponto pa = null;
            Ponto pb = null;
            Ponto pc = null;
            Ponto pd = null;
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

            if (x1 == 0.0D && x2 == 0.0D) {
                continue;
            }
            if (x1 > x2) {
                double aux = x1;
                x1 = x2;
                x2 = aux;
                Ponto paux = pa;
                pa = pc;
                pc = paux;
                paux = pb;
                pb = pd;
                pd = paux;
                aux = u1;
                u1 = u2;
                u2 = aux;
            }
            double e1 = (u1 * (pb.getY() - pa.getY())) / (pb.getY() - pa.getY());
            double e2 = ((1.0D - u1) * (pb.getY() - pa.getY())) / (pb.getY() - pa.getY());
            double e3 = (u2 * (pd.getY() - pc.getY())) / (pd.getY() - pc.getY());
            double e4 = ((1.0D - u2) * (pd.getY() - pc.getY())) / (pd.getY() - pc.getY());
            double Nxi = pb.getnX() * e1 + pa.getnX() * e2;
            double Nyi = pb.getnY() * e1 + pa.getnY() * e2;
            double Nzi = pb.getnZ() * e1 + pa.getnZ() * e2;
            double Nxf = pd.getnX() * e3 + pc.getnX() * e4;
            double Nyf = pd.getnY() * e3 + pc.getnY() * e4;
            double Nzf = pd.getnZ() * e3 + pc.getnZ() * e4;
            double Zi = pb.getZ() * e1 + pa.getZ() * e2;
            double Zf = pd.getZ() * e3 + pc.getZ() * e4;
            double Yi = pb.getY() * e1 + pa.getY() * e2;
            double Yf = pd.getY() * e3 + pc.getY() * e4;
            double Xi = pb.getX() * e1 + pa.getX() * e2;
            double Xf = pd.getX() * e3 + pc.getX() * e4;
            double deltaX = x2 - x1;
            double deltaNx = (Nxf - Nxi) / deltaX;
            double deltaNy = (Nyf - Nyi) / deltaX;
            double deltaNz = (Nzf - Nzi) / deltaX;
            double deltaZM = (Zf - Zi) / deltaX;
            double deltaXM = (Xf - Xi) / deltaX;
            double deltaYM = (Yf - Yi) / deltaX;
            double Nx = Nxi;
            double Ny = Nyi;
            double Nz = Nzi;
            double x = x1;
            double Z = Zi;
            double X = Xi;
            for (double Y = Yi; x < x2; Y += deltaYM) {
//                System.out.println(" for 2");
                Ponto ponto = new Ponto("", X, Y, Z);
                ponto.setnX(Nx);
                ponto.setnY(Ny);
                ponto.setnZ(Nz);
                IntensidadePhong(p, ponto);
//                int red = (int)(ponto.getIr()* (double)p.getCorFace().getRed());//(int) (0.25 * (double) p.getCorFace().getRed());//(int)(ponto.Ir * (double)p.getCor().getRed());
//                int green = (int)(ponto.getIg() * (double)p.getCorFace().getGreen());// (int) (0.25 * (double) p.getCorFace().getGreen());//(int)(ponto.Ig * (double)p.getCor().getGreen());
//                int blue = (int)(ponto.getIb() * (double)p.getCorFace().getBlue());//(int) (0.25 * (double) p.getCorFace().getBlue());//(int)(ponto.Ib * (double)p.getCor().getBlue());
                int red = (int) (0.25 * (double) p.getCorFace().getRed());//(int) (0.25 * (double) p.getCorFace().getRed());//(int)(ponto.Ir * (double)p.getCor().getRed());
                int green = (int) (0.25 * (double) p.getCorFace().getGreen());// (int) (0.25 * (double) p.getCorFace().getGreen());//(int)(ponto.Ig * (double)p.getCor().getGreen());
                int blue = (int) (0.25 * (double) p.getCorFace().getBlue());//(int) (0.25 * (double) p.getCorFace().getBlue());//(int)(ponto.Ib * (double)p.getCor().getBlue());
                Color cor = new Color(red <= 255 ? red >= 0 ? red : 0 : 255, green <= 255 ? green >= 0 ? green : 0 : 255, blue <= 255 ? blue >= 0 ? blue : 0 : 255);
                g.setColor(cor);
//                System.out.println("Color = "+cor.getRed()+" "+cor.getGreen()+" "+cor.getBlue());

                g.drawRect((int) x, y, 1, 1);
                Nx += deltaNx;
                Ny += deltaNy;
                Nz += deltaNz;
                x++;
                Z += deltaZM;
                X += deltaXM;
            }

        }

    }

    public void IntensidadePhong(Poligono p, Ponto ponto) {
        ponto.setIr(getIrGouraud(p, ponto));
        ponto.setIg(getIgGouraud(p, ponto));
        ponto.setIb(getIbGouraud(p, ponto));
    }

    private double getIrGouraud(Poligono p, Ponto ponto) {
        double ambiente = ambiente(inter.getLuzAmbiente().getIr(), p.getKaR());
        double difusa = difusa(inter.getLuzFundo().getIr(), p.getKdR(), ponto.getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().getIr(), p.getKsR(), p.getN(), inter.getLuzFundo().getLocal(), ponto.getNormal(), new Ponto("", inter.getCamera().getVx(), inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private double getIgGouraud(Poligono p, Ponto ponto) {
        double ambiente = ambiente(inter.getLuzAmbiente().getIg(), p.getKaG());
        double difusa = difusa(inter.getLuzFundo().getIg(), p.getKdG(), ponto.getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().getIg(), p.getKsG(), p.getN(), inter.getLuzFundo().getLocal(), ponto.getNormal(), new Ponto("", inter.getCamera().getVx(), inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private double getIbGouraud(Poligono p, Ponto ponto) {
        double ambiente = ambiente(inter.getLuzAmbiente().getIb(), p.getKaB());
        double difusa = difusa(inter.getLuzFundo().getIb(), p.getKdB(), ponto.getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().getIb(), p.getKsB(), p.getN(), inter.getLuzFundo().getLocal(), ponto.getNormal(), new Ponto("", inter.getCamera().getVx(), inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
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
}
