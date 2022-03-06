package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

public interface GetRatingStatsCallBack {
	void processGetMapCallBack(List<RatingBackendEntity> response, String searchString);
}
