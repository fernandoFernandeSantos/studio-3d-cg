/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;

/**
 *
 * @author alienware
 */
public class Matriz//muda de tamanho 
{

    private double[][] matriz;

    /**
     * Constroi um matriz com tamanho 4x4
     */
    public Matriz() {
        this.matriz = new double[4][4];
    }

    /**
     * Constroi uma matriz com tamanho LxC
     *
     * @param l
     * @param c
     */
    public Matriz(int l, int c) {
        this.matriz = new double[l][c];
    }

    /**
     * Seta uma matriz identidade
     */
    public void setIdentidade() {
        int diagonal = this.getColunas() > this.getLinhas() ? this.getLinhas() : this.getColunas();
        for (int i = 0; i < diagonal; i++) {
            this.set(i, i, 1);
        }
    }

    /**
     * Set um valor em determinada posição
     *
     * @param x
     * @param y
     * @param valor
     */
    public void set(int x, int y, double valor) {
        if (x >= matriz.length || y >= matriz[0].length)//verifica se algum indice esta fora do limite
        {
            int linhas = x >= matriz.length ? x + 1 : matriz.length;//calcula o novo numero de linhas
            int colunas = y >= matriz[0].length ? y + 1 : matriz[0].length;//calcula o novo numero de colunas

            double aux[][] = new double[linhas][colunas];//faz a copia dos valores
            for (int i = 0; i < this.matriz.length; i++) {
                System.arraycopy(this.matriz[i], 0, aux[i], 0,
                        this.matriz[i].length);
            }
            this.matriz = aux;
        }
        this.matriz[x][y] = valor;//atribui o valor no indice

    }

    public double get(int x, int y) {
        return matriz[x][y];
    }

    public void clear() {
        this.matriz = new double[3][3];
    }

    public void clear(int linhas, int colunas) {
        this.matriz = new double[linhas][colunas];
    }

    public void print(String nome) {
        System.out.print("Matriz " + nome + " (");
        for (int i = 0; i < this.getLinhas(); i++) {
            System.out.print("\n\t");
            for (int j = 0; j < this.getColunas(); j++) {
                System.out.print(this.get(i, j) + " | ");
            }
        }
        System.out.print("\n)\n");
    }

    public int getColunas() {
        return this.matriz[0].length;
    }

    public int getLinhas() {
        return this.matriz.length;
    }

    /**
     * Copia a matriz do parametro para this
     *
     * @param m
     */
    public void copy(Matriz m) {
        for (int i = m.getLinhas() - 1; i >= 0; i--) {
            for (int j = m.getColunas() - 1; j >= 0; j--) {
                this.set(i, j, m.get(i, j));
            }
        }
    }

    /**
     * Copia a matriz this e retorna a copia
     *
     * @return matriz
     */
    public Matriz copy() {
        Matriz resultado = new Matriz();
        for (int i = this.getLinhas() - 1; i >= 0; i--) {
            for (int j = this.getColunas() - 1; j >= 0; j--) {
                resultado.set(i, j, this.get(i, j));
            }
        }
        return resultado;
    }

    /**
     * Seta um valor para uma linha especifica
     *
     * @param linha
     * @param valor
     */
    public void setLine(int linha, double valor) {
        this.set(linha, 0, valor);
        for (int i = 0; i < this.getColunas(); i++) {
            this.set(linha, i, valor);
        }
    }

    /**
     * adiciona as cordenadas de um ponto a matriz
     *
     * @param p
     */
    public void addPonto(Ponto p) {
        int aux = this.getColunas();
        this.set(0, aux, p.getX());
        this.set(1, aux, p.getY());
        this.set(2, aux, p.getZ());
        this.set(3, aux, 1);
    }

    /**
     * Adiciona um array de pontos a matriz
     *
     * @param pontos
     */
    public void addPontos(ArrayList<Ponto> pontos) {
        int iterador = this.getColunas() + pontos.size() - 1;
        for (Ponto p : pontos) {
            this.set(0, iterador, p.getX());
            this.set(1, iterador, p.getY());
            this.set(2, iterador, p.getZ());
            iterador--;
        }
        this.setLine(3, 1);
    }

    public void setPontos(ArrayList<Ponto> pontos) {

        this.clear(4, pontos.size());
        int iterador = 0;
        for (Ponto p : pontos) {
            this.set(0, iterador, p.getX());
            this.set(1, iterador, p.getY());
            this.set(2, iterador, p.getZ());
            iterador++;
        }
        this.setLine(3, 1);
    }

