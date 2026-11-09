# Como rodar (passo a passo pro colega)

Projeto Android enxuto. A ideia é só provar que a solução funciona: o celular lê a placa da moto, lê a do chassi e diz se batem.

## 1. Abrir

1. Instale o **Android Studio** (qualquer versão recente, 2024+).
2. `File > Open` e escolha a pasta `placa-check`.
3. Na primeira abertura o Android Studio vai pedir pra **sincronizar o Gradle** e **gerar o Gradle Wrapper** — aceite tudo. Ele baixa o que falta sozinho.

## 2. Rodar no celular

1. No celular Android: Ajustes > Sobre > toque 7x em "Número da versão" pra liberar o **modo desenvolvedor**, depois ative **Depuração USB**.
2. Conecte por USB, aceite o aviso no celular.
3. No Android Studio, selecione o aparelho no topo e clique no **▶ (Run)**.
4. O app pede permissão de câmera na primeira vez — autorize.

## 3. Usar (fluxo automático, sem toque)

1. Aponte o **viewfinder** (faixa de cima) pra placa da **MOTO** e segure firme ~2s. Quando a leitura trava, dá um **bip** (ou vibra) e o número aparece na linha "motor".
2. Ele passa **sozinho** pro **CHASSI** — segure ~2s de novo (outro bip).
3. **Resultado:** as duas fotos aparecem **lado a lado** (zoom na placa) + o veredito:
   - **Consistente** (verde): batem, estão no lote e a leitura veio firme.
   - **Inconsistência identificada** (vermelho): algo não bate; mostra a **% de precisão**.
4. **↻** em cada linha refaz só aquele item. **⚙** (topo) escolhe **som/vibração** e o **operador**.
5. **Salvar** grava o log (`analises.csv`) e, **só quando dá inconsistência**, as fotos de evidência; depois **limpa** pra próxima análise.

Onde ficam os registros: pasta de arquivos do app no device (`Android/data/com.placacheck/files/` → `analises.csv` + `fotos/`). Dá pra puxar via `adb pull`.

> O app **nunca aprova no automático quando tem dúvida** — erra sempre pro lado seguro. Co-piloto, não substituto.

## 4. Pra demo ficar convincente

Abra `app/src/main/java/com/placacheck/dados/Lote.kt` e troque os números do mock por **alguns chassis reais** do lote que você vai mostrar. Assim o MATCH acontece de verdade na frente do chefe.

## O que isso NÃO é ainda

Esta é a Fase 1 (POC com ML Kit, sem treino). Ela já valida o fluxo e serve pra **coletar as fotos reais** da linha. O reconhecimento de número gravado a laser em metal (o caso do dígito pela metade) melhora muito na Fase 2, com um modelo treinado nessas fotos. Detalhes em `TRAINING-PLAN.md`.
