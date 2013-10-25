/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 * Representa vetores de n coordenadas
 * @author alienware
 */
public final class Vetor {

    private double[] vetor;
    /**
     * construtor padrão da classe, cria um vetor de 3 posições
     */
    public Vetor() {
        this.vetor = new double[3];
    }
    /**
     * Constroi um vetor com um ponto dado
     * @param p ponto
     */
    public Vetor(Ponto p){
        this();
        this.set(0,p.getX());
        this.set(1,p.getY());
        this.set(2,p.getZ());
    }
    /**
     * Constroi um vetor com n posições
     * @param n numero de posições
     */
    public Vetor(int n) {
        this.vetor = new double[n];
    }
    /**
     * Constroi um vetor com os valores de x,y e z
     * @param x
     * @param y
     * @param z 
     */
    public Vetor(double x, double y, double z) {
        this.vetor = new double[3];
        this.vetor[0] = x;
        this.vetor[1] = y;
        this.vetor[2] = z;
    }
    /**
     * Constroi um vetor com x, y, z e a coordenada homogenea
     * @param x
     * @param y
     * @param z
     * @param w 
     */
    public Vetor(double x, double y, double z, double w) {
        this.vetor = new double[4];
        this.vetor[0] = x;
        this.vetor[1] = y;
        this.vetor[2] = z;
        this.vetor[3] = w;
    }
    /**
     * seta um posição no vetor
     * @param n posição
     * @param v valor
     */
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
/**
 * soma de dois vetores this + a
 * @param a vetor dois
 */
    public void soma(Vetor a) {
        for (int i = 0; i < a.length(); i++) {
            this.vetor[i] += a.get(i);
        }
    }
/**
 * soma de dois vetores a + b
 * @param a
 * @param b
 * @return 
 */
    public static Vetor soma(Vetor a, Vetor b) {
        Vetor resultado = new Vetor(a.length());
        for (int i = 0; i < resultado.length(); i++) {
            resultado.set(i, a.get(i) + b.get(i));
        }
        return resultado;
    }
/**
 * Subtração de dois vetores this - a
 * @param a 
 */
    public void subtracao(Vetor a) {
        for (int i = 0; i < a.length(); i++) {
            this.vetor[i] -= a.get(i);
        }
    }
/**
 * Subtração de dois vetores a - b
 * @param a
 * @param b
 * @return vetor
 */
    public static Vetor subtracao(Vetor a, Vetor b) {
        Vetor resultado = new Vetor(a.length());
        for (int i = 0; i < resultado.length(); i++) {
            resultado.set(i, a.get(i) - b.get(i));
        }
        return resultado;
    }
/**
 * Multiplica o vetor por um escalar this * escalar
 * @param escalar 
 */
    public void multiplicarEscalar(double escalar) {
        for (int i = 0; i < this.length(); i++) {
            this.set(i, this.get(i) * escalar);
        }
    }
/**
 * Multiplica o vetor por um escalar a * escalar
 * @param escalar
 * @param a
 * @return vetor
 */
    public static Vetor multiplicarEscalar(double escalar, Vetor a) {
        Vetor resultado = new Vetor(a.length());
        for (int i = 0; i < a.length(); i++) {
            resultado.set(i, a.get(i) * escalar);
        }
        return resultado;
    }
/**
 * faz produto escalar de dois vetoes this * a
 * @param a
 * @return double
 */
    public double produtoEscalar(Vetor a) {
        double resultado = 0;
        for (int i = 0; i < a.length(); i++) {
            resultado += this.get(i) * a.get(i);
        }
        return resultado;
    }
/**
 * faz produto escalar Vetor a * Vetor v
 * @param a
 * @param v
 * @return double
 */
    public static double produtoEscalar(Vetor a, Vetor v) {
        double resultado = 0;
        for (int i = 0; i < a.length(); i++) {
            resultado += a.get(i) * v.get(i);
        }

        return resultado;
    }
/**
 * Faz produto vetorial this * Vetor a
 * @param a 
 */
    public void produtoVetorial(Vetor a) {
        Vetor aux = this.copy();
        this.set(0, aux.get(1) * a.get(2) - aux.get(2) * a.get(1));
        this.set(1, aux.get(2) * a.get(1) - aux.get(0) * a.get(2));
        this.set(2, aux.get(0) * a.get(1) - aux.get(1) * a.get(0));
    }
/**
 * Faz o produto vetorial Vetor a * Vetor b
 * @param a
 * @param b
 * @return Vetor
 */
    public static Vetor produtoVetorial(Vetor a, Vetor b) {
        Vetor aux = new Vetor(3);
        aux.set(0, a.get(1) * b.get(2) - a.get(2) * b.get(1));
        aux.set(1, a.get(2) * b.get(0) - a.get(0) * b.get(2));
        aux.set(2, a.get(0) * b.get(1) - a.get(1) * b.get(0));

        return aux;
    }
/**
 * Calcula a norma do vetor
 * @return double
 */
    public double getNorma() {
        double aux = 0;
        for (int i = 0; i < this.length(); i++) {
            aux += Math.pow(this.get(i), 2);
        }

        return Math.sqrt(aux);
    }
/**
 * Calcula o modulo do vetor
 * @return double
 */
    public double getModulo() {
        double res = 0;

        for (int i = 0; i < this.length(); i++) {
            res += (this.get(i) * this.get(i));
        }

        return Math.sqrt(res);
    }
/**
 * Normaliza o vetor
 */
    public void normalizar() {
        this.multiplicarEscalar(1 / this.getNorma());
    }
/**
 * Calcula o angulo entre dois vetores this -- Vetor a
 * @param a
 * @return double
 */
    public double getAngulo(Vetor a) {

        double moduloThis = this.getModulo();
        double moduloA = a.getModulo();


        double produto_escalar = Vetor.produtoEscalar(this, a);

        double rad = (produto_escalar / (moduloThis * moduloA));

        double angulo = Math.acos(rad);
        angulo = (180 * angulo) / Math.PI;

       
        return angulo;
    }
/**
 * Calcula o angulo entre dois vetores Vetor a -- Vetor b
 * @param a
 * @param b
 * @return double
 */
    public static double getAngulo(Vetor a, Vetor b) {

        return a.getAngulo(b);

    }
/**
 * Imprime um vetor com a string passada
 * @param nome 
 */
    public void print(String nome) {
        System.out.print("Vetor " + nome + " (\n");
        for (int i = 0; i < this.length(); i++) {
            System.out.print(this.get(i) + " | ");
        }
        System.out.print("\n)\n");
    }
/**
 * Corta o vetor na posição indicada
 * @param x 
 */
    public void cut(int x) {
        Vetor aux = new Vetor(x);
        for (int i = 0; i < x; i++) {
            aux.set(i, this.get(i));
        }
        this.vetor = aux.vetor;
    }
}
