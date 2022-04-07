module com.dt002g.reviewapplication.frontend {


    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;
	requires okhttp3;
	requires gson;
	requires retrofit2.converter.gson;
	requires retrofit2;
	requires javafx.graphics;
	requires java.sql;
	requires retrofit2.converter.jackson;
	requires Sentiment.Analysis;
	requires okio;


	opens com.dt002g.reviewapplication.frontend to javafx.fxml;
    exports com.dt002g.reviewapplication.frontend;
    exports com.dt002g.reviewapplication.frontend.service;

    
}
