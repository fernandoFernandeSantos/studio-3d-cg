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
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Mateus
 */
public class PanelPerspectiva extends javax.swing.JPanel {

    private Interface inter;
    ArrayList<Matriz> matrizesCompostas;
    ArrayList<Poligono> poligonosOrganizados;
            ArrayList<Matriz> matrizesAux;
    public PanelPerspectiva(Interface _i) {
        initComponents();
        inter = _i;
        matrizesCompostas = new ArrayList<>();
        poligonosOrganizados = new ArrayList<>();
        matrizesAux = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Camera c = inter.getCamera();

        int visualizacaoAtual = this.inter.getVizualizacaoAtual();

        matrizesCompostas.clear();
        poligonosOrganizados.clear();
        matrizesAux.clear();

        for (Poligono p : inter.getPoligonos()) {
            poligonosOrganizados.add(c.GerarPerspectiva(this.getWidth(), this.
                    getHeight(), p));
            matrizesAux.add(c.getMatrizAux());
            matrizesCompostas.add(c.getComposta());
        }

        for (int cc = 0; cc < (poligonosOrganizados.size() - 1); cc++) {
            for (int d = 0; d < poligonosOrganizados.size() - cc - 1; d++) {

                Vetor a = new Vetor(poligonosOrganizados.get(d).getCentro());
                Vetor b = new Vetor(poligonosOrganizados.get(d + 1).getCentro());
                double modulo = c.getVRPtoFP().getModulo();

//                if (poligonosOrganizados.get(d).getCentro().getZ()
//                        < poligonosOrganizados.get(d + 1).getCentro().getZ()) {
                if (Vetor.subtracao(a, c.getVRP3()).getModulo() < Vetor.
                        subtracao(b, c.getVRP3()).getModulo()) /* For descending order use < */ {
                    Collections.swap(poligonosOrganizados, d, d + 1);
                    Collections.swap(matrizesAux, d, d + 1);
                    Collections.swap(matrizesCompostas, d, d + 1);
                }
            }
        }

        for (Poligono Paux : poligonosOrganizados) {
            Matriz aux = null;
            Poligono ocultaFace = null;
            switch (visualizacaoAtual) {
                case 1:
                    g2D.setColor(Paux.getCor());
                    for (Aresta a : Paux.getArestas()) {
                        this.drawline(g2D, a);
                    }
                    break;
                case 2:
                    g2D.setColor(Paux.getCor());
                    aux = matrizesAux.get(poligonosOrganizados.indexOf(Paux));
                    ocultaFace = Paux.copy();
                    ocultaFace.setPontos(aux);
                    for (int i = 0; i < Paux.getFaces().size(); i++) {
                        Face f = Paux.getFaces().get(i);
                        Face f1 = ocultaFace.getFaces().get(i);
                        f1.gerarVetorPlano();
                        Vetor norma = f1.getVetorPlano();
                        norma.normalizar();
                        if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
                            g2D.setColor(Paux.getCor());
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }

                    }
                    break;
                case 3:
                    g2D.setColor(Paux.getCor());
                    aux = matrizesAux.get(poligonosOrganizados.indexOf(Paux));
                    ocultaFace = Paux.copy();
                    ocultaFace.setPontos(aux);
                    for (int i = 0; i < Paux.getFaces().size(); i++) {
                        Face f = Paux.getFaces().get(i);
                        Face f1 = ocultaFace.getFaces().get(i);
                        f1.gerarVetorPlano();
                        Vetor norma = f1.getVetorPlano();
                        norma.normalizar();
                        if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
                            g.setColor(Paux.getCorFace());
                            preenchimento(f, g);
                            g2D.setColor(Paux.getCor());
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }
                    }


                    break;
                case 4:
                    g2D.setColor(Paux.getCor());
                    aux = matrizesAux.get(poligonosOrganizados.indexOf(Paux));//persp
                    ocultaFace = Paux.copy();
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
                        if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
                            Face f2 = zpol.getFaces().get(i);
                            for (int j = 0; j < f2.getPontos().size(); j++) {
                                double mediaX = 0;
                                double mediaY = 0;
                                double mediaZ = 0;
                                int counter = 1;
                                for (int k = 0; k < zpol.getFaces().size();
                                        k++) {
                                    for (int l = 0; l < zpol.getFaces().get(
                                            k).getPontos().size(); l++) {
                                        if (f2.getPontos().get(j).getNome()
                                                == zpol.getFaces().get(k).
                                                getPontos().get(l).getNome()) {
                                            zpol.getFaces().get(k).
                                                    gerarVetorPlano();
                                            zpol.getFaces().get(k).
                                                    getVetorPlano().
                                                    normalizar();
                                            mediaX += zpol.getFaces().get(k).
                                                    getVetorPlano().get(0);
                                            mediaY += zpol.getFaces().get(k).
                                                    getVetorPlano().get(1);
                                            mediaZ += zpol.getFaces().get(k).
                                                    getVetorPlano().get(2);
                                            counter++;
                                        }
                                    }
                                }
                                f2.getPontos().get(j).
                                        setnX(mediaX / counter);
                                f2.getPontos().get(j).
                                        setnY(mediaY / counter);
                                f2.getPontos().get(j).
                                        setnZ(mediaZ / counter);
                            }
                            sobreamento(Paux, f2, g);
                            g2D.setColor(Paux.getCor());
                            for (Aresta a : f.getArestas()) {
                                this.drawline(g2D, a);
                            }
                        }

                    }

                    break;
            }
            if (inter.isMostrarPontos()) {
                this.paintPointNumbers(Paux, g2D);
            }
        }

