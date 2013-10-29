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
    ArrayList<Poligono> poligonosOrganizados;
    ArrayList<Matriz> matrizesAux;
    ArrayList<Poligono> poligonosOriginais;

    /**
     * Constroi o painel da perspectiva
     *
     * @param _i
     */
    public PanelPerspectiva(Interface _i) {
        initComponents();
        inter = _i;
        poligonosOrganizados = new ArrayList<>();
        matrizesAux = new ArrayList<>();
        poligonosOriginais = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //obtem a camera selecionada na interface
        Camera c = inter.getCamera();

        int visualizacaoAtual = this.inter.getVizualizacaoAtual();

        //limpa lixo de operações anteriores
        poligonosOrganizados.clear();
        matrizesAux.clear();
        poligonosOriginais.clear();
        
        for (Poligono p : inter.getPoligonos()) {
            //gera a perspectiva dos poligonos que estão na camera atual da interface
            poligonosOriginais.add(p.TransformarPerspectiva());
            poligonosOrganizados.add(c.GerarPerspectiva(this.getWidth(), this.
                    getHeight(), p));
            matrizesAux.add(c.getMatrizAux());
        }
        //ordena os poligonos de acordo com o a distancia do vrp
        for (int cc = 0; cc < (poligonosOrganizados.size() - 1); cc++) {
            for (int d = 0; d < poligonosOrganizados.size() - cc - 1; d++) {

                Vetor a = new Vetor(poligonosOrganizados.get(d).getCentro());
                Vetor b = new Vetor(poligonosOrganizados.get(d + 1).getCentro());
                double modulo = c.getVRPtoFP().getModulo();

                if (Vetor.subtracao(a, c.getVRP3()).getModulo() < Vetor.
                        subtracao(b, c.getVRP3()).getModulo()) /* For descending order use < */ {
                    Collections.swap(poligonosOrganizados, d, d + 1);
                    Collections.swap(matrizesAux, d, d + 1);
                    Collections.swap(poligonosOriginais,d,d+1);
                }
            }
        }

        for (Poligono Paux : poligonosOrganizados) {
            Matriz aux = null;
            Poligono ocultaFace = null;
            switch (visualizacaoAtual) {
                //identifica tipo de visualização | 1 -> Wireframe | 2 -> Wireframe com ocultação | 
                //3 -> Sombreamento costante | 4 - phong
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
                    //verifica que é visivel é desenha
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
                        //faz o preenchimento se for visível
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
                    //pinta o poligono com o phong
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
                        //verifica se é visivel ou não
                        if (Vetor.produtoEscalar(c.getVRPtoFP3(), norma) > 0) {
                            //
                            Face f2 = zpol.getFaces().get(i);
                            for (int j = 0; j < f2.getPontos().size(); j++) {
                                double mediaX = 0;
                                double mediaY = 0;
                                double mediaZ = 0;
                                int counter = 1;
                                /*
                                 * procura todos os pontos em todas as faces do poligono
                                 que sejam igual a face que ele ta olhando no momento ai ele pega a norma 
                                 * dessas faces e faz a media
                                 */
                                Poligono p_phong = poligonosOriginais.get(poligonosOrganizados.indexOf(Paux));
                                for (int k = 0; k < p_phong.getFaces().size();
                                        k++) {
                                    for (int l = 0; l <p_phong.getFaces().get(k).
                                            getPontos().size(); l++) {
                                        if (f2.getPontos().get(j).getNome()
                                                == p_phong.getFaces().get(k).
                                                getPontos().get(l).getNome()) {
                                            
                                            p_phong.getFaces().get(k).
                                                    gerarVetorPlano();
                                            p_phong.getFaces().get(k).
                                                    getVetorPlano().
                                                    normalizar();
                                            //faz uma media de uma face
                                            mediaX += p_phong.getFaces().get(k).
                                                    getVetorPlano().get(0);
                                            mediaY += p_phong.getFaces().get(k).
                                                    getVetorPlano().get(1);
                                            mediaZ += p_phong.getFaces().get(k).
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
                            //chama o metodo sombreamento com a face que foi feita a media
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
                g2D.
                        fillOval((int) pT.getX() - 1, (int) pT.getY()
                        - 1, 3, 3);
                g2D.drawString(pT.getNome(), (int) pT.getX() - 3,
                        (int) pT.getY() - 3);
            }
        }

    }

    /**
     * Faz o desenho da linha que recebe por parâmetro
     *
     * @param g grapics onde ira desenhar
     * @param a ares que vai desenhar
     */
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

    /**
     * Faz o sombreamento na perspectiva
     *
     * @param p poligono
     * @param f face
     * @param g graphics
     */
    public void sobreamento(Poligono p, Face f, Graphics g) {
        //os x e y daqui (getX() e getY()) os pia fazem na perspectiva, ou seja pegam o x e y em perspectiva
        //nossa face que esta sendo passada por parametro, o getX e getY dela estao sendo passados por parametro?
        ArrayList<Aresta> arestaFaceAtual = f.getArestas();
        double yInferior = ((Aresta) arestaFaceAtual.get(0)).getP1().getX();
        double ySuperior = ((Aresta) arestaFaceAtual.get(0)).getP1().getY();
        for (Aresta a : arestaFaceAtual) {
            if (a.getP1().getY() < yInferior) {
                yInferior = a.getP1().getY();
            }
            if (a.getP1().getY() > ySuperior) {
                ySuperior = a.getP1().getY();
            }
            if (a.getP2().getY() < yInferior) {
                yInferior = a.getP2().getY();
            }
            if (a.getP2().getY() > ySuperior) {
                ySuperior = a.getP2().getY();
            }
        }

        for (int itY = (int) ySuperior; itY > (int) yInferior && itY > 0; itY--) {
            double xFirst = 0.0;
            double xSecond = 0.0;
            double parametroU1 = 0.0;
            double parametroU2 = 0.0;
            boolean first = true;
            Ponto pA = null;
            Ponto pB = null;
            Ponto pC = null;
            Ponto pD = null;
            for (Aresta a : arestaFaceAtual) {
                Ponto p1 = a.getP1();
                Ponto p2 = a.getP2();
                if ((int) p1.getY() == (int) p2.getY()) {
                    continue;
                }
                double parametroUOriginal = ((double) itY - p1.getY()) / (p2.
                        getY() - p1.getY());
                int auxYValue = 0;
                if (p1.getY() <= p2.getY()) {
                    auxYValue = (int) p1.getY();
                } else {
                    auxYValue = (int) p2.getY();
                }
                if (parametroUOriginal < 0.0 || parametroUOriginal > 1.0 || itY
                        == auxYValue) {
                    continue;
                }
                if (first) {
                    xFirst = parametroUOriginal * (p2.getX() - p1.getX()) + p1.
                            getX();
                    pA = p1;
                    pB = p2;
                    parametroU1 = parametroUOriginal;
                    first = false;
                    continue;
                }
                xSecond = parametroUOriginal * (p2.getX() - p1.getX()) + p1.
                        getX();
                pC = p1;
                pD = p2;
                parametroU2 = parametroUOriginal;
                break;
            }
            if (xFirst == 0.0 && xSecond == 0.0) {
                continue;
            }
            if (xFirst > xSecond) {
                double auxiliar = xFirst;
                xFirst = xSecond;
                xSecond = auxiliar;
                Ponto paux = pA;
                pA = pC;
                pC = paux;
                paux = pB;
                pB = pD;
                pD = paux;
                auxiliar = parametroU1;
                parametroU1 = parametroU2;
                parametroU2 = auxiliar;
            }
            double e1 = (parametroU1 * (pB.getY() - pA.getY())) / (pB.getY()
                    - pA.getY());
            double e2 = ((1.0 - parametroU1) * (pB.getY() - pA.getY())) / (pB.
                    getY()
                    - pA.getY());
            double e3 = (parametroU2 * (pD.getY() - pC.getY())) / (pD.getY()
                    - pC.getY());
            double e4 = ((1.0 - parametroU2) * (pD.getY() - pC.getY())) / (pD.
                    getY()
                    - pC.getY());
            //até aqui é x e y perspectiva...


            //daqui para baixo é eles fazem x, y e z normais, acho que seria em mundo...
            //lá onde eu chamo o phong teria que montar a face com os pontos em perspectiva e no mundo
            //na classe ponto eu criei novos atributos mX, mY, mZ e cameraZ
            //teria que, lá onde chamo o phong, setar esses atributos e utilizar aqui para baixo (cameraZ só usa no zbuffer)
            //outra coisa que tem que ver é se minhas medias dos vetores normais estao sendo feitas corretamente
            //isso é feita tbm onde chamo o phong (if == 4)


            double Nxi = pB.getnX() * e1 + pA.getnX() * e2;
            double Nyi = pB.getnY() * e1 + pA.getnY() * e2;
            double Nzi = pB.getnZ() * e1 + pA.getnZ() * e2;
            double Nxf = pD.getnX() * e3 + pC.getnX() * e4;
            double Nyf = pD.getnY() * e3 + pC.getnY() * e4;
            double Nzf = pD.getnZ() * e3 + pC.getnZ() * e4;
            double Zi = pB.getZ() * e1 + pA.getZ() * e2;
            double Zf = pD.getZ() * e3 + pC.getZ() * e4;
            double Yi = pB.getY() * e1 + pA.getY() * e2;
            double Yf = pD.getY() * e3 + pC.getY() * e4;
            double Xi = pB.getX() * e1 + pA.getX() * e2;
            double Xf = pD.getX() * e3 + pC.getX() * e4;
            double variacaoX = xSecond - xFirst;
            double variacaoNx = (Nxf - Nxi) / variacaoX;
            double variacaoNy = (Nyf - Nyi) / variacaoX;
            double variacaoNz = (Nzf - Nzi) / variacaoX;
            double variacaZM = (Zf - Zi) / variacaoX;
            double variacaoXM = (Xf - Xi) / variacaoX;
            double variacaoYM = (Yf - Yi) / variacaoX;
            double Nx = Nxi;
            double Ny = Nyi;
            double Nz = Nzi;
            double x = xFirst;
            double Z = Zi;
            double X = Xi;
            for (double iteradorY = Yi; x < xSecond; iteradorY += variacaoYM) {
                Ponto ponto = new Ponto("", X, iteradorY, Z);
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
                g.drawRect((int) x, itY, 1, 1);
                Nx += variacaoNx;
                Ny += variacaoNy;
                Nz += variacaoNz;
                x++;
                Z += variacaZM;
                X += variacaoXM;
            }
          

        }

    }

    /**
     * Chama os métodos necessários para obter as variaveis para o phong
     *
     * @param p poligono
     * @param ponto
     */
    public void phong(Poligono p, Ponto ponto) {
        ponto.setIr(getIr(p, ponto));
        ponto.setIg(getIg(p, ponto));
        ponto.setIb(getIb(p, ponto));
    }

    /**
     * Obtem o I red
     *
     * @param p poligono
     * @param ponto
     * @return o valor de red em double
     */
    private double getIr(Poligono p, Ponto ponto) {

        Matriz src = inter.getCamera().getSRC();

        Matriz local = new Matriz(4, 1);
        local.set(0, 0, inter.getLuzFundo().getLocal().getX());
        local.set(1, 0, inter.getLuzFundo().getLocal().getY());
        local.set(2, 0, inter.getLuzFundo().getLocal().getZ());
        local.set(3, 0, 1);

        local = Matriz.multiplicacao(src, local);

        Ponto plocal = new Ponto("", local.get(0, 0), local.get(1, 0), local.get(2, 0));

        double ambiente = luzAmbiente(inter.getLuzAmbiente().getIr(), p.getKaR());
        double difusa = reflexaoDifusa(inter.getLuzFundo().getIr(), p.getKdR(),
                ponto.getNormal(), inter.getLuzFundo().getLocal(), ponto);

        double especular;
        if (difusa == 0.0) {
            especular = 0;
        } else {
            especular = reflexaoEspecular(inter.getLuzFundo().getIr(), p.
                    getKsR(), p.getN(), inter.getLuzFundo().getLocal(),
                    ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
                    inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        }

        return ambiente + difusa + especular;
    }

    /**
     * Obtem I do Green
     *
     * @param p poligono
     * @param ponto
     * @return valor double do Ig
     */
    private double getIg(Poligono p, Ponto ponto) {

        Matriz src = inter.getCamera().getSRC();

        Matriz matrizLoca = new Matriz(4, 1);
        matrizLoca.set(0, 0, inter.getLuzFundo().getLocal().getX());
        matrizLoca.set(1, 0, inter.getLuzFundo().getLocal().getY());
        matrizLoca.set(2, 0, inter.getLuzFundo().getLocal().getZ());
        matrizLoca.set(3, 0, 1);

        matrizLoca = Matriz.multiplicacao(src, matrizLoca);

        Ponto pLocal = new Ponto("", matrizLoca.get(0, 0), matrizLoca.get(1, 0),
                matrizLoca.get(2, 0));

        double ambiente = luzAmbiente(inter.getLuzAmbiente().getIg(), p.getKaG());


        double difusa = reflexaoDifusa(inter.getLuzFundo().getIg(), p.getKdG(),
                ponto.
                getNormal(), inter.getLuzFundo().getLocal(), ponto);

        double especular;
        if (difusa == 0.0) {
            especular = 0;
        } else {
            especular = reflexaoEspecular(inter.getLuzFundo().
                    getIg(), p.getKsG(), p.getN(), inter.getLuzFundo().getLocal(),
                    ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
                    inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        }

        return ambiente + difusa + especular;
    }

    /**
     * Obtem o valor de I do blue
     *
     * @param p
     * @param ponto
     * @return valor double do Ib
     */
    private double getIb(Poligono p, Ponto ponto) {

        Matriz src = inter.getCamera().getSRC();

        Matriz matrizLocal = new Matriz(4, 1);
        matrizLocal.set(0, 0, inter.getLuzFundo().getLocal().getX());
        matrizLocal.set(1, 0, inter.getLuzFundo().getLocal().getY());
        matrizLocal.set(2, 0, inter.getLuzFundo().getLocal().getZ());
        matrizLocal.set(3, 0, 1);
        Ponto pLocal = new Ponto("", matrizLocal.get(0, 0), matrizLocal. get(1, 0), matrizLocal.get(2, 0)
        );
        double ambiente = luzAmbiente(inter.getLuzAmbiente().getIb(), p.getKaB());

        double difusa = reflexaoDifusa(inter.getLuzFundo().getIb(), p.getKdB(),
                ponto.
                getNormal(), inter.getLuzFundo().getLocal(), ponto);

        double especular;
        if (difusa == 0) {
            especular = 0;
        } else {
            especular = reflexaoEspecular(inter.getLuzFundo().
                    getIb(), p.getKsB(), p.getN(), inter.getLuzFundo().getLocal(),
                    ponto.getNormal(), new Ponto("", inter.getCamera().getVx(),
                    inter.getCamera().getVy(), inter.getCamera().getVz()), ponto);
        }
        return ambiente + difusa + especular;
    }

    /**
     * Calcula a luz ambiente
     *
     * @param Ia
     * @param Ka
     * @return double valor luz ambiente
     */
    private static double luzAmbiente(double Ia, double Ka) {
        return Ia * Ka;
    }

    /**
     * Cálculo da reflexão difusa
     *
     * @param Il
     * @param Kd
     * @param normal
     * @param L
     * @param pontoObservado
     * @return double
     */
    private static double reflexaoDifusa(double Il, double Kd, Ponto normal,
                                         Ponto L,
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
        //chama o produto escalar de l e n
        if (escalar(n, l) > 0.0) {
            return Il * Kd * escalar(n, l);
        } else {
            return 0.0;
        }
    }

    /**
     * Calculo da Reflexao epecular
     *
     * @param Il
     * @param Ks
     * @param expoenteN
     * @param L
     * @param N
     * @param VRP
     * @param A
     * @return double com o valor da reflexao especular
     */
    private static double reflexaoEspecular(double Il, double Ks,
                                            double expoenteN,
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
        double DoisLN = 2 * escalar(l, n);
        r.setX(l.getX() - DoisLN * n.getX());
        r.setY(l.getY() - DoisLN * n.getY());
        r.setZ(l.getZ() - DoisLN * n.getZ());
        Ponto s = new Ponto("", VRP.getX() - A.getX(), VRP.getY() - A.getY(),
                VRP.getZ() - A.getZ());
        double normaS = norma(s);
        s.setX(s.getX() / normaS);
        s.setY(s.getY() / normaS);
        s.setZ(s.getZ() / normaS);

        if (escalar(r, s) > 0.0) {
            return Il * Ks * Math.pow(escalar(r, s), expoenteN);
        } else {
            return 0.0;
        }
    }

    /**
     * Norma de um ponto p
     *
     * @param p
     * @return double com o valor da norma
     */
    public static double norma(Ponto p) {
        return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ()
                * p.getZ());
    }

    /**
     * Escalar de dois pontos p1 e p2
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double escalar(Ponto p1, Ponto p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY() + p1.getZ() * p2.
                getZ();
    }

    /**
     * Faz o preenchimento da face passada
     *
     * @param f
     * @param g
     */
    public static void preenchimento(Face f, Graphics g) {
        ArrayList<Aresta> arestaFaceAtual = f.getArestas();
        double yInferior = arestaFaceAtual.get(0).getP1().getX();
        double ySuperior = arestaFaceAtual.get(0).getP1().getY();

        for (Aresta a : arestaFaceAtual) {
            if (a.getP1().getY() < yInferior) {
                yInferior = a.getP1().getY();
            }
            if (a.getP1().getY() > ySuperior) {
                ySuperior = a.getP1().getY();
            }
            if (a.getP2().getY() < yInferior) {
                yInferior = a.getP2().getY();
            }
            if (a.getP2().getY() > ySuperior) {
                ySuperior = a.getP2().getY();
            }
        }

        for (int y = (int) ySuperior; y > (int) yInferior && y > 0; y--) {
            double x1 = 0.0D;
            double x2 = 0.0D;
            boolean first = true;
            for (Aresta a : arestaFaceAtual) {
                Ponto p1 = a.getP1();
                Ponto p2 = a.getP2();
                if ((int) p1.getY() == (int) p2.getY()) {
                    continue;
                }
                double parametroU = ((double) y - p1.getY()) / (p2.getY() - p1.
                        getY());
                int auxCompar;
                if (p1.getY() <= p2.getY()) {
                    auxCompar = (int) p1.getY();
                } else {
                    auxCompar = (int) p2.getY();
                }
                if (parametroU < 0.0 || parametroU > 1.0 || y
                        == auxCompar) {
                    continue;
                }
                if (first) {
                    x1 = parametroU * (p2.getX() - p1.getX()) + p1.getX();
                    first = false;
                    continue;
                }
                x2 = parametroU * (p2.getX() - p1.getX()) + p1.getX();
                break;
            }


            if (x1 == 0.0 && x2 == 0.0) {
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
