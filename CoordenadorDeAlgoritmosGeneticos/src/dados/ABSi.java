package dados;

import ferramentas.Imprime;


/**
 *
 * @author jabour
 */
public class ABSi {

    int numLinhasMatrizAnterior;
    int matrizAbsi[][];

    //Instancia absi com base na abs completa
    public ABSi(int numLinhasMatrizAnterior, int COLUNAS, int[][] abs, int s,
            int[] filtro) {
        int j;
        this.numLinhasMatrizAnterior = numLinhasMatrizAnterior;
        matrizAbsi = new int[contaUns(filtro)][COLUNAS + 1];
        int posABSi = 0;
        for (int i = 0; i < numLinhasMatrizAnterior; i++) {
            if (filtro[i] == 1) {
                for (j = 0; j < COLUNAS; j++) {
                    matrizAbsi[posABSi][j] = abs[i][j];
                }
                matrizAbsi[posABSi++][j] = abs[i][COLUNAS + s];
            }
        }
    }

    //Instancia absi já com base em uma absi anterior (com um s só)
    public ABSi(int numLinhasMatrizAnterior, int COLUNAS, int[][] absi,
            int[] filtro) {
        int j;
        this.numLinhasMatrizAnterior = numLinhasMatrizAnterior;
        matrizAbsi = new int[contaUns(filtro)][COLUNAS + 1];
        int posABSi = 0;
        for (int i = 0; i < numLinhasMatrizAnterior; i++) {
            if (filtro[i] == 1) {
                for (j = 0; j < COLUNAS; j++) {
                    matrizAbsi[posABSi][j] = absi[i][j];
                }
                matrizAbsi[posABSi++][j] = absi[i][COLUNAS];
            }
        }
    }

    public int[] getLinha(int i) {
        return matrizAbsi[i];
    }

    private int contaUns(int[] f) {
        int qtd = 0;
        for (int i = 0; i < numLinhasMatrizAnterior; i++) {
            if (f[i] == 1) {
                qtd++;
            }
        }
        return qtd;
    }

    public void imprime() {
        ferramentas.Imprime.matriz(matrizAbsi);
    }

    public int[][] getMatrizAbsi() {
        return matrizAbsi;
    }

}