//        if (this.inter.getVizualizacaoAtual() == 1) {
//            for (Poligono p : poligonosOrganizados) {
//                g2D.setColor(p.getCor());
//                Poligono Paux = c.GerarPerspectiva(this.getWidth(), this.
//                        getHeight(), p);
//                for (Aresta a : Paux.getArestas()) {
//                    this.drawline(g2D, a);
//                }
//                if (inter.isMostrarPontos()) {
//                    this.paintPointNumbers(Paux, g2D);
//                }
//            }
//        }
//        if (this.inter.getVizualizacaoAtual() == 2) {
//            for (Poligono p : poligonosOrganizados) {
//                g2D.setColor(p.getCor());
//                Poligono Paux = c.GerarPerspectiva(this.getWidth(), this.
//                        getHeight(), p);
//                Matriz aux = c.getMatrizAux();
//                Poligono ocultaFace = Paux.copy();
//                ocultaFace.setPontos(aux);
//                for (int i = 0; i < Paux.getFaces().size(); i++) {
//                    Face f = Paux.getFaces().get(i);
//                    Face f1 = ocultaFace.getFaces().get(i);
//                    f1.gerarVetorPlano();
//                    Vetor norma = f1.getVetorPlano();
//                    norma.normalizar();
//                    if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
//                        g2D.setColor(p.getCor());
//                        for (Aresta a : f.getArestas()) {
//                            this.drawline(g2D, a);
//                        }
//                    }
//
//                }
//                if (inter.isMostrarPontos()) {
//                    this.paintPointNumbers(Paux, g2D);
//                }
//            }

//        }

