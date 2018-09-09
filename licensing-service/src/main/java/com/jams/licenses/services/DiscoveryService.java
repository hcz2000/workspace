package com.jams.licenses.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;



@Service
public class DiscoveryService {
	
    @Autowired
    private DiscoveryClient discoveryClient;

    public List<String> getEurekaServices(){
       List<String> services = new ArrayList<String>();
       
       List<String> serviceNames=discoveryClient.getServices();

       for(String serviceName: serviceNames) {
    	   List<ServiceInstance> instances=discoveryClient.getInstances(serviceName);
    	   for(ServiceInstance instance:instances) {
    		   services.add( String.format("%s:%s",serviceName,instance.getUri()));
    	   }
    	   
       }
       return services;
    }
}

