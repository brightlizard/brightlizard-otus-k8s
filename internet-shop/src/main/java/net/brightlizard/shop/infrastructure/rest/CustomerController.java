package net.brightlizard.shop.infrastructure.rest;

import net.brightlizard.shop.core.application.billing.facade.BillingFacade;
import net.brightlizard.shop.core.application.billing.model.Customer;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.infrastructure.rest.model.CreatedCustomer;
import net.brightlizard.shop.infrastructure.rest.model.NewCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private BillingFacade billingFacade;

    public CustomerController(BillingFacade paymentFacade) {
        this.billingFacade = paymentFacade;
    }

    @GetMapping(
        value = "/account",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CustomerAccount>> getAccount(){
        return new ResponseEntity(billingFacade.getCustomerAccounts(), HttpStatus.OK);
    }

    @PostMapping(
        value = "/customer",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreatedCustomer> createCustomer(@Valid @RequestBody NewCustomer newCustomer){
        Customer customer = billingFacade.createCustomer(convert(newCustomer));
        return new ResponseEntity(convert(customer),HttpStatus.OK);
    }

    private Customer convert(NewCustomer newCustomer) {
        Customer customer = new Customer(newCustomer.getName());
        return customer;
    }

    private CreatedCustomer convert(Customer customer) {
        CreatedCustomer createdCustomer = new CreatedCustomer();
        createdCustomer.setId(customer.getId());
        createdCustomer.setName(customer.getName());
        createdCustomer.setStatus(customer.getStatus());
        createdCustomer.setAccount(customer.getAccount());
        return createdCustomer;
    }

}
