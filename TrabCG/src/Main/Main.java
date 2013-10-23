/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Classes.Matriz;
import Interface.Interface;

/**
 *
 * @author alienware
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
        }

        Init i = new Init();
        i.Inicia();



    }
}
