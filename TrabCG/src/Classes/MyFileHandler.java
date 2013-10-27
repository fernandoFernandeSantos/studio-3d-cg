/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interface.Interface;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Classe que faz as operações com arquivos para o salvamento da cena
 *
 * @author Fernando Fernandes
 */
public class MyFileHandler {

    private RandomAccessFile File;
    private Interface i;

    /**
     * Constroi a classe com uma referencia a interface
     *
     * @param _i
     * @throws FileNotFoundException
     */
    public MyFileHandler(Interface _i) throws FileNotFoundException {
        i = _i;
    }

    /**
     * Abre o arquivo
     *
     * @param arquivo
     * @param type
     * @throws FileNotFoundException
     */
    public void openFile(File arquivo, String type) throws FileNotFoundException {
        this.File = new RandomAccessFile(arquivo, type);
    }

    /**
     * Salva primeiro o tipo , raio, numero de pontos e altura de todos os
     * poligonos da cena
     *
     * @throws IOException
     */
    public void Save() throws IOException {

        this.File.writeInt(i.getPoligonos().size());
        for (Poligono p : i.getPoligonos()) {
            this.saveString(p.getTipo());//salva tipo do poligono
            this.File.writeInt(p.getRaio());//salva o raio de construção
            this.File.writeInt(p.getNumPontos());//salva o numero d pontos de construção
            this.File.writeInt(p.getAltura());//slava a altura de contrução
            this.savePolAtributos(p);//sava os atributos do poligono
        }
        this.saveCamera(i.getCamera());
        //salva os atributos da iluminação fundo depois a de ambiente
        this.saveIluminacao(i.getLuzFundo());
        this.saveIluminacao(i.getLuzAmbiente());
    }

    /**
     * Faz o load da cena
     *
     * @throws IOException
     */
    public void Load() throws IOException {
        int Polsize = this.File.readInt();
        ArrayList<Poligono> lista = i.getPoligonos();
        ArrayList<Poligono> listaTransformada = i.getPoligonosTransformados();
        //limpa a lista que está em memória
        lista.clear();
        listaTransformada.clear();

        this.i.prismas = 0;
        this.i.piramides = 0;
        this.i.esferas = 0;

        for (int i = 0; i < Polsize; i++) {

            Poligono p = new Poligono();

            String tipo = this.loadString();
            int raio = this.File.readInt();
            int numPontos = this.File.readInt();
            int altura = this.File.readInt();
            System.out.println("raio = " + raio);
            System.out.println("numpontos = " + numPontos);
            System.out.println("altura = " + altura);

            if (tipo.equals("Prisma")) {
                p.GerarPrisma(numPontos, raio, altura);
                this.i.prismas++;
                this.i.addPoligonosBox("Prisma", this.i.prismas);
            }
            if (tipo.equals("Piramide")) {
                p.GerarPiramide(numPontos, raio, altura);
                this.i.piramides++;
                this.i.addPoligonosBox("Piramide", this.i.piramides);
            }
            if (tipo.equals("Esfera")) {
                p.GerarEsfera(numPontos, raio);
                this.i.esferas++;
                this.i.addPoligonosBox("Esfera", this.i.esferas);
            }

            this.loadPolAtributos(p);

            this.i.addPoligono(p);

        }
        Camera c = this.loadCamera();
        //load da iluminação do poligono
        Iluminacao luzFundo = this.loadIluminacao();
        Iluminacao luzAmbiente = this.loadIluminacao();
        i.setLuzFundo(luzFundo);
        i.setLuzAmbiente(luzAmbiente);
        i.setCamera(c);
    }

    //helpers
    public void saveString(String s) throws IOException {
        this.File.writeInt(s.length());
        this.File.writeChars(s);
    }

    public String loadString() throws IOException {
        int length = this.File.readInt();
        String s = "";
        for (int i = 0; i < length; i++) {
            s += this.File.readChar();
        }
        return s;
    }

