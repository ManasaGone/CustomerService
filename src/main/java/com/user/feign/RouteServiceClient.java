package com.user.feign;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.user.dtos.Route;



@FeignClient("ROUTESERVICE")
public interface RouteServiceClient {
    
	@GetMapping("route/ViewRouteById/{routeId}")
	public Route getRouteById(@PathVariable int routeId);
	 @GetMapping("route/ViewAllRoutes")
		public List<Route> getAllRoutes();
}
