package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

import com.dt002g.reviewapplication.frontend.Review;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewService {
	
	@GET("getAll")
	Call<List<ReviewBackendEntity>> getReviews();
	
	@GET("getTopReviews")
	Call<List<ReviewBackendEntity>> getTopReviews();
	
	@GET("getTopReviewsLargerThanId/{id}")
	Call<List<ReviewBackendEntity>> getTopReviewsLargerThanId(@Path("id")Long id);
	
	@GET("{id}")
	Call<ReviewBackendEntity> getReview(@Path("id")Long id);
}
