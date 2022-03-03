package com.dt002g.reviewapplication.frontend.service;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class ServiceBuilder {
	
	private static final String URL = "http://localhost:8080/api/v1/reviews/";
	
	private static ServiceBuilder instance;
	
	private ServiceBuilder() {
		
	}
	
	public static ServiceBuilder getInstance() {
		if(instance == null) {
			instance = new ServiceBuilder();
		}
		return instance;
	}
	
	private static OkHttpClient.Builder okHttp = new OkHttpClient.Builder();
	
	private static Gson gson = new GsonBuilder()
			.create();
	
	private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttp.build());
	//private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(URL).addConverterFactory(JacksonConverterFactory.create()).client(okHttp.build());

	private static Retrofit retrofit = builder.build();
	
	public static <S> S buildService(Class<S> serviceType) {
		return retrofit.create(serviceType);
	}
	

}
