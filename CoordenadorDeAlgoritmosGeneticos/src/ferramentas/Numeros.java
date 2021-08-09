package ferramentas;

/**
 *
 * @author jabour
 */
public class Numeros {

    public static final int NUMERO_PORTAS = 5;
    public static final int AND = 100;
    public static final int OR = 200;
    public static final int NAND = 300;
    public static final int NOR = 400;
    public static final int XOR = 500;
    public static final java.util.Random R = new java.util.Random();

    public static int[] decimalParaBinario(int dec, int COLUNAS) {
        int j = COLUNAS - 1;
        int bin[] = new int[COLUNAS];
        while (dec != 0) {
            bin[j--] = dec % 2;
            dec /= 2;
        }
        return bin;
    }

    public static int binarioParaDecimal(int[] bin, int tam) {
        int i, pot = 1, dec = 0;
        for (i = tam - 1; i >= 0; i--) {
            dec += pot * bin[i];
            pot *= 2;
        }
        return dec;
    }
    
    public static void setSeed(int s){
        R.setSeed(s);
    }
    
    public static int geraInt(int limiteSuperior) {
        return R.nextInt(limiteSuperior);
    }

    public static int sorteiaPorta() {
        int porta = geraInt(NUMERO_PORTAS) + 1;
        switch (porta) {
            case 1:
                return AND;
            case 2:
                return OR;
            case 3:
                return NAND;
            case 4:
                return NOR;
            case 5:
                return XOR;
            default:
                return 999;
        }
    }

    public static int resolvePorta(int entradaUm, int entradaDois, int porta) {
        boolean eUm, eDois, resultado = true;
        int intRes;
        eUm = entradaUm != 0;
        eDois = entradaDois != 0;
        switch (porta) {
            case 100:
                resultado = eUm && eDois;
                break;
            case 200:
                resultado = eUm || eDois;
                break;
            case 300:
                resultado = !(eUm && eDois);
                break;
            case 400:
                resultado = !(eUm || eDois);
                break;
            case 500:
                resultado = eUm ^ eDois;
                break;
        }

        if (resultado == true) {
            intRes = 1;
        } else {
            intRes = 0;
        }
        return intRes;
    }

}
