package com.jd.marketing.hystrix.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.jd.marketing.hystrix.service.command.MarketingServiceThreeGetByIdObservableCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class SemaphoreCommandOrAnnotationService {

	public MarketingEntiy getByIdWithCommand(Long id) throws InterruptedException, ExecutionException {
		com.netflix.hystrix.HystrixCommand<MarketingEntiy> command = new MarketingServiceThreeGetByIdObservableCommand(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.getClass().getSimpleName())), id);
		System.err.println("X=====>" + System.currentTimeMillis());
		Future<MarketingEntiy> f = command.queue();
		System.out.println("Y=====>" + System.currentTimeMillis());
		MarketingEntiy entity = f.get();
		System.out.println("Z=====>" + System.currentTimeMillis());
		return entity;
	}

	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")})
	public MarketingEntiy getByIdWithAnnotaion(Long id) throws InterruptedException, ExecutionException {
		Map<String, Long> uriVariables = new HashMap<>();
		uriVariables.put("id", id);
		MarketingEntiy entity = new RestTemplate().getForObject("http://127.0.0.1:8080/users/{id}", MarketingEntiy.class, uriVariables);
		return entity;
	}

}
