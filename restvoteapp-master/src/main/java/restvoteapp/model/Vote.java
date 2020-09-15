package restvoteapp.model;

import lombok.*;
import restvoteapp.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "votedate"}, name = "vote_unique_user_date_idx"))
public class Vote extends AbstractBaseEntity {

    @Column(name = "votedate", nullable = false)
    @NotNull
    private LocalDate votedate;

    @Column(name = "votetime", nullable = false)
    @NotNull
    private LocalTime votetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = {View.Web.class, Default.class})
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Vote(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
