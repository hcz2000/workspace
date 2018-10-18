package com.jams.portal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{

  @Value("${signing.key}")
  private String exampleProperty;

  public String getExampleProperty(){
    return exampleProperty;
  }
}