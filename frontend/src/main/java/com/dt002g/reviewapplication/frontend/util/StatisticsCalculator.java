package com.dt002g.reviewapplication.frontend.util;

import com.dt002g.reviewapplication.frontend.service.SentimentStatisticsBackendEntity;

import java.util.ArrayList;
import java.util.List;

public class StatisticsCalculator {
    public static double CorrelationCoefficient(List<SentimentStatisticsBackendEntity> reviewRatingByScoreMatrix){
        /*if(reviewRatingByScoreMatrix == null){
            reviewRatingByScoreMatrix = createMatrix();
        }*/
        double[] ratingPercentage;
        ratingPercentage = new double[101];
        double[] sentimentScorePercentage;
        sentimentScorePercentage = new double[5];

        double[][] ratingSentimentScorePercentage;
        ratingSentimentScorePercentage = new double[101][5];

        int[] ratingAmount;
        ratingAmount = new int[101];
        int[] sentimentScoreAmount;
        sentimentScoreAmount= new int[5];

        int totalRatings = 0;

        double ratingExpectedValue = 0;

        double sentimentScoreExpectedValue =0;

        double deviationRating= 0;

        double deviationSentimentScore = 0;

        for(SentimentStatisticsBackendEntity ssbe: reviewRatingByScoreMatrix){
            System.out.println("Rating: " + ssbe.rating + " minScore: " + ssbe.minScore + " maxScore: " + ssbe.maxScore + " amount: " + ssbe.amount);
            ratingAmount[ssbe.rating] += ssbe.amount;
            totalRatings += ssbe.amount;
            if(ssbe.minScore>= 0 && ssbe.maxScore <= 20){
                sentimentScoreAmount[0] += ssbe.amount;
            }
            else if(ssbe.minScore>= 20 && ssbe.maxScore <= 40){
                sentimentScoreAmount[1] += ssbe.amount;
            }
            else if(ssbe.minScore>= 40 && ssbe.maxScore <= 60){
                sentimentScoreAmount[2] += ssbe.amount;
            }
            else if(ssbe.minScore>= 60 && ssbe.maxScore <= 80){
                sentimentScoreAmount[3] += ssbe.amount;
            }
            else if(ssbe.minScore>= 80 && ssbe.maxScore <= 100){
                sentimentScoreAmount[4] += ssbe.amount;
            }
        }

        for(int i = 0; i < 101; i++){
            ratingPercentage[i] = ((double)ratingAmount[i])/((double)totalRatings);
            ratingExpectedValue += i * ratingPercentage[i];
        }
        for(int i = 0; i < 5; i++){
            sentimentScorePercentage[i] = ((double)sentimentScoreAmount[i])/ ((double)totalRatings);
            sentimentScoreExpectedValue += (i+1) * sentimentScorePercentage[i];
        }

        for(int i = 0; i < 101; i++){
            deviationRating += (i - ratingExpectedValue ) * (i - ratingExpectedValue ) * ratingPercentage[i];
        }
        for(int i = 0; i < 5; i++){
            deviationSentimentScore += ((i+1) -sentimentScoreExpectedValue) * ((i+1) -sentimentScoreExpectedValue) * sentimentScorePercentage[i];
        }

        double ratingsSentimentScorePecentageProduct = 0;

        double covarriance = 0;

        for(SentimentStatisticsBackendEntity ssbe: reviewRatingByScoreMatrix){
            if(ssbe.minScore>= 0 && ssbe.maxScore <= 20){
                ratingSentimentScorePercentage[ssbe.rating][0] = ((double)ssbe.amount)/((double)totalRatings);
            }
            else if(ssbe.minScore>= 20 && ssbe.maxScore <= 40){
                ratingSentimentScorePercentage[ssbe.rating][1] = ((double)ssbe.amount)/((double)totalRatings);
            }
            else if(ssbe.minScore>= 40 && ssbe.maxScore <= 60){
                ratingSentimentScorePercentage[ssbe.rating][2] = ((double)ssbe.amount)/((double)totalRatings);
            }
            else if(ssbe.minScore>= 60 && ssbe.maxScore <= 80){
                ratingSentimentScorePercentage[ssbe.rating][3] = ((double)ssbe.amount)/((double)totalRatings);
            }
            else if(ssbe.minScore>= 80 && ssbe.maxScore <= 100){
                ratingSentimentScorePercentage[ssbe.rating][4] = ((double)ssbe.amount)/((double)totalRatings);
            }
        }

        for(int i = 0; i < 101; i++){
            System.out.print("Rating: " + i);
            for(int j = 0; j < 5; j++){
                System.out.print(" | " + ratingSentimentScorePercentage[i][j]);
                ratingsSentimentScorePecentageProduct += i *(j+1) *ratingSentimentScorePercentage[i][j];
            }
            System.out.println();
        }

        covarriance = ratingsSentimentScorePecentageProduct  - (ratingExpectedValue * sentimentScoreExpectedValue);

        double correlationCofficient = covarriance / (Math.sqrt((deviationRating*deviationSentimentScore)));

        System.out.println("ratingExpectedValue: " + ratingExpectedValue);
        System.out.println("sentimentScoreExpectedValue: " + sentimentScoreExpectedValue);
        System.out.println("deviationRating: " + deviationRating);
        System.out.println("deviationSentimentScore: " + deviationSentimentScore);
        System.out.println("covarriance: " + covarriance);
        System.out.println("correlationCofficient: " + correlationCofficient);

        return 0;
    }

