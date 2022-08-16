package net.brightlizard.shop.core.application.order.model;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public enum OrderStatus {

    INITIALIZED,
    CREATED,
    FAILED,
    STORAGE_RESERVE_ERROR,
    STORAGE_RESERVE_SUCCESS,
    PAYMENT_ERROR,
    PAYMENT_SUCCESS,
    DELIVERY_ERROR,
    DELIVERY_SUCCESS

}
