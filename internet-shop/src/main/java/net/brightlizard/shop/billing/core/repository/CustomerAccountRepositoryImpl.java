package net.brightlizard.shop.billing.core.repository;

import net.brightlizard.shop.billing.core.model.Customer;
import net.brightlizard.shop.billing.core.model.CustomerAccount;
import net.brightlizard.shop.billing.core.model.CustomerAccountStatus;
import net.brightlizard.shop.billing.core.model.CustomerStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class CustomerAccountRepositoryImpl implements CustomerAccountRepository {

    private JdbcTemplate jdbcTemplate;

    public CustomerAccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Customer createCustomerAndAccount(Customer customer) {

        customer.setId(UUID.randomUUID().toString());
        String SQL = "INSERT INTO internet_shop.public.customer(id, name, status) VALUES (?,?,?)";
        jdbcTemplate.update(
            SQL,
            customer.getId(),
            customer.getName(),
            CustomerStatus.ACTIVE.toString()
        );

        CustomerAccount customerAccount = new CustomerAccount(UUID.randomUUID().toString(), customer.getId(), 0.0, CustomerAccountStatus.ACTIVE);
        customer.setAccount(customerAccount);

        String SQL2 = "INSERT INTO internet_shop.public.account(id, customer_id, balance, status) VALUES (?,?,?,?)";
        jdbcTemplate.update(
                SQL2,
                customerAccount.getId(),
                customerAccount.getCustomerId(),
                customerAccount.getBalance(),
                CustomerAccountStatus.ACTIVE.toString()
        );

        Customer customer1 = jdbcTemplate.queryForObject(
                "SELECT * FROM internet_shop.public.customer WHERE id = ?",
                (rs, rowNum) -> new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    CustomerStatus.valueOf(rs.getString("status"))
                ),
                customer.getId()
        );

        CustomerAccount customerAccount1 = jdbcTemplate.queryForObject(
                "SELECT * FROM internet_shop.public.account WHERE customer_id = ?",
                (rs, rowNum) -> new CustomerAccount(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getDouble("balance"),
                        CustomerAccountStatus.valueOf(rs.getString("status"))
                ),
                customer1.getId()
        );
        customer1.setAccount(customerAccount1);

        return customer1;
    }

    @Override
    public CustomerAccount findById(String id) {
        CustomerAccount customerAccount = jdbcTemplate.queryForObject(
                "SELECT * FROM internet_shop.public.account WHERE customer_id = ?",
                (rs, rowNum) -> new CustomerAccount(
                    rs.getString("id"),
                    rs.getString("customer_id"),
                    rs.getDouble("balance"),
                    CustomerAccountStatus.valueOf(rs.getString("status"))
                ),
                id
        );
        return customerAccount;
    }

    @Override
    public List<CustomerAccount> findAll() {
        String SQL = "SELECT * FROM internet_shop.public.account";
        List<CustomerAccount> customerAccounts = jdbcTemplate.query(
                SQL, (rs, rowNum) -> new CustomerAccount(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getDouble("balance"),
                        CustomerAccountStatus.valueOf(rs.getString("status"))
                )
        );

        return customerAccounts;
    }

    @Override
    public CustomerAccount update(CustomerAccount customerAccount) {
        String SQL = "UPDATE internet_shop.public.account " +
                "SET customer_id = ?, " +
                "balance = ?, " +
                "status = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                SQL,
                customerAccount.getCustomerId(),
                customerAccount.getBalance(),
                customerAccount.getStatus().toString(),
                customerAccount.getId()
        );

        return customerAccount;
    }

    @Override
    public List<Customer> findAllCustomers() {
        String SQL = "SELECT * FROM internet_shop.public.customer";
        List<Customer> customers = jdbcTemplate.query(
            SQL, (rs, rowNum) -> new Customer(
                rs.getString("id"),
                rs.getString("name"),
                CustomerStatus.valueOf(rs.getString("status"))
            )
        );

        customers.forEach(customer -> {
            String SQL2 = "SELECT * FROM internet_shop.public.account WHERE customer_id = ?";
            CustomerAccount account = jdbcTemplate.queryForObject(
                    SQL2,
                    (rs, rowNum) -> new CustomerAccount(
                            rs.getString("id"),
                            rs.getString("customer_id"),
                            rs.getDouble("balance"),
                            CustomerAccountStatus.valueOf(rs.getString("status"))
                    ),
                    customer.getId()
            );
            customer.setAccount(account);
        });

        return customers;
    }

    @Override
    public Customer findCustomerById(String id) {
        Customer customer = jdbcTemplate.queryForObject(
            "SELECT * FROM internet_shop.public.customer WHERE id = ?",
            (rs, rowNum) -> new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    CustomerStatus.valueOf(rs.getString("status"))
            ),
            id
        );
        return customer;
    }

}