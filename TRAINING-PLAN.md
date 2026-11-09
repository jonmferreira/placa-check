# Plano de treino do reconhecimento (pro APK on-device)

> Vale pro MVP do colega (fase 2) e é a base do pipeline-startup. O reconhecimento roda 100% no device, sem nuvem.

## O desafio real

Número gravado a laser em metal (DPM, Direct Part Marking): baixo contraste, reflexo, e o caso crítico do **dígito pela metade**. OCR genérico erra. A diferença de um bom sistema não é "ler certo", é **saber quando não tem certeza e sinalizar**.

## Estratégia em 2 fases

**Fase 1 (POC, hoje, sem treino):** ML Kit Text Recognition, on-device, nativo Android, grátis. Valida o fluxo (captura → lê → compara → sinaliza) e já serve pra **coletar as fotos** na condição real. Vai errar em alguns DPM, mas prova a ideia pro chefe do colega.

**Fase 2 (produto, treinado nas fotos reais):** modelo embutido no APK.
- **Opção A, Tesseract fine-tuned (.traineddata):** treina no font/estilo das placas (tesstrain). Clássico, roda via Tesseract4Android. Alinhado com "OCR clássico local".
- **Opção B, TFLite (detecção da ROI + CRNN/CTC):** mais robusto pra metal gravado. Exporta `.tflite`, vai nos assets do APK.

## Coleta de dados (o colega faz na linha)

- Fotografar **centenas a milhares** de placas (moto + chassi) na condição real: mesma distância, ângulo e luz da estação (câmera fixa).
- **Rotular** o número correto de cada.
- Guardar à parte os casos de **dígito pela metade** — são o ouro pra ensinar o "sinaliza".

## Pré-processamento (clássico, roda no device)

Como a câmera é fixa, dá pra ter **ROI fixo** (recorte da região da placa). Depois: realce de contraste (CLAHE), threshold adaptativo, deskew. Só isso já melhora muito o OCR.

## Augmentation (ensina a desconfiar)

Simular reflexo, variação de luz, rotação leve e, principalmente, **mascarar pedaços de dígitos** — pra o modelo aprender a dar **baixa confiança** no dígito incompleto em vez de chutar.

## Regra de decisão (o coração do co-piloto)

Aprova automático (verde) **só se as três forem verdade**:
1. O número da moto == número do chassi (as duas gravações batem).
2. O número está na **lista do lote** (pré-cadastrada).
3. Toda a leitura está **acima do limiar de confiança** por caractere.

Qualquer uma falhando → tela vermelha **"CONFERIR"**, manda pro humano. **Nunca aprovar errado** (o alto custo de um erro justifica errar pro lado seguro: aceitar alguns falso-alarmes, zero falso-aprovado).

## Validação

Conjunto de teste (fotos nunca vistas). Medir: acurácia por caractere, por placa, e a **taxa de sinalizar certo o dígito pela metade**. Meta: **zero falso-aprovado**.

## Embutir no APK

O `.tflite` (ou `.traineddata`) vai nos `assets`. Tudo on-device: sem nuvem, sem dado saindo da fábrica. Ideal pro chão de fábrica.
