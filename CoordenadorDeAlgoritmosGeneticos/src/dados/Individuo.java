package dados;

import dados.ABSi;
import ferramentas.*;
import java.util.Arrays;

public final class Individuo {

    int[][] matrizIndividuo;
    int[][] matrizSaidaC;
    int N1, N2;
    int QTD_CH;
    int LINHAS;
    ABSi absi;
    Topologia topologia;
    int COLUNAS;
    int TAM_UM_C_IND;
    int LINHAS_NOS;
    int COLUNAS_NOS;
    int NUMERO_NOS;

    public Individuo(int N1, int N2, int LINHAS, int COLUNAS, ABSi absi, int QTD_CH,
            Topologia topologia, int TAM_UM_C_IND, int NUMERO_NOS) {
        int i, j;
        this.N1 = N1;
        this.N2 = N2;
        this.LINHAS = LINHAS;
        this.COLUNAS = COLUNAS;
        this.absi = absi;
        this.QTD_CH = QTD_CH;
        this.topologia = topologia;
        this.TAM_UM_C_IND = TAM_UM_C_IND;
        this.NUMERO_NOS = NUMERO_NOS;

        matrizSaidaC = new int[LINHAS][QTD_CH];
        int ColunaAtualNaTopologia, LinhaAtualNaTopologia;
        LINHAS_NOS = topologia.getNumLinhas();
        COLUNAS_NOS = topologia.getNumCol();
        matrizIndividuo = new int[QTD_CH][TAM_UM_C_IND];
        for (i = 0; i < QTD_CH; i++) {
            matrizIndividuo[i][0] = 1;
        }
        for (j = 0; j < QTD_CH; j++) {
            ColunaAtualNaTopologia = 0;
            LinhaAtualNaTopologia = 0;
            for (i = 1; i <= TAM_UM_C_IND - 3; i += 3) {
                matrizIndividuo[j][i] = Numeros.geraInt(COLUNAS
                        + ColunaAtualNaTopologia * LINHAS_NOS) + 1;
                matrizIndividuo[j][i + 1] = matrizIndividuo[j][i];
                while (matrizIndividuo[j][i] == matrizIndividuo[j][i + 1]) {
                    matrizIndividuo[j][i + 1] = Numeros.geraInt(COLUNAS
                            + ColunaAtualNaTopologia * LINHAS_NOS) + 1;
                }
                matrizIndividuo[j][i + 2] = Numeros.sorteiaPorta();
                if (LinhaAtualNaTopologia++ == LINHAS_NOS - 1) {
                    LinhaAtualNaTopologia = 0;
                    ColunaAtualNaTopologia++;
                }
            }
            matrizIndividuo[j][i++] = Numeros.geraInt(NUMERO_NOS) + 1;
            matrizIndividuo[j][i] = -1;
        }
        this.calculaC();
        matrizIndividuo[0][TAM_UM_C_IND - 1] = this.avaliaIndividuo();
    }

    public Individuo(int[][] matrizPronta, int N1, int N2, int LINHAS, int COLUNAS,
            ABSi absi, int QTD_CH, Topologia topologia, int TAM_UM_C_IND,
            int NUMERO_NOS) {
        int i;
        this.N1 = N1;
        this.N2 = N2;
        this.LINHAS = LINHAS;
        this.COLUNAS = COLUNAS;
        this.absi = absi;
        this.QTD_CH = QTD_CH;
        this.topologia = topologia;
        this.TAM_UM_C_IND = TAM_UM_C_IND;
        this.NUMERO_NOS = NUMERO_NOS;
        matrizSaidaC = new int[LINHAS][QTD_CH];
        LINHAS_NOS = topologia.getNumLinhas();
        COLUNAS_NOS = topologia.getNumCol();

        matrizIndividuo = new int[QTD_CH][TAM_UM_C_IND];
        for (i = 0; i < QTD_CH; i++) {
            matrizIndividuo[i] = matrizPronta[i];
        }
        this.calculaC();
        matrizIndividuo[0][TAM_UM_C_IND - 1] = this.avaliaIndividuo();
    }