    public void saveCamera(Camera c) throws IOException {
        this.File.writeDouble(c.getVx());
        this.File.writeDouble(c.getVy());
        this.File.writeDouble(c.getVz());

        this.File.writeDouble(c.getFPx());
        this.File.writeDouble(c.getFPy());
        this.File.writeDouble(c.getFPz());

        this.File.writeDouble(c.getDistancia());

    }

    public Camera loadCamera() throws IOException {
        double Vx = this.File.readDouble();
        double Vy = this.File.readDouble();
        double Vz = this.File.readDouble();

        double FPx = this.File.readDouble();
        double FPy = this.File.readDouble();
        double FPz = this.File.readDouble();

        double Distancia = this.File.readDouble();
        return new Camera(Vx, Vy, Vz, FPx, FPy, FPz, Distancia);

    }

    /**
     * Salva os valores RGB no arquivo
     *
     * @param c Color
     * @throws IOException
     */
    public void saveCor(Color c) throws IOException {
        this.File.writeInt(c.getRed());
        this.File.writeInt(c.getGreen());
        this.File.writeInt(c.getBlue());
    }

    /**
     * Constroi uma nova cor de acordo com os dados do arquivo
     *
     * @return Color
     * @throws IOException
     */
    public Color loadCor() throws IOException {
        int r = this.File.readInt();
        int g = this.File.readInt();
        int b = this.File.readInt();
        return new Color(r, g, b);
    }

    /**
     * Salva todos os atributos do poligono
     *
     * @param p
     * @throws IOException
     */
    public void savePolAtributos(Poligono p) throws IOException {
        this.File.writeDouble(p.getRotationX());
        this.File.writeDouble(p.getRotationY());
        this.File.writeDouble(p.getRotationZ());

        this.File.writeDouble(p.getScaleX());
        this.File.writeDouble(p.getScaleY());
        this.File.writeDouble(p.getScaleZ());

        this.File.writeDouble(p.getShearXonY());
        this.File.writeDouble(p.getShearXonZ());
        this.File.writeDouble(p.getShearYonX());
        this.File.writeDouble(p.getShearYonZ());
        this.File.writeDouble(p.getShearZonX());
        this.File.writeDouble(p.getShearZonY());

        this.File.writeLong(p.getTranslationX());
        this.File.writeLong(p.getTranslationY());
        this.File.writeLong(p.getTranslationZ());
        /*
         * novo salve
         *double kar; double kdr;
         double ksr;  double kag;
         double kdg; double ksg;
         double kab; double kdb;
         double ksb; double kt;
         double n;
         */
        this.File.writeDouble(p.getKaR());
        this.File.writeDouble(p.getKdR());
        this.File.writeDouble(p.getKsR());
        this.File.writeDouble(p.getKaG());
        this.File.writeDouble(p.getKdG());
        this.File.writeDouble(p.getKsG());
        this.File.writeDouble(p.getKaB());
        this.File.writeDouble(p.getKdB());
        this.File.writeDouble(p.getKsB());
        this.File.writeDouble(p.getKt());
        this.File.writeDouble(p.getN());


        this.saveCor(p.getCor());
        this.saveCor(p.getCorFace());
    }

