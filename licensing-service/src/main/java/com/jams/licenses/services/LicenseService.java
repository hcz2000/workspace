package com.jams.licenses.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jams.licenses.clients.OrganizationDiscoveryClient;
import com.jams.licenses.config.ServiceConfig;
import com.jams.licenses.model.License;
import com.jams.licenses.model.Organization;
import com.jams.licenses.repository.LicenseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;
    
    @Autowired
    private OrganizationDiscoveryClient organizationDiscoveryClient;

    @Autowired
    ServiceConfig config;

    public List<License> getLicensesByOrg(String organizationId){
        return licenseRepository.findByOrganizationId( organizationId );
    }
    
    @HystrixCommand(
    		fallbackMethod = "buildFallbackLicense",
    		commandProperties={@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value="3000")}
    )
    public License getLicense(String organizationId,String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = organizationDiscoveryClient.getOrganization(organizationId);
        
        if(org!=null)
        	license.withOrganizationName( org.getName())
            .withContactName( org.getContactName())
            .withContactEmail( org.getContactEmail() )
            .withContactPhone( org.getContactPhone() )
            .withComment(config.getExampleProperty());

        return license;
    }
    
    private License buildFallbackLicense(String organizationId,String licenseId){
        License fallbackLicense = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        return fallbackLicense;
    }

    public void saveLicense(License license){
        license.withId( UUID.randomUUID().toString());

        licenseRepository.save(license);

    }

    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.delete( license.getLicenseId());
    }

}