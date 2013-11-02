/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Interface.Interface;

/**
 * Somente inicia a interface
 * @author Mateus
 */
public class Init {

    Interface i;

    public void Inicia() {
        i = new Interface(this);
        i.setVisible(true);
    }
    public void criaNovo(Interface it) {
        it.setVisible(false);
        it = null;
        System.gc();
        i = new Interface(this);
        i.setVisible(true);
    }

 
}
