package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.SimcardDto;
import uz.pdp.appcommunicationcompany.service.SimCardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/simCard")
public class SimCardController {
    @Autowired
    SimCardService simCardService;


    @GetMapping
    public HttpEntity<?> getAll(){
        List<SimCard> simCards = simCardService.getAll();
        return ResponseEntity.ok(simCards);
    }


    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id){
        ApiResponse apiResponse = simCardService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    @PostMapping
    public HttpEntity<?> add(@RequestBody SimcardDto simcardDto){
        ApiResponse apiResponse = simCardService.add(simcardDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }



    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody SimcardDto simcardDto){
        ApiResponse apiResponse = simCardService.edit(id, simcardDto);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id){
        ApiResponse apiResponse = simCardService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }


}
