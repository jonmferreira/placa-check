# Gera o notebook do pipeline de calibracao e treino do placa-check.
# Escreve .ipynb valido via json.dump (sem escaping manual).
import json, uuid, pathlib

cells = []
def md(t):  cells.append(("markdown", t))
def code(t): cells.append(("code", t))

md("""# placa-check, pipeline de calibracao e treino

Notebook reproduzivel do reconhecimento de numero gravado a laser (moto vs chassi, montadora).
Guarda cada etapa: filtragem, pre-processamento, baseline, treino (duas opcoes), checagem e otimizacao futura.

**Como usar:** e um template. Fica vivo quando as fotos reais chegarem em `dados/`.
Alvo: tudo roda local, sem nuvem, e o modelo final vai embarcado no APK.

**Regra inegociavel:** co-piloto, nunca aprova errado. Qualquer duvida vira CONFERIR e vai pro humano.
""")

md("""## Estado da arte (2024 a 2026)

Sintese da literatura recente sobre reconhecimento em metal gravado e OCR com rejeicao.
Papers na ultima celula (Referencias). Os de IEEE/Elsevier/Springer estao marcados para verificar o texto completo via Crawl4AI no IEEE.

**Reconhecimento em superficie metalica (DPM, baixo contraste):**
- Pre-processamento classico (cinza, CLAHE, threshold adaptativo) e a base.
- Fusao multi-exposicao melhora leitura em metal curvo e com reflexo.
- Frameworks de atencao ponta a ponta (ex. RFANET-OCR, 2025) sao o estado da arte para texto em metal.

**Arquitetura de reconhecimento de sequencia:**
- CRNN: CNN extrai features, Bi-LSTM modela a sequencia, CTC decodifica. Padrao da industria.
- Alternativa recente: baseada em atencao/transformer.

**Confianca e rejeicao (o ponto critico para o nosso caso):**
- A confianca do OCR costuma ser por caractere. Um unico caractere ruim derruba o conjunto.
  Isso valida o nosso design: usar a MENOR confianca por caractere como gatilho.
- Porem a confianca e mal calibrada (modelo confiante e errado). O limiar precisa ser
  CALIBRADO nas fotos reais, nao usar o valor cru.
- Abordagem em faixas: aceita confianca alta, manda a zona cinza para revisao humana.
  No nosso caso, qualquer coisa abaixo do limiar vira CONFERIR.
""")

code("""# Setup e configuracao
from pathlib import Path
import cv2
import numpy as np

# Pastas (coloque as fotos reais aqui quando chegarem)
RAIZ = Path.cwd().parent if (Path.cwd().name == 'notebooks') else Path.cwd()
DADOS = RAIZ / 'notebooks' / 'dados'          # imagens brutas
ROTULOS = RAIZ / 'notebooks' / 'rotulos.csv'  # arquivo,numero_correto
DADOS.mkdir(parents=True, exist_ok=True)

# Parametros que serao CALIBRADOS com as fotos reais
ROI = None            # (x, y, largura, altura); fixo porque a camera e fixa. None = imagem inteira
LIMIAR_CONFIANCA = 0.80
CLAHE_CLIP = 2.0
CLAHE_TILE = (8, 8)
THRESH_BLOCK = 31
THRESH_C = 10

print('OpenCV', cv2.__version__, '| pasta de dados:', DADOS)
""")

md("""## 1. Carregar e filtrar imagens

Carrega as fotos e descarta as ruins (muito tremidas ou escuras demais) antes de qualquer coisa.
Camera fixa ajuda: a maioria deve passar.
""")

code("""def nitidez(img):
    # variancia do Laplaciano: baixo = borrado
    return cv2.Laplacian(img, cv2.CV_64F).var()

def brilho(img):
    return float(img.mean())

def carregar_e_filtrar(pasta, min_nitidez=60.0, faixa_brilho=(25, 230)):
    bons, descartados = [], []
    for p in sorted(Path(pasta).glob('*')):
        if p.suffix.lower() not in {'.jpg', '.jpeg', '.png'}:
            continue
        img = cv2.imread(str(p))
        if img is None:
            descartados.append((p.name, 'nao abriu')); continue
        g = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        n, b = nitidez(g), brilho(g)
        if n < min_nitidez:
            descartados.append((p.name, f'borrada ({n:.0f})'))
        elif not (faixa_brilho[0] <= b <= faixa_brilho[1]):
            descartados.append((p.name, f'brilho ruim ({b:.0f})'))
        else:
            bons.append((p, img))
    return bons, descartados

bons, descartados = carregar_e_filtrar(DADOS)
print(f'{len(bons)} boas, {len(descartados)} descartadas')
for nome, motivo in descartados[:10]:
    print('  descartada:', nome, '->', motivo)
if not bons:
    print('\\nSem fotos ainda. Coloque as imagens em', DADOS, 'e rode de novo.')
""")

