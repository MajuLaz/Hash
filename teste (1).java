import java.util.Random;
import java.util.Scanner;

class Registro {
    private int codigo;

    public Registro(int codigo) {
        this.codigo = codigo;
    }

    public int getRegistro() {
        return codigo;
    }
}

class Tabela {

    private int[] vetor;
    private int tamanho;
    private int colisoes = 0;

    public Tabela(int tamanho) {
        this.tamanho = tamanho;
        vetor = new int[tamanho];

        // Preencha o vetor com -1
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = -1;
        }
    }

    public int hashResto(int codigo) {
        return codigo % tamanho;
    }

    public int hashMultiplicacao(int codigo) {
        double A = 0.6180339887; 
        double produto = A * codigo;
        double fracao = produto - (int) produto;
        return (int) (tamanho * fracao);
    }

    public int hashDobramento(int codigo) {
        int soma = 0;
        int tempCodigo = codigo;

        while (tempCodigo > 0) {
            soma += tempCodigo % 1000; 
            tempCodigo /= 1000;
        }

        return soma % tamanho;
    }

    public void adicionarRegistro(Registro novo_registro, int escolhaFuncao) {
        int codigo = novo_registro.getRegistro();
        int valor_vetor = -1; 
        if (escolhaFuncao == 1) {
            valor_vetor = hashResto(codigo);
        } else if (escolhaFuncao == 2) {
            valor_vetor = hashMultiplicacao(codigo);
        } else if (escolhaFuncao == 3) {
            valor_vetor = hashDobramento(codigo);
        } else {
        }

        if (valor_vetor != -1) {
            if (vetor[valor_vetor] == -1) {
                vetor[valor_vetor] = codigo;
            } else {
                // Colisões
                while (vetor[valor_vetor] != codigo && vetor[valor_vetor] != -1) {
                    valor_vetor = (valor_vetor + 1) % tamanho;
                    colisoes = colisoes + 1;
                }
                vetor[valor_vetor] = codigo;
            }
        }
    }


    public void buscarRegistro(Registro novo_registro) {
        int codigo = novo_registro.getRegistro();
        int valor = hashResto(codigo);

        while (valor < tamanho) {
            int chave = vetor[valor];

            if (codigo == chave) {
                System.out.println("O registro " + codigo + " está na posição " + valor);
                return;
            }

            valor = (valor + 1) % tamanho;
        }

        System.out.println("O registro não está na tabela");
    }

    public void imprimirVetor() {
        for (int i = 0; i < tamanho; i++) {
            System.out.println(i + "\t" + vetor[i]);
        }
    }

    public int getColisoes() {
        return colisoes;
    }
}

public class teste {
    public static Registro[] gerarConjuntoDeDados(int tamanho, long seed) {
        Random random = new Random(seed);
        Registro[] conjunto = new Registro[tamanho];
        for (int i = 0; i < tamanho; i++) {
            int codigo = random.nextInt(900000000) + 100000000;
            conjunto[i] = new Registro(codigo);
        }
        return conjunto;
    }

    public static void main(String[] args) {
        long seed = 12345;

        int[] tamanhosTabela = {10, 100, 500, 10000, 50000};
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de função de hash:");
        System.out.println("1 - Resto");
        System.out.println("2 - Multiplicação");
        System.out.println("3 - Dobramento");
        System.out.print("Digite o número correspondente: ");

        int escolhaFuncao = scanner.nextInt();

        for (int tamanho : tamanhosTabela) {
            Tabela tabela = new Tabela(tamanho);
            Registro[] conjuntoDeDados = gerarConjuntoDeDados(tamanho, seed);

            // Medir o tempo de inserção
            long startTimeInsercao = System.nanoTime();
            for (Registro registro : conjuntoDeDados) {
                tabela.adicionarRegistro(registro, escolhaFuncao);
            }
            long endTimeInsercao = System.nanoTime();
            double tempoInsercao = (endTimeInsercao - startTimeInsercao) / 1e6; // Converter para milissegundos


            int indiceBusca = tamanho / 2; 
            Registro registroBusca = conjuntoDeDados[indiceBusca];

            long startTimeBusca = System.nanoTime();
            tabela.buscarRegistro(registroBusca);
            long endTimeBusca = System.nanoTime();
            double tempoBusca = (endTimeBusca - startTimeBusca) / 1e6;


            int colisoes = tabela.getColisoes();
            
            System.out.println("Tabela de tamanho " + tamanho + " preenchida com sucesso.");
            System.out.println("Tempo de inserção: " + tempoInsercao + " milissegundos");
            System.out.println("Tempo de busca: " + tempoBusca + " milissegundos");
            System.out.println("Numero de colisões: " + colisoes);
            System.out.println();
        }
    }
}
