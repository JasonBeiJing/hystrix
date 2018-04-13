package com.jd.marketing.hystrix.service;

import org.springframework.stereotype.Service;

import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;

@Service
public class HystrixCacheService {
	
	@HystrixCommand
	@CacheResult
	public MarketingEntiy getById(@CacheKey Long id) {
		System.err.println("================");
		return new MarketingEntiy(id, "XYZ");
	}
	
	@HystrixCommand
	@CacheRemove(commandKey = "getById")
	public void removeCache(@CacheKey Long id) {}
}
