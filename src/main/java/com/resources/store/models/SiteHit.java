package com.resources.store.models;

public class SiteHit {
	
	private String id;
	private String user;
	private String country;
	private String region;
	private String language;
	
	public SiteHit() {
		// TODO Auto-generated constructor stub
	}

	public SiteHit(String id, String user, String country, String region, String language) {
		super();
		this.id = id;
		this.user = user;
		this.country = country;
		this.region = region;
		this.language = language;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	

}
