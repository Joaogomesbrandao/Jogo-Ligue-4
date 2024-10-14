def cria_placar():
    placar = [0, 0]
    return placar

def cria_tabuleiro():
    tabuleiro = [[0 for _ in range(7)] for _ in range(6)]
    return tabuleiro

def print_tabuleiro(tabuleiro):
    for linha in reversed(tabuleiro):
        print(linha)

def verifica_vitoria(tabuleiro, peca):
    for linha in range(6):
        for col in range(4):
            if tabuleiro[linha][col] == peca and tabuleiro[linha][col+1] == peca and tabuleiro[linha][col+2] == peca and tabuleiro[linha][col+3] == peca:
                return True

    for col in range(7):
        for linha in range(3):
            if tabuleiro[linha][col] == peca and tabuleiro[linha+1][col] == peca and tabuleiro[linha+2][col] == peca and tabuleiro[linha+3][col] == peca:
                return True

    for linha in range(3):
        for col in range(4):
            if tabuleiro[linha][col] == peca and tabuleiro[linha+1][col+1] == peca and tabuleiro[linha+2][col+2] == peca and tabuleiro[linha+3][col+3] == peca:
                return True

    for linha in range(3,6):
        for col in range(4):
            if tabuleiro[linha][col] == peca and tabuleiro[linha-1][col+1] == peca and tabuleiro[linha-2][col+2] == peca and tabuleiro[linha-3][col+3] == peca:
                return True

    return False

def procura_linha(tabuleiro, coluna):
    for linha in range(6):
        if tabuleiro[linha][coluna] == 0:
            return linha
    return None

def jogada(tabuleiro, jogador):
    while True:
        entrada = input()
        
        if entrada == 'd':
            print(f'O jogador {jogador} desistiu.')
            return "desistiu"

        if entrada.isdigit():
            col = int(entrada) - 1
            if 0 <= col <= 6:
                if tabuleiro[5][col] == 0:
                    linha = procura_linha(tabuleiro, col)
                    tabuleiro[linha][col] = jogador
                    return jogador
                else:
                    print("Coluna cheia. Tente outra.")
            else:
                print("Escolha uma coluna entre 1 e 7.")
        else:
            print("Entrada inválida. Tente novamente...")

def jogo():
    tabuleiro = cria_tabuleiro()
    turno = 0
    acabou = False

    while not acabou:
        print_tabuleiro(tabuleiro)
        jogador = 1 if turno % 2 == 0 else 2
        resultado = jogada(tabuleiro, jogador)

        if resultado == "desistiu":
            return 0
        elif verifica_vitoria(tabuleiro, jogador):
            print_tabuleiro(tabuleiro)
            print(f'O jogador {jogador} venceu!')
            return jogador
        else:
            turno += 1

def start_game():
    placar = cria_placar()
    continuar = True

    while continuar:
        vencedor = jogo()
        if vencedor == 0: break
        placar[vencedor - 1] += 1
        print(f'Placar - 1º jogador {placar[0]} x {placar[1]} 2º jogador')

        resposta = input("Deseja jogar novamente? (s/n): ").lower()
        if resposta != 's':
            continuar = False

    print("Fim")

start_game()