package com.dt002g.reviewapplication.frontend.service;

import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewBackendAPIService {
	
	private static ReviewBackendAPIService instance;
	private GetReviewsCallBack getReviewsCallBack;
	private GetRatingStatsCallBack getRatingStatsCallBack;
	
	private ReviewBackendAPIService() { }
	
	public static ReviewBackendAPIService getInstance() {
		if(instance == null) {
			instance = new ReviewBackendAPIService();
		}
		return instance;
	}
	
	private Callback<List<ReviewBackendEntity>> reviewCallback = new Callback<List<ReviewBackendEntity>>() {
		@Override
		public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
			if(response.isSuccessful()) {
				List<ReviewBackendEntity> reviews = response.body();
				getReviewsCallBack.processGetReviewsCallBack(reviews);
			}
			else {
				showErrorAlert(response);
			}
		}

		@Override
		public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
			Alert alert = new Alert(AlertType.WARNING ,"Request failed");
			alert.show();
		}
	};
	
	
	
	public void getReviews(GetReviewsCallBack getReviewsCallBack) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getReviews();
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getTop100Reviews(GetReviewsCallBack getReviewsCallBack) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTopReviews();
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getByStrings(GetReviewsCallBack getReviewsCallBack, Map<String, String> searchString) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getByStrings(searchString);
		reviewRequest.enqueue(reviewCallback);
	}
	public void getByRating(GetReviewsCallBack getReviewsCallBack, int rating) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getByRating(rating);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getTop100ReviewsByRatingLargerThanId(GetReviewsCallBack getReviewsCallBack, int rating, long minId) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByRatingLargerThanId(rating, minId);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getByRatingAndString(GetReviewsCallBack getReviewsCallBack, Map<Integer, String> params) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getByRatingAndString(params);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getTop100ReviewsByRatingAndStringsLargerThanId(GetReviewsCallBack getReviewsCallBack, int rating, Map<String, String> searchString, long id) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByRatingAndStringLargerThanId(id, rating, searchString);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getTop100ReviewsByStringsLargerThanId(GetReviewsCallBack getReviewsCallBack, Map<String, String> searchString, long id) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByStringsLargerThanId(id, searchString);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getTop100ReviewsByStringsInclusiveLargerThanId(GetReviewsCallBack getReviewsCallBack, Map<String, String> searchString, long id) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTop100ReviewsByStringsInclusiveLargerThanId(id, searchString);
		reviewRequest.enqueue(reviewCallback);
	}
	
public void getNumberOfReviewsByStrings(GetNumberOfReviewsCallBack getNumberOfReviewsCallBack, Map<String, String> searchString) {
	
	ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
	Call<Integer> reviewRequest = reviewService.getNumberOfReviewsByInclusiveStrings(searchString);
	reviewRequest.enqueue(new Callback<Integer>() {

		@Override
		public void onResponse(Call<Integer> call, Response<Integer> response) {
			if(response.isSuccessful()) {
				Integer numberOfReviews = response.body();
				getNumberOfReviewsCallBack.processGetNumberOfReviewsCallBack(numberOfReviews);
			}
			else {
				Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
				alert.show();
			}
		}

		@Override
		public void onFailure(Call<Integer> call, Throwable t) {
			Alert alert = new Alert(AlertType.WARNING ,"Request failed");
			alert.show();
			
		}
	});
}
	
	public void getTopReviewsLargerThanId(GetReviewsCallBack getReviewsCallBack, Long id) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTopReviewsLargerThanId(id);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getRatingByComment(GetRatingStatsCallBack getRatingStatsCallBack, String searchString) {
		this.getRatingStatsCallBack = getRatingStatsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<RatingBackendEntity>> reviewRequest = reviewService.getRatingByComment(searchString);
		reviewRequest.enqueue(new Callback<List<RatingBackendEntity>>() {

			@Override
			public void onResponse(Call<List<RatingBackendEntity>> call, Response<List<RatingBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<RatingBackendEntity> reviews = response.body();
					getRatingStatsCallBack.processGetMapCallBack(reviews, searchString);
				}
				else {
					showErrorAlert(response);
				}
			}

			@Override
			public void onFailure(Call<List<RatingBackendEntity>> call, Throwable t) {
				showErrorAlert(t);
			}
		});
	}
	public void getRatingByCommentsInclusive(GetRatingStatsCallBack getRatingStatsCallBack, Map<String, String> params, final String searchString) {
		this.getRatingStatsCallBack = getRatingStatsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<RatingBackendEntity>> reviewRequest = reviewService.getRatingByInclusiveSearchString(params);
		
		reviewRequest.enqueue(new Callback<List<RatingBackendEntity>>() {

			@Override
			public void onResponse(Call<List<RatingBackendEntity>> call, Response<List<RatingBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<RatingBackendEntity> reviews = response.body();
					
					
					getRatingStatsCallBack.processGetMapCallBack(reviews, searchString);
				}
				else {
					showErrorAlert(response);
				}
			}

			@Override
			public void onFailure(Call<List<RatingBackendEntity>> call, Throwable t) {
				showErrorAlert(t);
			}
		});
	}
	
	private void showErrorAlert(Throwable t) {
		Platform.runLater(new Runnable() {					
			@Override
			public void run() {
				System.out.println(t);
				Alert alert = new Alert(AlertType.WARNING ,t.getMessage());
				alert.show();	
			}
		});
	}
	
	private void showErrorAlert(Response<?> res) {
		Platform.runLater(new Runnable() {					
			@Override
			public void run() {
				System.out.println(res.body());
				Alert alert = new Alert(AlertType.WARNING ,res.body().toString());
				alert.show();	
			}
		});
	}
	
}
