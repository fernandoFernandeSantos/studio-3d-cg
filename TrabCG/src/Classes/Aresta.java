/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author alienware
 */
public class Aresta implements java.io.Serializable
{

    private Ponto p1;
    private Ponto p2;
    private String nome;

    public Aresta()
    {
        p1 = null;
        p2 = null;
        nome = "";
    }

    public Aresta(Ponto ponto_1, Ponto ponto_2, String nome)
    {
        this.p1 = ponto_1;
        this.p2 = ponto_2;
        this.nome = nome;
    }

    public Ponto getP1()
    {
        return p1;
    }

    public Ponto getP2()
    {
        return p2;
    }

    public String getNome()
    {
        return nome;
    }

    public void setPonto_1(Ponto ponto_1)
    {
        this.p1 = ponto_1;
    }

    public void setPonto_2(Ponto ponto_2)
    {
        this.p2 = ponto_2;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }
}
