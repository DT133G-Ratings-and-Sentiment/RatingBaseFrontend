/*package com.dt002g.reviewapplication.frontend.sentimentanalyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SentimentAnalyser {

	/*
	 * "Very negative" = 0 "Negative" = 1 "Neutral" = 2 "Positive" = 3
	 * "Very positive" = 4



	static Properties props;
	static StanfordCoreNLP pipeline;

	public void initialize() {
		 // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and sentiment
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit , pos, lemma, ner, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);

	}
	
	public List<String> getAdjectives(String sentence){
		List<String> adjectives = new ArrayList();
		CoreDocument document = pipeline.processToCoreDocument(sentence);
		for(CoreLabel token : document.tokens()) {
			if(token.tag().equals("JJ")) {
				adjectives.add(token.word());
			}
		}
		return adjectives;
	}

	public List<SentenceScore> getSentimentResult(String text) {		
		List<SentenceScore> sentenceScoreList = new ArrayList();
		if (text != null && text.length() > 0) {

			Annotation annotation = pipeline.process(text);

			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				SentimentScore sentimentScore = new SentimentScore();
				Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
				SimpleMatrix sm = RNNCoreAnnotations.getPredictions(tree);
				String sentimentType = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

				sentimentScore.setVeryPositive((double)Math.round(sm.get(4) * 100d));
				sentimentScore.setPositive((double)Math.round(sm.get(3) * 100d));
				sentimentScore.setNeutral((double)Math.round(sm.get(2) * 100d));
				sentimentScore.setNegative((double)Math.round(sm.get(1) * 100d));
				sentimentScore.setVeryNegative((double)Math.round(sm.get(0) * 100d));
				
				if(RNNCoreAnnotations.getPredictedClass(tree) == 0){
					sentimentScore.setSentimentType("Very Negative");
				}
				else if(RNNCoreAnnotations.getPredictedClass(tree) == 1){
					sentimentScore.setSentimentType("Negative");
				}
				else if(RNNCoreAnnotations.getPredictedClass(tree) == 2){
					sentimentScore.setSentimentType("Neutral");
				}
				else if(RNNCoreAnnotations.getPredictedClass(tree) == 3){
					sentimentScore.setSentimentType("Positive");
				}
				else if(RNNCoreAnnotations.getPredictedClass(tree) == 4){
					sentimentScore.setSentimentType("Very Positive");
				}
				String sentenceStr = sentence.get(CoreAnnotations.TextAnnotation.class);
				SentenceScore sentenceScore = new SentenceScore(sentenceStr, sentimentScore);
				sentenceScoreList.add(sentenceScore);
			}

		}
		return sentenceScoreList;
	
	}
}
*/