md("""## 2. Pre-processamento (classico OpenCV)

ROI fixo (recorte da regiao da placa), cinza, CLAHE (contraste), threshold adaptativo e deskew.
Isso ja melhora muito a leitura do metal gravado antes de qualquer OCR.
Nota: fusao multi-exposicao (varias fotos com exposicoes diferentes) e o upgrade do estado da arte para reflexo em metal; fica como hook na secao 7.
""")

code("""def recortar_roi(img, roi=ROI):
    if roi is None:
        return img
    x, y, w, h = roi
    return img[y:y+h, x:x+w]

def realcar(img):
    g = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) if img.ndim == 3 else img
    clahe = cv2.createCLAHE(clipLimit=CLAHE_CLIP, tileGridSize=CLAHE_TILE)
    g = clahe.apply(g)
    return cv2.adaptiveThreshold(g, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                 cv2.THRESH_BINARY, THRESH_BLOCK, THRESH_C)

def deskew(bin_img):
    pts = np.column_stack(np.where(bin_img > 0))
    if len(pts) < 50:
        return bin_img
    ang = cv2.minAreaRect(pts.astype(np.float32))[-1]
    ang = -(90 + ang) if ang < -45 else -ang
    h, w = bin_img.shape
    M = cv2.getRotationMatrix2D((w/2, h/2), ang, 1.0)
    return cv2.warpAffine(bin_img, M, (w, h), flags=cv2.INTER_CUBIC,
                          borderMode=cv2.BORDER_REPLICATE)

def preparar(img, roi=ROI):
    return deskew(realcar(recortar_roi(img, roi)))

# Visualizar antes e depois (se houver foto e matplotlib)
try:
    import matplotlib.pyplot as plt
    if bons:
        _, img0 = bons[0]
        prep = preparar(img0)
        fig, ax = plt.subplots(1, 2, figsize=(10, 4))
        ax[0].imshow(cv2.cvtColor(img0, cv2.COLOR_BGR2RGB)); ax[0].set_title('original'); ax[0].axis('off')
        ax[1].imshow(prep, cmap='gray'); ax[1].set_title('pre-processada'); ax[1].axis('off')
        plt.show()
except ImportError:
    print('matplotlib nao instalado (opcional): pip install matplotlib')
""")

md("""## 3. Baseline OCR (Fase 1, sem treino)

Mede o quao longe um OCR generico chega nas fotos reais, ja usando a menor confianca por caractere.
Opcoes: Tesseract (pytesseract) ou EasyOCR. Sao opcionais; instale uma delas para rodar.
Serve para decidir a Fase 2 e para ter um ponto de comparacao.
""")

code("""# Escolha um motor de baseline (instale a dependencia correspondente)
MOTOR = 'tesseract'   # 'tesseract' (pip install pytesseract + binario) ou 'easyocr' (pip install easyocr)

def ler_tesseract(bin_img, whitelist='0123456789'):
    import pytesseract
    cfg = f'--psm 7 -c tessedit_char_whitelist={whitelist}'
    d = pytesseract.image_to_data(bin_img, config=cfg, output_type=pytesseract.Output.DICT)
    digs, menor = [], 100.0
    for txt, conf in zip(d['text'], d['conf']):
        so = ''.join(ch for ch in txt if ch.isdigit())
        if not so:
            continue
        digs.append(so)
        c = float(conf) if str(conf).replace('.', '', 1).lstrip('-').isdigit() else 0.0
        if 0 <= c < menor:
            menor = c
    if not digs:
        return None
    return ''.join(digs), (menor / 100.0 if menor <= 100 else 0.0)

def ler_easyocr(bin_img):
    import easyocr
    global _READER
    try:
        _READER
    except NameError:
        _READER = easyocr.Reader(['en'], gpu=False)
    res = _READER.readtext(bin_img, allowlist='0123456789', detail=1)
    if not res:
        return None
    digs = ''.join(''.join(ch for ch in t if ch.isdigit()) for _, t, _ in res)
    menor = min((p for _, _, p in res), default=0.0)
    return (digs, float(menor)) if digs else None

def ler(bin_img):
    return ler_tesseract(bin_img) if MOTOR == 'tesseract' else ler_easyocr(bin_img)

# Rodar o baseline nas fotos boas (precisa de rotulos.csv para medir acuracia)
if bons:
    for p, img in bons[:5]:
        out = ler(preparar(img))
        print(p.name, '->', out)
""")

