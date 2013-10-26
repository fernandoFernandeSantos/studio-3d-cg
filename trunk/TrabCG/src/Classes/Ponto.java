/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author alienware
 */
public class Ponto implements java.io.Serializable {

    private String nome;
    private double X;
    private double Y;
    private double Z;
    double nX;
    double nY;
    double nZ;
    double Ir;
    double Ig;
    double Ib;
    double mX;
    double mY;
    double mZ;
    double cameraZ;

    /**
     * Cria uma instancia de um ponto normal
     *
     * @return Ponto com as cordenadas nX, nY e nZ
     */
    public Ponto getNormal() {
        return new Ponto("", nX, nY, nZ);
    }

    /**
     * construtor padrão
     */
    public Ponto() {
        nome = "";
    }

    /**
     * Contrutor com parametros definidos
     *
     * @param nome
     * @param X
     * @param Y
     * @param Z
     */
    public Ponto(String nome, double X, double Y, double Z) {
        this.nome = nome;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    /**
     * Faz a cópia do ponto
     *
     * @return um novo ponto como cópia
     */
    public Ponto copy() {
        Ponto r = new Ponto(this.nome, this.X, this.Y, this.Z);
        return r;
    }

    /**
     * Imprime um ponto iniciando com a string de parametro
     *
     * @param s string
     */
    public void print(String s) {
        System.out.println(s + " -> nome = " + this.getNome() + " x= " + this.getX() + " y= " + this.getY() + " Z= " + this.getZ() + " \n");
    }

    /**
     * Get Nome
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * Get x do ponto
     *
     * @return
     */
    public double getX() {
        return X;
    }

    /**
     * Get y do ponto
     *
     * @return
     */
    public double getY() {
        return Y;
    }

    /**
     * Get z do ponto
     *
     * @return
     */
    public double getZ() {
        return Z;
    }

    /**
     * Set Nome
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Set X
     *
     * @param X
     */
    public void setX(double X) {
        this.X = X;
    }

    /**
     * Set Y
     *
     * @param Y
     */
    public void setY(double Y) {
        this.Y = Y;
    }

    /**
     * Set Z
     *
     * @param Z
     */
    public void setZ(double Z) {
        this.Z = Z;
    }

    /**
     * Constrói um vetor com o ponto atual
     *
     * @return uma instancia de Vetor
     */
    public Vetor vetor() {
        return new Vetor(this.X, this.Y, this.Z, 1);
    }

    /**
     * Codigo Hash
     *
     * @return hash = 5
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    /**
     * Comparador entre dois pontos
     *
     * @param objeto do tipo ponto
     * @return se igual true, senão false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ponto other = (Ponto) obj;
        if (this.X != other.X) {
            return false;
        }
        if (this.Y != other.Y) {
            return false;
        }
        if (this.Z != other.Z) {
            return false;
        }
        return true;
    }

    /**
     * Get nX
     *
     * @return
     */
    public double getnX() {
        return nX;
    }

    /**
     * set nX
     *
     * @param nX
     */
    public void setnX(double nX) {
        this.nX = nX;
    }

    /**
     * Get nY
     *
     * @return
     */
    public double getnY() {
        return nY;
    }

    /**
     * set nY
     *
     * @param nY
     */
    public void setnY(double nY) {
        this.nY = nY;
    }

    /**
     * get nZ
     *
     * @return
     */
    public double getnZ() {
        return nZ;
    }

    /**
     * set nZ
     *
     * @param nZ
     */
    public void setnZ(double nZ) {
        this.nZ = nZ;
    }

    /**
     *
     * @return
     */
    public double getIr() {
        return Ir;
    }

    /**
     * set Ir
     *
     * @param Ir
     */
    public void setIr(double Ir) {
        this.Ir = Ir;
    }

    /**
     * Get Ig
     *
     * @return
     */
    public double getIg() {
        return Ig;
    }

    /**
     * set Ig
     *
     * @param Ig
     */
    public void setIg(double Ig) {
        this.Ig = Ig;
    }

    /**
     * get Ib
     *
     * @return
     */
    public double getIb() {
        return Ib;
    }

    /**
     * set Ib
     *
     * @param Ib
     */
    public void setIb(double Ib) {
        this.Ib = Ib;
    }

    public double getmX() {
        return mX;
    }

    public void setmX(double mX) {
        this.mX = mX;
    }

    public double getmY() {
        return mY;
    }

    public void setmY(double mY) {
        this.mY = mY;
    }

    public double getmZ() {
        return mZ;
    }

    public void setmZ(double mZ) {
        this.mZ = mZ;
    }

    public double getCameraZ() {
        return cameraZ;
    }

    public void setCameraZ(double cameraZ) {
        this.cameraZ = cameraZ;
    }
    
    
}