    /**
     * preenche a matriz com o valor
     *
     * @param valor
     */
    public void fill(double valor) {
        for (int i = 0; i < this.getLinhas(); i++) {
            for (int j = 0; j < this.getColunas(); j++) {
                this.matriz[i][j] = valor;
            }
        }
    }

//--------------------------------
//    funções aritméticas
//--------------------------------
    /**
     * soma de duas matrizes this + a
     *
     * @param a
     */
    public void soma(Matriz a) {//soma na matriz objeto
        try {
            if (this.getLinhas() != a.getLinhas() || this.getColunas() != a.getColunas()) {
                throw new Exception();
            }
            for (int i = 0; i < this.getLinhas(); i++) {
                for (int j = 0; j < this.getColunas(); j++) {
                    this.matriz[i][j] += a.get(i, j);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Matrizes com tamanhos diferentes");
        }

    }

    /**
     * Soma de duas matrizes a + b
     *
     * @param a
     * @param b
     * @return Matriz
     */
    public static Matriz soma(Matriz a, Matriz b) {//retorna uma soma matris com a soma das atrizes dos parametros

        try {
            if (a.getLinhas() != b.getLinhas() || a.getColunas() != b.getColunas()) {
                throw new Exception();
            }
            Matriz resultado = new Matriz(a.getLinhas(), a.getColunas());

            for (int i = 0; i < a.getLinhas(); i++) {
                for (int j = 0; j < a.getColunas(); j++) {
                    resultado.set(i, j, a.get(i, j) + b.get(i, j));
                }
            }
            return resultado;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Matrizes com tamanhos diferentes");
        }
        return null;

    }

    /**
     * Multiplicação de duas matrizes this * a
     *
     * @param a
     */
    public void multiplicacao(Matriz a) {
        try {
            if (this.getColunas() != a.getLinhas()) {
                throw new Exception();
            }

            Matriz resultado = new Matriz(this.getLinhas(), a.getColunas());
            double aux;

            for (int i = 0; i < this.getLinhas(); i++) {
                for (int j = 0; j < a.getColunas(); j++) {
                    aux = 0;
                    for (int k = 0; k < this.getColunas(); k++) {
                        aux += this.get(i, k) * a.get(k, j);
                    }
                    resultado.set(i, j, aux);

                }
            }
            for (int i = resultado.getLinhas() - 1; i >= 0; i--) {
                for (int j = resultado.getColunas() - 1; j >= 0; j--) {
                    this.set(i, j, resultado.get(i, j));
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println(
                    "Numero de colunas de a difere do numero de linhas de b");
        }
    }

    /**
     * multiplicação de duas matrizes a * b
     *
     * @param a
     * @param b
     * @return Matriz
     */
    public static Matriz multiplicacao(Matriz a, Matriz b) {
        try {
            if (a.getColunas() != b.getLinhas()) {
                throw new Exception();
            }
            Matriz resultado = new Matriz(a.getLinhas(), b.getColunas());
            double aux;

            for (int i = 0; i < a.getLinhas(); i++) {
                for (int j = 0; j < b.getColunas(); j++) {
                    aux = 0;
                    for (int k = 0; k < a.getColunas(); k++) {
                        aux += a.get(i, k) * b.get(k, j);
                    }
                    resultado.set(i, j, aux);

                }
            }

            return resultado;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println(
                    "Numero de colunas de a difere do numero de linhas de b");
        }


        return null;
    }

    /**
     * Multiplicação por escalar this * a
     *
     * @param a
     * @param escalar
     * @return
     */
    public static Matriz multiplicacaoEscalar(Matriz a, double escalar) {
        Matriz resultado = new Matriz(a.getLinhas(), a.getColunas());
        for (int i = 0; i < resultado.getLinhas(); i++) {
            for (int j = 0; j < resultado.getColunas(); j++) {
                resultado.set(i, j, escalar * a.get(i, j));
            }
        }
        return resultado;
    }

    /**
     * Determinante de p1, p2 e p3
     *
     * @param p1
     * @param p2
     * @param p3
     * @return double
     */
    public static double getDeterminante(Ponto p1, Ponto p2, Ponto p3) {
        double res = 0;

        res += (p1.getX() * p2.getY() * p3.getZ());
        res += (p1.getY() * p2.getZ() * p3.getX());
        res += (p1.getZ() * p2.getX() * p3.getY());
        res -= (p1.getZ() * p2.getY() * p3.getX());
        res -= (p1.getX() * p2.getZ() * p3.getY());
        res -= (p1.getY() * p2.getX() * p3.getZ());

        return res;
    }
    //--------------------------------
    // funções de geração de matrizes de trasformação
    //--------------------------------

    /**
     * Gera um matriz de translação
     *
     * @param x translação em x
     * @param y translação em y
     * @param z translação em z
     * @return
     */
    public static Matriz gerarTranslacao(double x, double y, double z) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(0, 3, x);
        resultado.set(1, 3, y);
        resultado.set(2, 3, z);
        return resultado;
    }

    /**
     * Gera um matriz de escala
     *
     * @param x escala e x
     * @param y escala em y
     * @param z escala em z
     * @return
     */
    public static Matriz gerarEscala(double x, double y, double z) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(0, 0, x);
        resultado.set(1, 1, y);
        resultado.set(2, 2, z);
        return resultado;
    }

    /**
     * Gera matriz de rotação em X
     *
     * @param angulo
     * @return Matriz
     */
    public static Matriz gerarRotacaoX(double angulo) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();

        double rad = (Math.PI / 180) * angulo;

        resultado.set(1, 1, (double) Math.cos(rad));
        resultado.set(1, 2, (double) Math.sin(rad) * -1);
        resultado.set(2, 1, (double) Math.sin(rad));
        resultado.set(2, 2, (double) Math.cos(rad));
        return resultado;
    }

    /**
     * Gera matriz de rotação em y
     *
     * @param angulo
     * @return Matriz
     */
    public static Matriz gerarRotacaoY(double angulo) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();

        double rad = (Math.PI / 180) * angulo;

        resultado.set(0, 0, (double) Math.cos(rad));
        resultado.set(0, 2, (double) Math.sin(rad));
        resultado.set(2, 0, (double) Math.sin(rad) * -1);
        resultado.set(2, 2, (double) Math.cos(rad));
        return resultado;
    }