md("""## 4. Dataset e aumento de dados

Rotulagem: um CSV `arquivo,numero_correto`. O aumento (augmentation) simula a realidade da linha:
reflexo, variacao de luz, leve rotacao e, principalmente, MASCARAR pedacos de digito, que e o que
ensina o modelo a dar baixa confianca no digito pela metade em vez de chutar.
""")

code("""import random

def aug_reflexo(g):
    over = np.zeros_like(g);
    cx, cy = random.randint(0, g.shape[1]-1), random.randint(0, g.shape[0]-1)
    cv2.circle(over, (cx, cy), random.randint(10, 40), 255, -1)
    over = cv2.GaussianBlur(over, (0, 0), 15)
    return cv2.addWeighted(g, 1.0, over, random.uniform(0.2, 0.5), 0)

def aug_luz(g):
    return np.clip(g.astype(np.float32) * random.uniform(0.6, 1.4), 0, 255).astype(np.uint8)

def aug_rotacao(g, graus=4):
    h, w = g.shape
    M = cv2.getRotationMatrix2D((w/2, h/2), random.uniform(-graus, graus), 1.0)
    return cv2.warpAffine(g, M, (w, h), borderMode=cv2.BORDER_REPLICATE)

def aug_mascarar_digito(g):
    # cobre parte da imagem para simular o digito pela metade (o caso de ouro)
    h, w = g.shape
    x = random.randint(0, max(0, w - w//6)); bw = w//6
    g = g.copy(); g[:, x:x+bw] = int(g.mean())
    return g

AUGS = [aug_reflexo, aug_luz, aug_rotacao, aug_mascarar_digito]

def aumentar(g, n=4):
    saida = []
    for _ in range(n):
        x = g
        for a in random.sample(AUGS, k=random.randint(1, 2)):
            x = a(x)
        saida.append(x)
    return saida

print('Funcoes de aumento prontas. Aplicar depois de ter as fotos e os rotulos.')
""")

md("""## 5A. Treino, opcao Tesseract fine-tune (.traineddata)

Clássico, mais simples de treinar, alinhado com OCR classico local. Roda via tesstrain.
Template de comandos (rodar fora do notebook, com as fotos rotuladas):
""")

code("""# TEMPLATE (nao roda aqui). Fine-tune do Tesseract com tesstrain.
# Requer: tesseract instalado, repo tesstrain, e os pares imagem/texto (ground truth).
#
#   git clone https://github.com/tesseract-ocr/tesstrain
#   # colocar imagens .png e .gt.txt (texto correto) em data/placacheck-ground-truth/
#   make training MODEL_NAME=placacheck START_MODEL=eng TESSDATA=/usr/share/tessdata \\
#        MAX_ITERATIONS=4000
#   # saida: placacheck.traineddata  ->  vai para os assets do APK
print('Ver comandos acima. Decidir Tesseract vs TFLite depois de ver as fotos.')
""")

md("""## 5B. Treino, opcao TFLite (CRNN + CTC)

Mais robusto para metal gravado e digito incompleto. Exporta `.tflite` para embarcar no APK.
Esqueleto da arquitetura (CNN -> Bi-LSTM -> CTC). Requer tensorflow; e template.
""")

