package dados;

import java.util.Arrays;

/**
 *
 * @author jabour
 */
public class Topologia {

    private int topologia[][];
    private int LINHAS_NOS, COLUNAS_NOS;

    public Topologia(int LINHAS_NOS, int COLUNAS_NOS, int COLUNAS) {
        int i, j;
        this.LINHAS_NOS = LINHAS_NOS;
        this.COLUNAS_NOS = COLUNAS_NOS;
        topologia = new int[LINHAS_NOS][COLUNAS_NOS];
        for (i = 0; i < COLUNAS_NOS; i++) {
            for (j = 0; j < LINHAS_NOS; j++) {
                topologia[j][i] = ++COLUNAS;
            }
        }
    }

    public void imprime() {
        ferramentas.Imprime.matriz(topologia);
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < LINHAS_NOS; i++) {
            str += Arrays.toString(topologia[i])+"\n";
        }
        return str;
    }

    public int getNumLinhas() {
        return topologia.length;
    }

    public int getNumCol() {
        return topologia[0].length;
    }
}
