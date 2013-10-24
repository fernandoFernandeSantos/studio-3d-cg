/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author alienware
 */
public final class Vetor {

    private double[] vetor;

    public Vetor() {
        this.vetor = new double[3];
    }
    
    public Vetor(Ponto p){
//        this.vetor = new double[3];
        this();
        this.set(0,p.getX());
        this.set(1,p.getY());
        this.set(2,p.getZ());
    }

    public Vetor(int n) {
        this.vetor = new double[n];
    }

    public Vetor(double x, double y, double z) {
        this.vetor = new double[3];
        this.vetor[0] = x;
        this.vetor[1] = y;
        this.vetor[2] = z;
    }

    public Vetor(double x, double y, double z, double w) {
        this.vetor = new double[4];
        this.vetor[0] = x;
        this.vetor[1] = y;
        this.vetor[2] = z;
        this.vetor[3] = w;
    }

    public void set(int n, double v) {
        if (n >= this.vetor.length) {
            double[] aux = new double[n + 1];
            System.arraycopy(this.vetor, 0, aux, 0, this.vetor.length);
            this.vetor = aux;
        }
        this.vetor[n] = v;
    }

    public Vetor copy() {
        Vetor resultado = new Vetor(this.length());
        for (int i = 0; i < this.length(); i++) {
            resultado.set(i, this.get(i));
        }
        return resultado;
    }

    public double get(int n) {
        return this.vetor[n];
    }

    public int length() {
        return this.vetor.length;
    }

    public void soma(Vetor a) {
        for (int i = 0; i < a.length(); i++) {
            this.vetor[i] += a.get(i);
        }
    }

    public static Vetor soma(Vetor a, Vetor b) {
        Vetor resultado = new Vetor(a.length());
        for (int i = 0; i < resultado.length(); i++) {
            resultado.set(i, a.get(i) + b.get(i));
        }
        return resultado;
    }

    public void subtracao(Vetor a) {
        for (int i = 0; i < a.length(); i++) {
            this.vetor[i] -= a.get(i);
        }
    }

    public static Vetor subtracao(Vetor a, Vetor b) {
        Vetor resultado = new Vetor(a.length());
        for (int i = 0; i < resultado.length(); i++) {
            resultado.set(i, a.get(i) - b.get(i));
        }
        return resultado;
    }

    public void multiplicarEscalar(double escalar) {
        for (int i = 0; i < this.length(); i++) {
            this.set(i, this.get(i) * escalar);
        }
    }

    public static Vetor multiplicarEscalar(double escalar, Vetor a) {
        Vetor resultado = new Vetor(a.length());
        for (int i = 0; i < a.length(); i++) {
            resultado.set(i, a.get(i) * escalar);
        }
        return resultado;
    }

    public double produtoEscalar(Vetor a) {
        double resultado = 0;
        for (int i = 0; i < a.length(); i++) {
            resultado += this.get(i) * a.get(i);
        }
        return resultado;
    }

    public static double produtoEscalar(Vetor a, Vetor v) {
        double resultado = 0;
        for (int i = 0; i < a.length(); i++) {
            resultado += a.get(i) * v.get(i);
        }

        return resultado;
    }

    public void produtoVetorial(Vetor a) {
        Vetor aux = this.copy();
        this.set(0, aux.get(1) * a.get(2) - aux.get(2) * a.get(1));
        this.set(1, aux.get(2) * a.get(1) - aux.get(0) * a.get(2));
        this.set(2, aux.get(0) * a.get(1) - aux.get(1) * a.get(0));
    }

    public static Vetor produtoVetorial(Vetor a, Vetor b) {
        Vetor aux = new Vetor(3);
        aux.set(0, a.get(1) * b.get(2) - a.get(2) * b.get(1));
        aux.set(1, a.get(2) * b.get(0) - a.get(0) * b.get(2));
        aux.set(2, a.get(0) * b.get(1) - a.get(1) * b.get(0));

        return aux;
    }

    public double getNorma() {
        double aux = 0;
        for (int i = 0; i < this.length(); i++) {
            aux += Math.pow(this.get(i), 2);
        }

        return Math.sqrt(aux);
    }

    public double getModulo() {
        double res = 0;

        for (int i = 0; i < this.length(); i++) {
            res += (this.get(i) * this.get(i));
        }

        return Math.sqrt(res);
    }

    public void normalizar() {
        this.multiplicarEscalar(1 / this.getNorma());
    }

    public double getAngulo(Vetor a) {

        double moduloThis = this.getModulo();
        double moduloA = a.getModulo();


        double produto_escalar = Vetor.produtoEscalar(this, a);

        double rad = (produto_escalar / (moduloThis * moduloA));

//        rad = (Math.PI / 180) * rad;
        double angulo = Math.acos(rad);
        angulo = (180 * angulo) / Math.PI;

       
        return angulo;
    }

    public static double getAngulo(Vetor a, Vetor b) {

        return a.getAngulo(b);

    }

    public void print(String nome) {
        System.out.print("Vetor " + nome + " (\n");
        for (int i = 0; i < this.length(); i++) {
            System.out.print(this.get(i) + " | ");
        }
        System.out.print("\n)\n");
    }

    public void cut(int x) {
        Vetor aux = new Vetor(x);
        for (int i = 0; i < x; i++) {
            aux.set(i, this.get(i));
        }
        this.vetor = aux.vetor;
    }
}
