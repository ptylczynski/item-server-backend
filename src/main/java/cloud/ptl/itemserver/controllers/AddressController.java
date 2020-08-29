package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.validation.AddressInvalid;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto.address.FullAddressModelAssembler;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullAddressDTO;
import cloud.ptl.itemserver.persistence.helper.AddressService;
import cloud.ptl.itemserver.persistence.helper.UserService;
import cloud.ptl.itemserver.persistence.projections.IdsOnly;
import cloud.ptl.itemserver.persistence.repositories.address.AddressRepository;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
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
import java.util.HashSet;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO move logic outside controller to helper class

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private FullAddressModelAssembler fullAddressModelAssembler;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    FullAddressDTO getFull(@PathVariable("id") Long id) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting addrss id: " + id.toString());
        Optional<AddressDAO> addressDAO = this.addressRepository.findById(id);
        this.addressService.checkIfAddressExists(id);
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
        this.logger.info("-----------");
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
    public CollectionModel<Long> getLocators(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Fetching locators of id: " + id);
        Optional<AddressDAO> addressDAO = this.addressRepository.findById(id, AddressDAO.class);
        this.addressService.checkIfAddressExists(id);
        this.logger.debug(addressDAO.get().toString());
        ArrayList<Long> ids = new ArrayList<>();
        for(UserDAO user : addressDAO.get().getLocators()){
            ids.add(user.getId());
        }
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
        this.logger.info("-----------");
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
        this.logger.info("-----------");
        this.logger.info("Deleting address");
        this.logger.debug("id is " + id.toString());
            Optional<AddressDAO> addressDAO = this.addressRepository.findById(id);
            this.addressService.checkIfAddressExists(id);
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
        this.logger.info("-----------");
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
        this.addressService.checkIfAddressExists(addressDAO.getId());
        this.logger.debug("Address updated");
        this.addressRepository.save(addressDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                AddressDAO.class.getName(),
                linkTo(AddressController.class).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/check-in/{id}")
    public EntityModel<String> checkIn(
            @PathVariable("id") Long addressId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Making check in");
        this.logger.debug("addressID=" + addressId + " userID=" + userId);
        this.addressService.checkIfAddressExists(addressId);
        this.userService.checkIfUserExist(userId);
        Optional<AddressDAO> addressDAO = this.addressRepository.findById(addressId, AddressDAO.class);
        Optional<UserDAO> userDAO = this.userRepository.findById(userId, UserDAO.class);
        userDAO.get().setLocatorOf(addressDAO.get());
        this.addressRepository.save(addressDAO.get());
        this.logger.debug("User checked in");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserController.class.toString(),
                linkTo(
                        methodOn(AddressController.class).checkIn(addressId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/check-out/{id}")
    public EntityModel<String> checkOut(
            @PathVariable("id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("checking out user");
        this.logger.debug("userId=" + userId);
        this.userService.checkIfUserExist(userId);
        Optional<UserDAO> userDAO = this.userRepository.findById(userId, UserDAO.class);
        userDAO.get().setLocatorOf(null);
        this.userRepository.save(userDAO.get());
        this.logger.debug("User check out");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserController.class.toString(),
                linkTo(
                        methodOn(AddressController.class).checkOut(userId)
                ).withSelfRel()
        ).getEntityModel();
    }
}
