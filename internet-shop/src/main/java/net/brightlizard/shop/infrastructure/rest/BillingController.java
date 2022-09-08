package net.brightlizard.shop.infrastructure.rest;

import net.brightlizard.shop.core.application.billing.facade.BillingFacade;
import net.brightlizard.shop.core.application.billing.model.Customer;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.model.DepositRequest;
import net.brightlizard.shop.core.application.billing.model.DepositResponse;
import net.brightlizard.shop.infrastructure.rest.model.CreatedCustomer;
import net.brightlizard.shop.infrastructure.rest.model.NewCustomer;
import net.brightlizard.shop.infrastructure.rest.model.NewDeposit;
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
@RequestMapping("/billing")
public class BillingController {

    private BillingFacade billingFacade;

    public BillingController(BillingFacade paymentFacade) {
        this.billingFacade = paymentFacade;
    }

    @PostMapping(
        value = "/customer",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreatedCustomer> createCustomer(@Valid @RequestBody NewCustomer newCustomer){
        Customer customer = billingFacade.createCustomer(convert(newCustomer));
        return new ResponseEntity(convert(customer),HttpStatus.OK);
    }

    @GetMapping(
        value = "/customer",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Customer>> getCustomers(){
        return new ResponseEntity(billingFacade.getCustomers(), HttpStatus.OK);
    }

    @GetMapping(
        value = "/customer/{customerId}/account",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CustomerAccount> getCustomers(@PathVariable String customerId){
        CustomerAccount customerAccount = billingFacade.getCustomerAccount(customerId);
        return new ResponseEntity(customerAccount, HttpStatus.OK);
    }

    @PutMapping(
        value = "/customer/{customerId}/account",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DepositResponse> deposit(
        @PathVariable String customerId,
        @RequestBody @Valid NewDeposit newDeposit
    ){
        DepositResponse depositResponse = billingFacade.deposit(new DepositRequest(customerId, newDeposit.getDepositValue()));
        return new ResponseEntity(depositResponse, HttpStatus.OK);
    }

    @GetMapping(
        value = "/account",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CustomerAccount>> getAccounts(){
        return new ResponseEntity(billingFacade.getCustomerAccounts(), HttpStatus.OK);
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
