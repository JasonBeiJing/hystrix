package com.netflix.hystrix.config;

import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

@Configuration
public class HystrixConfiguration {

	@PostConstruct
	public void init() {
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new CustomizedHystrixConcurrencyStrategy());
	}

	@Bean
	public HystrixCommandAspect hystrixAspect() {
		return new HystrixCommandAspect();
	}

	@Bean
	public ServletRegistrationBean hystrixMetricsStreamServlet() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new HystrixMetricsStreamServlet());
		registration.addUrlMappings("/hystrix.stream");
		return registration;
	}

	private static class CustomizedHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

		// 将需要传递到子线程的东西传进来，然后设置到子线程
		
		@Override
		public <T> Callable<T> wrapCallable(Callable<T> callable) {
			return new CustomizedCallable<T>(callable, RequestContextHolder.getRequestAttributes());
		}

		private static class CustomizedCallable<T> implements Callable<T> {

			private final Callable<T> delegate;
			private final RequestAttributes attributes;

			public CustomizedCallable(Callable<T> callable, RequestAttributes attributes) {
				this.attributes = attributes;
				this.delegate = callable;
			}

			@Override
			public T call() throws Exception {
				try {
					RequestContextHolder.setRequestAttributes(attributes);
					return delegate.call();
				} finally {
					RequestContextHolder.resetRequestAttributes();
				}
			}
		}
	}
}
