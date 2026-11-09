# placa-check

Verificação automática de numeração em linha de montagem. Co-piloto que lê o número gravado a laser na **placa da moto** e no **chassi**, confere se batem (e se batem com a lista do lote), e **sinaliza pro humano** os casos duvidosos (dígito pela metade, baixa confiança). Roda **on-device** num celular Android fixo na estação.

> Scaffold inicial gerado em 25/10/2026. O reconhecimento treinado entra quando tivermos fotos reais das placas. Ver `TRAINING-PLAN.md`.

## O problema

Na linha, um operador passa 9h/dia conferindo a olho se o número da moto bate com o do chassi. No cansaço, passa erro. Cada falha custa caro. Hoje é manual, com uma câmera que vê à distância.

## Decisões (tomadas com o Jonathan)

- **Reconhecimento:** OCR clássico local, on-device. POC com ML Kit; modelo treinado na fase 2.
- **Papel:** co-piloto que sinaliza. Nunca aprova automático no caso duvidoso.
- **Stack:** Android (Kotlin + CameraX). Sem nuvem, sem dado saindo da fábrica.

## Arquitetura

```
Celular fixo (CameraX)
  ├─ captura a placa da MOTO  → Reconhecedor → número + confiança
  ├─ captura o CHASSI         → Reconhecedor → número + confiança
  └─ Verificador:
       1) os dois números batem entre si?
       2) o número está na lista do lote (pré-cadastrada)?
       3) toda a leitura está acima do limiar de confiança?
       → MATCH (verde)  OU  CONFERIR (vermelho, sinaliza o humano)

Reconhecedor (plugável):
  - Fase 1: ReconhecedorMLKit (on-device, sem treino)
  - Fase 2: ReconhecedorTFLite / Tesseract (.traineddata) treinado nas fotos reais

Lista do lote: arquivo local (CSV/JSON) carregado ~1 semana antes da chegada do lote.
Display: resultado grande na tela do celular. Fase 3: espelhar/sinalizar o app da TV da estação.
```

## Estrutura

```
placa-check/
├── README.md
├── GET-START.md            ← passo a passo pro colega abrir e rodar
├── TRAINING-PLAN.md        ← como treinar o reconhecimento pro APK
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties       ← AndroidX + encoding (sem isso o build falha)
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/layout/activity_main.xml  ← câmera + painel de resultado
        └── java/com/placacheck/
            ├── Reconhecedor.kt           ← interface + Leitura (plugável)
            ├── MainActivity.kt           ← CameraX + fluxo de 2 toques + UI
            ├── ocr/ReconhecedorMLKit.kt  ← POC on-device (ML Kit)
            ├── logica/Verificador.kt     ← compara moto x chassi x lote + dígito parcial
            └── dados/Lote.kt             ← lista pré-cadastrada do lote
```

## Como rodar

Passo a passo completo pro colega em **`GET-START.md`**. Resumo:

1. Abrir a pasta no **Android Studio** (`File > Open`) e aceitar o sync + geração do Gradle Wrapper.
2. `build.gradle.kts` usa AGP 8.7.0 + Kotlin 2.0.21 (mesmo do VaultCam) — ajustar se o AS instalado for diferente.
3. Rodar num device físico (câmera exige device real).
4. POC: toca → lê a MOTO, toca de novo → lê o CHASSI e compara com a lista mock em `Lote.kt`.

## Roadmap

- [x] Fase 0: scaffold + plano de treino.
- [ ] Fase 1 (POC): ML Kit on-device validando o fluxo + coleta de fotos na linha.
- [ ] Fase 2 (produto): treinar o modelo nas fotos reais, embutir no APK, calibrar o limiar do dígito pela metade.
- [ ] Fase 3: modo celular → app da TV da estação (confirmar se a TV é Android TV ou só monitor).

## O que falta pra avançar

**Fotos reais das placas** (moto + chassi) tiradas na condição da estação (mesma distância, ângulo, luz), incluindo exemplos com dígito pela metade. É o insumo do treino.
