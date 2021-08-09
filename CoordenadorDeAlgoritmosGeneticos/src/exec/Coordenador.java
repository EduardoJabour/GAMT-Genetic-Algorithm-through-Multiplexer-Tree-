package exec;

import dados.Individuo;
import dados.Topologia;
import dados.Mux;
import dados.ABSi;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import ferramentas.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Coordenador {

    static ArrayList<Mux> muxes = new ArrayList<>();
    static int MUXES_EM_ANALISE = 0, muxID = 0;
    Topologia topologia;
    SimulatorOutputFrame saida;
    String setup = null;
    int abs[][];
    int s;
    int filtro[];

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

    Coordenador() {
        int semente;
        String strS = JOptionPane.showInputDialog("Informe o S (de 0 a COLUNAS-1)");
        try {
            s = Integer.parseInt(strS);
        } catch (NumberFormatException nfe) {
            Imprime.msg(nfe.getCause().toString());
            Imprime.msg("Erro no S");
            System.exit(0);
        }
        saida = new SimulatorOutputFrame("S"+s+"_Coordenador");
        saida.setLocation(280, 200);
        saida.setVisible(true);

/*        strS = JOptionPane.showInputDialog("Informe a semente de números"
                + "aleatórios(em branco para nenhuma): ");
        try {
            semente = Integer.parseInt(strS);
            Numeros.setSeed(semente);
            saida.jTextAreaAppend("Semente de números aleatórios em uso:" + semente);
        } catch (NumberFormatException nfe) {
            saida.jTextAreaAppend("Nenhuma semente setada. Programa será executado"
                    + "randomização default");
        }*/

        setup = leSetup();
        if (setup == null) {
            System.exit(0);
        }
        trataSetup();
        topologia = new Topologia(LINHAS_NOS, COLUNAS_NOS, COLUNAS);
        saida.jTextAreaAppend("\nTopologia:\n" + topologia.toString());
        abs = geraABSInicial();
        if (s < 0 || s > COLUNAS - 1) {
            saida.jTextAreaAppend("\nErro no valor de S");
            System.exit(0);
        }
        filtro = new int[LINHAS];
        for (int i = 0; i < LINHAS; i++) {
            filtro[i] = 1;
        }
        //Imprime.matriz(abs);
        init();
    }

    private void init() {
        ABSi absi = new ABSi(LINHAS, COLUNAS, abs, s, filtro);
        //absi.imprime();
        Ajudante ajudante = new Ajudante(muxID++, this, topologia, absi,
                -1,
                N1,
                N2,
                LINHAS,
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
        Mux mux = new Mux(0, -1, -1);
        this.adicionaMux(mux);
        ajudante.start();
    }

    public synchronized void adicionaMux(Mux mux) {
        muxes.add(mux);
        MUXES_EM_ANALISE++;
        saida.jTextAreaAppend("\n" + MUXES_EM_ANALISE + " mux(es) em análise");
    }

    public synchronized int getProximoMuxID() {
        return muxID++;
    }

    public synchronized void decrementaMUXES_EM_ANALISE() {
        MUXES_EM_ANALISE--;
        saida.jTextAreaAppend("\n" + MUXES_EM_ANALISE + " mux(es) em análise");
        if (MUXES_EM_ANALISE == 0) {
            saida.jTextAreaAppend("\nS" + s + " solucionado. Programa encerrado");
            saida.salvaAutomatico();
            Imprime.gravaSaidaEmDisco("S" + String.valueOf(s) + ".txt", muxes);
            System.exit(0);
        }
    }

    public synchronized void setSolucao(int muxID, Individuo individuoSolucao,
            int[] situacaoPortasMux, int[] ocorrenciaDasChaves) {
        int i = 0;
        while (muxes.get(i++).getMuxID() != muxID) {
        }
        i--;
        muxes.get(i).setIndividuo(individuoSolucao);
        muxes.get(i).setSituacaoPortas(situacaoPortasMux);
        muxes.get(i).setOcorrenciasDasChaves(ocorrenciaDasChaves);
        saida.jTextAreaAppend("\n" + muxes.get(i).toString());
        this.decrementaMUXES_EM_ANALISE();
    }

    private void trataSetup() {
        int i = 0, m, f;
        while (i < setup.length() - 3) {
            i = setup.indexOf("N1");
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                N1 = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("N2", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                N2 = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("MAXIMO_MUTACOES", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                MAXIMO_MUTACOES = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("NUMERO_MAX_GERACOES", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                NUMERO_MAX_GERACOES = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("GERACOES_SEM_MUDANCA", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                GERACOES_SEM_MUDANCA = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("PERSISTENCIA", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                PERSISTENCIA = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("INDIVIDUOS_POR_GERACAO", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                INDIVIDUOS_POR_GERACAO = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("QTD_CH", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                QTD_CH = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("LINHAS_NOS", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                LINHAS_NOS = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("COLUNAS_NOS", f);
            m = setup.indexOf("=", i);
            f = setup.indexOf(";", i);
            try {
                COLUNAS_NOS = Integer.parseInt(setup.substring(m + 1, f));
            } catch (NumberFormatException e) {
                Imprime.msg("Erro coletando número no arquivo de entrada");
            }

            i = setup.indexOf("FIM", f);
        }

        LINHAS = (int) Math.pow(2, N1 + N2);
        COLUNAS = N1 + N2;
        NUMERO_NOS = N1 + N2 + COLUNAS_NOS * LINHAS_NOS;
        TAM_UM_C_IND = 3 * (NUMERO_NOS - COLUNAS) + 3;
        saida.jTextAreaAppend(
                "\nS" + s
                + "\nN1=" + N1
                + "\nN2=" + N2
                + "\nMAXIMO_MUTACOES=" + MAXIMO_MUTACOES
                + "\nNUMERO_MAX_GERACOES=" + NUMERO_MAX_GERACOES
                + "\nGERACOES_SEM_MUDANCA=" + GERACOES_SEM_MUDANCA
                + "\nPERSISTENCIA=" + PERSISTENCIA
                + "\nINDIVIDUOS_POR_GERACAO=" + INDIVIDUOS_POR_GERACAO
                + "\nQTD_CH=" + QTD_CH
                + "\nLINHAS_NOS=" + LINHAS_NOS
                + "\nCOLUNAS_NOS=" + COLUNAS_NOS
                + "\nLINHAS " + LINHAS
                + "\nCOLUNAS=" + COLUNAS);
    }

    private int[][] geraABSInicial() {
        int absTemp[][] = new int[LINHAS][COLUNAS * 2];
        int linhaS[], parteN2[] = new int[N2], linhaAB[];
        int solDec;
        int i, j, valor = 0;
        for (i = 0; i < LINHAS; i++) {
            linhaAB = Numeros.decimalParaBinario(valor++, COLUNAS);
            for (j = 0; j < COLUNAS; j++) {
                absTemp[i][j] = linhaAB[j];
            }
            for (j = 0; j < N2; j++) {
                parteN2[j] = absTemp[i][j + N1];
            }
            solDec = Numeros.binarioParaDecimal(
                    absTemp[i], N1) * Numeros.binarioParaDecimal(parteN2, N2);
            linhaS = Numeros.decimalParaBinario(solDec, COLUNAS);
            for (j = 0; j < COLUNAS; j++) {
                absTemp[i][COLUNAS + j] = linhaS[j];
            }
        }
        return absTemp;
    }

    private String leSetup() {
        String strArq = "", linha;
        try {
            FileInputStream fluxoArq = new FileInputStream("setup.txt");
            InputStreamReader leitorArq = new InputStreamReader(fluxoArq);
            BufferedReader bufferArq = new BufferedReader(leitorArq);
            linha = bufferArq.readLine();
            while (linha != null) {
                strArq += linha;
                linha = bufferArq.readLine();
            }
            return strArq.trim();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Coordenador.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro lendo arquivo de configurações.");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Coordenador.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro lendo arquivo de configurações.");
            return null;
        }

    }

    public static void main(String args[]) {
        Coordenador coordenador = new Coordenador();
    }
}
