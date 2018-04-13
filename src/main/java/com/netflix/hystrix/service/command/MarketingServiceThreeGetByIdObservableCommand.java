package com.netflix.hystrix.service.command;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.entity.MarketingEntiy;

public class MarketingServiceThreeGetByIdObservableCommand extends HystrixCommand<MarketingEntiy> {
	private final Long id;
	
	public MarketingServiceThreeGetByIdObservableCommand(Setter setter, Long id) {
		super(setter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
				.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)
				.withExecutionIsolationSemaphoreMaxConcurrentRequests(10)
				.withExecutionTimeoutInMilliseconds(3000)
				.withMetricsRollingStatisticalWindowInMilliseconds(60000)
				.withMetricsRollingStatisticalWindowBuckets(10)
				.withCircuitBreakerRequestVolumeThreshold(10)
				.withCircuitBreakerErrorThresholdPercentage(10)
				.withCircuitBreakerSleepWindowInMilliseconds(30000))
			.andCommandKey(HystrixCommandKey.Factory.asKey("getAreaById"))
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
}
