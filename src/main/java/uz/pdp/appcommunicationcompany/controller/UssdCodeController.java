package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.UssdCodeDto;
import uz.pdp.appcommunicationcompany.service.UssdCodeService;

import java.util.List;

@RestController
@RequestMapping("/api/ussdCode")
public class UssdCodeController {
    @Autowired
    UssdCodeService ussdCodeService;


    @GetMapping
    public HttpEntity<?> getAll(){
        List<UssdCode> ussdCodes = ussdCodeService.getAll();
        return ResponseEntity.ok(ussdCodes);
    }


    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id){
        ApiResponse apiResponse = ussdCodeService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



    @PostMapping
    public HttpEntity<?> add(@RequestBody UssdCodeDto ussdCodeDto){
        ApiResponse apiResponse = ussdCodeService.add(ussdCodeDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id, @RequestBody UssdCodeDto ussdCodeDto){
        ApiResponse apiResponse = ussdCodeService.edit(id, ussdCodeDto);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id){
        ApiResponse apiResponse = ussdCodeService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }
}
