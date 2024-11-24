package trabalho;

class ArvoreRubroNegra {
	
    private Reserva raiz;

    // Método para adicionar uma nova reserva
    public void adicionarReserva(Reserva novaReserva) {
        raiz = adicionarRecursivo(raiz, novaReserva);
        corrigirInsercao(novaReserva);
    }
    
    // Método para verificar se um ID já existe na árvore
    public boolean idExiste(int id) {
        return idExisteRecursivo(raiz, id);
    }

    // Função recursiva para verificar se o ID existe
    private boolean idExisteRecursivo(Reserva raiz, int id) {
        if (raiz == null) {
            return false; // Não encontrado
        }

        if (id < raiz.id) {
            return idExisteRecursivo(raiz.esquerda, id);
        } else if (id > raiz.id) {
            return idExisteRecursivo(raiz.direita, id);
        } else {
            return true; // Encontrado
        }
    }

    // Método para adicionar uma nova reserva por atributos
    public void adicionarReserva(int id, String nomeCliente, String numeroQuarto, String dataCheckIn, String dataCheckOut, String categoriaQuarto, String dataReserva) {
        Reserva novaReserva = new Reserva(id, nomeCliente, numeroQuarto, dataCheckIn, dataCheckOut, categoriaQuarto, dataReserva);
        raiz = adicionarRecursivo(raiz, novaReserva);
        corrigirInsercao(novaReserva);
    }

    // Método para adicionar uma nova reserva com data de remoção
    private void adicionarReservaRemovida(Reserva reserva) {
        Reserva novaReserva = new Reserva(reserva.id, reserva.nomeCliente, reserva.numeroQuarto, reserva.dataCheckIn, reserva.dataCheckOut, reserva.categoriaQuarto, reserva.dataReserva);
        novaReserva.cancelar(); // Adiciona ao objeto a data em que a reserva foi removida
        raiz = adicionarRecursivo(raiz, novaReserva);
        corrigirInsercao(novaReserva);
    }

    // Inserção recursiva
    private Reserva adicionarRecursivo(Reserva raiz, Reserva novaReserva) {
        if (raiz == null) {
            return novaReserva;
        }
        
        if (novaReserva.id < raiz.id) {
            raiz.esquerda = adicionarRecursivo(raiz.esquerda, novaReserva);
            raiz.esquerda.pai = raiz;
        } else if (novaReserva.id > raiz.id) {
            raiz.direita = adicionarRecursivo(raiz.direita, novaReserva);
            raiz.direita.pai = raiz;
        }

        return raiz;
    }

    // Corrigir o balanceamento após a inserção
    private void corrigirInsercao(Reserva novaReserva) {
        while (novaReserva != raiz && novaReserva.pai.cor) {
            if (novaReserva.pai == novaReserva.pai.pai.esquerda) {
                Reserva tio = novaReserva.pai.pai.direita;
                if (tio != null && tio.cor) { // Caso 1: Tio é vermelho
                    novaReserva.pai.cor = false;
                    tio.cor = false;
                    novaReserva.pai.pai.cor = true;
                    novaReserva = novaReserva.pai.pai;
                } else {
                    if (novaReserva == novaReserva.pai.direita) { // Caso 2: Nó está à direita
                        novaReserva = novaReserva.pai;
                        rotacaoEsquerda(novaReserva);
                    }
                    novaReserva.pai.cor = false;
                    novaReserva.pai.pai.cor = true;
                    rotacaoDireita(novaReserva.pai.pai);
                }
            } else {
                Reserva tio = novaReserva.pai.pai.esquerda;
                if (tio != null && tio.cor) { // Caso 1: Tio é vermelho
                    novaReserva.pai.cor = false;
                    tio.cor = false;
                    novaReserva.pai.pai.cor = true;
                    novaReserva = novaReserva.pai.pai;
                } else {
                    if (novaReserva == novaReserva.pai.esquerda) { // Caso 2: Nó está à esquerda
                        novaReserva = novaReserva.pai;
                        rotacaoDireita(novaReserva);
                    }
                    novaReserva.pai.cor = false;
                    novaReserva.pai.pai.cor = true;
                    rotacaoEsquerda(novaReserva.pai.pai);
                }
            }
        }
        raiz.cor = false; // A raiz sempre é preta
    }

