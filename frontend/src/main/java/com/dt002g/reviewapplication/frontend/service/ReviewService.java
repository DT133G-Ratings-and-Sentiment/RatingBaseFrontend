package com.dt002g.reviewapplication.frontend.service;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
	
	@POST("getRatingByInclusiveSearchString/search")
	Call<List<RatingBackendEntity>> getRatingByInclusiveSearchString(@QueryMap Map<String, String> params);
	
	@GET("getTopReviewsByInclusiveStringsLargerThanId/{id}/search")
	Call<List<ReviewBackendEntity>> getTop100ReviewsByStringsInclusiveLargerThanId(@Path("id")long minId, @QueryMap Map<String, String> params);
	
	@GET("getNumberOfReviewsByInclusiveStrings/search")
	Call<Integer> getNumberOfReviewsByInclusiveStrings(@QueryMap Map<String, String> params);
	
	@Multipart
	@POST("csvupload")
	Call<ResponseBody> uploadCSVFile(@Part("description") RequestBody description,  @Part MultipartBody.Part csvFile);

	@GET("getNumberOfReviewsByRatingAndScoreMatrix")
	Call<List<SentimentStatisticsBackendEntity>> getNumberOfReviewsByRatingAndScoreMatrix();
	@GET("getNumberOfReviewsByRatingAndScoreMatrixMedian")
	Call<List<SentimentStatisticsBackendEntity>> getNumberOfReviewsByRatingAndScoreMatrixMedian();

	@GET("getNumberOfReviewsByRatingAndAvgScoreTotalMatrix")
	Call<List<SentimentStatisticsBackendEntity>> getNumberOfReviewsByRatingAndAvgScoreTotalMatrix();
	@GET("getNumberOfReviewsByRatingAndMedianScoreTotalMatrix")
	Call<List<SentimentStatisticsBackendEntity>> getNumberOfReviewsByRatingAndMedianScoreTotalMatrix();

	@GET("getNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrix")
	Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> getNumberOfAdjectivesByNameInReviewRatingAndAverageScoreRangeMatrix();

	@GET("getNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrix")
	Call<List<AdjectiveByReviewRatingAndScoreBackendEntity>> getNumberOfAdjectivesByNameInReviewRatingAndMedianScoreRangeMatrix();

	@GET("getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSame")
	Call<List<Pair<String,Long>>> getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSame();

	@GET("getAllReviewsWithAdjectiveMatrix")
	Call<List<ReviewsByAdjective>> getAllReviewsWithAdjectiveMatrix();

	@GET("getNumberOfReviewsWithAMountOfSentencesMatrix")
	Call<List<Pair<Long,Long>>> getNumberOfReviewsWithAMountOfSentencesMatrix();

	@GET("getAllReviewsWithAdjective/{adjective}")
	Call<List<ReviewBackendEntity>> getAllReviewsWithAdjective(@Path("adjective") String adjective);

	@GET("getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews")
	Call<List<Pair<String,Long>>> getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews();

	@GET("getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews")
	Call<List<AdjectiveReviewAmountAppearence>> getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews();

}
