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


public class MyFileHandler {

    private RandomAccessFile File;
    private Interface i;

    public MyFileHandler(Interface _i) throws FileNotFoundException {
        i = _i;
    }


    public void openFile(File arquivo, String type) throws FileNotFoundException {
        this.File = new RandomAccessFile(arquivo, type);
    }


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
    }

    public void Load() throws IOException {
        int Polsize = this.File.readInt();
        ArrayList<Poligono> lista = i.getPoligonos();
        ArrayList<Poligono> listaTransformada = i.getPoligonosTransformados();

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

    public void saveCor(Color c) throws IOException {
        this.File.writeInt(c.getRed());
        this.File.writeInt(c.getGreen());
        this.File.writeInt(c.getBlue());
    }

    public Color loadCor() throws IOException {
        int r = this.File.readInt();
        int g = this.File.readInt();
        int b = this.File.readInt();
        return new Color(r, g, b);
    }

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

        this.saveCor(p.getCor());
        this.saveCor(p.getCorFace());
    }

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

        Color Cp = this.loadCor();
        Color Cpf = this.loadCor();

        p.setCor(Cp);
        p.setCorFace(Cpf);

    }
}