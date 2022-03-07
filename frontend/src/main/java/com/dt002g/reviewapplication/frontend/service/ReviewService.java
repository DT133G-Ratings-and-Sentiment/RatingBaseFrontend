package com.dt002g.reviewapplication.frontend.service;

import java.util.List;
import java.util.Map;

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
	
	@GET("getTopReviewsLargerThanId/{id}")
	Call<List<ReviewBackendEntity>> getTopReviewsLargerThanId(@Path("id")Long id);
	
	@GET("{id}")
	Call<List<ReviewBackendEntity>> getReview(@Path("id")String id);
	
	@GET("getByString/{comment}")
	Call<List<ReviewBackendEntity>> getByString(@Path("comment") String comment);
	
	
	@GET("getByRating/{rating}")
	Call<List<ReviewBackendEntity>> getByRating(@Path("rating") int rating);
	
	@POST("getByStrings/search")
	Call<List<ReviewBackendEntity>> getByStrings(@QueryMap Map<String, String> params);
	
	@GET("getTopReviewsByStringsLargerThanId/{id}/search")
	Call<List<ReviewBackendEntity>> getTop100ReviewsByStringsLargerThanId(@Path("id")long minId, @QueryMap Map<String, String> params);
	
	@POST("getByRatingAndString/search{searchString}")
	Call<List<ReviewBackendEntity>> getByRatingAndString(@QueryMap Map<Integer, String> params);
	
	@GET("getTopReviewsByRatingAndStringsLargerThanId/{id}/{rating}/search{searchString}")
	Call<List<ReviewBackendEntity>> getTop100ReviewsByRatingAndStringLargerThanId(@Path("id")long minId, @Path("rating")int rating, @QueryMap Map<String, String> params);

	Call<ReviewBackendEntity> getReview(@Path("id")Long id);

	@GET("getTopReviewsByRatingLargerThanId/{rating}/{id}")
	Call<List<ReviewBackendEntity>> getTop100ReviewsByRatingLargerThanId(@Path("rating")int rating, @Path("id")long minId);
	
	@GET("getRatingByString/{searchString}")
	Call<List<RatingBackendEntity>> getRatingByComment(@Path("searchString") String searchString);
	
	@GET("getTopReviewsByInclusiveStringsLargerThanId/{id}/search")
	Call<List<ReviewBackendEntity>> getTop100ReviewsByStringsInclusiveLargerThanId(@Path("id")long minId, @QueryMap Map<String, String> params);
	
	@GET("getNumberOfReviewsByInclusiveStrings/search")
	Call<Integer> getNumberOfReviewsByInclusiveStrings(@QueryMap Map<String, String> params);
}