    public void imprime() {
        ferramentas.Imprime.matriz(matrizIndividuo);
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < QTD_CH; i++) {
            str += Arrays.toString(matrizIndividuo[i])+"\n";
        }
        return str;
    }

    public void imprimeAbsiC() {
        Imprime.duasMatrizes(absi.getMatrizAbsi(), matrizSaidaC);
    }

    public int[] getCircuito(int i) {
        if (i < QTD_CH) {
            return matrizIndividuo[i];
        } else {
            ferramentas.Imprime.msg("Erro no método getCircuito da classe"
                    + "Individuo");
            return null;
        }
    }

    public int getGene(int i, int j) {
        return matrizIndividuo[i][j];
    }

    private void calculaC() {
        int i, j;
        for (j = 0; j < QTD_CH; j++) {
            for (i = 0; i < LINHAS; i++) {
                matrizSaidaC[i][j] = calculaSaida(absi.getLinha(i), j);
            }
        }
    }

    private int calculaSaida(int linhaAbs[], int numCircuito) {
        int[][] TopologiaAtual
                = new int[topologia.getNumLinhas()][topologia.getNumCol()];
        int i, j, k = 1, a, b;    // k = Posicao No Individuo
        int Pos = COLUNAS + 1;    //posicao na MatrizTopologia
        int NohAtual;

        if (this.getCircuito(numCircuito)[TAM_UM_C_IND - 2] <= COLUNAS) {
            return linhaAbs[this.getCircuito(numCircuito)[TAM_UM_C_IND - 2] - 1];
        }

        for (j = 0; j < topologia.getNumCol(); j++) {
            for (i = 0; i < topologia.getNumLinhas(); i++) {
                if (this.getCircuito(numCircuito)[k] <= COLUNAS) {  // Entrada um eh porta de entrada
                    if (this.getCircuito(numCircuito)[k + 1] <= COLUNAS) {    // Entrada dois eh porta de entrada 
                        TopologiaAtual[i][j] = Numeros.resolvePorta(
                                linhaAbs[this.getCircuito(numCircuito)[k] - 1],
                                linhaAbs[this.getCircuito(numCircuito)[k + 1] - 1],
                                this.getCircuito(numCircuito)[k + 2]);
                    } else {   // Entrada dois nao eh porta de entrada (tem que pegar o estado atual na TopologiaAtual)
                        // Achando o estado atual na TopologiaAtual
                        NohAtual = COLUNAS + 1;
                        for (b = 0; b < COLUNAS_NOS; b++) {
                            for (a = 0; a < LINHAS_NOS; a++) {
                                if (NohAtual++ == this.getCircuito(numCircuito)[k + 1]) {
                                    TopologiaAtual[i][j] = Numeros.resolvePorta(
                                            linhaAbs[this.getCircuito(numCircuito)[k] - 1],
                                            TopologiaAtual[a][b], this.getCircuito(numCircuito)[k + 2]);
                                    b = COLUNAS_NOS;
                                    break;
                                }
                            }
                        }
                    }
                } else {   // Entrada um nao eh porta de entrada (tem que pegar o estado atual na TopologiaAtual)
                    if (this.getCircuito(numCircuito)[k + 1] <= COLUNAS) {    // Entrada dois eh porta de entrada
                        // Achando o estado atual na TopologiaAtual
                        NohAtual = COLUNAS + 1;
                        for (b = 0; b < COLUNAS_NOS; b++) {
                            for (a = 0; a < LINHAS_NOS; a++) {
                                if (NohAtual++ == this.getCircuito(numCircuito)[k]) {
                                    TopologiaAtual[i][j] = Numeros.resolvePorta(
                                            TopologiaAtual[a][b],
                                            linhaAbs[this.getCircuito(numCircuito)[k + 1] - 1],
                                            this.getCircuito(numCircuito)[k + 2]);
                                    //printf("\tResultado: %d (ponto 3)",TopologiaAtual[i][j]);
                                    b = COLUNAS_NOS;
                                    break;
                                }
                            }
                        }
                    } else {   // Entrada dois nao eh porta de entrada (tem que pegar o estado atual na TopologiaAtual)
                        // Achando o estado atual na TopologiaAtual
                        int AuxEntrUm = 0, AuxEntrDois = 0;
                        NohAtual = COLUNAS + 1;
                        for (b = 0; b < COLUNAS_NOS; b++) {
                            for (a = 0; a < LINHAS_NOS; a++) {
                                if (NohAtual++ == this.getCircuito(numCircuito)[k]) {
                                    AuxEntrUm = TopologiaAtual[a][b];
                                    b = COLUNAS_NOS;
                                    break;
                                }
                            }
                        }
                        NohAtual = COLUNAS + 1;
                        for (b = 0; b < COLUNAS_NOS; b++) {
                            for (a = 0; a < LINHAS_NOS; a++) {
                                if (NohAtual++ == this.getCircuito(numCircuito)[k + 1]) {
                                    AuxEntrDois = TopologiaAtual[a][b];
                                    b = COLUNAS_NOS;
                                    break;
                                }
                            }
                        }
                        TopologiaAtual[i][j] = Numeros.resolvePorta(AuxEntrUm, AuxEntrDois, this.getCircuito(numCircuito)[k + 2]);
                        //printf("\tResultado: %d (ponto 4)",TopologiaAtual[i][j]);
                    }
                }
                if (Pos++ == this.getCircuito(numCircuito)[TAM_UM_C_IND - 2]) {  //pos = posicao da saida no individuo
                    return (TopologiaAtual[i][j]);
                }
                k += 3;
            }
        }
        Imprime.msg("Programa chegou a um ponto inesperado no método"
                + "calculaSaida da Classe Individuo");
        return -999;
    }

    private int avaliaIndividuo() {
        int i, c;
        int qtdEntradasMux = (int) Math.pow(2, QTD_CH);
        int[] zeros = new int[qtdEntradasMux];
        int[] uns = new int[qtdEntradasMux];
        int ava = 0;
        int perfeito = 0;

        // Zerando os vetores 
        for (c = 0; c < qtdEntradasMux; c++) {
            zeros[c] = 0;
            uns[c] = 0;
        }

        // Conta zeros e uns
        for (i = 0; i < LINHAS; i++) { // Varrendo as linhas da tabela verdade entrada
            for (c = 0; c < qtdEntradasMux; c++) { // Varrendo as possibilidades de chaves
                if (Numeros.binarioParaDecimal(matrizSaidaC[i], QTD_CH) == c) {
                    if (absi.getLinha(i)[COLUNAS] == 0) {
                        zeros[c]++;
                    } else {
                        uns[c]++;
                    }
                }
            }
        }

/*
        for (c = 0; c < qtdEntradasMux; c++) {
            if (uns[c] == 0 && zeros[c] == 0) {
                ava += Math.cbrt(COLUNAS);//raiz cubica de linhas
                perfeito++;
            } else {
                if (zeros[c] > uns[c]) {
                    if (uns[c] != 0) {
                        ava += Math.round((float) zeros[c] / (float) uns[c]);
                    } else {
                        ava += (LINHAS / (N1 * N2)) * zeros[c];
                        perfeito++;
                    }
                } else {
                    if (zeros[c] != 0) {
                        ava += Math.round((float) uns[c] / (float) zeros[c]);
                    } else {
                        ava += (LINHAS / (N1 * N2)) * uns[c];
                        perfeito++;
                    }
                }
            }
        }        
*/        
        
        for (c = 0; c < qtdEntradasMux; c++) {
            if (uns[c] == 0 && zeros[c] == 0) {
                ava += Math.cbrt(COLUNAS);//raiz cubica de linhas
                perfeito++;
            } else {
                if (zeros[c] > uns[c]) {
                    if (uns[c] != 0) {
                        ava += Math.round ( 2 * ((float) zeros[c] / (float) uns[c]) );
                    } else {
                        ava += ( (LINHAS / (N1 * N2)) * zeros[c] ) + (qtdEntradasMux*qtdEntradasMux);
                        perfeito++;
                    }
                } else {
                    if (zeros[c] != 0) {
                        ava += Math.round( 2 * ((float) uns[c] / (float) zeros[c]) );
                    } else {
                        ava += ( (LINHAS / (N1 * N2)) * uns[c]) + (qtdEntradasMux*qtdEntradasMux);
                        perfeito++;
                    }
                }
            }
        }

        if (perfeito == qtdEntradasMux) {
            return ((-1) * ava);
        } else {
            return ava;
        }

    }

    public void imprimeSaidaC() {
        ferramentas.Imprime.matriz(matrizSaidaC);
    }

    public int[] getChaveDaLinha(int linha) {
        return matrizSaidaC[linha];
    }

}
