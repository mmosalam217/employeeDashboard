package com.resources.Filters;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.resources.store.Services.HitsService;
import com.resources.store.models.SiteHit;

@WebFilter("/*")
public class HitsFilter implements Filter{
	private Locale locale;
	private String language;
	private String region;
	private String country;
	private String userID = null;
	private ServletContext context;
	
	public HitsFilter() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void init(FilterConfig filterConfig) {
		this.context = filterConfig.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		
		 locale = req.getLocale();
		 language = locale.getDisplayLanguage();
		 region = locale.getCountry();
		 country = locale.getDisplayCountry();
		if(session != null) {
			userID = (String) session.getAttribute("userID");
		}
		try {
			HitsService hitsService = new HitsService();
			SiteHit hit = new SiteHit();
			hit.setId(UUID.randomUUID().toString());
			hit.setUser(userID);
			hit.setLanguage(language);
			hit.setRegion(region);
			hit.setCountry(country);
			
			Boolean isAdded = hitsService.addHit(hit);
			if(!isAdded) {
				throw new IOException("ModelISSUE");
			}
		}catch(Exception e){
			this.context.log(e.getLocalizedMessage());

			e.printStackTrace();
		}
	
		chain.doFilter(request, response);
		
		
	}
	
	public void destroy() {

			

	}

}
