package net.brightlizard.shop.order.infrastructure.repository;

import net.brightlizard.shop.order.core.model.OrderRequest;
import net.brightlizard.shop.order.core.repository.OrderRequestRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class OrderRequestRepositoryImpl implements OrderRequestRepository {

    private JdbcTemplate jdbcTemplate;

    public OrderRequestRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean containsKey(String requestId) {
        Integer foundedRequests = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM internet_shop.public.order_request WHERE requestid = ?",
            Integer.class,
            requestId
        );
        return foundedRequests > 0;
    }

    @Override
    public void putRequestHash(String requestId, String hash) {
        jdbcTemplate.update(
            "INSERT INTO internet_shop.public.order_request (requestid,orderhash) VALUES (?,?)",
            requestId, hash
        );
    }

    @Override
    public String getRequestHash(String requestId) {
        String SQL = "SELECT orderhash FROM internet_shop.public.order_request WHERE requestid = ?";
        String hash = jdbcTemplate.queryForObject(SQL, String.class, requestId);
        return hash;
    }

    @Override
    public List<String> findAll() {
        String SQL = "SELECT * FROM internet_shop.public.order_request";
        List<OrderRequest> orderRequests = jdbcTemplate.query(SQL, (rs, rowNum) -> new OrderRequest(rs.getString("requestid"), rs.getString("orderhash")));
        return orderRequests.stream().map(OrderRequest::getRequestId).collect(Collectors.toList());
    }

}
