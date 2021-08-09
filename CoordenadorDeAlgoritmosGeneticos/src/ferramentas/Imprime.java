package ferramentas;

import dados.Mux;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jabour
 */
public class Imprime {

    public static void msg(String s) {
        System.out.println(s);
    }

    public static void matriz(int m[][]) {
        int i, j;
        msg("-----------------------------------------------------");
        for (i = 0; i < m.length; i++) {
            for (j = 0; j < m[0].length; j++) {
                System.out.print(" " + m[i][j]);
            }
            msg("");
        }
        msg("-----------------------------------------------------");
    }

    public static void duasMatrizes(int m1[][], int m2[][]) {
        int i, j;
        if (m1.length != m2.length) {
            msg("Erro no método Imprime.duasMatrizes. "
                    + "Número de linhas diferentes.");
            return;
        }
        msg("-----------------------------------------------------");
        for (i = 0; i < m1.length; i++) {
            for (j = 0; j < m1[0].length; j++) {
                System.out.print(" " + m1[i][j]);
            }
            System.out.print(" |");
            for (j = 0; j < m2[0].length; j++) {
                System.out.print(" " + m2[i][j]);
            }
            msg("");
        }
        msg("-----------------------------------------------------");
    }

    public static void vetor(int v[]) {
        int i;
        msg("\n-----------------------------------------------------");
        for (i = 0; i < v.length; i++) {
            System.out.print(" " + v[i]);
        }
        msg("\n-----------------------------------------------------\n");
    }

    public static void gravaSaidaEmDisco(String nome, ArrayList<Mux> muxes) {
        try (FileOutputStream arquivoEmDisco = new FileOutputStream(nome)) {
            String s = "";
            for ( int i = 0 ; i < muxes.size() ; i++ ){
                s += muxes.get(i).toString()+"\n";
            }
            byte b[] = s.getBytes();
            arquivoEmDisco.write(b);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Imprime.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Imprime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
