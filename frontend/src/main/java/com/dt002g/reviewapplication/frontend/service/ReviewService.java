package com.dt002g.reviewapplication.frontend.service;

import java.util.List;
import java.util.Map;

import com.dt002g.reviewapplication.frontend.Review;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ReviewService {
	
	@GET("getAll")
	Call<List<ReviewBackendEntity>> getReviews();
	
	@GET("getTopReviews")
	Call<List<ReviewBackendEntity>> getTopReviews();
	
	@GET("{id}")
	Call<List<ReviewBackendEntity>> getReview(@Path("id")String id);
	
	@GET("getByString/{comment}")
	Call<List<ReviewBackendEntity>> getByString(@Path("comment") String comment);
	
	@GET("getByRating/{rating}")
	Call<List<ReviewBackendEntity>> getByRating(@Path("rating") int rating);
	
	@POST("getByStrings/search")
	Call<List<ReviewBackendEntity>> getByStrings(@QueryMap Map<String, String> params);
	
	@POST("getByRatingAndString/search{searchString}")
	Call<List<ReviewBackendEntity>> getByRatingAndString(@QueryMap Map<Integer, String> params);
}