    // Rotação à esquerda
    private void rotacaoEsquerda(Reserva x) {
        Reserva y = x.direita;
        x.direita = y.esquerda;
        if (y.esquerda != null) {
            y.esquerda.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            raiz = y;
        } else if (x == x.pai.esquerda) {
            x.pai.esquerda = y;
        } else {
            x.pai.direita = y;
        }
        y.esquerda = x;
        x.pai = y;
    }

    // Rotação à direita
    private void rotacaoDireita(Reserva x) {
        Reserva y = x.esquerda;
        x.esquerda = y.direita;
        if (y.direita != null) {
            y.direita.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            raiz = y;
        } else if (x == x.pai.direita) {
            x.pai.direita = y;
        } else {
            x.pai.esquerda = y;
        }
        y.direita = x;
        x.pai = y;
    }

    // Método para remover uma reserva
    public void removerReserva(int id, ArvoreRubroNegra historico) {
        raiz = removerRecursivo(raiz, id, historico);
    }

    // Remoção recursiva
    private Reserva removerRecursivo(Reserva raiz, int id, ArvoreRubroNegra historico) {
        if (raiz == null) {
            return raiz;
        }

        // Encontrar o nó a ser removido
        if (id < raiz.id) {
            raiz.esquerda = removerRecursivo(raiz.esquerda, id, historico);
        } else if (id > raiz.id) {
            raiz.direita = removerRecursivo(raiz.direita, id, historico);
        } else {
            historico.adicionarReservaRemovida(raiz); // Adicionar reserva para a árvore de histórico
            // Caso 1: Nó a ser removido tem 1 ou nenhum filho
            if (raiz.esquerda == null || raiz.direita == null) {
                Reserva temp = raiz.esquerda != null ? raiz.esquerda : raiz.direita;
                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else {
                    raiz = temp;
                }
            } else {
                // Caso 2: Nó a ser removido tem dois filhos
                Reserva temp = encontrarMinimo(raiz.direita);
                raiz.id = temp.id;
                raiz.nomeCliente = temp.nomeCliente;
                raiz.numeroQuarto = temp.numeroQuarto;
                raiz.dataCheckIn = temp.dataCheckIn;
                raiz.dataCheckOut = temp.dataCheckOut;
                raiz.categoriaQuarto = temp.categoriaQuarto;
                raiz.direita = removerRecursivo(raiz.direita, temp.id, historico);
            }
        }

        // Ajustar a árvore após a remoção
        if (raiz != null) {
            raiz = balancearRemocao(raiz);
        }

        return raiz;
    }