    /**
     * Faz o load para os atributos de um poligono
     *
     * @param p
     * @throws IOException
     */
    public void loadPolAtributos(Poligono p) throws IOException {//referencia
        double Rx = this.File.readDouble();
        double Ry = this.File.readDouble();
        double Rz = this.File.readDouble();

        p.Rotacionar(Rx, Ry, Rz);

        double SCx = this.File.readDouble();
        double SCy = this.File.readDouble();
        double SCz = this.File.readDouble();

        p.Escalar(SCx, SCy, SCz);

        double ShXonY = this.File.readDouble();
        double ShXonZ = this.File.readDouble();
        double ShYonX = this.File.readDouble();
        double ShYonZ = this.File.readDouble();
        double ShZonX = this.File.readDouble();
        double ShZonY = this.File.readDouble();

        p.CizalharXonY(ShXonY);
        p.CizalharXonZ(ShXonZ);
        p.CizalharYonX(ShYonX);
        p.CizalharYonZ(ShYonZ);
        p.CizalharZonX(ShZonX);
        p.CizalharZonY(ShZonY);

        long Tx = this.File.readLong();
        long Ty = this.File.readLong();
        long Tz = this.File.readLong();

        p.Transladar(Tx, Ty, Tz);
        /*
         * novo load
         *double kar; double kdr;
         double ksr;  double kag;
         double kdg;  double ksg;
         double kab; double kdb;
         double ksb; double kt;
         double n; */
        double kar = this.File.readDouble();
        double kdr = this.File.readDouble();
        double ksr = this.File.readDouble();
        double kag = this.File.readDouble();
        double kdg = this.File.readDouble();
        double ksg = this.File.readDouble();
        double kab = this.File.readDouble();
        double kdb = this.File.readDouble();
        double ksb = this.File.readDouble();
        double kt = this.File.readDouble();
        double n = this.File.readDouble();

        p.setKaR(kar);
        p.setKdR(kdr);
        p.setKsR(ksr);
        p.setKaG(kag);
        p.setKdG(kdg);
        p.setKsG(ksg);
        p.setKaB(kab);
        p.setKdB(kdb);
        p.setKsB(ksb);
        p.setKt(kt);
        p.setN(n);

        Color Cp = this.loadCor();
        Color Cpf = this.loadCor();

        p.setCor(Cp);
        p.setCorFace(Cpf);

    }

    /**
     * Salva a iluminação logo após a câmera
     *
     * @param lighting Iluminacao
     * @throws IOException
     */
    private void saveIluminacao(Iluminacao lighting) throws IOException {
        //RGB
        this.File.writeDouble(lighting.getIr());
        this.File.writeDouble(lighting.getIg());
        this.File.writeDouble(lighting.getIb());
        //salva o ponto
        this.savePoint(lighting.getLocal());
    }

    /**
     * Salva um ponto da iluminação
     *
     * @param pt
     * @throws IOException
     */
    private void savePoint(Ponto pt) throws IOException {
        this.saveString(pt.getNome());
        this.File.writeDouble(pt.getX());
        this.File.writeDouble(pt.getY());
        this.File.writeDouble(pt.getZ());
        this.File.writeDouble(pt.getnX());
        this.File.writeDouble(pt.getnY());
        this.File.writeDouble(pt.getnZ());

        this.File.writeDouble(pt.getIr());
        this.File.writeDouble(pt.getIg());
        this.File.writeDouble(pt.getIb());

        this.File.writeDouble(pt.getmX());
        this.File.writeDouble(pt.getmY());
        this.File.writeDouble(pt.getmZ());

        this.File.writeDouble(pt.getCameraZ());

    }

    /**
     * Carrega a iluminação
     *
     * @return
     * @throws IOException
     */
    private Iluminacao loadIluminacao() throws IOException {
        //pega na mesma sequencia que foi gravado RGB
        double ir = this.File.readDouble();
        double ig = this.File.readDouble();
        double ib = this.File.readDouble();

        Ponto aPoint = this.loadPonto();

        return (new Iluminacao(aPoint, ir, ig, ib));


    }

    /**
     * Faz load do ponto
     *
     * @return
     * @throws IOException
     */
    private Ponto loadPonto() throws IOException {
        String nome = this.loadString();
        double x = this.File.readDouble();
        double y = this.File.readDouble();
        double z = this.File.readDouble();
        double nx = this.File.readDouble();
        double ny = this.File.readDouble();
        double nz = this.File.readDouble();
        double ir = this.File.readDouble();
        double ig = this.File.readDouble();
        double ib = this.File.readDouble();
        double mx = this.File.readDouble();
        double my = this.File.readDouble();
        double mz = this.File.readDouble();
        double Cameraz = this.File.readDouble();

        Ponto aux = new Ponto(nome, x, y, z);
        aux.setCameraZ(Cameraz);
        aux.setIb(ib);
        aux.setIg(ig);
        aux.setIr(ir);
        aux.setmX(mx);
        aux.setmY(my);
        aux.setmZ(mz);
        aux.setnX(nx);
        aux.setnY(ny);
        aux.setnZ(nz);
        return aux;

    }
}