## Your privacy, your choice
We use essential cookies to make sure the site can function. We also use optional cookies for advertising, personalisation of content, usage analysis, and social media, as well as to allow video information to be shared for both marketing, analytics and editorial purposes.
By accepting optional cookies, you consent to the processing of your personal data - including transfers to third parties. Some third parties are outside of the European Economic Area, with varying standards of data protection.
See our [privacy policy](https://link.springer.com/privacystatement) for more information on the use of your personal data.
Manage preferences for further information and to change your choices.
Accept all cookies Reject optional cookies
[Skip to main content](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#main-content)
## Search
Search by keyword or author
Search
## Navigation
  * [ Find a journal ](https://link.springer.com/journals/)
  * [ Publish with us ](https://www.springernature.com/gp/authors)
  * [ Track your research ](https://link.springernature.com/home/)


# Deep Learning-Based Optical Character Recognition for Identifying On-Label Printed Part Numbers of Used Automotive Parts: A Comparative Study of Open Source and Commercial Methods
  * Conference paper
  * Open Access
  * First Online: 07 January 2025


  * pp 527–534
  * [Cite this conference paper](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#citeas)


You have full access to this [open access](https://www.springernature.com/gp/open-science/about/the-fundamentals-of-open-access-and-open-research) conference paper 
[ Download book PDF ](https://link.springer.com/content/pdf/10.1007/978-3-031-77429-4.pdf)
[ Download book EPUB ](https://link.springer.com/download/epub/10.1007/978-3-031-77429-4.epub)
[ Save conference paper ](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/save-research?_csrf=BzpggxHYGxXtABjFDZJGpFbX6quLi_c5)
[ View saved research ](https://link.springer.com/saved-research)
[ ![](https://media.springernature.com/w72/springer-static/cover/book/978-3-031-77429-4.jpg?as=webp) Sustainable Manufacturing as a Driver for Growth ](https://link.springer.com/book/10.1007/978-3-031-77429-4) (GCSM 2023) 
Deep Learning-Based Optical Character Recognition for Identifying On-Label Printed Part Numbers of Used Automotive Parts: A Comparative Study of Open Source and Commercial Methods 
[ Download book PDF ](https://link.springer.com/content/pdf/10.1007/978-3-031-77429-4.pdf)
[ Download book EPUB ](https://link.springer.com/download/epub/10.1007/978-3-031-77429-4.epub)
## Abstract
This paper explores the use of deep learning-based optical character recognition (OCR) to identify part numbers for used automotive parts. It compares open source and advanced AI methods to commercial tools from Google, Amazon, and Microsoft. The study finds that fine-tuned open source models outperform commercial services, especially for complex part numbers unrelated to any language structure. The preferred open source method, MaskedTextSpotter, is fine-tuned with image data from old vehicle and electrical parts, captured by a smartphone and 2D barcode scanner. Additionally, a new data augmentation method, CharChan, is introduced, replacing detected characters with random examples for better character recognition. The experiments demonstrate the efficacy of deep learning-based OCR for automotive part number identification.
You have full access to this open access chapter, [Download conference paper PDF](https://link.springer.com/content/pdf/10.1007/978-3-031-77429-4_58.pdf?pdf=inline%20link)
### Similar content being viewed by others
![](https://media.springernature.com/w92h120/springer-static/cover-hires/book/978-981-16-5655-2?as=webp)
###  [License Plate Recognition for Stolen Vehicles Using Optical Character Recognition ](https://link.springer.com/chapter/10.1007/978-981-16-5655-2_56?fromPaywallRec=false)
Chapter © 2022
![](https://media.springernature.com/w92h120/springer-static/cover-hires/book/978-3-030-59830-3?as=webp)
###  [License Plate Detection and Recognition by Convolutional Neural Networks ](https://link.springer.com/chapter/10.1007/978-3-030-59830-3_33?fromPaywallRec=false)
Chapter © 2020
![](https://media.springernature.com/w92h120/springer-static/cover-hires/book/978-981-19-0604-6?as=webp)
###  [Wild OCR: Deep Learning Architecture for Text Recognition in Images ](https://link.springer.com/chapter/10.1007/978-981-19-0604-6_46?fromPaywallRec=false)
Chapter © 2022
### Explore related subjects
Discover the latest articles, books and news in related subjects, suggested using machine learning.
  * [Object Recognition](https://link.springer.com/subjects/object-recognition)
  * [Reverse engineering](https://link.springer.com/subjects/reverse-engineering)
  * [Automated Pattern Recognition](https://link.springer.com/subjects/automated-pattern-recognition)
  * [Automotive Engineering](https://link.springer.com/subjects/automotive-engineering)
  * [Computer Vision](https://link.springer.com/subjects/computer-vision)
  * [Biometrics](https://link.springer.com/subjects/biometrics)
  * [Document Image Understanding and Processing Techniques](https://link.springer.com/subjects/document-image-understanding-and-processing-techniques)


##  1 Introduction
At the end of a product’s service life, there are various ways to reuse, recycle or dispose of the product [[1](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR1 "Mast J, von Unruh F, Irrek W \(2022\) R-Strategien als Leitlinien der Circular Economy, RETHINK. Impulse zur zirkulären Wertschöpfung - Enabling the Circular Economy, Bd. 2022 03, 25. Mai 2022. 
                  https://prosperkolleg.ruhr/wp-content/uploads/2022/05/rethink22-03r-strategien.pdf
                  
                ")]. For reuse, repair or remanufacturing, the identification of a product during the reverse logistics handling is crucial. Recently, AI-enhanced identification systems [[2](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR2 "Schlüter M, Niebuhr C, Lehr J, Krüger J \(2018\) Vision-based identification service for remanufacturing sorting. Procedia Manuf 21:384–391. 
                  https://doi.org/10.1016/j.promfg.2018.02.135
                  
                "), [3](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR3 "Schlüter M et al \(2021\) AI-enhanced identification, inspection and sorting for reverse logistics in remanufacturing. Procedia CIRP 98:300–305. 
                  https://doi.org/10.1016/j.procir.2021.01.107
                  
                ")] addressed the challenges to achieve a high recognition rate of part numbers despite the condition of nameplates, printed or embossed serial numbers and barcodes, which can deteriorate to the point of illegibility due to dirt and wear [[2](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR2 "Schlüter M, Niebuhr C, Lehr J, Krüger J \(2018\) Vision-based identification service for remanufacturing sorting. Procedia Manuf 21:384–391. 
                  https://doi.org/10.1016/j.promfg.2018.02.135
                  
                ")]. Nevertheless, the majority of products in circular economy still have - at least partly - legible part numbers which are typically read out and entered into a logistics system manually with keyboard inputs. Workers often have to wear gloves because - especially in the automotive aftermarket - old parts are very dirty and oily. The accurate identification of part numbers on dirty car parts is hindered by obscured characters and smudging caused by accumulated dirt and oil, presenting significant challenges for OCR systems striving to interpret partially illegible text accurately. An elegant solution for supporting the worker in the identification process would be to automatically read out and enter part numbers, captured by imaging systems such as handhelds, barcode scanners or stationary cameras. This paper analyzes state-of-the-art methods of AI-based text recognition methods for reading out part numbers plates of used parts. The objectives include the analysis and evaluation of different text detection and text recognition models. A comparison between commercially available and pre-trained as well as fine-tuned open source text recognition models will be performed finally.
##  2 Literature Review
**Optical Character Recognition (OCR)** converts images into analyzable, editable, and searchable data [[4](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR4 "Memon J, Sami M, Khan RA, Uddin M \(2020\) Handwritten optical character recognition \(OCR\): a comprehensive systematic literature review \(SLR\). IEEE Access 8:142642–142668. 
                  https://doi.org/10.1109/ACCESS.2020.3012542
                  
                ")]. Scene Text Recognition (STR) is a challenging aspect of OCR, given the diverse environmental factors in image capture. OCR involves two steps: text or character detection, where the region of Interest (RoI) containing the text is extracted, and text recognition in the extracted region using a specific method. Open source models for experiments were chosen based on their performance in competitions like ICDAR2019 Robust Reading Challenge on Arbitrary-Shaped Text (ArT) and Out of Vocabulary Scene Text Understanding.
**Cloud-based text recognition systems** The companies Google, Amazon and Microsoft each offer their own commercial cloud-based computer vision service. These three services can recognize objects, faces or text in images. The services of all three manufacturers offer pre-trained text recognition services that output the characters found for an image entered. All services incur costs that depend on the number of images to be recognized. In the context of the paper, the models are referred to as Google, Amazon, and Azure.
**Text detection** Deep Learning algorithms have made significant progress in text detection, particularly for scanned documents. However, detecting text in real environments with varying shapes, colors, and complexities remains a challenge. Text detection methods can be divided into word-based and character-based approaches. Among these methods, TextFuseNet (TFN) performed exceptionally well in the ICDAR2019 ArT competition. In this paper, TextFuseNet [[5](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR5 "Ye J, Chen Z, Liu J, Du B \(2020\) TextFuseNet: scene text detection with richer fused features. In: Proceedings of the twenty-ninth international joint conference on artificial intelligence, IJCAI-20, pp 516–522")] is used as an end-to-end model. Another effective text detector considered is the Pixel Aggregation Network (PAN) [[6](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR6 "Wang W, Xie E, Song X, Zang Y, Wang W, Lu T, Yu G, Shen C, Efficient and accurate arbitrary—shaped text detection with pixel aggregation network. 
                  https://doi.org/10.48550/ARXIV.1908.05900
                  
                ")], which showed promising results on the SCUT-CTW1500 and ICDAR2015 datasets.
**Text recognition** comes after text detection and individual symbols are recognized and arranged correctly. Baek’s design pattern [[7](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR7 "Baek J, Kim G, Lee J, Park S, Han D, Yun S, Oh SJ, Lee H, What is wrong with scene text recognition model comparisons? Dataset and model analysis. 
                  https://doi.org/10.48550/ARXIV.1904.01906
                  
                ")] represents modern STR models. Additionally, there are Transformer-encoder-decoder-based text recognition models like ViT-STR [[8](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR8 "Atienza R \(2021\) Vision transformer for fast and efficient scene text recognition. 
                  https://doi.org/10.48550/ARXIV.2105.08582
                  
                ")]. For text recognition in text boxes, the TRBA model (TPS-ResNet-BiLSTMAttn) [[7](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR7 "Baek J, Kim G, Lee J, Park S, Han D, Yun S, Oh SJ, Lee H, What is wrong with scene text recognition model comparisons? Dataset and model analysis. 
                  https://doi.org/10.48550/ARXIV.1904.01906
                  
                ")], which achieved third place in the ICDAR2019 ArT competition, was selected. The Vision Transformer for Image Recognition (ViT-STR) model, developed by Rotenzia [[8](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR8 "Atienza R \(2021\) Vision transformer for fast and efficient scene text recognition. 
                  https://doi.org/10.48550/ARXIV.2105.08582
                  
                ")], is another text recognition approach based on the decoder-encoder model.
**End-to-End** models integrate text detection and recognition into one pipeline, training them simultaneously for improved results. TextFuseNet (TFN) [[5](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR5 "Ye J, Chen Z, Liu J, Du B \(2020\) TextFuseNet: scene text detection with richer fused features. In: Proceedings of the twenty-ninth international joint conference on artificial intelligence, IJCAI-20, pp 516–522")] is an example of such, and combines character-based and word-based text detection approaches. Another end-to-end model is MaskTextSpotter (MTS) [[9](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR9 "Liao M, Lyu P, He M, Yao C, Wu W, Bai X \(2021\) Mask TextSpotter: an end-to-end trainable neural network for spotting text with arbitrary shapes. In: IEEE transactions on pattern analysis and machine intelligence, vol 43, Nr. 2, pp 532–548. 
                  https://doi.org/10.1109/TPAMI.2019.2937086
                  
                ")], which excels in recognizing text with arbitrary shapes and was best at ICDAR2019 ArT competition. DEER [[10](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR10 "Kim S, Shin S, Kim Y, Cho H, Kil T, Surh J, Park S, Lee B, Baek Y, DEER: detection-agnostic end-to-end recognizer for scene text spotting. 
                  https://doi.org/10.48550/ARXIV.2203.05122
                  
                ")] achieved the best results in the OOV Competition [[11](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR11 "Bitenand S, Bordilsand A, Delgadoand A \(2022\) ECCV 2022 Out of Vocabulary Scene
Text Understanding. 
                  https://rrc.cvc.uab.es/?ch=19,2022
                  
                ")], offering reduced dependency on detection and recognition modules by using a single reference point for each text instance instead of region-based recognition.
##  3 Materials and Methods
###  3.1 Dataset Description
The dataset consists of images of old parts from the automotive industry and old printer cartridges. In each case, a type plate of a component is present in the images. Part of the images were taken with a ZebraScanner DS 2200 barcode scanner and the other part with a standard smartphone. The images have different resolutions from 640 × 480 pixels up to 4032 × 3024 pixels. The main dataset consists of a total of 441 images, with 75 images representing old printer cartridges. Of the 441 images, 230 were captured in color with a smartphone and 211 in grayscale with the ZebraScanner.
**Fig. 1.**
[![figure 1](https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-3-031-77429-4_58/MediaObjects/559591_1_En_58_Fig1_HTML.jpg)](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/1)
Typical core label information
[Full size image](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/1)
The distribution of characters is even between test and training dataset. However, the letters “q” and “z” are rarely represented in the dataset, so only small improvements in the recognition of these letters can be expected. Figure [1](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig1) shows a typical label of an automotive core. It contains information about the manufacturer, country of manufacture, date, voltage, power and current values and other designations. In Fig. [1](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig1) the manufacturer is marked in yellow, text parts are marked in red and power/voltage values are marked in blue. The serial numbers highlighted in green are the most important information in this context and should be recognized by the procedures. These serial numbers belong to three different manufacturers: BOSCH, Mercedes and DENSO. It is a starter motor that is installed in different types of vehicles (Fig. [2](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig2)).
**Fig. 2.**
[![figure 2](https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-3-031-77429-4_58/MediaObjects/559591_1_En_58_Fig2_HTML.jpg)](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/2)
Top: STR Augmentation, Bottom: CharChan Augmentation
[Full size image](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/2)
**Data augmentations** : Augmentation techniques involve applying various transformations to the existing data, such as rotation, scaling, flipping, and adjusting brightness or contrast. These transformations create new variations of the original data, enabling the model to generalize better when faced with diverse and challenging real-world scenarios, including dirty and oily surfaces. To extend the main data set and generalize the training data, two augmentation procedures were applied to the main data set.
**STR Augmentation** : Rowel Atienza developed the STR augmentation model, which contains 36 different image augmentation functions to emulate real-world perturbations [[12](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR12 "Atienza R \(2021\) Data augmentation for scene text recognition. In: CoRR abs/2108.06949. 
                  https://arxiv.org/abs/2108.06949
                  
                ")]. The application of STR augmentation has significantly improved the accuracy of some models. For example, the accuracy of the Rosetta model was increased by 2.10%, the R2AM model by 1.48%, and the TRBA model by 1.06% [[12](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR12 "Atienza R \(2021\) Data augmentation for scene text recognition. In: CoRR abs/2108.06949. 
                  https://arxiv.org/abs/2108.06949
                  
                ")]. Atienza’s augmentation was developed specifically for text boxes that have already been cut out, so not all the framework’s features are applied to the present dataset. Augmentations that change the shape of the text boxes are therefore not considered. Of the total 36 available functions of the model, 28 are applied, resulting in an augmented dataset of 1174 images. We introduce **CharChan augmentation** to exchange characters within the text boxes of the ground truth. The objective is to hinder the model interpreting semantic information within the characters and to achieve better generalization. The characters on the images are cut out using the TFN model [[5](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#ref-CR5 "Ye J, Chen Z, Liu J, Du B \(2020\) TextFuseNet: scene text detection with richer fused features. In: Proceedings of the twenty-ninth international joint conference on artificial intelligence, IJCAI-20, pp 516–522")], which allows both word-based detection and character-based detection. Within the ground truth text boxes, the letters are rearranged, with the number of letters to be distributed being dictated by the original text box. The height of the new letters is adjusted to the height of the text box to avoid overlapping with other text boxes. Then the color or grayscale values of the individual character images are adjusted. This augmentation created additional 1126 images. Character recognition is particularly challenging for the model on these images.
###  3.2 Model Selection for Focused Analysis in Text Recognition
To focus the remainder of the paper and not include all models, they were evaluated using common evaluation metrics, as shown in Fig. [3](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig3). Based on this evaluation, certain models were selected that showed promise and performance. This targeted selection of promising models allows the paper to dive deeper into the study of these specific models and gain detailed insights into their strengths and weaknesses. This facilitates a more precise and focused analysis of text recognition and allows relevant aspects to be examined in more detail. The models were evaluated using the main dataset according to different metrics as shown in Fig. [3](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig3). The end-to-end models achieved significantly better results in this evaluation. For this reason, stand-alone detection or recognition models are not pursued further in this paper. Due to the long evaluation time, the DEER model is also not considered further. Of the end-to-end models, the MTS model has the highest recall value and is therefore considered further. Of the cloud-based services, the Google and Azure services show the best values.
**Fig. 3.**
[![figure 3](https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-3-031-77429-4_58/MediaObjects/559591_1_En_58_Fig3_HTML.png)](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/3)
Evaluation of models on main data set
[Full size image](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/3)
###  3.3 Fine-Tuning the MTS Model
To enhance results on the main dataset, the MTS model undergoes fine-tuning using a transfer learning approach. Pre-trained on datasets like SynthText, ICDAR2013, ICDAR2015, Total-Text, and SCUT-CTW, the MTS model leverages its prior knowledge of various characters and text recognition. The training is conducted in two variants. In the first variant, the model is trained in three steps: STR augmentation dataset, CharChan dataset, and finally, the main dataset. Each step undergoes 25,000 iterations. The second variant also consists of three steps, with 30,000 iterations each. It starts with training solely on the main dataset, followed by gradual inclusion of STR augmentation and CharChan datasets.
###  3.4 Evaluation Metrics
The evaluation primarily emphasizes the recall metric when assessing predicted text boxes. This emphasis stems from the crucial need to recognize all pertinent text boxes accurately. In this context, the precision metric holds less significance because identifying more than the necessary relevant boxes is not as critical. Any inaccurately identified text boxes can be rectified later by filtering them out from the ground truth dataset. The accuracy of the recognized text is assessed using the accuracy value. For this, the Character Error Rate (CER) is calculated. And the accuracy (Acc) value is calculated as follows: \\(Acc=1- \frac{CER}{textlength}\\)
##  4 Results
In this chapter, we conduct an evaluation of the models using the datasets presented in the previous chapter. We begin by evaluating the fine-tuned MTS model and analyzing its performance. Next, we compare the MTS model’s results with those of the Google Vision Cloud model.
**Fig. 4.**
[![figure 4](https://media.springernature.com/lw685/springer-static/image/chp%3A10.1007%2F978-3-031-77429-4_58/MediaObjects/559591_1_En_58_Fig4_HTML.png)](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/4)
Pretrained MTS (left) fine-tuned MTS (right) versus Google, Azure
[Full size image](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58/figures/4)
**Pre-trained model** : The MTS and Google models exhibit varying performance in text recognition across different datasets, as illustrated in Fig. [4](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig4). The abbreviations used are STR for the STR augmentation dataset, ZT for the CharChan dataset, and STD for the main dataset. The Google model demonstrates notably superior results in both text detection and recognition compared to the pre-trained MTS model. This improvement can possibly be attributed to the Google model’s enhanced generalization capabilities. It is likely that the Google model was trained on a larger and more diverse dataset, enabling it to recognize various text patterns and styles present in the input images more effectively. On the other hand, the MTS model may be less robust when encountering slightly different images, since it might not have encountered sufficient variation in the training data to handle such changes optimally. Fine-tuned model: Following the 3rd stage of training, the model achieves outstanding Accuracy, Recall, and Precision values on the main dataset, all surpassing 90%. A comparison between the Google, Azure and the MTS model after training (refer to Fig. [4](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58#Fig4)) reveals that the MTS model outperforms both cloudbased models across all evaluation metrics. Specifically, the MTS model detects fewer text boxes that are not present in the ground truth and successfully identifies nearly all ground truth text boxes. Moreover, the detected text demonstrates a very low error rate, with an accuracy of 94%. Comparison of the recognition rates for different classes indicates minimal differences between both models. The training in the 2nd variant did not yield any improvement in the results, and therefore, it is not further explored in this paper.
##  5 Discussion
There is a concern regarding the MTS model’s results after the second stage of the first variant’s training. In this stage, few test dataset images from the main dataset were mistakenly included in the training dataset of augmented images. This situation is known as “data leakage”, where the model benefits from test dataset information during training, potentially affecting its performance when encountering new, unseen test images. To make sure that the method did not overfit on the accidentally leaked data, these were sorted out in a following test. However, the values in Precision, Recall and Accuracy did not change significantly. Hence, data leakage was not found to be severe.
##  6 Conclusion
This paper analyzed and compared various text detection and recognition models using datasets of old parts from the automotive industry and old printer cartridges. End-to-end learned models outperformed stand-alone text detection models, showing at least a 15% improvement in recall values. Without fine-tuning, cloud-based models from Azure and Google achieved the best results in text detection with around 95% recall values, and in text recognition on the main dataset. In contrast, with fine-tuning for part number recognition, open source models outperformed commercial solutions, achieving more accurate and reliable text box contents with a 4 points lower character error rate (CER). While the DEER model showed exceptional performance potential, its high evaluation time made it unsuitable for time-sensitive reverse logistics operations. Such models can significantly support workers in part identification processes. By leveraging the text recognition’s ability to accurately identify and categorize parts, workers can streamline their tasks, leading to increased efficiency and productivity. The CharChan Augmentation proved to be beneficial in dismissing the contextual information of characters for the models. Instead of attempting to identify complete words in the character sequence, fine-tuning with CharChan augmentation method enables pretrained models to improve text recognition. This approach proves advantageous in applications where contextual information is not crucial for accurate text detection. In the future, better AI models for text recognition will be released, mainly trained for natural language recognition. By applying techniques like CharChan to those, they will likely perform better for part number recognition on-label or any other surface.
## References
  1. Mast J, von Unruh F, Irrek W (2022) R-Strategien als Leitlinien der Circular Economy, RETHINK. Impulse zur zirkulären Wertschöpfung - Enabling the Circular Economy, Bd. 2022 03, 25. Mai 2022. <https://prosperkolleg.ruhr/wp-content/uploads/2022/05/rethink22-03r-strategien.pdf>
  2. Schlüter M, Niebuhr C, Lehr J, Krüger J (2018) Vision-based identification service for remanufacturing sorting. Procedia Manuf 21:384–391. <https://doi.org/10.1016/j.promfg.2018.02.135>
  3. Schlüter M et al (2021) AI-enhanced identification, inspection and sorting for reverse logistics in remanufacturing. Procedia CIRP 98:300–305. <https://doi.org/10.1016/j.procir.2021.01.107>
[Article](https://doi.org/10.1016%2Fj.procir.2021.01.107) [ Google Scholar](https://scholar.google.com/scholar_lookup?&title=AI-enhanced%20identification%2C%20inspection%20and%20sorting%20for%20reverse%20logistics%20in%20remanufacturing&journal=Procedia%20CIRP&doi=10.1016%2Fj.procir.2021.01.107&volume=98&pages=300-305&publication_year=2021&author=Schl%C3%BCter%2CM)
  4. Memon J, Sami M, Khan RA, Uddin M (2020) Handwritten optical character recognition (OCR): a comprehensive systematic literature review (SLR). IEEE Access 8:142642–142668. <https://doi.org/10.1109/ACCESS.2020.3012542>
[Article](https://doi.org/10.1109%2FACCESS.2020.3012542) [ Google Scholar](https://scholar.google.com/scholar_lookup?&title=Handwritten%20optical%20character%20recognition%20%28OCR%29%3A%20a%20comprehensive%20systematic%20literature%20review%20%28SLR%29&journal=IEEE%20Access&doi=10.1109%2FACCESS.2020.3012542&volume=8&pages=142642-142668&publication_year=2020&author=Memon%2CJ&author=Sami%2CM&author=Khan%2CRA&author=Uddin%2CM)
  5. Ye J, Chen Z, Liu J, Du B (2020) TextFuseNet: scene text detection with richer fused features. In: Proceedings of the twenty-ninth international joint conference on artificial intelligence, IJCAI-20, pp 516–522
[ Google Scholar](https://scholar.google.com/scholar?&q=Ye%20J%2C%20Chen%20Z%2C%20Liu%20J%2C%20Du%20B%20%282020%29%20TextFuseNet%3A%20scene%20text%20detection%20with%20richer%20fused%20features.%20In%3A%20Proceedings%20of%20the%20twenty-ninth%20international%20joint%20conference%20on%20artificial%20intelligence%2C%20IJCAI-20%2C%20pp%20516%E2%80%93522)
  6. Wang W, Xie E, Song X, Zang Y, Wang W, Lu T, Yu G, Shen C, Efficient and accurate arbitrary—shaped text detection with pixel aggregation network. <https://doi.org/10.48550/ARXIV.1908.05900>
  7. Baek J, Kim G, Lee J, Park S, Han D, Yun S, Oh SJ, Lee H, What is wrong with scene text recognition model comparisons? Dataset and model analysis. <https://doi.org/10.48550/ARXIV.1904.01906>
  8. Atienza R (2021) Vision transformer for fast and efficient scene text recognition. <https://doi.org/10.48550/ARXIV.2105.08582>
  9. Liao M, Lyu P, He M, Yao C, Wu W, Bai X (2021) Mask TextSpotter: an end-to-end trainable neural network for spotting text with arbitrary shapes. In: IEEE transactions on pattern analysis and machine intelligence, vol 43, Nr. 2, pp 532–548. <https://doi.org/10.1109/TPAMI.2019.2937086>
  10. Kim S, Shin S, Kim Y, Cho H, Kil T, Surh J, Park S, Lee B, Baek Y, DEER: detection-agnostic end-to-end recognizer for scene text spotting. <https://doi.org/10.48550/ARXIV.2203.05122>
  11. Bitenand S, Bordilsand A, Delgadoand A (2022) ECCV 2022 Out of Vocabulary Scene Text Understanding. <https://rrc.cvc.uab.es/?ch=19,2022>
  12. Atienza R (2021) Data augmentation for scene text recognition. In: CoRR abs/2108.06949. <https://arxiv.org/abs/2108.06949>


[Download references](https://citation-needed.springer.com/v2/references/10.1007/978-3-031-77429-4_58?format=refman&flavour=references)
## Acknowledgements
This paper was written as part of the research and development project “Biological Transformation 4.0: Further development of Industry 4.0 by integrating biological principles (BioFusion 4.0)”, which is funded by the German Federal Ministry of Education and Research (BMBF) and supervised by the Project Management Agency Karlsruhe (PTKA), as well as part of the EIBA project 033R226 in the ReziProK programm, which was funded over the FONA platform for sustainable research.
## Author information
### Authors and Affiliations
  1. Fraunhofer Institute for Production Systems and Design Technology IPK, Berlin, Germany
Marian Schlüter, Christian Tepper, Clemens Briese, Ole Kroeger, Raul Vicente-Garcia & Jörg Krüger


Authors
  1. Marian Schlüter
[View author publications](https://link.springer.com/search?sortBy=newestFirst&contributor=Marian%20Schl%C3%BCter)
Search author on:[PubMed](http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&term=Marian%20Schl%C3%BCter)[Google Scholar](http://scholar.google.co.uk/scholar?as_q=&num=10&btnG=Search+Scholar&as_epq=&as_oq=&as_eq=&as_occt=any&as_sauthors=%22Marian%20Schl%C3%BCter%22&as_publication=&as_ylo=&as_yhi=&as_allsubj=all&hl=en)
  2. Christian Tepper
[View author publications](https://link.springer.com/search?sortBy=newestFirst&contributor=Christian%20Tepper)
Search author on:[PubMed](http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&term=Christian%20Tepper)[Google Scholar](http://scholar.google.co.uk/scholar?as_q=&num=10&btnG=Search+Scholar&as_epq=&as_oq=&as_eq=&as_occt=any&as_sauthors=%22Christian%20Tepper%22&as_publication=&as_ylo=&as_yhi=&as_allsubj=all&hl=en)
  3. Clemens Briese
[View author publications](https://link.springer.com/search?sortBy=newestFirst&contributor=Clemens%20Briese)
Search author on:[PubMed](http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&term=Clemens%20Briese)[Google Scholar](http://scholar.google.co.uk/scholar?as_q=&num=10&btnG=Search+Scholar&as_epq=&as_oq=&as_eq=&as_occt=any&as_sauthors=%22Clemens%20Briese%22&as_publication=&as_ylo=&as_yhi=&as_allsubj=all&hl=en)
  4. Ole Kroeger
[View author publications](https://link.springer.com/search?sortBy=newestFirst&contributor=Ole%20Kroeger)
Search author on:[PubMed](http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&term=Ole%20Kroeger)[Google Scholar](http://scholar.google.co.uk/scholar?as_q=&num=10&btnG=Search+Scholar&as_epq=&as_oq=&as_eq=&as_occt=any&as_sauthors=%22Ole%20Kroeger%22&as_publication=&as_ylo=&as_yhi=&as_allsubj=all&hl=en)
  5. Raul Vicente-Garcia
[View author publications](https://link.springer.com/search?sortBy=newestFirst&contributor=Raul%20Vicente-Garcia)
Search author on:[PubMed](http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&term=Raul%20Vicente-Garcia)[Google Scholar](http://scholar.google.co.uk/scholar?as_q=&num=10&btnG=Search+Scholar&as_epq=&as_oq=&as_eq=&as_occt=any&as_sauthors=%22Raul%20Vicente-Garcia%22&as_publication=&as_ylo=&as_yhi=&as_allsubj=all&hl=en)
  6. Jörg Krüger
[View author publications](https://link.springer.com/search?sortBy=newestFirst&contributor=J%C3%B6rg%20Kr%C3%BCger)
Search author on:[PubMed](http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&term=J%C3%B6rg%20Kr%C3%BCger)[Google Scholar](http://scholar.google.co.uk/scholar?as_q=&num=10&btnG=Search+Scholar&as_epq=&as_oq=&as_eq=&as_occt=any&as_sauthors=%22J%C3%B6rg%20Kr%C3%BCger%22&as_publication=&as_ylo=&as_yhi=&as_allsubj=all&hl=en)


### Corresponding author
Correspondence to Marian Schlüter.
## Editor information
### Editors and Affiliations
  1. Technische Universität Berlin, Berlin, Germany
Holger Kohl
  2. Technische Universität Berlin, Berlin, Germany
Günther Seliger
  3. Technische Universität Berlin, Berlin, Germany
Franz Dietrich
  4. Instituto Tecnológico de Buenos Aires, Buenos Aires, Argentina
Sebastián Mur


## Rights and permissions
**Open Access** This chapter is licensed under the terms of the Creative Commons Attribution 4.0 International License (<http://creativecommons.org/licenses/by/4.0/>), which permits use, sharing, adaptation, distribution and reproduction in any medium or format, as long as you give appropriate credit to the original author(s) and the source, provide a link to the Creative Commons license and indicate if changes were made.
The images or other third party material in this chapter are included in the chapter's Creative Commons license, unless indicated otherwise in a credit line to the material. If material is not included in the chapter's Creative Commons license and your intended use is not permitted by statutory regulation or exceeds the permitted use, you will need to obtain permission directly from the copyright holder.
[Reprints and permissions](https://s100.copyright.com/AppDispatchServlet?publisherName=SpringerNature&orderBeanReset=true&orderSource=SpringerLink&title=Deep%20Learning-Based%20Optical%20Character%20Recognition%20for%20Identifying%20On-Label%20Printed%20Part%20Numbers%20of%20Used%20Automotive%20Parts%3A%20A%20Comparative%20Study%20of%20Open%20Source%20and%20Commercial%20Methods&author=Marian%20Schl%C3%BCter%2C%20Christian%20Tepper%2C%20Clemens%20Briese%20et%20al&contentID=10.1007%2F978-3-031-77429-4_58&copyright=The%20Author%28s%29&publication=eBook&publicationDate=2025&startPage=527&endPage=534&imprint=The%20Author%28s%29&oa=CC%20BY)
## Copyright information
© 2025 The Author(s)
## About this paper
[![Check for updates. Verify currency and authenticity via CrossMark](https://link.springer.com/chapter/10.1007/978-3-031-77429-4_58)](https://crossmark.crossref.org/dialog/?doi=10.1007/978-3-031-77429-4_58)
### Cite this paper
Schlüter, M., Tepper, C., Briese, C., Kroeger, O., Vicente-Garcia, R., Krüger, J. (2025). Deep Learning-Based Optical Character Recognition for Identifying On-Label Printed Part Numbers of Used Automotive Parts: A Comparative Study of Open Source and Commercial Methods. In: Kohl, H., Seliger, G., Dietrich, F., Mur, S. (eds) Sustainable Manufacturing as a Driver for Growth. GCSM 2023. Lecture Notes in Mechanical Engineering. Springer, Cham. https://doi.org/10.1007/978-3-031-77429-4_58
### Download citation
  * [.RIS](https://citation-needed.springer.com/v2/references/10.1007/978-3-031-77429-4_58?format=refman&flavour=citation "Download this article's citation as a .RIS file")
  * [.ENW](https://citation-needed.springer.com/v2/references/10.1007/978-3-031-77429-4_58?format=endnote&flavour=citation "Download this article's citation as a .ENW file")
  * [.BIB](https://citation-needed.springer.com/v2/references/10.1007/978-3-031-77429-4_58?format=bibtex&flavour=citation "Download this article's citation as a .BIB file")


  * DOI: https://doi.org/10.1007/978-3-031-77429-4_58
  * Published: 07 January 2025
  * Publisher Name: Springer, Cham
  * Print ISBN: 978-3-031-77428-7
  * Online ISBN: 978-3-031-77429-4
  * eBook Packages: [Mechanical Engineering (R0)](https://link.springer.com/search?facet-content-type=%22Book%22&package=85268&facet-start-year=2025&facet-end-year=2025)[Springer Nature Proceedings excluding Computer Science](https://link.springer.com/search?facet-content-type=%22Book%22&package=85345&facet-start-year=2025&facet-end-year=2025)


### Share this paper
Anyone you share the following link with will be able to read this content:
Get shareable link
Sorry, a shareable link is not currently available for this article.
Copy shareable link to clipboard
Provided by the Springer Nature SharedIt content-sharing initiative 
### Keywords
  * [Optical character recognition](https://link.springer.com/search?query=Optical%20character%20recognition&facet-discipline="Engineering")
  * [Circular economy](https://link.springer.com/search?query=Circular%20economy&facet-discipline="Engineering")
  * [Artificial intelligence](https://link.springer.com/search?query=Artificial%20intelligence&facet-discipline="Engineering")
  * [Reverse logistics](https://link.springer.com/search?query=Reverse%20logistics&facet-discipline="Engineering")


## Publish with us
[Policies and ethics](https://www.springernature.com/gp/policies/book-publishing-policies)
### Profiles
  1. Jörg Krüger [ View author profile ](https://link.springer.com/researchers/59721625SN)


  *[DOI]: Digital Object Identifier
