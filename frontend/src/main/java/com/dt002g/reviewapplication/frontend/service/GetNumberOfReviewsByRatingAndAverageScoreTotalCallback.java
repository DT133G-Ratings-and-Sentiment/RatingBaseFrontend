package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

public interface GetNumberOfReviewsByRatingAndAverageScoreTotalCallback {
    void processGetNumberOfReviewsByRatingAndAverageScoreCallBack(List<SentimentStatisticsBackendEntity> response);
}
