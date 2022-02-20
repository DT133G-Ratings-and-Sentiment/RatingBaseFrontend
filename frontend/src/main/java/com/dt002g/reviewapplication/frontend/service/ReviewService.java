package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

import com.dt002g.reviewapplication.frontend.Review;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewService {
	
	@GET(".")
	Call<List<ReviewBackendEntity>> getReviews();
	
	@GET("{id}")
	Call<ReviewBackendEntity> getReview(@Path("id")String id);
}
