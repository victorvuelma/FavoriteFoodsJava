package me.vuelma.favoritefoods.kitchen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(KitchenController.class)
public class KitchenControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KitchenController kitchenController;

    @Test
    public void getKitchens() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Italiana");

        List<Kitchen> allKitchens = singletonList(kitchen);

        given(kitchenController.getAllKitchens()).willReturn(ResponseEntity.ok(allKitchens));

        mvc.perform(get("/kitchens")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(kitchen.getName())));
    }

    @Test
    public void getKitchenById() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Francesa");
        kitchen.setId(1321l);

        given(kitchenController.getKitchenById(kitchen.getId())).willReturn(ResponseEntity.ok(kitchen));

        mvc.perform(get("/kitchens/" + kitchen.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(kitchen.getName())));
    }

    @Test
    public void storeKitchen() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Francesa");

        mvc.perform(post("/kitchens")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(kitchen)))
                .andExpect(status().isOk());
    }

    public static String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