    /**
     * Gera matriz de rotação em z
     *
     * @param angulo
     * @return Matriz
     */
    public static Matriz gerarRotacaoZ(double angulo) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();

        double rad = (Math.PI / 180) * angulo;

        resultado.set(0, 0, (double) Math.cos(rad));
        resultado.set(0, 1, (double) Math.sin(rad) * -1);
        resultado.set(1, 0, (double) Math.sin(rad));
        resultado.set(1, 1, (double) Math.cos(rad));
        return resultado;
    }

    /**
     * Gera matriz de reflexão em X
     *
     * @return Matriz
     */
    public static Matriz gerarReflexaoX() {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(0, 0, -1);
        return resultado;
    }

    /**
     * Gera matriz de reflexão em Y
     *
     * @return Matriz
     */
    public static Matriz gerarReflexaoY() {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(1, 1, -1);
        return resultado;
    }

    /**
     * Gera matriz de reflexão em Z
     *
     * @return Matriz
     */
    public static Matriz gerarReflexaoZ() {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(2, 2, -1);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento no z
     *
     * @param x
     * @param y
     * @return Matriz
     */
    public static Matriz gerarCizalhamentoOnZ(double x, double y) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(0, 1, x);
        resultado.set(1, 0, y);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento de X no Z
     *
     * @param x
     * @return
     */
    public static Matriz gerarCizalhamentoXonZ(double x) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(0, 1, x);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento de Y no Z
     *
     * @param x
     * @return
     */
    public static Matriz gerarCizalhamentoYonZ(double x) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(1, 0, x);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento no X
     *
     * @param y
     * @param z
     * @return
     */
    public static Matriz gerarCizalhamentoOnX(double y, double z) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(1, 2, y);
        resultado.set(2, 1, z);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento de Y no X
     *
     * @param x
     * @return
     */
    public static Matriz gerarCizalhamentoYonX(double x) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(1, 2, x);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento de Z no X
     *
     * @param x
     * @return
     */
    public static Matriz gerarCizalhamentoZonX(double x) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(2, 1, x);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento no Y
     *
     * @param x
     * @param z
     * @return
     */
    public static Matriz gerarCizalhamentoOnY(double x, double z) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(2, 0, x);
        resultado.set(0, 2, z);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento de X no Y
     *
     * @param x
     * @return
     */
    public static Matriz gerarCizalhamentoXonY(double x) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(2, 0, x);
        return resultado;
    }

    /**
     * Gera matriz de cizalhamento de z no y
     *
     * @param x
     * @return
     */
    public static Matriz gerarCizalhamentoZonY(double x) {
        Matriz resultado = new Matriz(4, 4);
        resultado.setIdentidade();
        resultado.set(0, 2, x);
        return resultado;
    }

    //--------------------------------
    // funções de trasformação
    //--------------------------------
    /**
     * Remove da matriz a linha da posição do parametro
     *
     * @param linhaCortar
     * @return
     */
    public Matriz cut(int linhaCortar) {
        Matriz aux = new Matriz(this.getLinhas() - 1, this.getColunas());
        int cont = 0;

        for (int j = 0; j < this.getLinhas(); j++) {
            if (j != linhaCortar) {
                for (int k = 0; k < this.getColunas(); k++) {
                    aux.set(j - cont, k, this.get(j, k));
                }
            } else {
                cont = 1;
            }
        }

        return aux;
    }

    /**
     * O método gera a matriz de inversão do eixo y para coordenadas de tela
     *
     * @param xmin
     * @param ymin
     * @param xmax
     * @param ymax
     * @return
     */
    public static Matriz gerarJPV(double xmin, double ymin, double xmax, double ymax) {
        double umax = xmax;
        double vmax = ymax;
        double umin = xmin;
        double vmin = ymin;

        Matriz res = new Matriz(3, 3);

        res.setIdentidade();

        res.set(0, 0, (umax - umin) / (xmax - xmin));
        res.set(0, 2, (-xmin) * ((umax - umin) / (xmax - xmin)) + umin);
        res.set(1, 1, (vmin - vmax) / (ymax - ymin));
        res.set(1, 2, ymin * ((vmax - vmin) / (ymax - ymin)) + vmax);
        return res;

    }
}