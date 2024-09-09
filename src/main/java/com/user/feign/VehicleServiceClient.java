package com.user.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.user.dtos.Vehicle;


@FeignClient("VEHICLESERVICE")
public interface VehicleServiceClient {

	@GetMapping("vehicles/ViewVehicleById/{vehicleNo}")
    public Vehicle getVehicleByNo(@PathVariable String vehicleNo);

    @GetMapping("vehicles/name/{vehicleName}")
    Vehicle getVehicleByName(@PathVariable("vehicleName") String vehicleName);
    
    @GetMapping("vehicles/ViewAllVehicles")
    public List<Vehicle> getAllVehicles();

}
