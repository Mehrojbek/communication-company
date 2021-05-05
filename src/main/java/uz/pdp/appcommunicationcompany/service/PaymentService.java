package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.Detalizatsiya;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.enums.ActionType;
import uz.pdp.appcommunicationcompany.entity.payment.Payment;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.PaymentDto;
import uz.pdp.appcommunicationcompany.repository.DetalizatsiyaRepository;
import uz.pdp.appcommunicationcompany.repository.PaymentRepository;
import uz.pdp.appcommunicationcompany.repository.SimCardRepository;

import java.util.*;

@Service
public class PaymentService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    DetalizatsiyaRepository detalizatsiyaRepository;



    public List<Payment> getAll(){
        Employee branchDirectorOrDirector = usefulMethods.getBranchDirectorOrDirector();
        if (branchDirectorOrDirector != null){
            return paymentRepository.findAll();
        }
        return null;
    }



    public ApiResponse getOne(UUID id){
        Employee branchDirectorOrDirector = usefulMethods.getBranchDirectorOrDirector();
        if (branchDirectorOrDirector != null){
            Optional<Payment> optionalPayment = paymentRepository.findById(id);
            if (!optionalPayment.isPresent())
                return new ApiResponse("to'lov topilmadi",false);
            return new ApiResponse("OK",true,optionalPayment.get());
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse add(PaymentDto paymentDto){
        Employee paymentUser = usefulMethods.getPaymentUser();
        if (paymentUser != null){
            Payment payment = new Payment();
            payment.setAmount(paymentDto.getAmount());

            Optional<SimCard> optionalSimCard = simCardRepository.findById(paymentDto.getSimCardId());
            if (!optionalSimCard.isPresent())
                return new ApiResponse("simkarta topilmadi",false);
            SimCard simCard = optionalSimCard.get();

            if (simCard.isAccountNonExpired() && simCard.isAccountNonLocked() && simCard.isCredentialsNonExpired() && simCard.isEnabled()) {

                Detalizatsiya detalizatsiya = new Detalizatsiya();
                detalizatsiya.setSimCard(simCard);
                detalizatsiya.setActionType(ActionType.PAYMENT);
                detalizatsiya.setDoPayment(payment);
                detalizatsiyaRepository.save(detalizatsiya);

                simCard.setBalance(simCard.getBalance() + paymentDto.getAmount());
                simCardRepository.save(simCard);
                payment.setSimCard(simCard);
                paymentRepository.save(payment);
                return new ApiResponse("To'lov mufavvaqiyatli amalga oshirildi", true);
            }
            return new ApiResponse("sim karta bloklangan bo'lishi mumkin",false);
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse edit(UUID id, PaymentDto paymentDto){
        Employee numberManagerOrDirector = usefulMethods.getNumberManagerOrDirector();
        if (numberManagerOrDirector != null){
            Optional<Payment> optionalPayment = paymentRepository.findById(id);
            if (!optionalPayment.isPresent())
                return new ApiResponse("to'lov topilmadi",false);
            Payment editingPayment = optionalPayment.get();

            Optional<SimCard> optionalSimCard = simCardRepository.findById(paymentDto.getSimCardId());
            if (!optionalSimCard.isPresent())
                return new ApiResponse("simkarta topilmadi",false);
            SimCard simCard = optionalSimCard.get();
            if (simCard.isAccountNonExpired() && simCard.isAccountNonLocked() && simCard.isCredentialsNonExpired() && simCard.isEnabled()) {

                editingPayment.setSimCard(simCard);
                editingPayment.setAmount(paymentDto.getAmount());

                paymentRepository.save(editingPayment);
                return new ApiResponse("to'lov tahrirlandi", true);
            }
            return new ApiResponse("sim karta bloklangan bo'lishi mumkin",false);
        }
        return new ApiResponse("Xatolik",false);
    }





}
