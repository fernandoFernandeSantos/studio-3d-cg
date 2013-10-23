/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;

/**
 *
 * @author marcelo-note
 */
public class Face implements java.io.Serializable {

    private Poligono parentPol;
    private ArrayList<Aresta> arestas;
    private boolean Visivel;
    private Vetor vetorPlano;

    public Face(Poligono pol) {
        parentPol = pol;
        //pontos = new ArrayList<>();
        arestas = new ArrayList<>();
    }

    public Face(Poligono pol, ArrayList<Aresta> _arestas) {
        parentPol = pol;
        //pontos = _pontos;
        arestas = _arestas;
    }

    public void addAresta(String nome) {
        arestas.add(this.parentPol.getAresta(nome));
    }

    public ArrayList<Aresta> getArestas() {
        return arestas;
    }

    public boolean isVisivel(Vetor v) {

        Vetor vp = this.getVetorPlano();
//        vp.print("norma = ");
        Ponto a = this.getArestas().get(0).getP1();
//        a.print("ponto =");
//        double d = -(a.getX() * vp.get(0))

        double d = -(a.getX() * vp.get(0));
        d -= a.getY() * vp.get(1);
        d -= a.getZ() * vp.get(2);

//        d *= -1;
//        System.out.println("d = " + d);
//        double D = Vetor.produtoEscalar(v, vp) + d;
//        double D = (vp.get(0) + (vp.get(1) + (vp.get(2)))) + d;
        double D = vp.get(0) * v.get(0) + vp.get(1) * v.get(1) + vp.get(2) * v.get(2) + d;

       
        if (D < 0) {
            System.out.println("a frente");
            return false;
        } else {
            System.out.println("atras");
            return true;
        }
    }

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

    public void gerarVetorPlano() {

        ArrayList<Ponto> pontos = this.getPontos();

        Ponto a = pontos.get(0);
        Ponto b = null;
        Ponto c = null;
        for (Ponto p : pontos) {
            if (!p.equals(a)) {
                b = p;
                break;
            }
        }

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

        Vetor normal = Vetor.produtoVetorial(v1, v2);
        normal.normalizar();
        if (normal.get(0) == 0 && normal.get(1) == 0 && normal.get(2) == 0) {
            normal.print("norma = ");
        }
        Vetor vcentro = Vetor.subtracao(new Vetor(centrO.getX(), centrO.getY(),
                centrO.getZ()),
                new Vetor(a.getX(), a.getY(), a.getZ()));

        if (Vetor.getAngulo(vcentro, normal) <= 90) {
            normal.multiplicarEscalar(-1);
            this.vetorPlano = normal;
        } else {
            this.vetorPlano = normal;
        }


       
    }
}
