package com.dt002g.reviewapplication.frontend.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;


import com.dt002g.reviewapplication.frontend.util.SentimentAnalyser;

class Test {

	@org.junit.jupiter.api.Test
	void testSentimentAnalyser() {
		SentimentAnalyser sentimentAnalyser = new SentimentAnalyser();
		sentimentAnalyser.initialize();
		String testString = "They have a lovely old red post-box.";
		List<String> controlAdjectives = new ArrayList();
		controlAdjectives.add("lovely");
		controlAdjectives.add("old");
		controlAdjectives.add("red");
		List<String> adjectives = sentimentAnalyser.getAdjectives(testString);
		
		assertEquals(controlAdjectives, adjectives);

   
	}

}
