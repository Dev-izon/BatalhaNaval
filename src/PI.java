import java.util.Scanner;
import java.util.Random;
import java.sql.*; 

class Main {
    static  Connection conn = null;
    static String databaseName = "railway";
    static String url = "jdbc:mysql://root:OE3rrfT8y5VqaYGnoBGi@containers-us-west-53.railway.app:6668/" +databaseName;
    static String username = "root";
    static String password = "OE3rrfT8y5VqaYGnoBGi";
  public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
    Scanner ler = new Scanner(System.in);
    NomePlayer player = new NomePlayer();
    int matriz[][] = new int[10][10];
   
    int linha = 0;
    int coluna = 0;
    int id = 1;
    id += 1;
    int ops = 0;

    System.out.println("Bem-Vindo a Batalha Naval");

    System.out.println("Já jogou ?" + "\n [1] - SIM \n [2] - NÃO");
    ops = ler.nextInt();

    System.out.println("Informe seu nome");
    String sla = ler.nextLine();
    player.setNome(ler.nextLine()); 

    int pontuacao = Jogada(matriz, linha, coluna);
    tabuleiro(matriz, linha, coluna);
    mostrarTabuleiro(matriz);
    
    System.out.println("Jogo Finalizado " + player.getNome() + "\n Sua pontuação total foi: " +pontuacao);

    if(ops == 1) {
      update(player,pontuacao,id);
    } else {
      insert(player, pontuacao);
    } 
  }
  public static void tabuleiro(int[][] matriz,int linha, int coluna ) {
    Random random = new Random();

   for (int i = 0; i < 5; i++) { // Preenche o barco na matriz
      linha = random.nextInt(8); // número aleatório entre 0 e 8
      coluna = random.nextInt(8); // número aleatório entre 0 e 8
     
      matriz[linha][coluna] = 1;
      matriz[linha][coluna + 1] = 1;
      matriz[linha][coluna + 2] = 1;

      //TENTAR CORRIGIR O ENCAVAMENTO, CHECAGEM DE OCUPAÇÃO TÁ SENDO SOMENTE PRO NÚMERO QUE FOI GERADO PRIMEIRO, OS OUTROS SÓ SÃO ADIÇÃO, AI TEM QUE FAZER ESSA VERIFICAÇÃO
      for (int j = 0; j < i; j++) { // Checar se a posição anterior já não foi gerada
        if (matriz[linha][coluna] == matriz[j][coluna] && matriz[linha][coluna + 1] == matriz[j][coluna + 1]
            && matriz[linha][coluna + 2] == matriz[j][coluna + 2] && matriz[j][coluna] == matriz[linha][coluna]  && matriz[linha + 1][coluna] == matriz[j + 1][coluna] && matriz[linha + 2][coluna] == matriz[j + 2][coluna]) {
          do {
            linha = random.nextInt(8);
            coluna = random.nextInt(8);
          } while (matriz[linha][coluna] == matriz[j][coluna] && matriz[linha][coluna + 1] == matriz[j][coluna + 1]
            && matriz[linha][coluna + 2] == matriz[j][coluna + 2] && matriz[j][coluna] == matriz[linha][coluna] && matriz[linha + 1][coluna] == matriz[j + 1][coluna] && matriz[linha + 2][coluna] == matriz[j + 2][coluna]);
        }
      }
    }
  }
    // FUNÇÂO JOGADA
public static int Jogada(int matriz[][], int linha, int coluna) {
    Scanner ler = new Scanner(System.in);

    int jogadas = 5;
    int pontuacao = 0;
    int linhaJogada = 0;
    int colunaJogada = 0;

    while (jogadas > 0) {
            System.out.println("Digite a posição da linha (0 - 10)");
            linhaJogada = ler.nextInt();
       
            System.out.println("Digite a posição da coluna (0 - 10)");
            colunaJogada = ler.nextInt();
    
        if (matriz[linhaJogada][colunaJogada] == 1) {
            pontuacao += 50;
            jogadas--;

            System.out.println("Parabéns, você acertou um barco! Sua pontuação: " + pontuacao);

            // Define a posição acertada como 0
            matriz[linhaJogada][colunaJogada] = 0;
        } else {
            System.out.println("Você não acertou um barco.\n");
            pontuacao += 0;
            jogadas--;
            }
        } 
  return pontuacao;
}
  public static void mostrarTabuleiro(int[][] matriz) {
    // Imprime a matriz só para ver se tá funcionando
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        System.out.print(matriz[i][j] + " ");
      }
      System.out.println();
    }
  }
  public static void limparTela() {
    System.out.print("\n\npressione <enter> para voltar ao menu.\n");
    new Scanner(System.in).nextLine();
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  //FUNÇÃO FEITA APENAS PARA DAR UPDATE, INFELIZMENTE NÃO FUNCIONAR 100%, MAS UNS 90% VAI, SÓ ALTERA A ÚLTIMA PONTUAÇÃO DO ÚLTIMO ID
  public static void update(NomePlayer a, int pontuacao, int id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection(url, username, password);

    PreparedStatement ps = conn.prepareStatement("UPDATE pontos SET nome = ?, score = ? WHERE id = ?");
    ps.setString(1, a.getNome());
    ps.setInt(2, pontuacao);
    ps.setInt(3, id);

    int status = ps.executeUpdate();

    if(status != 0){
        System.out.println("Seus status foram atualizados com sucesso!");
    }
  }
  //FUNÇÃO FEITA APENAS PARA DAR INSERT NO BANCO DE DADOS, FUNCIONA PERFEITAMENTE, TODA VEZ QUE O USUÁRIO FOR JOGAR, ELA VAI CRIAR UM SLOT NOVO
  public static void insert(NomePlayer a, int pontuacao) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection(url, username, password);

    PreparedStatement ps = conn.prepareStatement("INSERT INTO pontos (nome,score) VALUES (?, ?);");
    ps.setString(1, a.getNome());
    ps.setInt(2, pontuacao);

    int status = ps.executeUpdate();

    if(status != 0){
        System.out.println("Seus status foram criados no Banco de Dados!");
    }
  }
}