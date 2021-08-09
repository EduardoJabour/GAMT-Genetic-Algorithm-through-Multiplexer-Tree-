package exec;

import dados.Topologia;
import dados.Geracao;
import dados.Individuo;
import dados.Mux;
import dados.ABSi;
import ferramentas.*;
import java.util.Arrays;

public class Ajudante extends Thread {

    SimulatorOutputFrame saida;

    private final Coordenador coord;
    private final Topologia topologia;

    private final ABSi absi;
    Geracao geracao;

    private final int muxID;

    int N1;
    int N2;
    int LINHAS;
    int COLUNAS;

    int MAXIMO_MUTACOES;
    int NUMERO_MAX_GERACOES;
    int GERACOES_SEM_MUDANCA;
    int PERSISTENCIA;
    int INDIVIDUOS_POR_GERACAO;
    int QTD_CH;

    int LINHAS_NOS;
    int COLUNAS_NOS;

    int TAM_UM_C_IND;
    int NUMERO_NOS;

    int muxIDAnterior;

    int s;

    public Ajudante(int muxID, Coordenador coord, Topologia topologia, ABSi absi,
            int muxIDAnterior,
            int N1,
            int N2,
            int LINHAS,
            int COLUNAS,
            int MAXIMO_MUTACOES,
            int NUMERO_MAX_GERACOES,
            int GERACOES_SEM_MUDANCA,
            int PERSISTENCIA,
            int INDIVIDUOS_POR_GERACAO,
            int QTD_CH,
            int LINHAS_NOS,
            int COLUNAS_NOS,
            int TAM_UM_C_IND,
            int NUMERO_NOS,
            int s) {
        this.muxID = muxID;
        this.coord = coord;
        this.topologia = topologia;
        this.absi = absi;
        this.N1 = N1;
        this.N2 = N2;
        this.LINHAS = LINHAS;
        this.COLUNAS = COLUNAS;

        this.MAXIMO_MUTACOES = MAXIMO_MUTACOES;
        this.NUMERO_MAX_GERACOES = NUMERO_MAX_GERACOES;
        this.GERACOES_SEM_MUDANCA = GERACOES_SEM_MUDANCA;
        this.PERSISTENCIA = PERSISTENCIA;
        this.INDIVIDUOS_POR_GERACAO = INDIVIDUOS_POR_GERACAO;
        this.QTD_CH = QTD_CH;

        this.LINHAS_NOS = LINHAS_NOS;
        this.COLUNAS_NOS = COLUNAS_NOS;
        this.TAM_UM_C_IND = TAM_UM_C_IND;
        this.NUMERO_NOS = NUMERO_NOS;

        this.muxIDAnterior = muxIDAnterior;

        this.s = s;

        saida = new SimulatorOutputFrame("S" + s + " Mux " + muxID);
        saida.setLocation(280+Numeros.geraInt(250), 200+Numeros.geraInt(250));
        saida.setVisible(true);
    }

    @Override
    public void run() {
        saida.jTextAreaAppend("LINHAS =====>>> " + LINHAS);
        Individuo individuoInicial = new Individuo(N1, N2, LINHAS, COLUNAS, absi,
                QTD_CH, topologia, TAM_UM_C_IND, NUMERO_NOS);
        //individuoInicial.imprimeAbsiC();
        saida.jTextAreaAppend("\n Indivíduo inicial:\n");
        saida.jTextAreaAppend(individuoInicial.toString());
        geracao = new Geracao(individuoInicial, topologia, N1, N2, LINHAS, COLUNAS,
                absi, MAXIMO_MUTACOES, INDIVIDUOS_POR_GERACAO, QTD_CH,
                TAM_UM_C_IND, NUMERO_NOS);
        //geracao.imprime();
        init();
    }

