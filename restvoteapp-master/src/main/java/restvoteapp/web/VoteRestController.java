package restvoteapp.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import restvoteapp.AuthorizedUser;
import restvoteapp.View;
import restvoteapp.model.Vote;
import restvoteapp.service.VoteService;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping(value = VoteRestController.URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Secured("ROLE_USER")
public class VoteRestController {

    static final String URL = "/api/votes";
    private final VoteService voteService;

    @GetMapping(value = "/today")
    public Vote getToday(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get today vote of user {}", authUser.getUsername());
        return voteService.getToday(LocalDate.now(), authUser.getId());
    }

    @GetMapping(value = "/{id}")
    public Vote getById(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get vote with id {} of user {}", id, authUser.getUsername());
        return voteService.getByIdAndUserId(id, authUser.getId());
    }

    @PostMapping(value = "/today", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@Validated(View.Web.class) @RequestBody Vote vote, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("create vote for restaurant {} of user {}", vote.getRestaurant(), authUser.getUsername());
        vote.setVotedate(LocalDate.now());
        vote.setVotetime(LocalTime.now());
        vote.setUser(authUser.getUser());
        Vote created = voteService.create(vote);
        URI uriOfNewResource = fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/today")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("delete today vote of user {}", authUser.getUsername());
        voteService.delete(authUser.getId());
    }

    @PutMapping(value = "/today", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Vote vote, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("update today vote with values {} of user {}", vote, authUser.getUsername());
        voteService.update(vote, authUser.getUser());
    }
}
