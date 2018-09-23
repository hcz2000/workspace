package com.jams.licenses.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jams.licenses.model.Organization;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private DiscoveryClient discoveryClient;
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationDiscoveryClient.class);

	public Organization getOrganization(String organizationId) {
		
        logger.debug("In Licensing Service OrganizationDiscoveryClient - getOrganization: {}",organizationId);
        
		List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

		if (instances.size() == 0) {
			return null;
		}
		String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(),
				organizationId);
		
		logger.debug("In Licensing Service OrganizationDiscoveryClient - serviceUri: {}",serviceUri);

		ResponseEntity<Organization> restExchange = restTemplate.exchange(serviceUri, HttpMethod.GET, null,
				Organization.class, organizationId);
		return restExchange.getBody();
	}

}
