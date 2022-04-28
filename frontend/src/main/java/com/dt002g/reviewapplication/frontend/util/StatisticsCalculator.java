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

    public static double getCorrelationCoefficient(List<SentimentStatisticsBackendEntity> reviewRatingByScoreMatrix){
        if(reviewRatingByScoreMatrix == null){
            reviewRatingByScoreMatrix = createMatrix2();
        }
        int ratingScaleNumbers = 101;
        int scoreScaleNumbers = 101;
        double[] ratingPercentage;
        ratingPercentage = new double[ratingScaleNumbers];
        double[] sentimentScorePercentage;
        sentimentScorePercentage = new double[scoreScaleNumbers];

        double[][] ratingSentimentScorePercentage;
        ratingSentimentScorePercentage = new double[ratingScaleNumbers][scoreScaleNumbers];

        double[][] ratingSentimentScoreAmount;
        ratingSentimentScoreAmount = new double[ratingScaleNumbers][scoreScaleNumbers];

        int[] ratingAmount;
        ratingAmount = new int[ratingScaleNumbers];
        int[] sentimentScoreAmount;
        sentimentScoreAmount= new int[scoreScaleNumbers];

        int totalRatings = 0;

        double ratingExpectedValue = 0;

        double sentimentScoreExpectedValue =0;

        double deviationRating= 0;

        double deviationSentimentScore = 0;

        for(SentimentStatisticsBackendEntity ssbe: reviewRatingByScoreMatrix){
            System.out.println("Rating: " + ssbe.rating + " minScore: " + ssbe.minScore + " maxScore: " + ssbe.maxScore + " amount: " + ssbe.amount);
            ratingAmount[ssbe.rating] += ssbe.amount;
            sentimentScoreAmount[(int)ssbe.minScore] += ssbe.amount;
            ratingSentimentScoreAmount[ssbe.rating][(int)ssbe.minScore] += ssbe.amount;
            totalRatings += ssbe.amount;
        }

        for(int k = 0; k < ratingScaleNumbers; k++){
            ratingPercentage[k] = ((double)ratingAmount[k])/((double)totalRatings);
            ratingExpectedValue += k * ratingPercentage[k];
            sentimentScorePercentage[k] = ((double)sentimentScoreAmount[k])/ ((double)totalRatings);
            sentimentScoreExpectedValue += k * sentimentScorePercentage[k];
            for(int j = 0; j < scoreScaleNumbers; j++){
                ratingSentimentScorePercentage[k][j] = ratingSentimentScoreAmount[k][j]/ ((double)totalRatings);
            }
        }

        for(int i = 0; i < ratingScaleNumbers; i++){
            deviationRating += (i - ratingExpectedValue ) * (i - ratingExpectedValue ) * ratingPercentage[i];
        }
        for(int i = 0; i < scoreScaleNumbers; i++){
            deviationSentimentScore += (i -sentimentScoreExpectedValue) * (i -sentimentScoreExpectedValue) * sentimentScorePercentage[i];
        }

        double ratingsSentimentScorePecentageProduct = 0;

        double covarriance = 0;


        for(int i = 0; i < ratingScaleNumbers; i++){
            System.out.print("Rating: " + i);
            for(int j = 0; j < scoreScaleNumbers; j++){
                System.out.print("| Score: " + j +" | percentage:" + ratingSentimentScorePercentage[i][j]);
                ratingsSentimentScorePecentageProduct += i * j *ratingSentimentScorePercentage[i][j];
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

    private static List<SentimentStatisticsBackendEntity> createMatrix2() {
        List<SentimentStatisticsBackendEntity> matrix = new ArrayList<>();
        matrix.add(new SentimentStatisticsBackendEntity(0, 5, 5, 1));
        matrix.add(new SentimentStatisticsBackendEntity(0, 10, 10, 1));
        matrix.add(new SentimentStatisticsBackendEntity(10, 10, 10, 1));
        matrix.add(new SentimentStatisticsBackendEntity(10, 30, 30, 1));
        matrix.add(new SentimentStatisticsBackendEntity(10, 35, 35, 1));
        matrix.add(new SentimentStatisticsBackendEntity(20, 10, 10, 1));
        matrix.add(new SentimentStatisticsBackendEntity(20, 20, 20, 1));

        matrix.add(new SentimentStatisticsBackendEntity(30, 30, 30, 2));
        matrix.add(new SentimentStatisticsBackendEntity(30, 45, 45, 1));
        matrix.add(new SentimentStatisticsBackendEntity(40, 10, 10, 1));
        matrix.add(new SentimentStatisticsBackendEntity(50, 40, 40, 1));
        matrix.add(new SentimentStatisticsBackendEntity(50, 80, 80, 1));
        matrix.add(new SentimentStatisticsBackendEntity(50, 85, 85, 1));

        matrix.add(new SentimentStatisticsBackendEntity(60, 60, 60, 2));
        matrix.add(new SentimentStatisticsBackendEntity(60, 75, 75, 2));
        matrix.add(new SentimentStatisticsBackendEntity(70, 70, 70, 1));
        matrix.add(new SentimentStatisticsBackendEntity(80, 75, 75, 1));

        matrix.add(new SentimentStatisticsBackendEntity(90, 90, 90, 2));
        matrix.add(new SentimentStatisticsBackendEntity(100, 80, 80, 1));
        matrix.add(new SentimentStatisticsBackendEntity(100, 90, 90, 1));
        matrix.add(new SentimentStatisticsBackendEntity(100, 100, 100, 1));
        /*
        0, 5
        0, 10
        10, 10
        10, 30
        10, 35
        20, 10
        20, 20
        30, 30
        30, 30
        30, 45
        40, 10
        50, 40
        50, 80
        50, 85
        60, 60
        60, 60
        60, 75
        60, 75
        70, 70
        80, 75
        90, 90
        90, 90
        100, 80
        100, 90
        100, 100*/
        /*
        0
        0
        10
        10
        10
        20
        20
        30
        30
        30
        40
        50
        50
        50
        60
        60
        60
        60
        70
        80
        90
        90
        100
        100
        100
        */
        /*
        5
        10
        10
        30
        35
        10
        20
        30
        30
        45
        10
        40
        80
        85
        60
        60
        75
        75
        70
        75
        90
        90
        80
        90
        100
        */
        return matrix;
    }
}
