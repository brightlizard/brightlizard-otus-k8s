package net.brightlizard.shop.core.application.order.repository;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class OrderRequestRepositoryHashMapImpl implements OrderRequestRepository {

    private HashMap<String, String> orderRequest = new HashMap<>();

    @Override
    public boolean containsKey(String requestId) {
        return orderRequest.containsKey(requestId);
    }

    @Override
    public void putRequestHash(String requestId, String hash) {
        orderRequest.put(requestId, hash);
    }

    @Override
    public String getRequestHash(String requestId) {
        return orderRequest.get(requestId);
    }

    @Override
    public List<String> findAll() {
        return orderRequest.keySet().stream().collect(Collectors.toList());
    }

}
