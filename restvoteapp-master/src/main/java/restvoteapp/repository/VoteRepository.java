package restvoteapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.Vote;

import java.time.LocalDate;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Vote getByIdAndUserId(int id, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.votedate = :votedate AND v.user.id = :id")
    Vote getByVotedateAndUserId(@Param("votedate") LocalDate votedate, @Param("id") int userId);

    @Modifying
    @Transactional
    void deleteByUserIdAndVotedate(int userId, LocalDate votedate);
}
