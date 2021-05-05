package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.entity.payment.Payment;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.PaymentDto;
import uz.pdp.appcommunicationcompany.service.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;


    @GetMapping
    public HttpEntity<?> getAll(){
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }



    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id){
        ApiResponse apiResponse = paymentService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



    @PostMapping
    public HttpEntity<?> add(@RequestBody PaymentDto paymentDto){
        ApiResponse apiResponse = paymentService.add(paymentDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }



    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody PaymentDto paymentDto){
        ApiResponse apiResponse = paymentService.edit(id, paymentDto);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }
}
