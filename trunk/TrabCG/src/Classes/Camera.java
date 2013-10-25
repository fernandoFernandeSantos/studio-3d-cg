/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcelo
 */
public class Camera {

    private double Vx;//VRP
    private double Vy;
    private double Vz;
    private double FPx;//Ponto focal
    private double FPy;
    private double FPz;
    private double distancia;
    private Matriz composta;
    private Matriz cpPSRC;
    private Matriz matrizAux;

    public Camera() {
        Vx = 0;//VRP
        Vy = 0;
        Vz = 100;

        FPx = 0;//Ponto focal
        FPy = 0;
        FPz = 0;
    }

    public Camera(double _vx, double _vy, double _vz, double _fx, double _fy, double _fz, double _distancia) {
        Vx = _vx;//VRP
        Vy = _vy;
        Vz = _vz;

        FPx = _fx;//Ponto focal
        FPy = _fy;
        FPz = _fz;

        distancia = _distancia;
    }

    public double getVx() {
        return Vx;
    }

    public double getVy() {
        return Vy;
    }

    public double getVz() {
        return Vz;
    }

    public double getFPx() {
        return FPx;
    }

    public double getFPy() {
        return FPy;
    }

    public double getFPz() {
        return FPz;
    }

    public double getDistancia() {
        return distancia;
    }

    public Vetor getFP() {
        return new Vetor(this.getFPx(), this.getFPy(), this.getFPz(), 1);
    }

    public Vetor getFP3() {
        return new Vetor(this.getFPx(), this.getFPy(), this.getFPz());
    }

    public Vetor getVRP() {
        return new Vetor(this.getVx(), this.getVy(), this.Vz, 1);
    }

    public Vetor getVRP3() {
        return new Vetor(this.getVx(), this.getVy(), this.Vz);
    }

    public Matriz getComposta() {
        return this.composta;
    }

    public Matriz getcpPSRC() {
        return this.cpPSRC;
    }

    public void setVx(double Vx) {
        this.Vx = Vx;
    }

    public void setVy(double Vy) {
        this.Vy = Vy;
    }

    public void setVz(double Vz) {
        this.Vz = Vz;
    }

    public void setFPx(double FPx) {
        this.FPx = FPx;
    }

    public void setFPy(double FPy) {
        this.FPy = FPy;
    }

