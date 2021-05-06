package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.entity.simcard.Services;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.ServiceDto;
import uz.pdp.appcommunicationcompany.service.ServiceForService;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    ServiceForService serviceForService;


    @GetMapping
    public HttpEntity<?> getAll(){
        List<Services> services = serviceForService.getAll();
        return ResponseEntity.ok(services);
    }



    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id){
        ApiResponse apiResponse = serviceForService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



    @PostMapping
    public HttpEntity<?> add(@RequestBody ServiceDto serviceDto){
        ApiResponse apiResponse = serviceForService.add(serviceDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id, @RequestBody ServiceDto serviceDto){
        ApiResponse apiResponse = serviceForService.edit(id, serviceDto);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id){
        ApiResponse apiResponse = serviceForService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }


    @GetMapping("/buy")
    public HttpEntity<?> buy(@RequestParam Integer serviceId){
        ApiResponse apiResponse = serviceForService.buy(serviceId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
