package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.payload.SpentDto;

import uz.pdp.appcommunicationcompany.service.SpentMbMinuteSmsService;

@RestController
@RequestMapping("/api/spent")
public class SpentMbMinuteSmsController {
    @Autowired
    SpentMbMinuteSmsService spentMbMinuteSmsService;

    @PostMapping
    public void mb(@RequestBody SpentDto spentDto){
        spentMbMinuteSmsService.spent(spentDto);
    }
}
