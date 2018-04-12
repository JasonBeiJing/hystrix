package com.jd.marketing.hystrix.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jd.marketing.hystrix.controller.namespace.MarketingApiNamespace;
import com.jd.marketing.hystrix.entity.MarketingEntiy;
import com.jd.marketing.hystrix.service.MarketingServiceFour;

@RestController
public class MarketingControllerFour {

	@Autowired
	private MarketingServiceFour marketingService;
	
	@GetMapping(value = MarketingApiNamespace.MARKETING_API + "/four/{id}")
	public MarketingEntiy get(@PathVariable Long id){
		
		marketingService.getById(id);
		marketingService.getById(id);
		marketingService.getById(id);
		
		marketingService.removeApplicationCache(id);
		
		marketingService.getById(id);
		marketingService.getById(id);
		marketingService.getById(id);
		
		return marketingService.getById(id);
	}
	
	@GetMapping(value = MarketingApiNamespace.MARKETING_API + "/four")
	public void preLoad() {
		marketingService.loadById(new Random().nextLong());
	}
	
}
