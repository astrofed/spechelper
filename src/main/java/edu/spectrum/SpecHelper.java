package edu.spectrum;

import javax.swing.*;

import edu.spectrum.gui.SpecHelperFrame;
import edu.spectrum.logging.Messager;

public class SpecHelper {
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                    .getInstalledLookAndFeels())
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            Messager.publish("SYNTHelper - main", ex);
        } catch (InstantiationException ex) {
            Messager.publish("SYNTHelper - main", ex);
        } catch (IllegalAccessException ex) {
            Messager.publish("SYNTHelper - main", ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Messager.publish("SYNTHelper - main", ex);
        }
        // </editor-fold>

		/* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SpecHelperFrame app = new SpecHelperFrame();
                app.setVisible(true);
                app.setExtendedState(app.getExtendedState()
                        | JFrame.MAXIMIZED_BOTH);
            }
        });
    }
}
