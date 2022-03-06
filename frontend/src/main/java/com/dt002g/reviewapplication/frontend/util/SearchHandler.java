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
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;

import retrofit2.Call;
import retrofit2.http.Path;

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
		//ReviewBackendAPIService.getInstance().getReviews(getReviewsCallBack);
		ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(getReviewsCallBack, 0L);
	}
	
	
	public void getByRating(GetReviewsCallBack getReviewsCallBack, int rating, long id) {
		//ReviewBackendAPIService.getInstance().getByRating(getReviewsCallBack, rating);
		ReviewBackendAPIService.getInstance().getTop100ReviewsByRatingLargerThanId(getReviewsCallBack, rating, id);
	}
	
	public void getByRatingAndString(GetReviewsCallBack getReviewsCallBack, int rating, String searchString) {
		Map<Integer, String> params = new HashMap<Integer, String>();
		params.put(rating, searchString);
		ReviewBackendAPIService.getInstance().getByRatingAndString(getReviewsCallBack, params);
	}
	
	public void getByRatingAndStrings(GetReviewsCallBack getReviewsCallBack, int rating, String searchString, long id) {
		System.out.println("Search by rating and strings");
		List<String> stringList = new ArrayList<String>(Arrays.asList( searchString.split("\\s+")));
		Map<String, String> params = new HashMap<String, String>();
		
		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
		}

		ReviewBackendAPIService.getInstance().getTop100ReviewsByRatingAndStringsLargerThanId(getReviewsCallBack, rating, params, id);

	}
	
	public void getByStrings(GetReviewsCallBack getReviewsCallBack, GetRatingStatsCallBack getRatingStatsCallBack, String searchString, long id) {
		System.out.println("Search by string");
		List<String> stringList = new ArrayList<String>(Arrays.asList( searchString.split("\\s+")));
		Map<String, String> params = new HashMap<String, String>();
		
		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
			ReviewBackendAPIService.getInstance().getRatingByComment(getRatingStatsCallBack, stringList.get(i));
		}
		
		//ReviewBackendAPIService.getInstance().getByStrings(getReviewsCallBack, params);
		ReviewBackendAPIService.getInstance().getTop100ReviewsByStringsLargerThanId(getReviewsCallBack, params, id);
	}
	
	public void getByStringsInclusive(GetReviewsCallBack getReviewsCallBack, String searchString, long id) {
		System.out.println("Search by string inclusive");
		List<String> stringList = new ArrayList<String>(Arrays.asList( searchString.split("\\s+")));
		Map<String, String> params = new HashMap<String, String>();
		
		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
		}
		
		//ReviewBackendAPIService.getInstance().getByStrings(getReviewsCallBack, params);
		ReviewBackendAPIService.getInstance().getTop100ReviewsByStringsInclusiveLargerThanId(getReviewsCallBack, params, id);
	}
	
	public void getNumberOfReviewsByInclusiveStrings(GetNumberOfReviewsCallBack getNumberOfReviewsCallBack, String searchString) {
		System.out.println("Search by string inclusive");
		List<String> stringList = new ArrayList<String>(Arrays.asList( searchString.split("\\s+")));
		Map<String, String> params = new HashMap<String, String>();
		
		for(int i = 0; i < stringList.size(); i++) {
			params.put("searchString" + (i+1), stringList.get(i));
		}
		
		ReviewBackendAPIService.getInstance().getNumberOfReviewsByStrings(getNumberOfReviewsCallBack, params);
	}
	
	
	public void getTopReviewsLargerThanId(GetReviewsCallBack getReviewsCallBack, Long id){
		ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(getReviewsCallBack, id);
	}
	/*public List<ReviewBackendEntity> getTop100ReviewsByRatingLargerThanId(int rating, long minId){
		
	}*/
}
