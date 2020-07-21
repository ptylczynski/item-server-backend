package cloud.ptl.itemserver.controllers;


import cloud.ptl.itemserver.error.exception.item.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.item.ObjectNotFound;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/type")
public class ItemTypeController {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private BasicErrorResolverManager basicErrorResolverManager;

    @GetMapping("/food/{id}")
    public EntityModel<FoodTypeDAO> foodTypeDAOEntityModel(
            @PathVariable Long id) throws ObjectNotFound {
        Optional<FoodTypeDAO> foodTypeDAO = foodTypeRepository.findById(id);
        if(foodTypeDAO.isEmpty())
            throw new ObjectNotFound(
                    id,
                    linkTo(
                            methodOn(ItemTypeController.class).foodTypeDAOEntityModel(id)
                    ).withSelfRel()
            );
        return EntityModel.of(foodTypeDAO.get(),
                linkTo(methodOn(ItemTypeController.class).foodTypeDAOEntityModel(id)).withSelfRel());
    }

    @GetMapping("/food/all")
    public CollectionModel<FoodTypeDAO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Page<FoodTypeDAO> foodTypeDAOS = foodTypeRepository.findAll(
                PageRequest.of(page, size)
        );
        return CollectionModel.of(
                foodTypeDAOS.getContent(),
                linkTo(
                        methodOn(ItemTypeController.class).getAll(page, size)
                ).withSelfRel()
        );
    }

    @PostMapping("food")
    public EntityModel<String> stringEntityModel(
            FoodTypeDAO foodTypeDAO,
            BindingResult bindingResult) throws ObjectInvalid {

        if(bindingResult.hasErrors())
            throw new ObjectInvalid(
                    foodTypeDAO,
                    bindingResult,
                    linkTo(ItemTypeController.class).withSelfRel()
            );
        this.foodTypeRepository.save(foodTypeDAO);

        return EntityModel.of("Food type added",
                linkTo(ItemTypeController.class).withRel("controller"));

    }
}
