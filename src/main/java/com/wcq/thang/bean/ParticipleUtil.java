package com.wcq.thang.bean;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author wcq
 * @date 2019/12/11 19:49
 */
public class ParticipleUtil {
    /**
     * 结巴分词
     * @param content
     * @return
     */
    public String jieBa(String content){
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
        WordDictionary.getInstance().init(Paths.get("conf"));
        List<String> strings = jiebaSegmenter.sentenceProcess(content);
        StringBuilder data = new StringBuilder();
        // 简单处理示例
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).equals("，") ||
                    strings.get(i).equals("。") ||
                    strings.get(i).equals("、") ||
                    strings.get(i).equals("：") ||
                    strings.get(i).equals("？")) {
                continue;
            }
            data.append(strings.get(i)).append("/");
        }
        //Search模式，用于对用户查询词分词
        //Index模式，用于对索引文档分词
        //List<SegToken> process = jiebaSegmenter.process(content, JiebaSegmenter.SegMode.SEARCH);
        //String data = process.toString();
        return data.toString();
    }
    /**
     * 标准分词
     * @param content
     * @return
     */
    public String standard(String content){
        List<Term> segment = StandardTokenizer.segment(content);
        return segment.toString();
    }

    /**
     * NLP分词
     * @param content
     * @return
     */
    public String NLP(String content){
        String res=null;
        res = NLPTokenizer.segment(content).toString()+"\n";
        res += NLPTokenizer.analyze(content).translateLabels().toString();
        res +=NLPTokenizer.analyze(content).toString();
        return res;
    }

    /**
     * 索引分词
     * @param content
     * @return
     */
    public String indexPar(String content){
        String res = "";
        List<Term> termList = IndexTokenizer.segment(content);
        for (Term term : termList)
        {
            res += term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]"+"\n";
        }
        return res;
    }

    /**
     * crf分词
     * @param content
     * @return
     */
    public String  crf(String content){
        CRFLexicalAnalyzer analyzer = null;
        try {
            analyzer = new CRFLexicalAnalyzer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return analyzer.analyze(content).toString();
    }

    /**
     * N最短路径分词
     * @param content
     * @return
     */
    public String NShort(String content){
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
         return nShortSegment.seg(content)+"\n"+shortestSegment.seg(content);
    }

    /**
     * 关键词提取
     * @param content
     * @param size
     * @return
     */
    public String extractionKeywords (String content,int size){
        List<String> keywordList = HanLP.extractKeyword(content, size);
        return keywordList.toString();
    }

    /**
     * 自动摘要
     * @param content
     * @param size
     * @return
     */
    public String automaticSummarization(String content,int size){
        List<String> sentenceList = HanLP.extractSummary(content,size);
        return sentenceList.toString();
    }

    /**
     * 短语提取
     * @param content
     * @param size
     * @return
     */
    public String extractPhrase(String content,int size){
        List<String> phraseList = HanLP.extractPhrase(content, size);
        return phraseList.toString();
    }

    /**
     * 依存句法分析
     * @param content
     * @return
     */
    public String analysis(String content){
        String res = null;
        CoNLLSentence sentence = HanLP.parseDependency(content);
        res = sentence.toString()+"\n";
        // 可以方便地遍历它
        for (CoNLLWord word : sentence)
        {
            res += String.format("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
        }
        // 也可以直接拿到数组，任意顺序或逆序遍历
//        CoNLLWord[] wordArray = sentence.getWordArray();
//        for (int i = wordArray.length - 1; i >= 0; i--)
//        {
//            CoNLLWord word = wordArray[i];
//            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
//        }
        // 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
        CoNLLWord[] wordArray = sentence.getWordArray();
        CoNLLWord head = wordArray[12];
        while ((head = head.HEAD) != null)
        {
            if (head == CoNLLWord.ROOT) System.out.println(head.LEMMA);
            else res+=String.format("%s --(%s)--> ", head.LEMMA, head.DEPREL);
        }
        return res;
    }

    /**
     * 繁体转为简体
     * @param content
     * @return
     */
    public static String ftoJ(String content){
        return HanLP.convertToSimplifiedChinese(content);
    }
}