    private void init() {
        int i, j, numeroGeracao = 1;
        int melhorRank = -1;
        int melhorRankAteOMomento = -1;
        int melhorIndividuoGeracao;
        int multiplicadorDeMutacoes = 1;
        int cont = 1, sinal = 0, per = 0;
        Individuo melhorIndividuo;
        int[][] matrizIndividuoTemp;
        int[] situacaoPortasMux;
        int[] ocorrenciaDasChaves;
        int qtdEntradasMux = (int) Math.pow(2, QTD_CH);
        while (numeroGeracao <= NUMERO_MAX_GERACOES && melhorRank > -2) { // colocar no futuro como condicao do while  && MelhorRank < RANK_OBJETIVO_PRIMEIRO_MUX 
            // Introduz mutacoes nos outros (n-1) Individuos da geracao
            geracao.modificaIndividuosDaGeracao(multiplicadorDeMutacoes);
            //geracao.imprime();
            // Seleciona o melhor individuo dessa nova geracao
            melhorIndividuoGeracao = geracao.getMelhorDaGeracao(); // Retorna a posicao do primeiro circuito do melhor individuo da geracao
            melhorRank = geracao.getGene(melhorIndividuoGeracao, TAM_UM_C_IND - 1);
            // Coloca o melhor no topo da geracao
            geracao.replicaMelhorIndividuoPorTodaAMatrizGeracao(melhorIndividuoGeracao);

            if (melhorRank > melhorRankAteOMomento) {
                saida.jTextAreaAppend("\n\n Evolução!!!!  Geração: " + numeroGeracao
                        + "\nMelhor rank até esta geração: " + melhorRank
                        + "\nValor do Multiplicador de Mutações: "
                        + multiplicadorDeMutacoes + "\n");

                saida.jTextAreaAppend("\n"
                        + geracao.getStrIndividuo(melhorIndividuoGeracao));
                melhorRankAteOMomento = melhorRank;
                cont = 1;
                multiplicadorDeMutacoes = 1;
                sinal = 0;
            } else {
                cont++;
                if (sinal == 1) {
                    per++;
                    if (per > PERSISTENCIA) {
                        geracao.modificaSaidaDosIndividuosDaGeracao();
                        per = 0;
                        multiplicadorDeMutacoes++;
                        saida.jTextAreaAppend(", " + multiplicadorDeMutacoes);
                    }
                }
            }

            if (cont % GERACOES_SEM_MUDANCA == 0) {
                multiplicadorDeMutacoes++;
                sinal = 1;
                saida.jTextAreaAppend("\n\n Após " + GERACOES_SEM_MUDANCA
                        + " gerações sem evolução no indivíduo, na geração "
                        + numeroGeracao + ",\no Multiplicador de Mutações recebeu "
                        + multiplicadorDeMutacoes);
            }

            if (multiplicadorDeMutacoes > (TAM_UM_C_IND - 2) * 2 / MAXIMO_MUTACOES) {
                multiplicadorDeMutacoes = 1;
                sinal = 0;
            }

            numeroGeracao++;
            if (numeroGeracao % (NUMERO_MAX_GERACOES / 20) == 0) {
                saida.jTextAreaAppend("\n\n Geração " + numeroGeracao
                        + " Melhor rank ate agora: " + melhorRank + "\n");
            }
        }  //Fim do loop principal do Ajudante

// Colocar o melhor Individuo encontrado dentro de Individuo Solucao para armazenar
        matrizIndividuoTemp = new int[QTD_CH][TAM_UM_C_IND];
        for (i = 0; i < QTD_CH; i++) {
            for (j = 0; j < TAM_UM_C_IND; j++) {
                matrizIndividuoTemp[i][j] = geracao.getGene(i, j);
            }
        }
        melhorIndividuo = new Individuo(
                matrizIndividuoTemp, N1, N2, LINHAS, COLUNAS, absi, QTD_CH,
                topologia, TAM_UM_C_IND, NUMERO_NOS);

        // Imprimir resposta apos buscar a solucao para um determinado Si
        //melhorIndividuo.imprimeAbsiC();
        if (melhorRank < -2) {
            saida.jTextAreaAppend("Encontrado na geração: " + (numeroGeracao - 1) + " \nCom Rank: "
                    + (-1 * melhorRank) + "\nTemos um Indivíduo que soluciona "
                    + "a saída S"+s);
        } else {
            saida.jTextAreaAppend("\n\nApós avaliar " + (numeroGeracao - 1)
                    + " gerações não foi "
                    + "possível encontrar um Indivíduo que solucionasse S"+s
                    + "\nCom um Rank de " + melhorRankAteOMomento + " o melhor "
                    + "Individuo encontrado está no Mux do arquivo geral de "
                    + "saída (S"+s+".txt).");
        }

        // Preenche o vetor SituacaoPortasMux e OcorrenciaDasChaves
        situacaoPortasMux = new int[qtdEntradasMux];
        ocorrenciaDasChaves = new int[qtdEntradasMux];
        for (i = 0; i < qtdEntradasMux; i++) {
            situacaoPortasMux[i] = retornaSituacaoPortasMux(melhorIndividuo, i);
            ocorrenciaDasChaves[i] = retornaOcorrenciaDaChaveNoMux(
                    melhorIndividuo, i);
        }
        for (i = 0; i < qtdEntradasMux; i++) {
            if (situacaoPortasMux[i] == 2) {
                iniciaAjudante(i, melhorIndividuo);
            }
        }
        saida.jTextAreaAppend("\nPortas Mux: ");
        saida.jTextAreaAppend(Arrays.toString(situacaoPortasMux));
        saida.jTextAreaAppend("\nOcorrência de chaves: ");
        saida.jTextAreaAppend(Arrays.toString(ocorrenciaDasChaves));
        saida.jTextAreaAppend("\nFIM");
        saida.salvaAutomatico();
        saida.dispose();
        coord.setSolucao(muxID, melhorIndividuo, situacaoPortasMux,
                ocorrenciaDasChaves);

    } //Fim do método init

