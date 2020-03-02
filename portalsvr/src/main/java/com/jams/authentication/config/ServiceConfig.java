package com.jams.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServiceConfig {
  @Value("${signing.key}")
  private String jwtSigningKey="";
  
  @Value("${keystore.file}")
  private String keyStoreFile="s";
  
  @Value("${keystore.password}")
  private String keyStorePassword="";
  
  @Value("${keystore.keypair}")
  private String keypair="";


  public String getJwtSigningKey() {
    return jwtSigningKey;
  }
  
  public String getKeyStoreFile() {
	    return keyStoreFile;
  }
  
  public String getKeyStorePassword() {
	    return keyStorePassword;
  }
  
  public String getKeypair() {
	    return keypair;
}

}
