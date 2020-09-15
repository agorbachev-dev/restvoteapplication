package restvoteapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import restvoteapp.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Restaurant getById(int id);


    @Query(nativeQuery = true, value = "select ID, NAME\n" +
            "from (\n" +
            "         SELECT ID, NAME\n" +
            "         FROM (SELECT COUNT(ROWNUM) sum, R.ID ID, R.NAME NAME\n" +
            "               FROM RESTAURANT R\n" +
            "                        JOIN VOTE V on R.ID = V.RESTAURANT_ID\n" +
            "               WHERE V.VOTEDATE = ?\n" +
            "               GROUP BY R.ID)\n" +
            "                  right join (SELECT max(sum) summ\n" +
            "                              from (\n" +
            "                                       SELECT sum\n" +
            "                                       FROM (SELECT COUNT(ROWNUM) as sum\n" +
            "                                             FROM RESTAURANT R\n" +
            "                                                      JOIN VOTE V on R.ID = V.RESTAURANT_ID\n" +
            "                                             WHERE V.VOTEDATE = ?\n" +
            "                                             GROUP BY R.ID)\n" +
            "                                       ORDER BY sum DESC)) on sum = summ)\n" +
            "order by ID, NAME;")
    List<Restaurant> getWinnersOnDate(LocalDate date, LocalDate date1);
}
