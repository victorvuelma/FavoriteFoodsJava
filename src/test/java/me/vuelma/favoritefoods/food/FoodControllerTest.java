package me.vuelma.favoritefoods.food;

import me.vuelma.favoritefoods.TestUtil;
import me.vuelma.favoritefoods.ingredient.Ingredient;
import me.vuelma.favoritefoods.ingredient.IngredientRepository;
import me.vuelma.favoritefoods.kitchen.Kitchen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
@WebMvcTest(FoodController.class)
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private FoodRepository foodRepository;

    @Autowired
    @MockBean
    private IngredientRepository ingredientRepository;

    @Test
    public void getAllFoods() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Italiana");

        Food food1 = new Food();
        food1.setId(1L);
        food1.setName("Macarrão com Carne");
        food1.setPreparationTime(30);
        food1.setKitchen(kitchen);

        Food food2 = new Food();
        food2.setId(2L);
        food2.setName("Carne");
        food2.setPreparationTime(10);
        food2.setKitchen(kitchen);

        List<Food> foods = Arrays.asList(food1, food2);

        when(foodRepository.findAll()).thenReturn(foods);

        mockMvc.perform(get("/foods"))
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Macarrão com Carne")))
                .andExpect(jsonPath("$[0].preparationTime", is(30)))
                .andExpect(jsonPath("$[0].kitchen.id", is(1)))
                .andExpect(jsonPath("$[0].kitchen.name", is("Italiana")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Carne")))
                .andExpect(jsonPath("$[1].preparationTime", is(10)))
                .andExpect(jsonPath("$[1].kitchen.id", is(1)))
                .andExpect(jsonPath("$[1].kitchen.name", is("Italiana")));

        verify(foodRepository, times(1)).findAll();
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void getFoodById_Ok() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Italiana");

        Food food = new Food();
        food.setId(1L);
        food.setName("Macarrão");
        food.setPreparationTime(30);
        food.setKitchen(kitchen);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        mockMvc.perform(get("/foods/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Macarrão")))
                .andExpect(jsonPath("$.preparationTime", is(30)))
                .andExpect(jsonPath("$.kitchen.id", is(1)))
                .andExpect(jsonPath("$.kitchen.name", is("Italiana")));

        verify(foodRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void getFoodById_NotFound() throws Exception {
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/foods/{id}", 1))
                .andExpect(status().isNotFound());

        verify(foodRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void createFood() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Italiana");

        Food food = new Food();
        food.setId(1L);
        food.setName("Macarrão com Carne");
        food.setPreparationTime(30);
        food.setKitchen(kitchen);

        when(foodRepository.save(food)).thenReturn(food);

        mockMvc.perform(post("/foods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(food)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Macarrão com Carne")))
                .andExpect(jsonPath("$.preparationTime", is(30)))
                .andExpect(jsonPath("$.kitchen.id", is(1)))
                .andExpect(jsonPath("$.kitchen.name", is("Italiana")));

        verify(foodRepository, times(1)).save(food);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void updateFood_Ok() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Italiana");

        Food food = new Food();
        food.setId(1L);
        food.setName("Macarrão com Carne");
        food.setPreparationTime(30);
        food.setKitchen(kitchen);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        when(foodRepository.save(food)).thenReturn(food);

        Food updatedFood = new Food();
        updatedFood.setName("Macarrão");
        updatedFood.setPreparationTime(20);
        updatedFood.setKitchen(kitchen);

        mockMvc.perform(put("/foods/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFood)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Macarrão")))
                .andExpect(jsonPath("$.preparationTime", is(20)))
                .andExpect(jsonPath("$.kitchen.id", is(1)))
                .andExpect(jsonPath("$.kitchen.name", is("Italiana")));

        verify(foodRepository, times(1)).findById(1L);
        verify(foodRepository, times(1)).save(food);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void updateFood_Created() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Italiana");

        Food food = new Food();
        food.setId(1L);
        food.setName("Macarrão");
        food.setPreparationTime(20);
        food.setKitchen(kitchen);

        when(foodRepository.findById(1L)).thenReturn(Optional.empty());
        when(foodRepository.save(food)).thenReturn(food);

        mockMvc.perform(put("/foods/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(food)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Macarrão")))
                .andExpect(jsonPath("$.preparationTime", is(20)))
                .andExpect(jsonPath("$.kitchen.id", is(1)))
                .andExpect(jsonPath("$.kitchen.name", is("Italiana")));

        verify(foodRepository, times(1)).findById(1L);
        verify(foodRepository, times(1)).save(food);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void deleteFood_Ok() throws Exception {
        Kitchen kitchen = new Kitchen();
        kitchen.setId(1L);
        kitchen.setName("Italiana");

        Food food = new Food();
        food.setId(1L);
        food.setName("Macarrão");
        food.setPreparationTime(20);
        food.setKitchen(kitchen);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        mockMvc.perform(delete("/foods/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Macarrão")))
                .andExpect(jsonPath("$.preparationTime", is(20)))
                .andExpect(jsonPath("$.kitchen.id", is(1)))
                .andExpect(jsonPath("$.kitchen.name", is("Italiana")));

        verify(foodRepository, times(1)).findById(1L);
        verify(foodRepository, times(1)).delete(food);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void deleteFood_NotFound() throws Exception {
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/foods/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(foodRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void getIngredientsByFoodId_Ok() throws Exception {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName("Carne");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName("Macarrão");

        Food food = new Food();
        food.setId(1L);
        food.setIngredients(Arrays.asList(ingredient1, ingredient2));

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        mockMvc.perform(get("/foods/{id}/ingredients", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Carne")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Macarrão")));

        verify(foodRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    public void getIngredientsByFoodId_NotFound() throws Exception {
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/foods/{f_id}/ingredients", 1L))
                .andExpect(status().isNotFound());

        verify(foodRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }

}
