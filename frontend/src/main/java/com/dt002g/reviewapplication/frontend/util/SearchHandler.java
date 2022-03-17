package com.dt002g.reviewapplication.frontend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dt002g.reviewapplication.frontend.service.GetNumberOfReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.GetRatingStatsCallBack;
import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;

public class SearchHandler {
	
	private static SearchHandler instance = null;
	private SearchHandler() {}
	
	public static SearchHandler getInstance() {
		if(instance == null) {
			instance = new SearchHandler();
		}
		return instance;
	}
	private Map<String, String> splitStringAtSpace(String searchString) {
		List<String> stringList = new ArrayList<>(Arrays.asList(searchString.split("\\s+")));
		Map<String, String> params = new HashMap<>();

		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
		}
		return params;
	}
	
	public void getAll(GetReviewsCallBack getReviewsCallBack) {
		ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(getReviewsCallBack, 0L);
	}

	public void getByRating(GetReviewsCallBack getReviewsCallBack, int rating, long id) {
		ReviewBackendAPIService.getInstance().getTop100ReviewsByRatingLargerThanId(getReviewsCallBack, rating, id);
	}
	
	public void getByRatingAndString(GetReviewsCallBack getReviewsCallBack, int rating, String searchString) {
		Map<Integer, String> params = new HashMap<>();
		params.put(rating, searchString);
		ReviewBackendAPIService.getInstance().getByRatingAndString(getReviewsCallBack, params);
	}
	
	public void getByRatingAndStrings(GetReviewsCallBack getReviewsCallBack, int rating, String searchString, long id) {
		System.out.println("Search by rating and strings");
		Map<String, String> params = splitStringAtSpace(searchString);
		ReviewBackendAPIService.getInstance().getTop100ReviewsByRatingAndStringsLargerThanId(getReviewsCallBack, rating, params, id);
	}

	public void getByStrings(GetReviewsCallBack getReviewsCallBack, GetRatingStatsCallBack getRatingStatsCallBack, String searchString, long id) {
		System.out.println("Search by string");
		List<String> stringList = new ArrayList<>(Arrays.asList(searchString.split("\\s+")));
		Map<String, String> params = new HashMap<>();
		
		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
			if(id <= 0) {
				ReviewBackendAPIService.getInstance().getRatingByComment(getRatingStatsCallBack, stringList.get(i));
			}
		}
		ReviewBackendAPIService.getInstance().getTop100ReviewsByStringsLargerThanId(getReviewsCallBack, params, id);
	}
	
	public void getByStringsInclusive(GetReviewsCallBack getReviewsCallBack, String searchString, long id) {
		System.out.println("Search by string inclusive");
		Map<String, String> params = splitStringAtSpace(searchString);
		ReviewBackendAPIService.getInstance().getTop100ReviewsByStringsInclusiveLargerThanId(getReviewsCallBack, params, id);
	}
	
	public void getNumberOfReviewsByInclusiveStrings(GetNumberOfReviewsCallBack getNumberOfReviewsCallBack, GetRatingStatsCallBack getRatingStatsCallBack, String searchString) {
		System.out.println("Search by string inclusive");
		Map<String, String> params = splitStringAtSpace(searchString);
		ReviewBackendAPIService.getInstance().getRatingByCommentsInclusive(getRatingStatsCallBack, params, searchString);
		ReviewBackendAPIService.getInstance().getNumberOfReviewsByStrings(getNumberOfReviewsCallBack, params);
	}

	public void getTopReviewsLargerThanId(GetReviewsCallBack getReviewsCallBack, Long id){
		ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(getReviewsCallBack, id);
	}
}
