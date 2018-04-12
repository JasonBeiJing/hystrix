package com.jd.marketing.hystrix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jd.marketing.hystrix.controller.namespace.MarketingApiNamespace;
import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.jd.marketing.hystrix.entity.exception.AccessDeniedException;
import com.jd.marketing.hystrix.entity.exception.InvalidParamException;
import com.jd.marketing.hystrix.service.MarketingServiceTwo;

@RestController
public class MarketingControllerTwo {

	@Autowired
	private MarketingServiceTwo marketingService;
	
	@GetMapping(value = MarketingApiNamespace.MARKETING_API + "/two/{id}")
	public MarketingEntiy get(@PathVariable Long id) throws InvalidParamException, AccessDeniedException{
		return marketingService.getById(id);
	}
}
