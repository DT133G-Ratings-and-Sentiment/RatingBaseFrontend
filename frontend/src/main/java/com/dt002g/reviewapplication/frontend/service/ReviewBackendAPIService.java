package com.dt002g.reviewapplication.frontend.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewBackendAPIService {
	
	private static ReviewBackendAPIService instance;
	private GetReviewsCallBack getReviewsCallBack;

	private ReviewBackendAPIService() { }
	
	public static ReviewBackendAPIService getInstance() {
		if(instance == null) {
			instance = new ReviewBackendAPIService();
		}
		return instance;
	}
	
	private final Callback<List<ReviewBackendEntity>> reviewCallback = new Callback<List<ReviewBackendEntity>>() {
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
			Platform.runLater(() -> {
				System.out.println(t.getMessage());
				Alert alert = new Alert(AlertType.WARNING ,"Request failed" + t.getMessage());
				alert.show();
			});
		}
	};

	private void ratingsCallBack(GetRatingStatsCallBack getRatingStatsCallBack, String searchString, Call<List<RatingBackendEntity>> reviewRequest) {
		reviewRequest.enqueue(new Callback<>() {
			@Override
			public void onResponse(Call<List<RatingBackendEntity>> call, Response<List<RatingBackendEntity>> response) {
				if (response.isSuccessful()) {
					List<RatingBackendEntity> reviews = response.body();
					getRatingStatsCallBack.processGetMapCallBack(reviews, searchString);
				} else {
					showErrorAlert(response);
				}
			}
			@Override
			public void onFailure(Call<List<RatingBackendEntity>> call, Throwable t) {
				showErrorAlert(t);
			}
		});
	}

	private void sentimentRequest(GetSentimentStatisticsCallBack getSentimentStatisticsCallBack, Call<List<SentimentStatisticsBackendEntity>> sentimentRequest) {
		sentimentRequest.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NotNull Call<List<SentimentStatisticsBackendEntity>> call, @NotNull Response<List<SentimentStatisticsBackendEntity>> response) {
				if (response.isSuccessful()) {

					List<SentimentStatisticsBackendEntity> sentimentStats = response.body();
					getSentimentStatisticsCallBack.processGetSentimentStatisticsCallBack(sentimentStats);
				} else {
					showErrorAlert(response);
				}
			}
			@Override
			public void onFailure(Call<List<SentimentStatisticsBackendEntity>> call, Throwable t) {
				showErrorAlert(t);
			}
		});
	}





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
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
					alert.show();
				});
			}
		}

		@Override
		public void onFailure(Call<Integer> call, Throwable t) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.WARNING ,"Request failed");
				alert.show();
			});
			
		}
	});
}
	public void getSentimentMatrix(GetSentimentStatisticsCallBack getSentimentStatisticsCallBack) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<SentimentStatisticsBackendEntity>> sentimentRequest = reviewService.getNumberOfReviewsByRatingAndScoreMatrix();
		sentimentRequest(getSentimentStatisticsCallBack, sentimentRequest);
	}

	public void getSentimentMatrixMedian(GetSentimentStatisticsCallBack getSentimentStatisticsCallBack) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<SentimentStatisticsBackendEntity>> sentimentRequest = reviewService.getNumberOfReviewsByRatingAndScoreMatrixMedian();
		sentimentRequest(getSentimentStatisticsCallBack, sentimentRequest);
	}


	public void getTopReviewsLargerThanId(GetReviewsCallBack getReviewsCallBack, Long id) {
		this.getReviewsCallBack = getReviewsCallBack;
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getTopReviewsLargerThanId(id);
		reviewRequest.enqueue(reviewCallback);
	}
	
	public void getRatingByComment(GetRatingStatsCallBack getRatingStatsCallBack, String searchString) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<RatingBackendEntity>> reviewRequest = reviewService.getRatingByComment(searchString);
		ratingsCallBack(getRatingStatsCallBack, searchString, reviewRequest);
	}

	public void getRatingByCommentsInclusive(GetRatingStatsCallBack getRatingStatsCallBack, Map<String, String> params, final String searchString) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<RatingBackendEntity>> reviewRequest = reviewService.getRatingByInclusiveSearchString(params);
		ratingsCallBack(getRatingStatsCallBack, searchString, reviewRequest);
	}

	public void uploadCSVFile(File file, UploadCSVFileCallBack uploadCSVFileCallBack, int numberOfRows) {
		System.out.println("uploading SCV file from frontend");
		RequestBody descriptionPart = RequestBody.create(okhttp3.MultipartBody.FORM, "csvFile");
		RequestBody filePart = RequestBody.create(MediaType.parse("text/plain"), file);
		MultipartBody.Part fileMultiPart = MultipartBody.Part.createFormData("csvFile", file.getName(), filePart);
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<ResponseBody> uploadCSVFileRequest = reviewService.uploadCSVFile(descriptionPart, fileMultiPart);
		System.out.println("HERE");
		uploadCSVFileRequest.enqueue(new Callback<ResponseBody>() {

			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()) {
					System.out.println("Successfully uploaded SCV file from frontend");
					file.delete();
					uploadCSVFileCallBack.processUploadCSVFileCallBack(numberOfRows);
				}
				else {
					System.out.println("Failed to  upload SCV file from frontend");
					showErrorAlert(response);
					file.delete();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				System.out.println("Failed to  upload SCV file from frontend");
				showErrorAlert(t);
				file.delete();
			}
		});
		
	}
	
	private void showErrorAlert(Throwable t) {
		Platform.runLater(() -> {
			System.out.println(t);
			Alert alert = new Alert(AlertType.WARNING ,t.getMessage());
			alert.show();
		});
	}
	
	private void showErrorAlert(Response<?> res) {
		Platform.runLater(() -> {
			try {
				System.out.println(res.body());
				Alert alert = new Alert(AlertType.WARNING ,res.body().toString());
				alert.show();
			}
			catch(NullPointerException e) {
				System.out.println(e.getMessage());
			}
		});
	}
}
