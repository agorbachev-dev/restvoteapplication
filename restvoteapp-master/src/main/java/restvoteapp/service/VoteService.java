package restvoteapp.service;

import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.User;
import restvoteapp.model.Vote;
import restvoteapp.repository.VoteRepository;
import restvoteapp.util.exception.TimeExpiredException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.util.Assert.notNull;
import static restvoteapp.util.ValidationUtil.*;

@Setter
@Service
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantService restaurantService;
    private LocalTime checkTime = LocalTime.of(11, 0, 0);

    public VoteService(VoteRepository voteRepository, RestaurantService restaurantService) {
        this.voteRepository = voteRepository;
        this.restaurantService = restaurantService;
    }

    public Vote getByIdAndUserId(int id, int authUserId) {
        return checkNotFoundWithId(voteRepository.getByIdAndUserId(id, authUserId), id);
    }

    @Transactional
    public Vote create(Vote vote) {
        notNull(vote, "vote must not be null");
        checkNew(vote);

        return voteRepository.save(vote);
    }

    public Vote getToday(LocalDate date, int authUserId) {
        return checkNotFound(voteRepository.getByVotedateAndUserId(date, authUserId), "No votes for today");
    }

    private void checkTimeExpired(LocalTime time) {
        if (time.compareTo(checkTime) > 0) {
            throw new TimeExpiredException("Vote can be edited or deleted due 11:00 AM");
        }
    }

    @Transactional
    public void delete(int authUserId) {
        LocalDateTime dateTime = LocalDateTime.now();
        checkTimeExpired(dateTime.toLocalTime());
        voteRepository.deleteByUserIdAndVotedate(authUserId, dateTime.toLocalDate());
    }

    @Transactional
    public void update(Vote vote, User user) {
        notNull(vote, "Vote must not be null");
        vote.setVotetime(LocalTime.now());
        vote.setVotedate(LocalDate.now());
        checkTimeExpired(vote.getVotetime());
        Vote todayInDb = checkNotFound(getToday(LocalDate.now(), user.getId()), "not found today vote for user with id" + user.getId());
        vote.setId(todayInDb.getId());
        vote.setUser(todayInDb.getUser());
        voteRepository.save(vote);
    }
}

