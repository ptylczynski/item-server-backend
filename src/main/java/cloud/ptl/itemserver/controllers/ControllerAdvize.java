package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.item.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.item.ObjectNotFound;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.conversion.AddressEditor;
import cloud.ptl.itemserver.persistence.conversion.FoodTypeEditor;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ControllerAdvize {
    @Autowired
    private AddressEditor addressEditor;

    @Autowired
    private FoodTypeEditor foodTypeEditor;

    @Autowired
    private BasicErrorResolverManager basicErrorResolverManager;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(AddressDAO.class, addressEditor);
        webDataBinder.registerCustomEditor(FoodTypeDAO.class, foodTypeEditor);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(ObjectInvalid ex){
        return basicErrorResolverManager.resolve(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(ObjectNotFound ex){
        return basicErrorResolverManager.resolve(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(DataIntegrityViolationException ex){
        return basicErrorResolverManager.resolve(ex);
    }
}
