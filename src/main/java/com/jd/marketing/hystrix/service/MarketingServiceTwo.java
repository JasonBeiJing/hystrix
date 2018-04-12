package com.jd.marketing.hystrix.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.jd.marketing.hystrix.entity.exception.AccessDeniedException;
import com.jd.marketing.hystrix.entity.exception.InvalidParamException;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
@DefaultProperties(
		defaultFallback = "fallback",
		threadPoolProperties = {
			@HystrixProperty(name = "coreSize", value = "10"), 
			@HystrixProperty(name = "maximumSize", value = "10"),
			@HystrixProperty(name = "maxQueueSize", value = "-1"), //default=-1, turns it off and makes us use SynchronousQueue
			@HystrixProperty(name = "queueSizeRejectionThreshold", value = "5") //即使没有达到maxQueueSize，如果达到queueSizeRejectionThreshold该值后，请求也会被拒绝。因为maxQueueSize不能被动态修改，这个参数将允许我们动态设置该值。if maxQueueSize == -1，该字段将不起作用
		}, 
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"), //每个请求超时时间
			@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"), //设置统计滚动窗口的长度,用于监控和熔断器, 10s
			@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"), // 必须能被metrics.rollingStats.timeInMilliseconds整除, 10s / 10 
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"), //在一个时间段内，请求达到requestVolumeThreshold的值，才会进行熔断与否的判断
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), //表示在一个统计窗口内有50%的请求处理失败，会触发熔断
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") //熔断多长时间后再次尝试请求
		})
public class MarketingServiceTwo {
	
	@HystrixCommand(
			fallbackMethod = "fallbackGet",
			//groupKey = "", //default => the runtime class name of annotated method
			//commandKey = "", //default => the name of annotated method
			//threadPoolKey = "", //HystrixCommand运行所在的线程池，如果该参数不设置则使用GroupKey作为ThreadPoolKey
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "60000"),
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"), 
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000")
				},
			ignoreExceptions = {InvalidParamException.class}, //抛出类型为InvalidParamException的异常，则此异常将被包装在HystrixBadRequestException中，并重新抛出，而不触发fallback函数 
			raiseHystrixExceptions = {HystrixException.RUNTIME_EXCEPTION})
	public MarketingEntiy getById(Long id) throws InvalidParamException, AccessDeniedException{
		if(id < 0) {
			throw new InvalidParamException(id, "id must be > 0");
		}else if(id==0) {
			throw new AccessDeniedException();
		}
		Map<String, Long> uriVariables = new HashMap<>();
		uriVariables.put("id", id);
		MarketingEntiy user = new RestTemplate().getForObject("http://127.0.0.1:8080/users/{id}", MarketingEntiy.class, uriVariables);
		return user;
	}
	
	/**
	 * 
	 * @param id mandatory
	 * @param e optional
	 * @return
	 */
	private MarketingEntiy fallbackGet(Long id, Throwable e) {
		e.printStackTrace();
		return new MarketingEntiy(id, "Hmmmmm....fallback for getById(...)");
	}
	
	private MarketingEntiy fallback() {
		System.err.println("==== default fallback ==== ");
        return new MarketingEntiy(-1L, "JD");
    }
}
