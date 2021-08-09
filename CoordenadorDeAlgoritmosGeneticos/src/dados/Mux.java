package dados;

import ferramentas.Imprime;
import java.util.Arrays;

public class Mux {

    private final int muxID;
    private int portaPai, pai;
    private Individuo individuoSolucao;
    private int[] situacaoPortas;
    private int[] ocorrenciaDasChaves;
    private int status;

    public Mux(int muxID, int pai, int portaPai) {
        this.muxID = muxID; //(0 é o primeiro MUX)
        this.pai = pai; //(-1 = não tem pai)
        this.portaPai = portaPai; //(-1 = não tem pai)
    }

    public void setIndividuo(Individuo individuoSolucao) {
        this.individuoSolucao = individuoSolucao;
    }

    public void setSituacaoPortas(int[] situacaoPortasMux) {
        this.situacaoPortas = situacaoPortasMux;
    }

    public void setOcorrenciasDasChaves(int[] ocorrenciaDasChaves) {
        this.ocorrenciaDasChaves = ocorrenciaDasChaves;
    }

    public int getMuxID() {
        return muxID;
    }

    public int getPai() {
        return pai;
    }

    public int getPortaPai() {
        return portaPai;
    }

    public void setPai(int pai) {
        this.pai = pai;
    }

    public void setPortaPai(int portaPai) {
        this.portaPai = portaPai;
    }

    public void imprime() {
        Imprime.msg(this.toString());
    }

    @Override
    public String toString() {
        String str = "Mux " + this.getMuxID() + ". Ligado ao Mux " + this.getPai();
        str += " na porta " + this.getPortaPai();
        str += "\nIndivíduo solução:";
        str += "\n"+individuoSolucao.toString();
        str += "\nSituação das portas: " + Arrays.toString(situacaoPortas);
        str += "\nOcorrência das chaves " + Arrays.toString(ocorrenciaDasChaves)+"\n";
        return str;
    }

}
