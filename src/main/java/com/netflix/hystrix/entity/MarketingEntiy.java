package com.netflix.hystrix.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MarketingEntiy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3174700384421288255L;

	private Long id;
	private String name;
	
	
	public MarketingEntiy() {
		super();
	}
	public MarketingEntiy(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
