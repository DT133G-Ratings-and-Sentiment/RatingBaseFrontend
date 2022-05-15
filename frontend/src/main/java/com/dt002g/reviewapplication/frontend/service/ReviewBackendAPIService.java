package com.dt002g.reviewapplication.frontend.service;

import java.io.File;
import java.util.ArrayList;
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

	public void getNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrix(GetNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrixCallBack getNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrixCallBack) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> reviewRequest = reviewService.getNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrix();

		reviewRequest.enqueue(new Callback<List<AdjectiveByReviewRatingAndScoreBackendEntity>>() {

								  @Override
								  public void onResponse(Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> call, Response<List<AdjectiveByReviewRatingAndScoreBackendEntity>> response) {
									  if(response.isSuccessful()) {
										  List<AdjectiveByReviewRatingAndScoreBackendEntity> adjectiveByReviewRatingAndScoreBackendEntities = response.body();
										  getNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrixCallBack.processGetNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrixCallBack(adjectiveByReviewRatingAndScoreBackendEntities);
									  }
									  else {
										  Platform.runLater(() -> {
											  Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
											  alert.show();
										  });
									  }
								  }

								  @Override
								  public void onFailure(Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> call, Throwable t) {
									  Platform.runLater(() -> {
										  Alert alert = new Alert(AlertType.WARNING ,"Request failed");
										  alert.show();
									  });

								  }
							  });
	}

	public void getNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrix(GetNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrixCallBack getNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrixCallBack) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> reviewRequest = reviewService.getNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrix();

		reviewRequest.enqueue(new Callback<List<AdjectiveByReviewRatingAndScoreBackendEntity>>() {

			@Override
			public void onResponse(Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> call, Response<List<AdjectiveByReviewRatingAndScoreBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<AdjectiveByReviewRatingAndScoreBackendEntity> adjectiveByReviewRatingAndScoreBackendEntities = response.body();
					getNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrixCallBack.processGetNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrixCallBack(adjectiveByReviewRatingAndScoreBackendEntities);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}
/*
	public void getNumberOfReviewsByRatingAndAverageScoreTotal(GetNumberOfReviewsByRatingAndAverageScoreTotalCallback getNumberOfReviewsByRatingAndAverageScoreTotalCallback) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<SentimentStatisticsBackendEntity>> reviewRequest = reviewService.getNumberOfReviewsByRatingAndScoreMatrix();

		reviewRequest.enqueue(new Callback<List<SentimentStatisticsBackendEntity>>() {

			@Override
			public void onResponse(Call<List<SentimentStatisticsBackendEntity>> call, Response<List<SentimentStatisticsBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<SentimentStatisticsBackendEntity> numberOfReviewsByRatingAndAverageScoreTotal = response.body();
					getNumberOfReviewsByRatingAndAverageScoreTotalCallback.processGetNumberOfReviewsByRatingAndAverageScoreCallBack(numberOfReviewsByRatingAndAverageScoreTotal);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<SentimentStatisticsBackendEntity>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}
*/
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
					System.out.println("Successfully uploaded SCV file from frontend: " + file.getName());
					file.delete();
					uploadCSVFileCallBack.processUploadCSVFileCallBack(numberOfRows);
				}
				else {
					System.out.println("Failed to  upload SCV file from frontend: " + file.getName());
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


	public void getNumberOfReviewsByRatingAndAverageScoreTotal(GetNumberOfReviewsByRatingAndAverageScoreTotalCallback getNumberOfReviewsByRatingAndAverageScoreTotalCallback) {
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<SentimentStatisticsBackendEntity>> reviewRequest = reviewService.getNumberOfReviewsByRatingAndAvgScoreTotalMatrix();

		reviewRequest.enqueue(new Callback<List<SentimentStatisticsBackendEntity>>() {

			@Override
			public void onResponse(Call<List<SentimentStatisticsBackendEntity>> call, Response<List<SentimentStatisticsBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<SentimentStatisticsBackendEntity> numberOfReviewsByRatingAndAverageScoreTotal = response.body();
					getNumberOfReviewsByRatingAndAverageScoreTotalCallback.processGetNumberOfReviewsByRatingAndAverageScoreCallBack(numberOfReviewsByRatingAndAverageScoreTotal);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<SentimentStatisticsBackendEntity>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getNumberOfReviewsByRatingAndMedianScoreTotalMatrix(GetNumberOfReviewsByRatingAndAverageScoreTotalCallback getNumberOfReviewsByRatingAndAverageScoreTotalCallback){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<SentimentStatisticsBackendEntity>> reviewRequest = reviewService.getNumberOfReviewsByRatingAndMedianScoreTotalMatrix();
		reviewRequest.enqueue(new Callback<List<SentimentStatisticsBackendEntity>>() {

			@Override
			public void onResponse(Call<List<SentimentStatisticsBackendEntity>> call, Response<List<SentimentStatisticsBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<SentimentStatisticsBackendEntity> matrix= response.body();
					getNumberOfReviewsByRatingAndAverageScoreTotalCallback.processGetNumberOfReviewsByRatingAndAverageScoreCallBack(matrix);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<SentimentStatisticsBackendEntity>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSame(GetNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<Pair<String,Long>>> reviewRequest = reviewService.getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSame();
		reviewRequest.enqueue(new Callback<List<Pair<String,Long>>>() {

			@Override
			public void onResponse(Call<List<Pair<String,Long>>> call, Response<List<Pair<String,Long>>> response) {
				if(response.isSuccessful()) {
					List<Pair<String,Long>> matrix= response.body();
					getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack.processGetNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack(matrix);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<Pair<String,Long>>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getAllReviewsWithAdjectiveMatrix(GetAllReviewsWithAdjectiveMatrixCallBack getAllReviewsWithAdjectiveMatrixCallBack){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewsByAdjective>> reviewRequest = reviewService.getAllReviewsWithAdjectiveMatrix();
		reviewRequest.enqueue(new Callback<List<ReviewsByAdjective>>() {

			@Override
			public void onResponse(Call<List<ReviewsByAdjective>> call, Response<List<ReviewsByAdjective>> response) {
				if(response.isSuccessful()) {
					List<ReviewsByAdjective> matrix= response.body();
					getAllReviewsWithAdjectiveMatrixCallBack.processGetAllReviewsWithAdjectiveMatrixCallBack(matrix);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , "Response code: " + response.code() + " response message: " + response.message().toString() + " error body:"+ response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<ReviewsByAdjective>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getAllReviewsWithAdjective(String adjective, GetAllReviewsWithAdjectiveCallBack getAllReviewsWithAdjectiveCallBack){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<ReviewBackendEntity>> reviewRequest = reviewService.getAllReviewsWithAdjective(adjective);
		reviewRequest.enqueue(new Callback<List<ReviewBackendEntity>>() {

			@Override
			public void onResponse(Call<List<ReviewBackendEntity>> call, Response<List<ReviewBackendEntity>> response) {
				if(response.isSuccessful()) {
					List<ReviewBackendEntity> reviews= response.body();
					getAllReviewsWithAdjectiveCallBack.processGetAllReviewsWithAdjectiveCallBack(reviews);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , "Response code: " + response.code() + " response message: " + response.message().toString() + " error body:"+ response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<ReviewBackendEntity>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getNumberOfReviewsWithAMountOfSentencesMatrix(GetNumberOfReviewsWithAMountOfSentencesMatrixCallBack getNumberOfReviewsWithAMountOfSentencesMatrixCallBack){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<Pair<Long,Long>>> reviewRequest = reviewService.getNumberOfReviewsWithAMountOfSentencesMatrix();
		reviewRequest.enqueue(new Callback<List<Pair<Long,Long>>>() {

			@Override
			public void onResponse(Call<List<Pair<Long,Long>>> call, Response<List<Pair<Long,Long>>> response) {
				if(response.isSuccessful()) {
					List<Pair<Long,Long>> data= response.body();
					List<AmountOfRatingsWithAMountOfScentences> matrix = new ArrayList<>();
					for(Pair<Long,Long> p: data){
						matrix.add(new AmountOfRatingsWithAMountOfScentences(p.first, p.second));
					}
					getNumberOfReviewsWithAMountOfSentencesMatrixCallBack.processGetNumberOfReviewsWithAMountOfSentencesMatrixCallBack(matrix);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<Pair<Long,Long>>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews(GetListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<Pair<String,Long>>> reviewRequest = reviewService.getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews();
		reviewRequest.enqueue(new Callback<List<Pair<String,Long>>>() {

			@Override
			public void onResponse(Call<List<Pair<String,Long>>> call, Response<List<Pair<String,Long>>> response) {
				if(response.isSuccessful()) {
					List<Pair<String,Long>> data= response.body();
					/*List<Pair<String,Long>> listOfAdjectivesAndTimesItAppearsInReviews = new ArrayList<>();
					for(Pair<String,Long> p: data){
						listOfAdjectivesAndTimesItAppearsInReviews.add(p);
					}*/
					getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack.processGetListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack(data);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<Pair<String,Long>>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}

	public void getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews(GetMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack){
		ReviewService reviewService = ServiceBuilder.getInstance().buildService(ReviewService.class);
		Call<List<AdjectiveReviewAmountAppearence>> reviewRequest = reviewService.getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews();
		reviewRequest.enqueue(new Callback<List<AdjectiveReviewAmountAppearence>>() {

			@Override
			public void onResponse(Call<List<AdjectiveReviewAmountAppearence>> call, Response<List<AdjectiveReviewAmountAppearence>> response) {
				if(response.isSuccessful()) {
					List<AdjectiveReviewAmountAppearence> data= response.body();
					getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack.processGetMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack(data);
				}
				else {
					Platform.runLater(() -> {
						Alert alert = new Alert(AlertType.WARNING , response.errorBody().toString());
						alert.show();
					});
				}
			}

			@Override
			public void onFailure(Call<List<AdjectiveReviewAmountAppearence>> call, Throwable t) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.WARNING ,"Request failed");
					alert.show();
				});

			}
		});
	}
}
