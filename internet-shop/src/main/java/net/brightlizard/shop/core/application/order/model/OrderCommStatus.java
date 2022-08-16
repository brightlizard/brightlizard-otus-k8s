package net.brightlizard.shop.core.application.order.model;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public enum OrderCommStatus {

    NO_COMMUNICATION,
    SEND_REQ_TO_STORAGE_SUCCESS,
    SEND_REQ_TO_STORAGE_ERROR,
    SEND_REQ_TO_PAYMENT_SUCCESS,
    SEND_REQ_TO_PAYMENT_ERROR,
    SEND_REQ_TO_STORAGE_ROLLBACK_SUCCESS,
    SEND_REQ_TO_STORAGE_ROLLBACK_ERROR,
    SEND_REQ_TO_DELIVERY_SUCCESS,
    SEND_REQ_TO_DELIVERY_ERROR

}
