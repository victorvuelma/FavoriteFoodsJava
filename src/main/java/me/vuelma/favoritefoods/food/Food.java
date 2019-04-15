package me.vuelma.favoritefoods.food;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import me.vuelma.favoritefoods.ingredient.Ingredient;
import me.vuelma.favoritefoods.kitchen.Kitchen;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "foods")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotBlank
    @Column(unique = true)
    @NotNull(message = "Provide ingredient Name")
    private String name;
    @NotNull
    @Min(value = 1, message = "Preparation time should be equal or more than 1")
    private int preparationTime;

    @ManyToOne
    @NotNull
    private Kitchen kitchen;

    @ManyToMany()
    @JoinTable(name = "food_ingredients",
            joinColumns = {@JoinColumn(name = "foods_id")},
            inverseJoinColumns = {@JoinColumn(name = "ingredients_id")})
    private List<Ingredient> ingredients;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

}
