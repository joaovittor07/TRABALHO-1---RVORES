package trabalho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Reserva {
    int id; // ID do cliente ou CPF
    String nomeCliente;
    String numeroQuarto;
    String dataCheckIn;
    String dataCheckOut;
    String categoriaQuarto;
    String dataReserva; // Data em que a reserva foi realizada
    boolean cor; // Cor do nó: verdadeiro para vermelho, falso para preto
    Reserva pai, esquerda, direita;
    String dataCancelamento;
    
    // Construtor
    public Reserva(int id, String nomeCliente, String numeroQuarto, String dataCheckIn, String dataCheckOut, String categoriaQuarto, String dataReserva) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.numeroQuarto = numeroQuarto;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.categoriaQuarto = categoriaQuarto;
        this.dataReserva = dataReserva;
        this.cor = true; // Inicialmente, todos os nós são vermelhos
        this.pai = null;
        this.esquerda = null;
        this.direita = null;
    }

    // Métodos para comparar datas (em formato String ou Date)
    public boolean estaDisponivelPara(String checkIn, String checkOut) {
        // Verificar se as datas de check-in e check-out da reserva não se sobrepõem com o intervalo desejado
        return !(checkIn.compareTo(this.dataCheckOut) < 0 && checkOut.compareTo(this.dataCheckIn) > 0);
    }
    
    // Método para imprimir a reserva
    public void imprimirReserva() {
        System.out.println("ID Cliente: " + id);
        System.out.println("Nome Cliente: " + nomeCliente);
        System.out.println("Número do Quarto: " + numeroQuarto);
        System.out.println("Data Check-In: " + dataCheckIn);
        System.out.println("Data Check-Out: " + dataCheckOut);
        System.out.println("Categoria do Quarto: " + categoriaQuarto);
        System.out.println("Data da Reserva: " + dataReserva);
        System.out.println("Cor do Nó: " + (cor ? "Vermelho" : "Preto"));
        System.out.println();
    }

    public void cancelar() {
        dataCancelamento = LocalDateTime.now().toString();
    }

    public boolean conflita(Reserva outra) {
        if (this.numeroQuarto.equalsIgnoreCase(outra.numeroQuarto)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate thisCheckIn = LocalDate.parse(this.dataCheckIn, formatter);
            LocalDate thisCheckOut = LocalDate.parse(this.dataCheckOut, formatter);
            LocalDate outraCheckIn = LocalDate.parse(outra.dataCheckIn, formatter);
            LocalDate outraCheckOut = LocalDate.parse(outra.dataCheckOut, formatter);
    
            // Check for overlap
            return thisCheckIn.isBefore(outraCheckOut) && thisCheckOut.isAfter(outraCheckIn);
        } else {
            return false;
        }
    }

}

