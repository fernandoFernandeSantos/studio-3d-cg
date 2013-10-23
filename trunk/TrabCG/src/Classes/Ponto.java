/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author alienware
 */
public class Ponto implements java.io.Serializable{

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

    public Ponto getNormal() {
        return new Ponto("", nX, nY, nZ);
    }
    
    public Ponto() {
        nome = "";
    }

    public Ponto(String nome, double X, double Y, double Z) {
        this.nome = nome;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public Ponto copy() {
        Ponto r = new Ponto(this.nome, this.X, this.Y, this.Z);
        return r;
    }

    public void print(String s) {
        System.out.println(s + " -> nome = " + this.getNome() + " x= " + this.getX() + " y= " + this.getY() + " Z= " + this.getZ() + " \n");
    }

    public String getNome() {
        return nome;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setX(double X) {
        this.X = X;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public void setZ(double Z) {
        this.Z = Z;
    }

    public Vetor vetor() {
        return new Vetor(this.X, this.Y, this.Z, 1);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

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
    
    public double getnX() {
        return nX;
    }

    public void setnX(double nX) {
        this.nX = nX;
    }

    public double getnY() {
        return nY;
    }

    public void setnY(double nY) {
        this.nY = nY;
    }

    public double getnZ() {
        return nZ;
    }

    public void setnZ(double nZ) {
        this.nZ = nZ;
    }

    public double getIr() {
        return Ir;
    }

    public void setIr(double Ir) {
        this.Ir = Ir;
    }

    public double getIg() {
        return Ig;
    }

    public void setIg(double Ig) {
        this.Ig = Ig;
    }

    public double getIb() {
        return Ib;
    }

    public void setIb(double Ib) {
        this.Ib = Ib;
    }
}
