package com.utn.mobile.myapplication.service;

import org.springframework.http.converter.StringHttpMessageConverter;



public class SesionService extends AbstractService {

    //Singleton
    private static final SesionService INSTANCE = new SesionService();
    public static SesionService get() {
        return INSTANCE;
    }

    private SesionService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    @Override
    protected Object deserialize(String json) {
        return null;
    }
}
