package restvoteapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import restvoteapp.View;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "menu", uniqueConstraints = @UniqueConstraint(columnNames = {"menudate", "restaurant_id"}, name = "menus_unique_menudate_restaurant_idx"))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Menu extends AbstractBaseEntity {

    @Column(name = "menudate", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDate menudate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "dishes", nullable = false)
    @NotBlank
    private String dishes;

    public Menu(Integer id, Restaurant restaurant, String dishes){
        super(id);
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.menudate = LocalDate.now();
    }

    public Menu(Integer id, LocalDate menudate, Restaurant restaurant, String dishes){
        super(id);
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.menudate = menudate;
    }
}
