package cloud.ptl.itemserver.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Value("${server.version}")
    private String version;

    @GetMapping("/")
    public EntityModel<Object> get(){
        return EntityModel.of("hello",
                WebMvcLinkBuilder.linkTo(MainController.class).withSelfRel()
        );
    }

    @GetMapping("/version")
    public EntityModel<String> getVersion(){
        return EntityModel.of(
            this.version,
                WebMvcLinkBuilder.linkTo(MainController.class).withSelfRel()
        );
    }
}