//
//        if (this.inter.getVizualizacaoAtual() == 4) {
//            for (Poligono p : poligonosOrganizados) {
//                g2D.setColor(p.getCor());
//                Paux = c.GerarPerspectiva(this.getWidth(), this.getHeight(), p);
//                aux = c.getMatrizAux();
//                ocultaFace = Paux.copy();
//                ocultaFace.setPontos(aux);
//                Poligono zpol = Paux.copy();
//
//                Matriz zmaux = new Matriz();
//                for (int i = zpol.getPontos().size() - 1; i >= 0; i--) {
//                    zmaux.set(0, i, zpol.getPontos().get(i).getX());
//                    zmaux.set(1, i, zpol.getPontos().get(i).getY());
//                    zmaux.set(2, i, aux.get(2, i));
//                }
//                zpol.setPontos(zmaux);
//                for (int i = 0; i < Paux.getFaces().size(); i++) {
//                    Face f = Paux.getFaces().get(i);
//                    Face f1 = ocultaFace.getFaces().get(i);
//                    f1.gerarVetorPlano();
//                    Vetor norma = f1.getVetorPlano();
//                    norma.normalizar();
//                    if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
//                        Face f2 = zpol.getFaces().get(i);
//                        for (int j = 0; j < f2.getPontos().size(); j++) {
//                            double mediaX = 0;
//                            double mediaY = 0;
//                            double mediaZ = 0;
//                            int counter = 1;
//                            for (int k = 0; k < zpol.getFaces().size(); k++) {
//                                for (int l = 0; l < zpol.getFaces().get(k).getPontos().size(); l++) {
//                                    if (f2.getPontos().get(j).getNome() == zpol.getFaces().get(k).getPontos().get(l).getNome()) {
//                                        zpol.getFaces().get(k).gerarVetorPlano();
//                                        zpol.getFaces().get(k).getVetorPlano().normalizar();
//                                        mediaX += zpol.getFaces().get(k).getVetorPlano().get(0);
//                                        mediaY += zpol.getFaces().get(k).getVetorPlano().get(1);
//                                        mediaZ += zpol.getFaces().get(k).getVetorPlano().get(2);
//                                        counter++;
//                                    }
//                                }
//                            }
//                            f2.getPontos().get(j).setnX(mediaX / counter);
//                            f2.getPontos().get(j).setnY(mediaY / counter);
//                            f2.getPontos().get(j).setnZ(mediaZ / counter);
//                        }
//                        sobreamento(p, f2, g);
//                        g2D.setColor(p.getCor());
//                        for (Aresta a : f.getArestas()) {
//                            this.drawline(g2D, a);
//                        }
//                    }
//
//                }
//                if (inter.isMostrarPontos()) {
//                    this.paintPointNumbers(Paux, g2D);
//                }
//            }
//
//        }
//        if (this.inter.getVizualizacaoAtual() == 3) {
//            for (Poligono p : poligonosOrganizados) {
//                g2D.setColor(p.getCor());
//                Poligono Paux = c.GerarPerspectiva(this.getWidth(), this.getHeight(), p);
//                Matriz aux = c.getMatrizAux();
//                Poligono ocultaFace = Paux.copy();
//                ocultaFace.setPontos(aux);
//                for (int i = 0; i < Paux.getFaces().size(); i++) {
//                    Face f = Paux.getFaces().get(i);
//                    Face f1 = ocultaFace.getFaces().get(i);
//                    f1.gerarVetorPlano();
//                    Vetor norma = f1.getVetorPlano();
//                    norma.normalizar();
//                    if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
//                        g.setColor(p.getCorFace());
//                        preenchimento(f, g);
//                        g2D.setColor(p.getCor());
//                        for (Aresta a : f.getArestas()) {
//                            this.drawline(g2D, a);
//                        }
//                    }
//                }
//                if (inter.isMostrarPontos()) {
//                    this.paintPointNumbers(Paux, g2D);
//                }
//            }
//
//        }


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
        g.drawLine((int) Math.round(a.getP1().getX()), (int) Math.round(a.
                getP1().getY()),
                (int) Math.round(a.getP2().getX()), (int) Math.round(a.getP2().
                getY()));
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

    public void sobreamento(Poligono p, Face f, Graphics g) {
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
                if (u < 0.0D || u > 1.0D || y
                        == (int) (p1.getY() <= p2.getY() ? p1.getY() : p2.getY())) {
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
            double e2 = ((1.0D - u1) * (pb.getY() - pa.getY())) / (pb.getY()
                    - pa.getY());
            double e3 = (u2 * (pd.getY() - pc.getY())) / (pd.getY() - pc.getY());
            double e4 = ((1.0D - u2) * (pd.getY() - pc.getY())) / (pd.getY()
                    - pc.getY());
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
                Ponto ponto = new Ponto("", X, Y, Z);
                ponto.setnX(Nx);
                ponto.setnY(Ny);
                ponto.setnZ(Nz);
                phong(p, ponto);
                int red = (int) (ponto.getIr() * (double) p.getCorFace().
                        getRed());
                int green = (int) (ponto.getIg() * (double) p.getCorFace().
                        getGreen());
                int blue = (int) (ponto.getIb() * (double) p.getCorFace().
                        getBlue());
                int transparencia = (int) (255 * (1 - p.getKt()));

                Color cor = new Color(
                        red <= 255 ? red >= 0 ? red : 0 : 255, green
                        <= 255 ? green >= 0 ? green : 0 : 255, blue
                        <= 255 ? blue >= 0 ? blue : 0 : 255, transparencia);
                g.setColor(cor);
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

    public void phong(Poligono p, Ponto ponto) {
        ponto.setIr(getIr(p, ponto));
        ponto.setIg(getIg(p, ponto));
        ponto.setIb(getIb(p, ponto));
    }

    private double getIr(Poligono p, Ponto ponto) {
        
        Matriz src = matrizesCompostas.get(poligonosOrganizados.indexOf(p));
        
        Matriz local = new Matriz(4,1);
        local.set(0,0,inter.getLuzFundo().getLocal().getX());
        local.set(1,0,inter.getLuzFundo().getLocal().getY());
        local.set(2,0,inter.getLuzFundo().getLocal().getZ());
        local.set(3,0,1);
        
        local = Matriz.multiplicacao(src, local);
        
        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));
        
        double ambiente = ambiente(inter.getLuzAmbiente().getIr(), p.getKaR());
        
//        double difusa = difusa(inter.getLuzFundo().getIr(), p.getKdR(), ponto.
//                getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double difusa = difusa(inter.getLuzFundo().getIr(), p.getKdR(), ponto.
                getNormal(), plocal, ponto);
        
