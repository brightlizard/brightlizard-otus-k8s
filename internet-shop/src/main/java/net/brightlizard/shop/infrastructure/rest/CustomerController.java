package net.brightlizard.shop.infrastructure.rest;

import net.brightlizard.shop.core.application.payment.facade.PaymentFacade;
import net.brightlizard.shop.core.application.payment.model.CustomerAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private PaymentFacade paymentFacade;

    public CustomerController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @GetMapping(
        value = "/account",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CustomerAccount>> getAccount(){
        return new ResponseEntity(paymentFacade.getCustomerAccounts(), HttpStatus.OK);
    }

}
