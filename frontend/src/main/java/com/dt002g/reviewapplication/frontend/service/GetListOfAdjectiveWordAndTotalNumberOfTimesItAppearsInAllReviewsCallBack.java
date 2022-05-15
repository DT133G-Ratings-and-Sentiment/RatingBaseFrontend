package com.dt002g.reviewapplication.frontend.service;

import java.util.List;

public interface GetListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack {
    public void processGetListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack(List<Pair<String,Long>> listOfAdjectivesAndTimesItAppearsInReviews);
}
