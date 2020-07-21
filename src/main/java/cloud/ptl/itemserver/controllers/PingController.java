package cloud.ptl.itemserver.controllers;

import lombok.Data;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
public class PingController {
    @ResponseBody
    @GetMapping("/ping")
    EntityModel<PingResponse> ping(){
        return EntityModel.of(new PingResponse(), linkTo(PingController.class).withRel("test"));
    }

    @Data
    private class PingResponse{
        private String text;
        private LocalDate date;

        public PingResponse(){
            this.text = "Pong";
            this.date = LocalDate.of(2020,10,21);
        }
    }
}
