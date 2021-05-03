package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.PlanDto;
import uz.pdp.appcommunicationcompany.service.PlanService;

@RestController
@RequestMapping("/api/plan")
public class PlanController {
    @Autowired
    PlanService planService;


    //GET ALL PLAN LIST
    @GetMapping
    public HttpEntity<?> getAll(@RequestParam boolean physicalPerson){
        ApiResponse apiResponse = planService.getAll(physicalPerson);
        return ResponseEntity.ok(apiResponse);
    }


    //GET ONE PLAN
    @GetMapping("/id")
    public HttpEntity<?> getOne(@PathVariable Integer id){
        ApiResponse apiResponse = planService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    //ADD PLAN
    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody PlanDto planDto){
        ApiResponse apiResponse = planService.add(planDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }




}
