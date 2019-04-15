package me.vuelma.favoritefoods.kitchen;

import me.vuelma.favoritefoods.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(KitchenController.class)
public class KitchenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private KitchenRepository kitchenRepository;

    @Test
    public void findById_NotFound() throws Exception {
        when(kitchenRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/kitchens/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(kitchenRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(kitchenRepository);
    }

    @Test
    public void findById_Found() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Italiana");
        kitchen.setId(1L);

        when(kitchenRepository.findById(1L)).thenReturn(Optional.of(kitchen));

        mockMvc.perform(get("/kitchens/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Italiana")));

        verify(kitchenRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(kitchenRepository);
    }

    @Test
    public void addKitchen_Ok() throws  Exception{
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Francesa");

        when(kitchenRepository.save(kitchen)).thenReturn(kitchen);

        mockMvc.perform(post("/kitchens")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(kitchen)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Francesa")));

        verify(kitchenRepository, times(1)).save(kitchen);
        verifyNoMoreInteractions(kitchenRepository);
    }

    @Test
    public void updateKitchen_Found() throws Exception{
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Italiana");
        kitchen.setId(1L);

        when(kitchenRepository.findById(1L)).thenReturn(Optional.of(kitchen));
        when(kitchenRepository.save(kitchen)).thenReturn(kitchen);

        mockMvc.perform(put("/kitchens/{id}", 1L)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(kitchen)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Italiana")));

        verify(kitchenRepository, times(1)).findById(1L);
        verify(kitchenRepository, times(1)).save(kitchen);
        verifyNoMoreInteractions(kitchenRepository);
    }

    @Test
    public void updateKitchen_NotFound() throws Exception{
        Kitchen kitchen = new Kitchen();
        kitchen.setName("Italiana");
        kitchen.setId(1L);

        when(kitchenRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/kitchens/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(kitchen)))
                .andExpect(status().isNotFound());

        verify(kitchenRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(kitchenRepository);
    }
    
}
