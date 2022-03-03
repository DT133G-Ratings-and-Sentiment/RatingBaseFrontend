package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

import com.dt002g.reviewapplication.frontend.Review;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewBackendAPIService {
	
	private static ReviewBackendAPIService instance;
	
	private ReviewBackendAPIService() { }
	
	public static ReviewBackendAPIService getInstance() {
		if(instance == null) {
			instance = new ReviewBackendAPIService();
		}
		return instance;
	}
	
	public void getReviews(GetReviewsCallBack getReviewsCallBack) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getReviews();
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews = response.body();
					getReviewsCallBack.processGetReviewsCallBack(reviews);
				}
				else {
					
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void getTop100Reviews(GetReviewsCallBack getReviewsCallBack) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTopReviews();
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews = response.body();
					getReviewsCallBack.processGetReviewsCallBack(reviews);
				}
				else {
					
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
