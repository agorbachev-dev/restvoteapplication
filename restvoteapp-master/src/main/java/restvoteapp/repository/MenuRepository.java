package restvoteapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> getAllByMenudate(LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = :id")
    Menu getById(@Param("id") int id);
}