code("""# TEMPLATE (nao roda aqui sem tensorflow e sem as fotos). Esqueleto CRNN + CTC.
def montar_crnn(altura=32, largura=128, n_classes=11):  # 10 digitos + blank do CTC
    import tensorflow as tf
    from tensorflow.keras import layers, Model
    inp = layers.Input(shape=(altura, largura, 1), name='imagem')
    x = inp
    for f in (32, 64, 128):
        x = layers.Conv2D(f, 3, padding='same', activation='relu')(x)
        x = layers.MaxPooling2D((2, 2))(x)
    # colapsa a altura, mantem a largura como passo temporal
    x = layers.Reshape((x.shape[2], x.shape[1] * x.shape[3]))(x)
    x = layers.Bidirectional(layers.LSTM(128, return_sequences=True))(x)
    x = layers.Bidirectional(layers.LSTM(128, return_sequences=True))(x)
    out = layers.Dense(n_classes, activation='softmax', name='logits')(x)
    return Model(inp, out)

# Treino com tf.nn.ctc_loss, early stopping, e export:
#   conv = tf.lite.TFLiteConverter.from_keras_model(modelo)
#   open('placacheck.tflite','wb').write(conv.convert())   # -> assets do APK
print('Esqueleto CRNN pronto como template. Preencher treino quando houver dataset.')
""")

md("""## 6. Checagem e a regra de ouro (zero falso-aprovado)

A metrica que importa nao e so acuracia: e a taxa de FALSO-APROVADO, que tem que ser ZERO.
Simula a decisao final (moto == chassi E no lote E confianca acima do limiar) e CALIBRA o limiar.
A literatura alerta: a confianca crua e mal calibrada, entao o limiar e escolhido aqui, nos dados reais.
""")

code("""from dataclasses import dataclass

@dataclass
class Leitura:
    texto: str
    confianca_min: float

def decidir(moto, chassi, lote, limiar):
    # retorna 'MATCH' ou 'CONFERIR'
    if moto is None or chassi is None:
        return 'CONFERIR'
    if moto.confianca_min < limiar or chassi.confianca_min < limiar:
        return 'CONFERIR'
    if moto.texto != chassi.texto:
        return 'CONFERIR'
    if lote and moto.texto not in lote:
        return 'CONFERIR'
    return 'MATCH'

def avaliar(casos, lote, limiar):
    # casos: lista de (moto: Leitura, chassi: Leitura, numero_verdadeiro)
    falso_aprovado = falso_alarme = ok = 0
    for moto, chassi, verdade in casos:
        d = decidir(moto, chassi, lote, limiar)
        correto = (moto and chassi and moto.texto == verdade and chassi.texto == verdade
                   and (verdade in lote if lote else True))
        if d == 'MATCH' and not correto:
            falso_aprovado += 1          # NUNCA pode acontecer
        elif d == 'CONFERIR' and correto:
            falso_alarme += 1            # aceitavel (humano confere)
        else:
            ok += 1
    return dict(falso_aprovado=falso_aprovado, falso_alarme=falso_alarme, ok=ok)

def calibrar_limiar(casos, lote, grade=np.linspace(0.5, 0.98, 25)):
    # escolhe o MENOR limiar que ainda da zero falso-aprovado (menos falso-alarme)
    linhas = []
    for L in grade:
        r = avaliar(casos, lote, float(L))
        linhas.append((round(float(L), 3), r['falso_aprovado'], r['falso_alarme'], r['ok']))
    seguros = [l for l in linhas if l[1] == 0]
    melhor = min(seguros, key=lambda l: l[2]) if seguros else None
    print('limiar | falso_aprov | falso_alarme | ok')
    for l in linhas:
        marca = '  <-- escolhido' if l == melhor else ''
        print(f'  {l[0]:.3f} |     {l[1]:>3} |      {l[2]:>3} | {l[3]:>3}{marca}')
    return melhor[0] if melhor else None

print('Rode calibrar_limiar(casos, lote) quando tiver as leituras reais. Meta: falso_aprovado = 0.')
""")

md("""## 7. Otimizacao futura (hooks)

O que ajustar quando chegar mais dado ou piorar: ROI, limiar, parametros do CLAHE, coletar mais
casos dificeis (digito pela metade), e testar fusao multi-exposicao. Versione o modelo a cada mudanca.
""")

