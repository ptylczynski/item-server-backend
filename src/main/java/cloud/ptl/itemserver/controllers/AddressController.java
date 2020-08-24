package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.validation.AddressInvalid;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto.address.FullAddressModelAssembler;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullAddressDTO;
import cloud.ptl.itemserver.persistence.projections.IdsOnly;
import cloud.ptl.itemserver.persistence.repositories.address.AddressRepository;
import cloud.ptl.itemserver.templates.ConfirmationTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    private final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private FullAddressModelAssembler fullAddressModelAssembler;

    @GetMapping("/{id}")
    FullAddressDTO getFull(@PathVariable("id") Long id) throws ObjectNotFound {
        this.logger.info("Getting addrss id: " + id.toString());
        Optional<AddressDAO> addressDAO = this.addressRepository.findById(id);
        if(addressDAO.isEmpty()) {
            this.logger.debug("Address does not exist");
            throw new ObjectNotFound(
                    id,
                    WebMvcLinkBuilder.linkTo(
                            methodOn(AddressController.class).getFull(id)
                    ).withSelfRel()
            );
        }
        return fullAddressModelAssembler
                .toModel(addressDAO.get())
                .add(
                    linkTo(AddressController.class).withSelfRel()
        );
    }

    @GetMapping("/all")
    public CollectionModel<FullAddressDTO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        this.logger.info("Getting all addresses");
        Page<AddressDAO> addressDAOPage = addressRepository.findAll(
                PageRequest.of(page, size)
        );
        return fullAddressModelAssembler
                .toCollectionModel(addressDAOPage.toList())
                .add(
                        WebMvcLinkBuilder.linkTo(
                            methodOn(AddressController.class).getAll(page, size)
                        ).withSelfRel()
                );
    }

    @GetMapping("/locators/{id}")
    public CollectionModel<IdsOnly> getLocators(
            @PathVariable Long id
    ){
        ArrayList<IdsOnly> ids = this.addressRepository.findById(id, IdsOnly.class);
        return CollectionModel.of(
                ids,
                linkTo(
                        methodOn(AddressController.class).getLocators(id)
                ).withSelfRel());
    }

    @PostMapping
    EntityModel<String> stringEntityModel(
            AddressDAO addressDAO,
            BindingResult bindingResult) throws AddressInvalid, ObjectInvalid {
        this.logger.info("Saving new address");
        this.logger.debug(addressDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("Address has errors");
            this.logger.debug(bindingResult.getAllErrors().toString());
            throw new ObjectInvalid(
                    addressDAO,
                    bindingResult,
                    WebMvcLinkBuilder.linkTo(AddressController.class).withSelfRel()
            );
        }
        this.logger.debug("Address saved");
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
        this.logger.info("Deleting address");
        this.logger.debug("id is " + id.toString());
            Optional<AddressDAO> addressDAO = this.addressRepository.findById(id);
            if(addressDAO.isEmpty()) {
                this.logger.debug("Address does not exist");
                throw new ObjectNotFound(
                        id,
                        linkTo(
                                methodOn(AddressController.class).delete(id)
                        ).withSelfRel()
                );
            }
            this.logger.debug("Address deleted");
            this.addressRepository.delete(addressDAO.get());
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.DELETE,
                AddressDAO.class.getName(),
                linkTo(
                        methodOn(AddressController.class).delete(id)
                ).withRel("controller")
        ).getEntityModel();
    }

    @PutMapping("/{id}")
    public EntityModel<String> put(
            @ModelAttribute AddressDAO addressDAO,
            BindingResult bindingResult
    ) throws ObjectInvalid, ObjectNotFound {
        this.logger.info("Updateing address");
        this.logger.debug(addressDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("Address has errors");
            this.logger.debug(bindingResult.getAllErrors().toString());
            throw new ObjectInvalid(
                    addressDAO,
                    bindingResult,
                    linkTo(AddressController.class).withSelfRel()
            );
        }
        Optional<AddressDAO> oldAddressDAO = this.addressRepository.findById(addressDAO.getId());
        if(oldAddressDAO.isEmpty()) {
            this.logger.debug("Address to update does not exist");
            throw new ObjectNotFound(
                    addressDAO.getId(),
                    linkTo(AddressController.class).withSelfRel()
            );
        }
        this.logger.debug("Address updated");
        this.addressRepository.save(addressDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                AddressDAO.class.getName(),
                linkTo(AddressController.class).withSelfRel()
        ).getEntityModel();
    }
}
