# placa-check — Fluxo de UX (design do Jonathan, 09/11/2026)

> Redesenho do fluxo de uso. Substitui o scaffold atual (câmera full-screen + leitura por toque) por **viewfinder pequeno + leitura automática + painel de status grande**. Captura fiel do que o Jonathan descreveu; pontos a confirmar marcados.

## Princípio
O operador não deve "operar um app" — ele aponta e o app conduz. A câmera é um detalhe pequeno; o que importa na tela é o **estado** (o que foi lido, bate ou não). Co-piloto: nunca aprova errado (ver `README.md`).

## Layout (retrato, celular fixo na estação)
```
┌─────────────────────────────┐
│  [ viewfinder ~20% ]        │  ← câmera pequena no topo, com uma moldura/alvo
│                             │
├─────────────────────────────┤
│                             │
│   PAINEL DE STATUS (~80%)   │  ← o foco visual
│                             │
│   • etapa atual             │
│   • ID lido (moto / chassi) │
│   • veredito + precisão     │
│                             │
└─────────────────────────────┘
```
- **Viewfinder:** ~20% da altura, topo, com uma moldura/retângulo-alvo indicando onde encostar a placa.
- **Painel:** o resto da tela. Texto grande, legível a distância (operador não encosta no celular).

## Fluxo (automático, sem toque)
1. **Aponta pra placa da MOTO.** Quando a leitura fica **estável ~2s**, trava sozinha → mostra **"Placa lida"** + o **ID** lido. Marca como lido.
2. **Avança sozinho pro CHASSI.** Painel: "Agora o chassi". Mesmo mecanismo (estável ~2s → trava).
3. **Mostra o resultado:**
   - **Batem:** estado positivo — ID + "consistente" + precisão (ex.: "98%").
   - **Não batem:** `inconsistência identificada` + precisão (ex.: "98% de precisão") → sinaliza o humano (cor de alerta).
4. Reseta pro próximo par (nova moto).

## Resultado em COLUNAS (duas linhas: label + valor lido)
O ID lido aparece numa mini-tabela de 2 linhas, label à esquerda e valor à direita (alinhado em coluna):
```
┌──────────┬──────────────────────┐
│ motor    │  KCO8E1 6838306      │
│ chassi   │  KC08E1 6838306      │
└──────────┴──────────────────────┘
```
- Linha 1: `motor` (placa da moto) · Linha 2: `chassi`.
- Coluna da esquerda = label fixo; coluna da direita = valor lido (grande, monoespaçado pra alinhar dígito).
- Enquanto uma etapa não foi lida, o valor fica vazio/`—`. Quando lê, preenche a linha e segue pra próxima.
- No veredito, a linha divergente ganha destaque (vermelho) pra mostrar ONDE não bate.

## Estados do painel (copy base, a refinar)
| Estado | Linha principal | Linha de apoio |
|---|---|---|
| Aguardando moto | `Aponte para a placa da MOTO` | moldura/alvo no viewfinder |
| Moto lida | `Placa lida — <ID moto>` | `Agora aponte para o CHASSI` |
| Aguardando chassi | `Aponte para o CHASSI` | — |
| MATCH | `Consistente — <ID>` | `precisão <conf>%` (verde) |
| CONFERIR | `Inconsistência identificada` | `precisão <conf>%` · `moto <ID> × chassi <ID>` (vermelho) |
| Baixa confiança | `Leitura duvidosa — confira` | sem travar; pede reposicionar (evitar reflexo) |

## Pontos a confirmar (decisão do Jonathan, não inferir)
1. **Gatilho dos 2s:** leitura ESTÁVEL por ~2s (o OCR repete o mesmo valor em quadros seguidos e trava) — é o default que faz sentido. Alternativa: contagem fixa de 2s e captura. → confirmar.
2. **"98% de precisão":** é a **confiança da leitura** (do OCR). Na inconsistência, mostra que a leitura em si é confiável (98%), então o "não bate" é real, não glitch. Se a confiança for baixa, não deveria afirmar inconsistência — deveria pedir reconferir. → confirmar a semântica da copy.
3. **Motor do OCR on-device:** o scaffold usa **ML Kit**, mas quem leu o DPM gravado a laser no teste foi o **RapidOCR** (ML Kit no DPM é não-provado). Leitura automática por stream pesa mais que captura única — a escolha do motor afeta este fluxo. → ligado à decisão em aberto do `README.md`.

## Impacto técnico (quando for implementar)
- Trocar `ImageCapture` (foto por toque) por **`ImageAnalysis`** (stream) + rodar o reconhecedor por quadro, com **debounce de estabilidade** (N quadros iguais ⇒ travou).
- Layout: `activity_main.xml` deixa de ser `FrameLayout` com câmera `match_parent` → vira vertical: viewfinder com altura ~20% (weight) + painel com o resto.
- Máquina de estados: `AGUARDA_MOTO → MOTO_LIDA → AGUARDA_CHASSI → RESULTADO → reset`, cada transição automática por estabilidade, sem o botão.

## IMPLEMENTADO (09/11/2026)
Primeira versão do fluxo codada (orientação **portrait**). Arquivos:
- `res/layout/activity_main.xml` — viewfinder ~20% + alvo + ⚙; painel com título, **tabela motor/chassi** (valor monoespaçado + **% de certeza** + botão **↻ refazer item**), **par de fotos lado a lado** (zoom na placa, `gone` até o resultado), dica/veredito e botão **Salvar**.
- `MainActivity.kt` — `ImageAnalysis` (stream) + **trava por estabilidade ~2s** + cooldown; máquina de estados MOTO→CHASSI→RESULTADO; **captura o frame no lock** (`recorteCentral` = zoom na placa); veredito com **precisão %**; `refazer(item)` relê só um; `salvarAnalise()`.
- `Feedback.kt` — **check sonoro (ToneGenerator) OU vibração** conforme `Config`.
- `Config.kt` — preferências: sinal (som/vibração) + **operador**.
- `Registro.kt` — **log CSV** (`getExternalFilesDir/analises.csv`): timestamp, operador, valores, conf, resultado, motivo, nomes das fotos. **Fotos salvas só em INCONSISTÊNCIA** (evidência em `fotos/`); em consistência grava só a linha. Depois do salvar → limpa pra próxima.
- `AndroidManifest.xml` — permissão `VIBRATE`, orientação portrait.

### Regras de produto fixadas (Jonathan, 09/11)
- Sinal sonoro/vibração ao travar cada leitura — operador se guia pelo som (config escolhe som ou vibração).
- Resultado mostra as duas fotos lado a lado SEMPRE; **salvar foto no disco só quando inconsistente** (acerto não precisa de evidência).
- `% de certeza` por linha + `↻` pra refazer só aquele item.
- Botão Salvar grava o log (timestamp/operador) e limpa.

### Pendências técnicas conhecidas
- **Motor OCR:** ainda `ReconhecedorMLKit` (a decisão ML Kit × RapidOCR segue aberta; ML Kit costuma devolver `confidence` nula em Latin → % pode sair 0 e cair sempre em CONFERIR). 
- **"Zoom na placa"** é recorte central aproximado (operador centraliza no alvo). Exato = reconhecedor retornar o bounding box dos dígitos e recortar por ele.
- Nunca foi compilado ainda — ver nota de build no README/GET-START.
