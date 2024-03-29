package net.brightlizard.shop.event.model.order;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public enum OrderStatusEventModel {

    INITIALIZED,
    CREATED,
    FAILED,
    STORAGE_RESERVE_ERROR,
    STORAGE_RESERVE_SUCCESS,
    STORAGE_RESERVE_ROLLBACK,
    PAYMENT_ERROR,
    PAYMENT_SUCCESS,
    PAYMENT_ROLLBACK,
    DELIVERY_ERROR,
    DELIVERY_SUCCESS

}
