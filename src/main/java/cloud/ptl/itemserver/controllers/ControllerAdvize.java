package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.error.exception.validation.UserAlreadyAddedToBundle;
import cloud.ptl.itemserver.error.exception.validation.UserNotAddedToBundle;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.BundleEditor;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.FoodTypeEditor;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.UserEditor;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ControllerAdvize {
    @Autowired
    private BundleEditor addressEditor;

    @Autowired
    private FoodTypeEditor foodTypeEditor;

    @Autowired
    private UserEditor userEditor;

    @Autowired
    private BasicErrorResolverManager basicErrorResolverManager;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(BundleDAO.class, addressEditor);
        webDataBinder.registerCustomEditor(FoodTypeDAO.class, foodTypeEditor);
        webDataBinder.registerCustomEditor(UserDAO.class, userEditor);

    }

    // TODO add proper response codes
    // TODO move logic to error handler from Spring MVC

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(ObjectUnformatable ex){
        return basicErrorResolverManager.resolve(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(UserAlreadyAddedToBundle ex){
        return basicErrorResolverManager.resolve(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(UserNotAddedToBundle ex){
        return basicErrorResolverManager.resolve(ex);
    }
}