//        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
//                getIr(), p.getKsR(), p.getN(), inter.getLuzFundo().getLocal(),
//                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
//                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
                getIr(), p.getKsR(), p.getN(), plocal,
                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private double getIg(Poligono p, Ponto ponto) {
        
        Matriz src = matrizesCompostas.get(poligonosOrganizados.indexOf(p));
        
        Matriz local = new Matriz(4,1);
        local.set(0,0,inter.getLuzFundo().getLocal().getX());
        local.set(1,0,inter.getLuzFundo().getLocal().getY());
        local.set(2,0,inter.getLuzFundo().getLocal().getZ());
        local.set(3,0,1);
        
        local = Matriz.multiplicacao(src, local);
        
        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));
        
        double ambiente = ambiente(inter.getLuzAmbiente().getIg(), p.getKaG());
        
//        double difusa = difusa(inter.getLuzFundo().getIg(), p.getKdG(), ponto.
//                getNormal(), inter.getLuzFundo().getLocal(), ponto);
        double difusa = difusa(inter.getLuzFundo().getIg(), p.getKdG(), ponto.
                getNormal(), plocal, ponto);
        
//        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
//                getIg(), p.getKsG(), p.getN(), inter.getLuzFundo().getLocal(),
//                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
//                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
                getIg(), p.getKsG(), p.getN(), plocal,
                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        return ambiente + difusa + especular;
    }

    private double getIb(Poligono p, Ponto ponto) {
        
        Matriz src = matrizesCompostas.get(poligonosOrganizados.indexOf(p));
        
        Matriz local = new Matriz(4,1);
        local.set(0,0,inter.getLuzFundo().getLocal().getX());
        local.set(1,0,inter.getLuzFundo().getLocal().getY());
        local.set(2,0,inter.getLuzFundo().getLocal().getZ());
        local.set(3,0,1);
        
        local = Matriz.multiplicacao(src, local);
        
        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));
        
        double ambiente = ambiente(inter.getLuzAmbiente().getIb(), p.getKaB());
        
        double difusa = difusa(inter.getLuzFundo().getIb(), p.getKdB(), ponto.
                getNormal(), inter.getLuzFundo().getLocal(), ponto);
        
        double especular = difusa == 0.0D ? 0.0D : especular(inter.getLuzFundo().
                getIb(), p.getKsB(), p.getN(), inter.getLuzFundo().getLocal(),
                ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
                inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
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

    public static void preenchimento(Face f, Graphics g) {
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
                if (u < 0.0D || u > 1.0D || y
                        == (int) (p1.getY() <= p2.getY() ? p1.getY() : p2.getY())) {
                    continue;
                }
                if (first) {
                    x1 = u * (p2.getX() - p1.getX()) + p1.getX();
                    first = false;
                    continue;
                }
                x2 = u * (p2.getX() - p1.getX()) + p1.getX();
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
}
