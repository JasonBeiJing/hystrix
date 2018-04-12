package com.jd.marketing.hystrix.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.netflix.hystrix.HystrixCollapser.Scope;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;

@Service
public class MarketingServiceFour {
	
	@HystrixCommand
	@CacheResult
	public MarketingEntiy getById(@CacheKey Long id) {
		System.err.println("================");
		return new MarketingEntiy(id, "XYZ");
		//throw new RuntimeException();
	}
	
	@HystrixCommand
	@CacheRemove(commandKey = "getApplicationById")
	public void removeApplicationCache(@CacheKey Long id) {}
	
	@HystrixCollapser(batchMethod = "find", collapserProperties = {
			@HystrixProperty(name = "timerDelayInMilliseconds", value = "1000"),
			@HystrixProperty(name = "maxRequestsInBatch", value = "50")
	}, scope = Scope.GLOBAL)
	public MarketingEntiy loadById(Long id) {
		return null;
	}
	
	@HystrixCommand
	public List<MarketingEntiy> find(List<Long> ids){
		ResponseEntity<List<MarketingEntiy>> response =
				new RestTemplate().exchange("http://127.0.0.1:8080/users?ids="+StringUtils.join(ids, ","),
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<MarketingEntiy>>() {
		            });
		return response.getBody();
	}
}
