package com.dt002g.reviewapplication.frontend.service;

import java.util.List;
import java.util.Map;

import com.dt002g.reviewapplication.frontend.Review;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
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
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
			}
		});
	}
	
	public void getByStrings(GetReviewsCallBack getReviewsCallBack, Map<String, String> searchString) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getByStrings(searchString);
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews = response.body();
					getReviewsCallBack.processGetReviewsCallBack(reviews);
				}
				else {
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
			}
		});
	}
	public void getByRating(GetReviewsCallBack getReviewsCallBack, int rating) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getByRating(rating);
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews = response.body();
					getReviewsCallBack.processGetReviewsCallBack(reviews);
				}
				else {
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
			}
		});
	}
	
	public void getTop100ReviewsByRatingLargerThanId(GetReviewsCallBack getReviewsCallBack, int rating, long minId) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByRatingLargerThanId(rating, minId);
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews = response.body();
					getReviewsCallBack.processGetReviewsCallBack(reviews);
				}
				else {
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
			}
		});
	}
	
public void getByRatingAndString(GetReviewsCallBack getReviewsCallBack, Map<Integer, String> params) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getByRatingAndString(params);
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews = response.body();
					getReviewsCallBack.processGetReviewsCallBack(reviews);
				}
				else {
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
			}
		});
	}

public void getTop100ReviewsByRatingAndStringsLargerThanId(GetReviewsCallBack getReviewsCallBack, int rating, Map<String, String> searchString, long id) {
	
	ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
	Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByRatingAndStringLargerThanId(id, rating, searchString);
	reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

		@Override
		public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
			if(response.isSuccessful()) {
				List<ReviewBackendEntity> reviews = response.body();
				getReviewsCallBack.processGetReviewsCallBack(reviews);
			}
			else {
				Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
				alert.show();
			}
		}

		@Override
		public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
			Alert alert = new Alert(AlertType.WARNING ,"Request failed");
			alert.show();
		}
	});
}

public void getTop100ReviewsByStringsLargerThanId(GetReviewsCallBack getReviewsCallBack, Map<String, String> searchString, long id) {
	
	ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
	Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByStringsLargerThanId(id, searchString);
	reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

		@Override
		public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
			if(response.isSuccessful()) {
				List<ReviewBackendEntity> reviews = response.body();
				getReviewsCallBack.processGetReviewsCallBack(reviews);
			}
			else {
				Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
				alert.show();
			}
		}

		@Override
		public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
			Alert alert = new Alert(AlertType.WARNING ,"Request failed");
			alert.show();
		}
	});
}
	
	public void getTopReviewsLargerThanId(GetReviewsCallBack getReviewsCallBack, Long id) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTopReviewsLargerThanId(id);
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
	
	public void getRatingByComment(GetRatingStatsCallBack getRatingStatsCallBack, String searchString) {
		
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<RatingBackendEntity>> reviewRequest = reviewService.getRatingByComment(searchString);
		reviewRequest.enqueue(new Callback<List<RatingBackendEntity>>() {

			@Override
			public void onResponse(Call<List<RatingBackendEntity>> call, Response<List<RatingBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<RatingBackendEntity> reviews = response.body();
					getRatingStatsCallBack.processGetMapCallBack(reviews);
				}
				else {
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				}
			}

			@Override
			public void onFailure(Call<List<RatingBackendEntity>> call, Throwable t) {
			
				Platform.runLater(new Runnable() {					
					@Override
					public void run() {
						System.out.println(t);
						Alert alert = new Alert(AlertType.WARNING ,t.getMessage());
						alert.show();	
					}
				});
			}
		});
	}
}
