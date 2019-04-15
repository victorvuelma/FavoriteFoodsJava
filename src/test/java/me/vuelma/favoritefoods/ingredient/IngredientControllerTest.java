package me.vuelma.favoritefoods.ingredient;

import me.vuelma.favoritefoods.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(IngredientController.class)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private IngredientRepository ingredientRepository;

    @Test
    public void getAllIngredients() throws Exception {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Macarrão");
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Carne");
        ingredient2.setId(2L);

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientRepository.findAll()).thenReturn(ingredients);

        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Macarrão")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Carne")));

        verify(ingredientRepository, times(1)).findAll();
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void getIngredientById_Ok() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Carne");
        ingredient.setId(1L);

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        mockMvc.perform(get("/ingredients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Carne")));

        verify(ingredientRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void getIngredientById_NotFound() throws Exception {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ingredients/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(ingredientRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void createIngredient() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Carne");
        ingredient.setId(1L);

        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);

        mockMvc.perform(post("/ingredients/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredient)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Carne")));

        verify(ingredientRepository, times(1)).save(ingredient);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void updateIngredient_Ok() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Carne");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        Ingredient updatedIngredient = new Ingredient();
        updatedIngredient.setId(1L);
        updatedIngredient.setName("Macarrão");
        when(ingredientRepository.save(updatedIngredient)).thenReturn(updatedIngredient);

        mockMvc.perform(put("/ingredients/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedIngredient)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Macarrão")));

        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).save(updatedIngredient);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void updateIngredient_NotFound() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Carne");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/ingredients/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredient)))
                .andExpect(status().isNotFound());

        verify(ingredientRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void deleteIngredient_Ok() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Carne");
        ingredient.setId(1L);

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        mockMvc.perform(delete("/ingredients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Carne")));

        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).delete(ingredient);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    public void deleteIngredient_NotFound() throws Exception {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/ingredients/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(ingredientRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ingredientRepository);
    }
}
