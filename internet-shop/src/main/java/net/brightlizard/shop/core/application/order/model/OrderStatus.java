package net.brightlizard.shop.core.application.order.model;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public enum OrderStatus {

    CREATED,
    SEND_REQ_TO_STORAGE_SUCCESS,
    SEND_REQ_TO_STORAGE_ERROR,
    STORAGE_RESERVE_ERROR,
    STORAGE_RESERVE_SUCCESS,
    SEND_REQ_TO_PAYMENT_SUCCESS,
    SEND_REQ_TO_PAYMENT_ERROR,
    PAYMENT_ERROR,
    SEND_REQ_TO_STORAGE_ROLLBACK_SUCCESS,
    SEND_REQ_TO_STORAGE_ROLLBACK_ERROR,
    PAYMENT_SUCCESS,
    SEND_REQ_TO_DELIVERY_SUCCESS,
    SEND_REQ_TO_DELIVERY_ERROR,
    FAILED

}
