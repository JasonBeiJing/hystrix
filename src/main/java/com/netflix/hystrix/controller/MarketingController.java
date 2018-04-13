package com.netflix.hystrix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.entity.MarketingEntiy;
import com.netflix.hystrix.service.HystrixCacheService;
import com.netflix.hystrix.service.HystrixCollapserService;
import com.netflix.hystrix.service.SemaphoreCommandOrAnnotationService;
import com.netflix.hystrix.service.ThreadAnnotationService;
import com.netflix.hystrix.service.ThreadCommandService;

@RestController
@RequestMapping("/hystrix")
public class MarketingController {

	@Autowired
	private ThreadCommandService threadCommandService;
	@Autowired
	private ThreadAnnotationService threadAnnotationService;
	@Autowired
	private SemaphoreCommandOrAnnotationService semaphoreCommandOrAnnotationService;
	@Autowired
	private HystrixCacheService hystrixCacheService;
	@Autowired
	private HystrixCollapserService hystrixCollapserService;
	
	//*****************************************************//
	
	@GetMapping("/thread/command")
	public MarketingEntiy beijing(@RequestParam(required=false, defaultValue="111") Long id) throws Exception{
		return threadCommandService.getByIdWithCommand(id);	
	}
	
	@GetMapping("/thread/annotation")
	public MarketingEntiy shanghai(@RequestParam(required=false, defaultValue="222") Long id) throws Exception{
		return threadAnnotationService.getByIdWithAnnotation(id);
	}
	
	//*****************************************************//
	
	@GetMapping("/semaphore/command")
	public MarketingEntiy guangzhou(@RequestParam(required=false, defaultValue="333") Long id) throws Exception{
		return semaphoreCommandOrAnnotationService.getByIdWithCommand(id);
	}
	
	@GetMapping("/semaphore/annotation")
	public MarketingEntiy shenzhen(@RequestParam(required=false, defaultValue="444") Long id) throws Exception{
		return semaphoreCommandOrAnnotationService.getByIdWithAnnotaion(id);
	}
	
	//*****************************************************//
	
	@GetMapping("/cache/annotation")
	public MarketingEntiy tianjin(@RequestParam(required=false, defaultValue="555") Long id){
		
		hystrixCacheService.getById(id);
		hystrixCacheService.getById(id);
		hystrixCacheService.getById(id);
		
		hystrixCacheService.removeCache(id);
		
		hystrixCacheService.getById(id);
		hystrixCacheService.getById(id);
		hystrixCacheService.getById(id);
		
		return hystrixCacheService.getById(id);
	}
	
	//*****************************************************//
	
	@GetMapping("/collapser")
	public void chongqing(@RequestParam Long id) {
		hystrixCollapserService.loadById(id);
	}
}
