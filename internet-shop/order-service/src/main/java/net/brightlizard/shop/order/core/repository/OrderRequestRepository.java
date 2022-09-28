package net.brightlizard.shop.order.core.repository;

import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface OrderRequestRepository /*extends Repository<String, String>*/ {

    boolean containsKey(String requestId);
    void putRequestHash(String requestId, String hash);
    String getRequestHash(String requestId);
    List<String> findAll();

}
