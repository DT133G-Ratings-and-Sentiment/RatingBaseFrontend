package com.dt002g.reviewapplication.frontend.service;

import java.util.List;
import java.util.Map;

import com.dt002g.reviewapplication.frontend.Review;

public interface GetReviewsCallBack {
	void processGetReviewsCallBack(List<ReviewBackendEntity> response);
}
