package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(password = "test", username = "test")
public class AddressTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BundleRepository addressRepository;

    @Test
    // get address does not exit
    void getOneAddress_and4xx() throws Exception {
        this.mockMvc.perform(
                get("/address/{id}", 1L)
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    // get address does exist
    void getOneAddress_and2xx() throws Exception {
        this.mockMvc.perform(
                get("/address/{id}", 2L)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void putAddress_andCheckExistence() throws Exception {

        this.mockMvc.perform(
                post("/address/")
                    .param("building", "21")
                    .param("city", "Zbąszynek")
                    .param("country", "Polska")
                    .param("home", "1231")
                    .param("street", "Wolności")
                    .param("zip", "21-413")
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(
                get("/address/all")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andDo(print())
                .andExpect(content().string(containsString("Zbąszynek")));
    }

}
