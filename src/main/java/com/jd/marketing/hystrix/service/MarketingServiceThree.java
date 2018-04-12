package com.jd.marketing.hystrix.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.jd.marketing.hystrix.service.command.MarketingServiceThreeGetByIdObservableCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MarketingServiceThree {

//	@HystrixCommand(commandProperties= {
//			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//	})
	public MarketingEntiy getById(Long id) throws InterruptedException, ExecutionException{
		com.netflix.hystrix.HystrixCommand<MarketingEntiy> command = new MarketingServiceThreeGetByIdObservableCommand(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.getClass().getSimpleName())), id);
		System.err.println("X=====>"+System.currentTimeMillis());
		Future<MarketingEntiy> f = command.queue();
		System.out.println("Y=====>"+System.currentTimeMillis());
		MarketingEntiy entity = f.get();
		System.out.println("Z=====>"+System.currentTimeMillis());
		return entity;

//		Map<String, Long> uriVariables = new HashMap<>();
//		uriVariables.put("id", id);
//		MarketingEntiy entity = new RestTemplate().getForObject("http://127.0.0.1:8080/users/{id}", MarketingEntiy.class, uriVariables);
//		return entity;
	}
}
