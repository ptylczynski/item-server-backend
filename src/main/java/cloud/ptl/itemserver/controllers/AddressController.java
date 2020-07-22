package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.address.AddressInvalid;
import cloud.ptl.itemserver.error.exception.item.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.item.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.repositories.address.AddressRepository;
import cloud.ptl.itemserver.templates.ConfirmationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/{id}")
    EntityModel<AddressDAO> getFull(@PathVariable("id") Long id) throws ObjectNotFound {
        Optional<AddressDAO> addressDAO = this.addressRepository.findById(id);
        if(addressDAO.isEmpty())
            throw new ObjectNotFound(
                    id,
                    WebMvcLinkBuilder.linkTo(
                            methodOn(AddressController.class).getFull(id)
                    ).withSelfRel()
            );
        return EntityModel.of(addressDAO.get(),
                linkTo(
                        methodOn(AddressController.class).getFull(1L)
                ).withSelfRel());
    }

    @GetMapping("/all")
    public CollectionModel<AddressDAO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Page<AddressDAO> addressDAOPage = addressRepository.findAll(
                PageRequest.of(page, size)
        );
        return CollectionModel.of(
                addressDAOPage.getContent(),
                WebMvcLinkBuilder.linkTo(
                        methodOn(AddressController.class).getAll(page, size)
                ).withSelfRel()
        );
    }

    @PostMapping
    EntityModel<String> stringEntityModel(
            AddressDAO addressDAO,
            BindingResult bindingResult) throws AddressInvalid, ObjectInvalid {
        if(bindingResult.hasErrors())
            throw new ObjectInvalid(
                    addressDAO,
                    bindingResult,
                    WebMvcLinkBuilder.linkTo(AddressController.class).withSelfRel()
            );
        addressRepository.save(addressDAO);
        return new ConfirmationTemplate(
            ConfirmationTemplate.Token.ADD,
                AddressDAO.class.getName(),
                linkTo(AddressController.class).withRel("controller")
        ).getEntityModel();
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> delete(
            @PathVariable Long id
    ) throws ObjectNotFound {
            Optional<AddressDAO> addressDAO = this.addressRepository.findById(id);
            if(addressDAO.isEmpty())
                throw new ObjectNotFound(
                        id,
                        linkTo(
                                methodOn(AddressController.class).delete(id)
                        ).withSelfRel()
                );
            this.addressRepository.delete(addressDAO.get());
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.DELETE,
                AddressDAO.class.getName(),
                linkTo(
                        methodOn(AddressController.class).delete(id)
                ).withRel("controller")
        ).getEntityModel();
    }
}
