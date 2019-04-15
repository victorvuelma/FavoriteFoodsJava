package me.vuelma.favoritefoods.ingredient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.vuelma.favoritefoods.food.Food;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode
@Table(name = "ingredients")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter
    private long id;

    @NotBlank
    @NotNull(message = "Provide ingredient Name")
    @Setter
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "food_ingredients",
            joinColumns = {@JoinColumn(name = "ingredients_id")},
            inverseJoinColumns = {@JoinColumn(name = "foods_id")})
    @JsonBackReference
    private List<Food> foods;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

}
