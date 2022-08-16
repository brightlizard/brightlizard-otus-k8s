package net.brightlizard.shop.infrastructure.rest;

import net.brightlizard.shop.core.application.storage.facade.StorageFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */

@RestController
@RequestMapping("/storage")
public class StorageController {

    private StorageFacade storageFacade;

    public StorageController(StorageFacade storageFacade) {
        this.storageFacade = storageFacade;
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity listStorage(){
        return new ResponseEntity(storageFacade.getItems(), HttpStatus.OK);
    }

}