    private void iniciaAjudante(int porta, Individuo melhorIndividuo) {
        int[] filtro = new int[LINHAS];
        for (int i = 0; i < LINHAS; i++) {
            if (Numeros.binarioParaDecimal(melhorIndividuo.getChaveDaLinha(i), QTD_CH) == porta) {
                filtro[i] = 1;
            } else {
                filtro[i] = 0;
            }
        }
        ABSi absiTemp = new ABSi(LINHAS, COLUNAS, absi.getMatrizAbsi(), filtro);
        //absiTemp.imprime();
        int proximoMuxID = coord.getProximoMuxID();
        //LINHAS = absiTemp.getMatrizAbsi().length;
        Ajudante ajudante = new Ajudante(proximoMuxID, coord, topologia, absiTemp,
                muxID,
                N1,
                N2,
                absiTemp.getMatrizAbsi().length,
                COLUNAS,
                MAXIMO_MUTACOES,
                NUMERO_MAX_GERACOES,
                GERACOES_SEM_MUDANCA,
                PERSISTENCIA,
                INDIVIDUOS_POR_GERACAO,
                QTD_CH,
                LINHAS_NOS,
                COLUNAS_NOS,
                TAM_UM_C_IND,
                NUMERO_NOS,
                s);
        Mux mux = new Mux(proximoMuxID, muxID, porta);
        coord.adicionaMux(mux);
        ajudante.start();
    }

    // na linha do numero max geracoes posso colocar um multiplicador pra crescer o numero de gerações a medida que o programa anda
    
    private int retornaSituacaoPortasMux(Individuo individuo, int chave) {
        int i;
        int zeros = 0, uns = 0;

        /* A codificacao que sera utilizada
	0 = porta travada em zero
	1 = porta travada em um
	-1 = porta nao utilizada
	2 = porta com necessidade de mais um MUX
         */
        // Conta zeros e uns
        for (i = 0; i < LINHAS; i++) { // Varrendo as linhas da tabela verdade entrada
            if (Numeros.binarioParaDecimal(individuo.getChaveDaLinha(i),
                    QTD_CH) == chave) {
                if (absi.getLinha(i)[COLUNAS] == 0) {
                    zeros++;
                } else {
                    uns++;
                }
            }
        }

        // Identifica se a porta eh zero, um ou se ainda eh preciso adicionar MUX
        if (uns == 0 && zeros == 0) {
            return (-1);
        } else {
            if (zeros > uns) {
                if (uns != 0) {
                    return (2); // Mais zeros do que Uns e Uns nao eh 0
                } else {
                    return (0); // Mais zeros do que Uns e Uns eh 0
                }
            } else {
                if (zeros != 0) {
                    return (2); // Mais Uns do que Zeros e Zeros nao eh 0
                } else {
                    return (1); // Mais Uns do que Zeros e Zeros eh 0
                }
            }
        }
    }

    private int retornaOcorrenciaDaChaveNoMux(Individuo individuo, int chave) {
        int i, ocorrenciaDeterminadaChave = 0;

        for (i = 0; i < LINHAS; i++) { // Varrendo as linhas da tabela verdade entrada
            //ERRO DE 21/06/2020 PODE ESTAR NESTA LINHA RETORNADA
            if (Numeros.binarioParaDecimal(individuo.getChaveDaLinha(i),
                    QTD_CH) == chave) {
                ocorrenciaDeterminadaChave++;
            }
        }
        return ocorrenciaDeterminadaChave;
    }

}
