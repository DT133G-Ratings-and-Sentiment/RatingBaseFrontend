package com.dt002g.reviewapplication.frontend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;

public class SearchHandler {
	
	private static SearchHandler instance = null;
	private SearchHandler() {
		
	}
	
	public static SearchHandler getInstance() {
		if(instance == null) {
			instance = new SearchHandler();
		}
		return instance;
	}
	
	public void getAll(GetReviewsCallBack getReviewsCallBack) {
		ReviewBackendAPIService.getInstance().getReviews(getReviewsCallBack);
	}
	
	
	public void getByRating(GetReviewsCallBack getReviewsCallBack, int rating) {
		ReviewBackendAPIService.getInstance().getByRating(getReviewsCallBack, rating);
	}
	
	public void getByRatingAndString(GetReviewsCallBack getReviewsCallBack, int rating, String searchString) {
		Map<Integer, String> params = new HashMap<Integer, String>();
		params.put(rating, searchString);
		ReviewBackendAPIService.getInstance().getByRatingAndString(getReviewsCallBack, params);
	}
	
	public void getByStrings(GetReviewsCallBack getReviewsCallBack, String searchString) {
		List<String> stringList = new ArrayList<String>(Arrays.asList( searchString.split("\\s+")));
		Map<String, String> params = new HashMap<String, String>();
		
		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
		}
		
		ReviewBackendAPIService.getInstance().getByStrings(getReviewsCallBack, params);
	}
}
