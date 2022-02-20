package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

import com.dt002g.reviewapplication.frontend.Review;

public interface GetReviewsCallBack {
	void processGetReviewsCallBack(List<ReviewBackendEntity> response);
}
