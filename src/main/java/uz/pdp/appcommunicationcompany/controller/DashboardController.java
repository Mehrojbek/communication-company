package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.GroupByPlan;
import uz.pdp.appcommunicationcompany.repository.SimCardRepository;
import uz.pdp.appcommunicationcompany.service.SimCardService;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    SimCardService simCardService;

    @GetMapping("/soldSimCardsForBranchDirector")
    public HttpEntity<?> getSimCardsForBranchDirector(){
        List<SimCard> simCardsForBranchDirector = simCardService.getAllSimCardsForBranchDirector();
        return ResponseEntity.ok(simCardsForBranchDirector);
    }



    @GetMapping("/simCardsForDirector")
    public HttpEntity<?> getSimCardsForDirector(){
        List<SimCard> all = simCardService.getAll();
        return ResponseEntity.ok(all);
    }


    @GetMapping("/simCardsForDirectorPerQuarter")
    public HttpEntity<?> getAllSimCardsForDirectorPerQuarter(@RequestParam Integer quarterNumber){
        List<SimCard> simCards = simCardService.getAllPerQuarter(quarterNumber);
        return ResponseEntity.ok(simCards);
    }


    @GetMapping("/simCardsForDirectorPerMonth")
    public HttpEntity<?> getSimCardsForDirectorPerMonth(@RequestParam Integer monthNumber){
        List<SimCard> allPerMonth = simCardService.getAllPerMonth(monthNumber);
        return ResponseEntity.ok(allPerMonth);
    }


    @GetMapping("/simCardsForDirectorPerDay")
    public HttpEntity<?> getSimCardsForDirectorPerDay(@RequestParam Integer numberDayMonth){
        List<SimCard> allPerDay = simCardService.getAllPerDay(numberDayMonth);
        return ResponseEntity.ok(allPerDay);
    }


    @GetMapping("/simCardsGroupByPlanOrderDesc")
    public HttpEntity<?> getSimCardsGroupByPlanOrderDesc(){
        List<GroupByPlan> groupByPlans = simCardService.getAllGroupByPlanOrderDesc();
        return ResponseEntity.ok(groupByPlans);
    }
}
