package net.brightlizard.shop.core.application.billing.repository;

import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.model.CustomerAccountStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

}