    public void setFPz(double FPz) {
        this.FPz = FPz;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public void setFP(double x, double y, double z) {
        this.setFPx(x);
        this.setFPy(y);
        this.setFPz(z);
    }

    public void setVRP(double x, double y, double z) {
        this.setVx(x);
        this.setVy(y);
        this.setVz(z);
    }

    public void setVRP(Vetor vrp) {
        this.setVRP(vrp.get(0),
                vrp.get(1),
                vrp.get(2));
    }

    public void GerarIntermediarios() {
        //Cria vetor VRP
        Vetor VRP = this.getVRP();

        Vetor P = this.getFP();


        double dp = this.getDistancia();
        Matriz cpP = new Matriz(4, 1);
        cpP.set(0, 0, VRP.get(0) + (P.get(0) - VRP.get(0)) * dp / 100);
        cpP.set(1, 0, VRP.get(1) + (P.get(1) - VRP.get(1)) * dp / 100);
        cpP.set(2, 0, VRP.get(2) + (P.get(2) - VRP.get(2)) * dp / 100);
        cpP.set(3, 0, 1);

        Vetor Y = new Vetor(0, 1, 0);
        Vetor N = Vetor.subtracao(VRP, P);

        Vetor n = N.copy();
        n.normalizar();

        Vetor V = new Vetor(Y.length());
        V.set(0, Y.get(0) - ((Vetor.produtoEscalar(Y, n)) * n.get(0)));
        V.set(1, Y.get(1) - ((Vetor.produtoEscalar(Y, n)) * n.get(1)));
        V.set(2, Y.get(2) - ((Vetor.produtoEscalar(Y, n)) * n.get(2)));

        Vetor v = V.copy();
        v.normalizar();
        Vetor U = new Vetor(V.get(1) * N.get(2) - (V.get(2) * N.get(1)), V.get(2) * N.
                get(0) - (V.get(0) * N.get(2)), V.get(0) * N.get(1) - (V.get(1)
                * N.get(0)));

        Vetor u = U.copy();
        u.normalizar();

        Matriz matrizRotacao = new Matriz(4, 4);
        for (int i = 0; i < u.length(); i++) {
            matrizRotacao.set(0, i, u.get(i));
        }
        matrizRotacao.set(0, 3, 0);
        for (int i = 0; i < v.length(); i++) {
            matrizRotacao.set(1, i, v.get((int) i));
        }
        matrizRotacao.set(1, 3, 0);
        for (int i = 0; i < n.length(); i++) {
            matrizRotacao.set(2, i, n.get(i));
        }
        matrizRotacao.set(2, 3, 0);
        for (int i = 0; i < 3; i++) {
            matrizRotacao.set(3, i, 0);
        }
        matrizRotacao.set(3, 3, 1);

        Matriz matrizTranslacao = Matriz.gerarTranslacao(-VRP.get(0),
                -VRP.get(1), -VRP.get(2));
        //--------------------------------------------
//        Matriz matrizTranslacao = new Matriz(4, 4);
//        matrizTranslacao.setIdentidade();
//        matrizTranslacao.set(0, 3, -VRP.get(0));
//        matrizTranslacao.set(1, 3, -VRP.get(1));
//        matrizTranslacao.set(2, 3, -VRP.get(2));
//        matrizTranslacao.prlong("matriz translacao");
        //----------------------------------------------
        Matriz SRC = Matriz.multiplicacao(matrizRotacao, matrizTranslacao);

        Matriz cpPSRC = Matriz.multiplicacao(SRC, cpP);

        double distanciaVRPePP = Math.sqrt(Math.pow((cpPSRC.get(0, 0) - 0), 2) + Math.
                pow((cpPSRC.get(1, 0) - 0), 2) + Math.
                pow((cpPSRC.get(2, 0) - 0), 2));

        Matriz Perspectiva = new Matriz(4, 4);
        Perspectiva.setIdentidade();
        Perspectiva.set(3, 2, (-1) / distanciaVRPePP);

        Perspectiva.multiplicacao(SRC);

        this.composta = Perspectiva;
        this.cpPSRC = cpPSRC;
       
    }

    public Poligono GerarPerspectiva(long width, long heigh, Poligono p) {
        Matriz pontos = GerarPerspectiva(width, heigh, p.TransformarPerspectiva().getMatrizPontos());
        Poligono res = p.copy();
        res.setPontos(pontos);
        return res;
    }

    public Vetor getVRPtoFP() {
        Vetor VRP = this.getVRP();
        Vetor FP = this.getFP();
        return Vetor.subtracao(VRP, FP);
    }

    public Vetor getVRPtoFP3() {
        Vetor VRP = this.getVRP3();
        Vetor FP = this.getFP3();
        Vetor r = Vetor.subtracao(VRP, FP);
        r.normalizar();
        return r;
    }

    public Matriz GerarPerspectiva(long width, long height, Matriz pontos) {

        Vetor window = new Vetor(2);
        window.set(0, 4);
        window.set(1, 3);
        Vetor viewPort = new Vetor(0, 0, width, height);
        Matriz aux = Matriz.multiplicacao(this.getComposta(), pontos);
        this.setMatrizAux(aux);
//        aux.prlong("aux antes");
        for (int i = 0; i < aux.getColunas(); i++) {
            aux.set(0, i, aux.get(0, i) / aux.get(3, i));
            aux.set(1, i, aux.get(1, i) / aux.get(3, i));
            aux.set(2, i, aux.get(2, i) / aux.get(3, i));
            aux.set(3, i, aux.get(3, i) / aux.get(3, i));
        }
//        aux.prlong("aux");\
        double[] z = new double[aux.getColunas()];
        for (int i = 0; i < z.length; i++) {
            z[i] = aux.get(2, i);
        }
        Matriz newAux = aux.cut(2);
//        newAux.prlong("aux cutada");
        Matriz windowFinal = new Matriz(4, 2);
        Matriz cpRSRC = this.getcpPSRC();

        windowFinal.set(0, 0, cpPSRC.get(0, 0) - window.get(0));
        windowFinal.set(0, 1, cpPSRC.get(0, 0) + window.get(0));
        windowFinal.set(1, 0, cpPSRC.get(1, 0) - window.get(1));
        windowFinal.set(1, 1, cpPSRC.get(1, 0) + window.get(1));
        windowFinal.set(2, 0, cpPSRC.get(2, 0));
        windowFinal.set(2, 1, cpPSRC.get(2, 0));
        windowFinal.set(3, 0, cpPSRC.get(3, 0));
        windowFinal.set(3, 1, cpPSRC.get(3, 0));
//        windowFinal.prlong("windowFinal");
        Matriz MJP = new Matriz(3, 3);
        MJP.setIdentidade();
        MJP.set(0, 0, (viewPort.get(2) - viewPort.get(0)) / (windowFinal.get(0,
                1) - windowFinal.get(0, 0)));
        MJP.
                set(0, 2, (-windowFinal.get(0, 0)) * MJP.get(0, 0) + viewPort.
                get(0));
        MJP.set(1, 1, (viewPort.get(3) - viewPort.get(1)) / (windowFinal.get(1,
                1) - windowFinal.get(1, 0)));
        MJP.
                set(1, 2, (-windowFinal.get(1, 0)) * MJP.get(1, 1) + viewPort.
                get(1));
//        MJP.prlong("MJP");
        newAux = Matriz.multiplicacao(MJP, newAux);
//        Matriz matrizComZ = new Matriz(newAux.getLinhas(), newAux.getColunas() + 1);
//        matrizComZ = newAux.copy();
//        for (int i = 0; i < z.length; i++) {
//            matrizComZ.set(2, i, z[i]);
//            matrizComZ.set(3, i, 1);
//        }
//        this.setMatrizComZ(matrizComZ);
        //        newAux.prlong("newAux");
       
        return newAux;


    }

    public Matriz getMatrizAux() {
        return matrizAux;
    }

    public void setMatrizAux(Matriz matrizAUx) {
        this.matrizAux = matrizAUx;
    }
}
