package com.netflix.hystrix.service.command;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.entity.MarketingEntiy;

public class MarketingServiceOneGetByIdCommand extends HystrixCommand<MarketingEntiy> {

	private final Long id;
	
	public MarketingServiceOneGetByIdCommand(Long id) {
		super(Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MarketingServiceOne"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("getById"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("MarketingServiceOne-getById-HystrixThreadPoolKey"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
					.withExecutionTimeoutInMilliseconds(3000)
					.withMetricsRollingStatisticalWindowInMilliseconds(60000)
					.withMetricsRollingStatisticalWindowBuckets(10)
					.withCircuitBreakerRequestVolumeThreshold(10)
					.withCircuitBreakerErrorThresholdPercentage(10)
					.withCircuitBreakerSleepWindowInMilliseconds(30000))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
					.withCoreSize(10)
					.withMaximumSize(10)
					.withMaxQueueSize(-1))
			);
		this.id = id;
	}

	@Override
	protected MarketingEntiy run() throws Exception {
		Map<String, Long> uriVariables = new HashMap<>();
		uriVariables.put("id", id);
		return new RestTemplate().getForObject("http://127.0.0.1:8080/users/{id}", MarketingEntiy.class, uriVariables);
	}
	
	@Override
	protected MarketingEntiy getFallback() {
		return new MarketingEntiy(id, "anonymous");
	}

	/**
     * Key to be used for request caching.
     * By default this returns null which means "do not cache".
     * To enable caching override this method and return a string key uniquely representing the state of a command instance.
     * If multiple command instances in the same request scope match keys then only the first will be executed and all others returned from cache.
     */
//	@Override
//	protected String getCacheKey() {
//		return id.toString();
//	}
}
