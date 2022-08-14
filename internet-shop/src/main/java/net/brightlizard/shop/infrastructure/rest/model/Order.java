package net.brightlizard.shop.infrastructure.rest.model;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class Order {

    private String id;

    @NotNull
    private String consumer;

    @NotNull
    private List<String> itemsIds;




}
