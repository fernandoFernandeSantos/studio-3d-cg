// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Luz.java

package Classes;

import java.io.Serializable;

// Referenced classes of package objetos:
//            Ponto3D

public class Iluminacao
    implements Serializable
{

    public Iluminacao(Ponto local, double Ir, double Ig, double Ib)
    {
        this.local = local;
        this.Ir = Ir;
        this.Ig = Ig;
        this.Ib = Ib;
    }

    public Iluminacao()
    {
        local = new Ponto();
    }

    public double getIr()
    {
        return Ir;
    }

    public void setIr(double Ir)
    {
        this.Ir = Ir;
    }

    public double getIb()
    {
        return Ib;
    }

    public void setIb(double Ib)
    {
        this.Ib = Ib;
    }

    public double getIg()
    {
        return Ig;
    }

    public void setIg(double Ig)
    {
        this.Ig = Ig;
    }

    public Ponto getLocal()
    {
        return local;
    }

    public void setLocal(Ponto  local)
    {
        this.local = local;
    }

    public Ponto local;
    public double Ir;
    public double Ig;
    public double Ib;
}