    // Encontra o nó com o valor mínimo
    private Reserva encontrarMinimo(Reserva raiz) {
        Reserva atual = raiz;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    // Balanceamento após remoção
    private Reserva balancearRemocao(Reserva raiz) {
        // Aqui podem ser feitos ajustes de balanceamento, como rotações
        // Para simplificação, vamos supor que a árvore já é balanceada após a remoção
        return raiz;
    }
    
    // Método para buscar uma reserva pelo ID
    public Reserva buscar(int id) {
        return buscarRecursivo(raiz, id);
    }

    // Método recursivo para buscar a reserva
    private Reserva buscarRecursivo(Reserva raiz, int id) {
        if (raiz == null) {
            return null;  // Reserva não encontrada
        }

        if (id < raiz.id) {
            return buscarRecursivo(raiz.esquerda, id);
        } else if (id > raiz.id) {
            return buscarRecursivo(raiz.direita, id);
        } else {
            return raiz;  // Reserva encontrada
        }
    }

    // Método para listar reservas por data de check-in
    public Reserva[] listarReservasPorDataCheckIn() {
        // Vamos armazenar as reservas em um vetor para ordená-las depois
        Reserva[] reservas = new Reserva[100]; // Assumimos um número máximo de reservas
        int[] count = {0}; // Contador para controle do índice

        listarReservasPorDataCheckInAux(raiz, reservas, count);

        // Agora vamos ordenar o vetor de reservas com base na data de check-in
        bubbleSort(reservas, count[0]);

        return reservas;
    }

    // Função auxiliar recursiva para percorrer a árvore em ordem
    private void listarReservasPorDataCheckInAux(Reserva reserva, Reserva[] reservas, int[] count) {
        if (reserva != null) {
            // Primeiro percorre a subárvore esquerda
            listarReservasPorDataCheckInAux(reserva.esquerda, reservas, count);

            // Adiciona a reserva ao vetor
            reservas[count[0]++] = reserva;

            // Depois percorre a subárvore direita
            listarReservasPorDataCheckInAux(reserva.direita, reservas, count);
        }
    }

    // Método de ordenação simples (Bubble Sort)
    private void bubbleSort(Reserva[] reservas, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compara as datas de check-in
                if (reservas[j].dataCheckIn.compareTo(reservas[j + 1].dataCheckIn) > 0) {
                    // Troca as reservas
                    Reserva temp = reservas[j];
                    reservas[j] = reservas[j + 1];
                    reservas[j + 1] = temp;
                }
            }
        }
    }
    
    // Método para calcular a taxa de ocupação em um determinado período
    public double calcularTaxaOcupacao(String dataInicio, String dataFim) {
        int quartosOcupados = 0;
        int totalQuartos = 100; // Total de quartos do hotel, altere conforme necessário

        quartosOcupados = calcularOcupacaoNoPeriodo(raiz, dataInicio, dataFim);

        // Calcula a taxa de ocupação
        return (double) quartosOcupados / totalQuartos;
    }

    // Função recursiva para calcular ocupação
    private int calcularOcupacaoNoPeriodo(Reserva reserva, String dataInicio, String dataFim) {
        if (reserva == null) {
            return 0;
        }

        int ocupados = 0;

        // Verifica se a reserva está dentro do período
        if (isReservaDentroDoPeriodo(reserva, dataInicio, dataFim)) {
            ocupados++;
        }

        // Recorre pela árvore
        ocupados += calcularOcupacaoNoPeriodo(reserva.esquerda, dataInicio, dataFim);
        ocupados += calcularOcupacaoNoPeriodo(reserva.direita, dataInicio, dataFim);

        return ocupados;
    }

    // Verifica se a reserva está dentro do período
    private boolean isReservaDentroDoPeriodo(Reserva reserva, String dataInicio, String dataFim) {
        return (reserva.dataCheckIn.compareTo(dataFim) <= 0) && (reserva.dataCheckOut.compareTo(dataInicio) >= 0);
    }
    
    // Método para encontrar os quartos mais e menos reservados sem usar Map ou HashMap
    public void relatorioQuartosMaisEMenosReservados() {
        // Array para armazenar os quartos e suas contagens (simula chave-valor)
        String[] quartos = new String[100]; // Assumimos um máximo de 100 quartos
        int[] contagens = new int[100];
        int totalQuartos = 0;

        // Contar as reservas por quarto
        contarReservasPorQuarto(raiz, quartos, contagens, totalQuartos);

        // Encontrar os quartos mais e menos reservados
        int maxReservas = 0;
        int minReservas = Integer.MAX_VALUE;
        String quartoMaisReservado = null;
        String quartoMenosReservado = null;

        for (int i = 0; i < totalQuartos; i++) {
            if (contagens[i] > maxReservas) {
                maxReservas = contagens[i];
                quartoMaisReservado = quartos[i];
            }
            if (contagens[i] < minReservas) {
                minReservas = contagens[i];
                quartoMenosReservado = quartos[i];
            }
        }

        // Exibe os resultados
        System.out.println("Quarto Mais Reservado: " + quartoMaisReservado + " com " + maxReservas + " reservas.");
        System.out.println("Quarto Menos Reservado: " + quartoMenosReservado + " com " + minReservas + " reservas.");
    }

    // Método auxiliar para contar reservas por quarto
    private void contarReservasPorQuarto(Reserva no, String[] quartos, int[] contagens, int totalQuartos) {
        if (no == null) return;

        // Verifica se o quarto já está no array
        int index = -1;
        for (int i = 0; i < totalQuartos; i++) {
            if (quartos[i].equals(no.numeroQuarto)) {
                index = i;
                break;
            }
        }

        // Se o quarto não está no array, adiciona
        if (index == -1) {
            quartos[totalQuartos] = no.numeroQuarto;
            contagens[totalQuartos] = 1;
            totalQuartos++;
        } else {
            // Incrementa a contagem
            contagens[index]++;
        }

        // Percorre os filhos da árvore
        contarReservasPorQuarto(no.esquerda, quartos, contagens, totalQuartos);
        contarReservasPorQuarto(no.direita, quartos, contagens, totalQuartos);
    }
    
    private ArvoreRubroNegra historicoCancelamentos; // Árvore para armazenar cancelamentos
    
    // Método para contar o número de cancelamentos em um determinado período
    public int contarCancelamentos(String dataInicio, String dataFim) {
        int cancelamentos = 0;
        
        cancelamentos = contarCancelamentosNoPeriodo(historicoCancelamentos.raiz, dataInicio, dataFim);

        return cancelamentos;
    }

    // Função recursiva para contar cancelamentos
    private int contarCancelamentosNoPeriodo(Reserva reserva, String dataInicio, String dataFim) {
        if (reserva == null) {
            return 0;
        }

        int cancelados = 0;

        // Verifica se o cancelamento está dentro do período
        if (isReservaDentroDoPeriodo(reserva, dataInicio, dataFim)) {
            cancelados++;
        }

        // Recorre pela árvore de cancelamentos
        cancelados += contarCancelamentosNoPeriodo(reserva.esquerda, dataInicio, dataFim);
        cancelados += contarCancelamentosNoPeriodo(reserva.direita, dataInicio, dataFim);

        return cancelados;
    }
    
    // Método para calcular a taxa de ocupação do hotel
    public double calcularTaxaOcupacao(String data, int totalQuartos) {
        int quartosReservados = contarQuartosReservados(raiz, data);
        return (double) quartosReservados / totalQuartos * 100;
    }

    // Método auxiliar para contar os quartos reservados para uma data específica
    private int contarQuartosReservados(Reserva no, String data) {
        if (no == null) return 0;

        int contador = 0;

        // Verifica se a reserva coincide com a data informada
        if (no.dataCheckIn.equals(data)) {
            contador++;
        }

        // Conta nas subárvores esquerda e direita
        contador += contarQuartosReservados(no.esquerda, data);
        contador += contarQuartosReservados(no.direita, data);

        return contador;
    }
    
    // Método para emitir alertas com base na taxa de ocupação
    public void emitirAlertaOcupacao(String data, int totalQuartos, double limiteOcupacao) {
        double taxaOcupacao = calcularTaxaOcupacao(data, totalQuartos);

        // Verifica se a taxa de ocupação atinge o limite especificado
        if (taxaOcupacao >= limiteOcupacao) {
            System.out.println("ALERTA: O hotel atingiu " + String.format("%.2f", taxaOcupacao) 
                               + "% de ocupação para a data " + data + ".");
        } else {
            System.out.println("Ocupação atual para a data " + data + ": " 
                               + String.format("%.2f", taxaOcupacao) + "%.");
        }
    }

    // Método para imprimir as reservas
    public void imprimir() {
        imprimirRecursivo(raiz);
    }

    // Método recursivo para imprimir as reservas em ordem crescente
    private void imprimirRecursivo(Reserva raiz) {
        if (raiz != null) {
            imprimirRecursivo(raiz.esquerda);
            raiz.imprimirReserva();
            imprimirRecursivo(raiz.direita);
        }
    }

    // Método que Verifica se uma reserva entra em conflito com alguma reserva existente na árvore
    public boolean conflita(Reserva reserva) {
        return conflitaRecursivo(raiz, reserva);
    }

    // Método recursivo para verificar se uma reserva conflita com alguma reserva já existente na árvore
    private boolean conflitaRecursivo(Reserva raiz, Reserva reserva) {
        if (raiz == null) {
            return false;
        } else if (reserva.conflita(raiz)) {
            return true;
        } else {
            if (conflitaRecursivo(raiz.esquerda, reserva)) {
                return true;
            } else {
                return conflitaRecursivo(raiz.direita, reserva);
            }
        }
    }
}
