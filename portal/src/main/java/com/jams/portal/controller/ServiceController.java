package com.jams.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jams.portal.config.ServiceConfig;

import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping(value="services")
public class ServiceController {
    @Autowired
    private ServiceConfig config;

    @RequestMapping(value="/getkey",method = RequestMethod.GET)
    public String getConfig() {
        return config.getExampleProperty();
    }
}