code("""# Varredura de parametros de pre-processamento vs leitura (template).
# Precisa de rotulos para medir. Varia CLAHE_CLIP e THRESH_BLOCK e ve o impacto.
def varrer_preproc(bons_rotulados, clips=(1.5, 2.0, 3.0), blocos=(21, 31, 41)):
    # bons_rotulados: lista de (img, numero_verdadeiro)
    resultados = []
    for clip in clips:
        for bloco in blocos:
            global CLAHE_CLIP, THRESH_BLOCK
            CLAHE_CLIP, THRESH_BLOCK = clip, bloco
            acertos = 0
            for img, verdade in bons_rotulados:
                out = ler(preparar(img))
                if out and out[0] == verdade:
                    acertos += 1
            resultados.append((clip, bloco, acertos, len(bons_rotulados)))
    for clip, bloco, a, n in sorted(resultados, key=lambda r: -r[2]):
        print(f'CLAHE_CLIP={clip} THRESH_BLOCK={bloco} -> {a}/{n}')
    return resultados

print('Hook de otimizacao pronto. Coletar hard cases num conjunto separado e re-treinar.')
""")

md("""## 8. Exportar para o APK

O modelo final (`placacheck.tflite` ou `placacheck.traineddata`) vai nos `assets` do APK.
Tudo roda no aparelho: sem nuvem, sem dado saindo da fabrica. Os parametros calibrados
(ROI, LIMIAR_CONFIANCA, CLAHE) viram constantes no app (ver `Verificador.kt` e `ReconhecedorMLKit.kt`).
""")

md("""## Referencias

Levantadas por busca (out/2026). Marcadas [verificar] as de IEEE/Elsevier/Springer, cujo texto completo
deve ser confirmado via Crawl4AI no IEEE antes de citar em proposta oficial (regra: so arXiv/ACM/MDPI
sao confiaveis sem leitura do full text).

**Metal gravado e baixo contraste**
- RFANET-OCR: framework de atencao ponta a ponta para deteccao e reconhecimento de texto em superficies metalicas (2025). Zenodo 15316312. [verificar]
- Recognition of characters on curved metal workpiece surfaces based on multi-exposure image fusion and deep neural networks. Neurocomputing, ScienceDirect S0925231222011080. [verificar]
- Image Enhancement Combined with LLM Collaboration for Low-Contrast Image Character Recognition (aco fundido, 2025). ScienceDirect S1546221825009348. [verificar]
- Deep Learning-Based OCR for Identifying On-Label Printed Part Numbers of Used Automotive Parts (2024/2025). Springer 10.1007/978-3-031-77429-4_58. [verificar]

**Surveys e industrial**
- From Detection to Understanding: A Systematic Survey of Deep Learning for Scene Text Processing. MDPI Applied Sciences 15(17):9247 (2025).
- The Industrial Application of AI-Based OCR in Modern Manufacturing. MDPI Sustainability 16(5):2161. doi 10.3390/su16052161.

**Confianca, rejeicao e calibracao (o nucleo do co-piloto)**
- Revisiting Confidence Estimation: Towards Reliable Failure Prediction. arXiv:2403.02886.
- Abstaining from VLM-Generated OCR Errors via Latent Representation Probes. arXiv:2511.19806.
- Entropy Heat-Mapping: Localizing GPT-Based OCR Errors. arXiv:2505.00746.
- Perception, Layout, and Validation: Calibrated Confidence for Reliable Straight-Through Processing. arXiv:2609.20110.
- Teaching VLMs to Admit Uncertainty in OCR (Guan et al., ICLR 2026). [verificar]

**Arquitetura base**
- CRNN (CNN + Bi-LSTM + CTC) para reconhecimento de sequencia: padrao da industria para OCR de linha unica.
""")

# Monta o notebook
nb = {
  "cells": [],
  "metadata": {
    "kernelspec": {"display_name": "Python 3", "language": "python", "name": "python3"},
    "language_info": {"name": "python", "version": "3.12"}
  },
  "nbformat": 4, "nbformat_minor": 5
}
for typ, src in cells:
    c = {"cell_type": typ, "metadata": {}, "source": src.splitlines(keepends=True),
         "id": uuid.uuid4().hex[:8]}
    if typ == "code":
        c["outputs"] = []; c["execution_count"] = None
    nb["cells"].append(c)

out = pathlib.Path(__file__).parent / "pipeline-calibracao-treino.ipynb"
out.write_text(json.dumps(nb, ensure_ascii=False, indent=1), encoding="utf-8")
print("ok ->", out, "|", len(nb["cells"]), "celulas")
