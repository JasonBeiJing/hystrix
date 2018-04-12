package com.jd.marketing.hystrix.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jd.marketing.hystrix.controller.namespace.MarketingApiNamespace;
import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.jd.marketing.hystrix.service.MarketingServiceOne;

@RestController
public class MarketingControllerOne {

	@Autowired
	private MarketingServiceOne marketingService;
	
	@GetMapping(value = MarketingApiNamespace.MARKETING_API + "/one/{id}")
	public MarketingEntiy get(@PathVariable Long id) throws InterruptedException, ExecutionException{
		return marketingService.getById(id);	
	}
}
