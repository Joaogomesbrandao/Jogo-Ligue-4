(defn cria-tabuleiro []
  (vec (repeat 6 (vec (repeat 7 0)))))

(defn imprime-tabuleiro [tabuleiro]
  (doseq [linha (reverse tabuleiro)]
    (println linha)))

(defn procura-linha [tabuleiro col]
  (loop [linha 0]
    (if (< linha 6)
      (if (= 0 (get-in tabuleiro [linha col]))
        linha
        (recur (inc linha)))
      nil)))

(defn jogada [tabuleiro jogador]
  (loop [tabuleiro-atual tabuleiro]
    (imprime-tabuleiro tabuleiro)
    (let [entrada (read-line)
          resultado (cond
                      (= entrada "d")
                      (do
                        (println (str "O jogador " jogador " desistiu!"))
                        (System/exit 0))
                      
                      (re-matches #"\d" entrada)
                      (let [col (dec (Integer/parseInt entrada))]
                        (if (and (>= col 0) (< col 7))
                          (let [linha (procura-linha tabuleiro-atual col)]
                            (if linha
                              (assoc-in tabuleiro-atual [linha col] jogador)
                              (do
                                (println "Coluna cheia, tente outra.")
                                tabuleiro-atual)))
                          (do
                            (println "Entrada inválida, escolha de 1 a 7.")
                            tabuleiro-atual)))
                      
                      :else (do
                              (println "Entrada inválida, tente novamente.")
                              tabuleiro-atual))]

      (if (= resultado tabuleiro-atual)
        (recur tabuleiro-atual)
        resultado))))

(defn verifica-vitoria [tabuleiro peca]
  (or
    (some true?
      (map (fn [linha]
             (some (fn [col]
                     (and (= (get-in tabuleiro [linha col]) peca)
                          (= (get-in tabuleiro [linha (+ col 1)]) peca)
                          (= (get-in tabuleiro [linha (+ col 2)]) peca)
                          (= (get-in tabuleiro [linha (+ col 3)]) peca)))
                   (range 4)))
           (range 6)))
    
    (some true?
      (map (fn [col]
             (some (fn [linha]
                     (and (= (get-in tabuleiro [linha col]) peca)
                          (= (get-in tabuleiro [(+ linha 1) col]) peca)
                          (= (get-in tabuleiro [(+ linha 2) col]) peca)
                          (= (get-in tabuleiro [(+ linha 3) col]) peca)))
                   (range 3)))
           (range 7)))
    
    (some true?
      (map (fn [linha]
             (some (fn [col]
                     (and (= (get-in tabuleiro [linha col]) peca)
                          (= (get-in tabuleiro [(+ linha 1) (+ col 1)]) peca)
                          (= (get-in tabuleiro [(+ linha 2) (+ col 2)]) peca)
                          (= (get-in tabuleiro [(+ linha 3) (+ col 3)]) peca)))
                   (range 4)))
           (range 3)))
    
    (some true?
      (map (fn [linha]
             (some (fn [col]
                     (and (= (get-in tabuleiro [linha col]) peca)
                          (= (get-in tabuleiro [(- linha 1) (+ col 1)]) peca)
                          (= (get-in tabuleiro [(- linha 2) (+ col 2)]) peca)
                          (= (get-in tabuleiro [(- linha 3) (+ col 3)]) peca)))
                   (range 4)))
           (range 3 6)))))

(defn jogar []
  (let [tabuleiro (cria-tabuleiro)]
    (loop [turno 0
           tabuleiro-atual tabuleiro]
      (let [jogador (if (zero? (mod turno 2)) 1 2)
            tabuleiro-atualizado (jogada tabuleiro-atual jogador)]
        (cond
          (verifica-vitoria tabuleiro-atualizado jogador)
          (do
            (imprime-tabuleiro tabuleiro-atualizado)
            (println (str "O jogador " jogador " venceu!"))
            jogador)

          :else
          (recur (inc turno) tabuleiro-atualizado))))))

(defn start-game
  ([]
   (start-game [0 0]))
  ([placar]
   (let [continuar true]
   (while continuar
     (let [vencedor (jogar)]
       (if (and vencedor (<= vencedor 2))
         (do
           (let [novo-placar (update-in placar [(dec vencedor)] inc)]
             (println (str "Placar: 1º Jogador " (first novo-placar) " - " (second novo-placar) " 2º Jogador"))

             (println "Você deseja continuar jogando? (s/n):")
             (let [resposta (clojure.string/lower-case (read-line))]
               (if (= resposta "s")
                 (start-game novo-placar)
                 (do
                   (println "Fim de jogo!")
                   (System/exit 0))))))))))))

(start-game)