    private static List<SentimentStatisticsBackendEntity> createMatrix() {
        List<SentimentStatisticsBackendEntity> matrix = new ArrayList<>();
        int minScore = 0;
        int maxScore = 20;
        int amount = 0;
        for(int i = 0; i < 101; i++){
            for(int j = 0; j < 5; j++){
                if( i ==0){
                    if(j ==0){
                        amount = 5;
                    }
                    else if(j ==1){
                        amount = 4;
                    }
                    else if(j ==2){
                        amount = 3;
                    }
                    else if(j ==3){
                        amount = 2;
                    }
                    else if(j ==4){
                        amount = 1;
                    }
                }
                else if( i ==20){
                    if(j ==0){
                        amount = 4;
                    }
                    else if(j ==1){
                        amount = 4;
                    }
                    else if(j ==2){
                        amount = 3;
                    }
                    else if(j ==3){
                        amount = 2;
                    }
                    else if(j ==4){
                        amount = 2;
                    }
                }
                else if( i ==40){
                    if(j ==0){
                        amount = 2;
                    }
                    else if(j ==1){
                        amount = 3;
                    }
                    else if(j ==2){
                        amount = 5;
                    }
                    else if(j ==3){
                        amount = 3;
                    }
                    else if(j ==4){
                        amount = 2;
                    }
                }
                else if( i ==80){

                    if(j ==0){
                        amount = 2;
                    }
                    else if(j ==1){
                        amount = 2;
                    }
                    else if(j ==2){
                        amount = 3;
                    }
                    else if(j ==3){
                        amount = 4;
                    }
                    else if(j ==4){
                        amount = 4;
                    }
                }
                else if(i == 100){
                    if(j ==0){
                        amount = 1;
                    }
                    else if(j ==1){
                        amount = 2;
                    }
                    else if(j ==2){
                        amount = 3;
                    }
                    else if(j ==3){
                        amount = 4;
                    }
                    else if(j ==4){
                        amount = 5;
                    }
                }
                else{
                    amount = 0;
                }
                matrix.add(new SentimentStatisticsBackendEntity(i, minScore, maxScore, amount));
                minScore += 20;
                maxScore += 20;
            }
            minScore = 0;
            maxScore = 20;
        }
        return matrix;
    }
}
