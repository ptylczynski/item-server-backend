package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.error.exception.permission.InsufficientPermission;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.BundleDAOEditor;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.FoodTypeDAOEditor;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.LocaleDAOEditor;
import cloud.ptl.itemserver.persistence.conversion.spring.editor.UserDAOEditor;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.i18n.LocaleDAO;
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
    private BundleDAOEditor bundleEditor;

    @Autowired
    private FoodTypeDAOEditor foodTypeEditor;

    @Autowired
    private UserDAOEditor userEditor;

    @Autowired
    private LocaleDAOEditor localeDAOEditor;

    @Autowired
    private BasicErrorResolverManager basicErrorResolverManager;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(BundleDAO.class, bundleEditor);
        webDataBinder.registerCustomEditor(FoodTypeDAO.class, foodTypeEditor);
        webDataBinder.registerCustomEditor(UserDAO.class, userEditor);
        webDataBinder.registerCustomEditor(LocaleDAO.class, localeDAOEditor);
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(ObjectUnformatable ex){
        return basicErrorResolverManager.resolve(ex);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(InsufficientPermission ex) { return basicErrorResolverManager.resolve(ex);}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityModel<ErrorTemplate> handle(Exception ex){ return basicErrorResolverManager.resolve(ex); }
}
