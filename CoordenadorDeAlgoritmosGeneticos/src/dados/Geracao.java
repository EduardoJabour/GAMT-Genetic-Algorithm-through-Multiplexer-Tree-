package dados;


import dados.ABSi;
import ferramentas.*;
import java.util.Arrays;

public class Geracao {

    int[][] matrizGeracao;
    Individuo individuo;
    Topologia topologia;
    ABSi absi;
    int N1, N2;
    int LINHAS;
    int COLUNAS;
    int MAXIMO_MUTACOES, INDIVIDUOS_POR_GERACAO, QTD_CH, TAM_UM_C_IND;
    int NUMERO_NOS;

    public Geracao(Individuo individuo, Topologia topologia, int N1, int N2,
            int LINHAS, int COLUNAS, ABSi absi, int MAXIMO_MUTACOES,
            int INDIVIDUOS_POR_GERACAO, int QTD_CH, int TAM_UM_C_IND,
            int NUMERO_NOS) {
        this.individuo = individuo;
        this.topologia = topologia;
        this.absi = absi;
        this.N1 = N1;
        this.N2 = N2;
        this.LINHAS = LINHAS;
        this.COLUNAS = COLUNAS;
        this.MAXIMO_MUTACOES = MAXIMO_MUTACOES;
        this.INDIVIDUOS_POR_GERACAO = INDIVIDUOS_POR_GERACAO;
        this.QTD_CH = QTD_CH;
        this.TAM_UM_C_IND = TAM_UM_C_IND;
        this.NUMERO_NOS = NUMERO_NOS;
        matrizGeracao = new int[QTD_CH * INDIVIDUOS_POR_GERACAO][TAM_UM_C_IND];
        preencheMatrizGeracao();
    }

    private void preencheMatrizGeracao() {
        int i, j, k;
        for (i = 0; i < INDIVIDUOS_POR_GERACAO * QTD_CH; i += QTD_CH) {
            for (j = 0; j < TAM_UM_C_IND; j++) {
                for (k = 0; k < QTD_CH; k++) {
                    matrizGeracao[i + k][j] = individuo.getGene(k, j);
                }
            }
        }
    }

    public void modificaIndividuosDaGeracao(int mdm) {
        int i, j, k, cMutado;
        int posicaoAMudar, entradaTemp;
        int colunaAtualNaTopologia, linhaAtualNaTopologia;
        int[][] matrizIndividuoTemp = new int[QTD_CH][TAM_UM_C_IND];
        Individuo indTemp;
        for (k = QTD_CH; k < QTD_CH * INDIVIDUOS_POR_GERACAO; k = k + QTD_CH) {
            for (j = 1; j <= (MAXIMO_MUTACOES * mdm); j++) {
                colunaAtualNaTopologia = 0; // Reseta a coluna da topologia para a nova mutacao
                linhaAtualNaTopologia = 0; // Reseta a linha da topologia para a nova mutacao
                cMutado = Numeros.geraInt(QTD_CH);
                posicaoAMudar = Numeros.geraInt(TAM_UM_C_IND - 2) + 1;
                for (i = 1; i <= TAM_UM_C_IND - 3; i = i + 3) { // Nos
                    if (i == posicaoAMudar) {
                        entradaTemp = Numeros.geraInt((COLUNAS + colunaAtualNaTopologia * topologia.getNumLinhas())) + 1;
                        while (entradaTemp == matrizGeracao[k + cMutado][i + 1]) {
                            entradaTemp = Numeros.geraInt((COLUNAS + colunaAtualNaTopologia * topologia.getNumLinhas())) + 1;
                        }
                        matrizGeracao[k + cMutado][i] = entradaTemp;
                        i = TAM_UM_C_IND - 2;
                        break;
                    }
                    if (i + 1 == posicaoAMudar) {
                        matrizGeracao[k + cMutado][i + 1] = matrizGeracao[k + cMutado][i];
                        while (matrizGeracao[k + cMutado][i] == matrizGeracao[k + cMutado][i + 1]) {
                            matrizGeracao[k + cMutado][i + 1] = Numeros.geraInt((COLUNAS + colunaAtualNaTopologia * topologia.getNumLinhas())) + 1;
                        }
                        i = TAM_UM_C_IND - 2;
                        break;
                    }
                    if (i + 2 == posicaoAMudar) {
                        matrizGeracao[k + cMutado][i + 2] = Numeros.sorteiaPorta();
                        i = TAM_UM_C_IND - 2;
                        break;
                    }
                    if (linhaAtualNaTopologia++ == topologia.getNumLinhas() - 1) {
                        linhaAtualNaTopologia = 0;
                        colunaAtualNaTopologia++;
                    }
                }
                if (i == posicaoAMudar) {  // Saida
                    matrizGeracao[k + cMutado][i] = Numeros.geraInt(NUMERO_NOS) + 1;
                }
                // Deixei o rank anterior inalterado, em algum momento futuro ele devera ser recalculado
            }
            for (i = 0; i < QTD_CH; i++) {
                matrizIndividuoTemp[i] = matrizGeracao[i + k];
            }
            indTemp = new Individuo(
                    matrizIndividuoTemp, N1, N2, LINHAS, COLUNAS, absi, QTD_CH,
                    topologia, TAM_UM_C_IND, NUMERO_NOS);
            matrizGeracao[k][TAM_UM_C_IND - 1]
                    = indTemp.getGene(0, TAM_UM_C_IND - 1);
        }
    }

    public int getMelhorDaGeracao() {
        int maiorRank = -1;
        int posicao = -1;
        int i;
        for (i = 0; i < INDIVIDUOS_POR_GERACAO * QTD_CH; i += QTD_CH) {
            if (matrizGeracao[i][TAM_UM_C_IND - 1] < -2) {
                posicao = i;
                return (posicao);
            } else {
                if (matrizGeracao[i][TAM_UM_C_IND - 1] > maiorRank) {
                    maiorRank = matrizGeracao[i][TAM_UM_C_IND - 1];
                    posicao = i;
                }
            }
        }
        return (posicao);
    }

    public int getGene(int i, int j) {
        return matrizGeracao[i][j];
    }

    public void replicaMelhorIndividuoPorTodaAMatrizGeracao(int melhor) {
        for (int i = 0; i < QTD_CH * INDIVIDUOS_POR_GERACAO; i += QTD_CH) {
            for (int k = 0; k < QTD_CH; k++) {
                for (int j = 0; j < TAM_UM_C_IND; j++) {
                    matrizGeracao[i + k][j] = matrizGeracao[melhor + k][j];
                }
            }
        }
    }

    public void modificaSaidaDosIndividuosDaGeracao() {
        int k, cMutado;
        cMutado = Numeros.geraInt(QTD_CH);
        for (k = QTD_CH; k < QTD_CH * INDIVIDUOS_POR_GERACAO; k += QTD_CH) {
            matrizGeracao[k + cMutado][TAM_UM_C_IND - 2]
                    = Numeros.geraInt(NUMERO_NOS) + 1;
        }
    }

    public void imprime() {
        ferramentas.Imprime.matriz(matrizGeracao);
    }

    public void imprimeIndividuo(int ind) {
        for (int i = ind ; i < ind+QTD_CH; i++) {
            Imprime.vetor(matrizGeracao[i]);
        }
    }

    public String getStrIndividuo(int ind){
        String strInd = "";
        for (int i = ind ; i < ind+QTD_CH; i++) {
            strInd += Arrays.toString(matrizGeracao[i])+"\n";
        }
        return strInd;
    }
    
}
