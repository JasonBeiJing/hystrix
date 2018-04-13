package com.netflix.hystrix.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCollapser.Scope;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.entity.MarketingEntiy;

@Service
public class HystrixCollapserService {
	
	@HystrixCollapser(batchMethod = "find", collapserProperties = {
			@HystrixProperty(name = "timerDelayInMilliseconds", value = "10000"),
			@HystrixProperty(name = "maxRequestsInBatch", value = "50")
	}, scope = Scope.GLOBAL)
	public MarketingEntiy getByIdWithAnnotation(Long id) {
		return null;
	}
	
	@HystrixCommand
	public List<MarketingEntiy> find(List<Long> ids){
		ResponseEntity<List<MarketingEntiy>> response =
				new RestTemplate().exchange("http://127.0.0.1:8080/users?ids="+StringUtils.join(ids, ","),
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<MarketingEntiy>>() {
		            });
		List<MarketingEntiy> xx = response.getBody();
		System.err.println("---- size ---- " + StringUtils.join(ids, ","));
		for(MarketingEntiy x:xx) {
			System.err.println(x.getId() + " ===> " + x.getName());
		}
		return xx;
	}
}
