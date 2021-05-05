package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.entity.simcard.Plan;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.PlanDto;
import uz.pdp.appcommunicationcompany.service.PlanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plan")
public class PlanController {
    @Autowired
    PlanService planService;


    //GET ALL PLAN LIST
    @GetMapping
    public HttpEntity<?> getAll(){
        List<Plan> all = planService.getAll();
        return ResponseEntity.ok(all);
    }


    //GET ONE PLAN
    @GetMapping("/id")
    public HttpEntity<?> getOne(@PathVariable Integer id){
        ApiResponse apiResponse = planService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    //ADD PLAN
    @PostMapping
    public HttpEntity<?> add(@RequestBody PlanDto planDto){
        ApiResponse apiResponse = planService.add(planDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id,@RequestBody PlanDto planDto){
        ApiResponse apiResponse = planService.edit(id, planDto);
        return ResponseEntity.status(apiResponse.isSuccess()? 202 : 409).body(apiResponse);
    }



    //PLANGA ULANISH
    @GetMapping("/setPlan")
    public HttpEntity<?> setPlan(@RequestParam Integer planId){
        ApiResponse apiResponse = planService.setPlan(planId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }




}
