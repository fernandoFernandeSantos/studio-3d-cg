/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;

/**
 * Representa uma face do poligono
 *
 * @author marcelo-note
 */
public class Face implements java.io.Serializable {

    private Poligono parentPol;
    private ArrayList<Aresta> arestas;
    private boolean Visivel;
    private Vetor vetorPlano;

    /**
     * Construtor da face, recebe o poligono atual
     *
     * @param pol
     */
    public Face(Poligono pol) {
        parentPol = pol;

        arestas = new ArrayList<>();
    }

    /**
     * Construtor da face, recebe o poligono e as arestas da face F
     *
     * @param pol
     * @param _arestas
     */
    public Face(Poligono pol, ArrayList<Aresta> _arestas) {
        parentPol = pol;

        arestas = _arestas;
    }

    public void addAresta(String nome) {
        arestas.add(this.parentPol.getAresta(nome));
    }

    /**
     * Método retorna as arestas da face
     *
     * @return ArrayList de Arestas
     */
    public ArrayList<Aresta> getArestas() {
        return arestas;
    }

    public boolean isVisivel(Vetor v) {

        Vetor vp = this.getVetorPlano();

        Ponto a = this.getArestas().get(0).getP1();


        double d = -(a.getX() * vp.get(0));
        d -= a.getY() * vp.get(1);
        d -= a.getZ() * vp.get(2);


        double D = vp.get(0) * v.get(0) + vp.get(1) * v.get(1) + vp.get(2) * v.get(2) + d;


        if (D < 0) {
            //System.out.println("a frente");
            return false;
        } else {
            //System.out.println("atras");
            return true;
        }
    }
/**
 * Obtem todos os pontos da face
 * @return array de pontos
 */
    public ArrayList<Ponto> getPontos() {
        ArrayList<Ponto> pontos = new ArrayList<>();

        Ponto p = this.arestas.get(0).getP1();
        pontos.add(p);

        int i = 1;
        while (i < arestas.size()) {
            for (Aresta a : arestas) {
                if (p.equals(a.getP1())) {
                    if (!pontos.contains(a.getP2())) {
                        p = a.getP2();
                        pontos.add(p);
                        i++;
                        break;
                    }
                }
                if (p.equals(a.getP2())) {
                    if (!pontos.contains(a.getP1())) {
                        p = a.getP1();
                        pontos.add(p);
                        i++;
                        break;
                    }
                }
            }
        }

        return pontos;
    }
/**
 * Calcula a area entre dois vetores
 * @param x vetor
 * @param y vetor
 * @return double
 */
    private double getArea(Vetor x, Vetor y) {
        double area = 0;
        for (int i = 0; i < x.length(); i++) {
            if (i != x.length() - 1) {
                area += (x.get(i) * y.get(i + 1)) - (x.get(i + 1) * y.get(i));
            } else {
                area += (x.get(i) * y.get(0)) - (x.get(0) * y.get(i));
            }
        }
        area /= 2;

        return area;
    }
/**
 * Calcula area entre os vetores X e Y
 * @return double
 */
    public double calculaAreaXY() {

        ArrayList<Ponto> pontos = this.getPontos();

        Vetor x = new Vetor(pontos.size());
        Vetor y = new Vetor(pontos.size());

        int iterator = 0;
        for (Ponto p : pontos) {
            x.set(iterator, p.getX());
            y.set(iterator, p.getY());
            iterator++;
        }

        return getArea(x, y);

    }
/**
 * Calcula area entre os vetores X e Z
 * @return double
 */
    public double calculaAreaXZ() {

        ArrayList<Ponto> pontos = this.getPontos();

        Vetor x = new Vetor(pontos.size());
        Vetor z = new Vetor(pontos.size());

        int iterator = 0;
        for (Ponto p : pontos) {
            x.set(iterator, p.getX());
            z.set(iterator, p.getZ());
            iterator++;
        }

        return getArea(x, z);
    }
/**
 * Calcula area entre os vetores Y e Z
 * @return double
 */
    public double calculaAreaYZ() {

        ArrayList<Ponto> pontos = this.getPontos();

        Vetor y = new Vetor(pontos.size());
        Vetor z = new Vetor(pontos.size());

        int iterator = 0;
        for (Ponto p : pontos) {
            y.set(iterator, p.getY());
            z.set(iterator, p.getZ());
            iterator++;
        }

        return getArea(y, z);
    }

    public Vetor getVetorPlano() {
        return vetorPlano;
    }

  /**
   * Gera o vetor Normal ao plano da face
   */
    public void gerarVetorPlano() {

        ArrayList<Ponto> pontos = this.getPontos();

        Ponto a = pontos.get(0);
        Ponto b = null;
        Ponto c = null;
        //pega um ponto que não é igual ao primeiro ponto
        for (Ponto p : pontos) {
            if (!p.equals(a)) {
                b = p;
                break;
            }
        }
        //pega outro ponto que não é igual ao primeiro e nem ao segundo ponto
        for (Ponto p : pontos) {
            if (!p.equals(a) && !p.equals(b)) {
                c = p;
            }
        }


        Vetor v1 = Vetor.subtracao(new Vetor(b.getX(), b.getY(), b.getZ()),
                new Vetor(a.getX(), a.getY(), a.getZ()));

        Vetor v2 = Vetor.subtracao(new Vetor(c.getX(), c.getY(), c.getZ()),
                new Vetor(a.getX(), a.getY(), a.getZ()));

        Ponto centrO = this.parentPol.getCentro();
        //gera o vetor normal com os vetores obtidos acima
        Vetor normal = Vetor.produtoVetorial(v1, v2);
        normal.normalizar();

        Vetor vcentro = Vetor.subtracao(new Vetor(centrO.getX(), centrO.getY(),
                centrO.getZ()),
                new Vetor(a.getX(), a.getY(), a.getZ()));

        //verifica se o vetor está na direção certa, ou seja para fora da face
        if (Vetor.getAngulo(vcentro, normal) > 90) {
            normal.multiplicarEscalar(-1);
            this.vetorPlano = normal;
        } else {
            this.vetorPlano = normal;
        }



    }
}
