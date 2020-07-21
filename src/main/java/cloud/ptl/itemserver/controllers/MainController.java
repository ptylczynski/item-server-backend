package cloud.ptl.itemserver.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;

@RestController
public class MainController {

    @GetMapping("/")
    public EntityModel<Object> get(){
        return EntityModel.of("hello",
                WebMvcLinkBuilder.linkTo(MainController.class).withSelfRel()
        );
    }
}
