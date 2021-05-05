package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uz.pdp.appcommunicationcompany.entity.Detalizatsiya;
import uz.pdp.appcommunicationcompany.entity.enums.ActionType;
import uz.pdp.appcommunicationcompany.entity.simcard.*;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;
import uz.pdp.appcommunicationcompany.payload.SpentDto;

import uz.pdp.appcommunicationcompany.repository.DetalizatsiyaRepository;
import uz.pdp.appcommunicationcompany.repository.PlanRepository;
import uz.pdp.appcommunicationcompany.repository.SimCardRepository;

import java.util.Set;

@Service
public class SpentMbMinuteSmsService {
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    PlanRepository planRepository;
    @Autowired
    DetalizatsiyaRepository detalizatsiyaRepository;

    public void spent(SpentDto spentDto){

        SimCard simCard = simCardRepository.getOne(spentDto.getSimCardId());

        Plan plan = simCard.getPlan();

        Detalizatsiya detalizatsiya = new Detalizatsiya();
        String packageType = spentDto.getPackageType();

        switch (packageType){
            case "SMS":
                detalizatsiya.setActionType(ActionType.SMS);
                break;
            case "MB":
                detalizatsiya.setActionType(ActionType.MB);
                break;
            case "MINUTE":
                detalizatsiya.setActionType(ActionType.MINUTE);
                break;
        }

        detalizatsiya.setSimCard(simCard);
        detalizatsiya.setAmount(spentDto.getAmount());
        detalizatsiyaRepository.save(detalizatsiya);


        Set<PackageForPlan> packageForPlans = plan.getPackageForPlans();
        if (packageForPlans != null) {
            for (PackageForPlan packageForPlan : packageForPlans) {
                if (packageForPlan.getPackageType().getType().name().equals(spentDto.getPackageType())) {
                    Double amount = packageForPlan.getAmount();
                    if (amount >= spentDto.getAmount()) {
                        packageForPlan.setAmount(amount - spentDto.getAmount());
                        planRepository.save(plan);
                        return;
                    } else {
                        packageForPlan.setAmount(0d);
                        spentDto.setAmount(spentDto.getAmount() - amount);
                    }
                    planRepository.save(plan);
                    break;
                }
            }
        }

        Set<SimCardPackage> packages = simCard.getPackages();
        for (SimCardPackage simCardPackage : packages) {
           if (simCardPackage.getAPackage().getPackageType().getType().name().equals(spentDto.getPackageType())){
               Double amount = simCardPackage.getAPackage().getAmount();
               Package aPackage = simCardPackage.getAPackage();
               if (amount >= spentDto.getAmount()){
                   aPackage.setAmount(amount - spentDto.getAmount());
                   planRepository.save(plan);
                   return;
               }else {
                   aPackage.setAmount(0d);
                   spentDto.setAmount(spentDto.getAmount() - amount);
               }
               planRepository.save(plan);
           }
        }


        Double balance = simCard.getBalance();
        Set<DefaultPrice> defaultPrices = plan.getDefaultPrices();
        for (DefaultPrice defaultPrice : defaultPrices) {
            if (defaultPrice.getPackageType().getType().name().equals(spentDto.getPackageType())) {
                Double currentPrice = defaultPrice.getPrice();
                double price = spentDto.getAmount() * currentPrice;
                simCard.setBalance(balance - price);
                simCardRepository.save(simCard);
            }
        }
    }
}
