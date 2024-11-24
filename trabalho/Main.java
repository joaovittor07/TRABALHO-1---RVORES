package trabalho;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArvoreRubroNegra arvoreReservas = new ArvoreRubroNegra();
        ArvoreRubroNegra arvoreHistorico = new ArvoreRubroNegra();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Bem vindo ao nosso Hotel ===");
            System.out.println("1. Adicionar reserva");
            System.out.println("2. Remover reserva (e mover para histórico)");
            System.out.println("3. Buscar reserva por ID");
            System.out.println("4. Listar reservas por data de check-in");
            System.out.println("5. Calcular taxa de ocupação em um período");
            System.out.println("6. Relatório de quartos mais e menos reservados");
            System.out.println("7. Exibir histórico de reservas canceladas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                	System.out.println();
                	System.out.println("--------------------------");
                    System.out.print("ID da reserva: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nome do cliente: ");
                    String nomeCliente = scanner.nextLine();
                    System.out.print("Número do quarto: ");
                    String numeroQuarto = scanner.nextLine();
                    System.out.print("Data de check-in (YYYY-MM-DD): ");
                    String dataCheckIn = scanner.nextLine();
                    System.out.print("Data de check-out (YYYY-MM-DD): ");
                    String dataCheckOut = scanner.nextLine();
                    System.out.print("Categoria do quarto: ");
                    String categoriaQuarto = scanner.nextLine();
                	System.out.println("--------------------------");

                    Reserva novaReserva = new Reserva(id, nomeCliente, numeroQuarto, dataCheckIn, dataCheckOut, categoriaQuarto, LocalDateTime.now().toString());

                    // Verificar se o ID já existe na árvore
                    if (arvoreReservas.idExiste(id)) {
                        System.out.println();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Conflito de ID: Já existe uma reserva com este ID!");
                        System.out.println("-----------------------------------------------------");
                        System.out.println();
                    } else if (arvoreReservas.conflita(novaReserva)) {
                        System.out.println();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Reserva em conflito de horário com reserva existente!");
                        System.out.println("-----------------------------------------------------");
                        System.out.println();
                    } else {
                        arvoreReservas.adicionarReserva(novaReserva);
                        System.out.println();
                        System.out.println("-------------------------------");
                        System.out.println("Reserva adicionada com sucesso!");
                        System.out.println("-------------------------------");
                        System.out.println();
                    }
                    break;

                case 2:
                	System.out.println();
                	System.out.println("------------------------------");
                    System.out.print("ID da reserva a ser removida: ");
                    int idRemover = scanner.nextInt();
                    Reserva reservaRemovida = arvoreReservas.buscar(idRemover);
                    if (reservaRemovida != null) {
                        arvoreReservas.removerReserva(idRemover, arvoreHistorico);
                        System.out.println();
                        System.out.println("-------------------------------------------------------");
                        System.out.println("Reserva removida e adicionada ao histórico com sucesso!");
                    	System.out.println("-------------------------------------------------------");
                    	System.out.println();
                    } else {
                    	System.out.println();
                    	System.out.println("-----------------------");
                        System.out.println("Reserva não encontrada.");
                        System.out.println("-----------------------");
                        System.out.println();
                    }
                    break;

                case 3:
                	System.out.println();
                	System.out.println("-----------------------------");
                    System.out.print("ID da reserva a ser buscada: ");
                    int idBuscar = scanner.nextInt();
                    Reserva reservaEncontrada = arvoreReservas.buscar(idBuscar);
                    if (reservaEncontrada != null) {
                    	System.out.println();
                    	System.out.println("---------------------------");
                        System.out.println("Reserva encontrada: " + reservaEncontrada);
                        System.out.println();
                    } else {
                    	System.out.println();
                    	System.out.println("-----------------------");
                        System.out.println("Reserva não encontrada.");
                        System.out.println("-----------------------");
                        System.out.println();
                    }
                    break;

                case 4:
                    Reserva[] reservas = arvoreReservas.listarReservasPorDataCheckIn();
                    System.out.println();
                    System.out.println("---------------------------------------");
                    System.out.println("Reservas listadas por data de check-in:");
                    System.out.println("---------------------------------------");
                    System.out.println();
                    for (Reserva reserva : reservas) {
                        if (reserva != null) {
                            reserva.imprimirReserva();
                        }
                    }
                    break;

                case 5:
                	System.out.println();
                	System.out.println("-----------------------------");
                    System.out.print("Data de início (YYYY-MM-DD): ");
                    String dataInicio = scanner.nextLine();
                    System.out.print("Data de término (YYYY-MM-DD): ");
                    String dataFim = scanner.nextLine();
                    double taxaOcupacao = arvoreReservas.calcularTaxaOcupacao(dataInicio, dataFim);
                    System.out.printf("Taxa de ocupação no período: %.2f%%%n", taxaOcupacao * 100);
                    System.out.println("-----------------------------");
                    System.out.println();
                    break;

                case 6:
                	System.out.println();
                	System.out.println("-------------------------------");
                    arvoreReservas.relatorioQuartosMaisEMenosReservados();
                    System.out.println("-------------------------------");
                    System.out.println();
                    break;

                case 7:
                	System.out.println();
                	System.out.println("---------------------------------");
                    System.out.println("Histórico de reservas canceladas:");
                    System.out.println("---------------------------------");
                    System.out.println();
                    arvoreHistorico.imprimir();
                    break;

                case 0:
                	System.out.println();
                	System.out.println("------------------------");
                    System.out.println("Encerrando o programa...");
                    System.out.println("------------------------");
                    System.out.println();
                    scanner.close();
                    return;

                default:
                	System.out.println();
                	System.out.println("--------------------------------");
                    System.out.println("Opção inválida. Tente novamente.");
                    System.out.println("--------------------------------");
                    System.out.println();
            }
        }
    }
}