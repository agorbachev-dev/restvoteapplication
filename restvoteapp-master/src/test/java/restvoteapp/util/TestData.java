package restvoteapp.util;

import restvoteapp.model.Menu;
import restvoteapp.model.Restaurant;
import restvoteapp.model.User;
import restvoteapp.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestData {
    public static User USER1 = new User("user1", "user1@example.com", "user1");
    public static User USER2 = new User("user2", "user2@example.com", "user2");
    public static User ADMIN1 = new User("admin1", "admin1@example.com", "admin1");

    public static Restaurant REST1 = new Restaurant("REST1");
    public static Restaurant REST2 = new Restaurant("REST2");

    public static Menu MENU1 = new Menu();
    public static Menu MENU2 = new Menu();

    public static Vote VOTE1 = new Vote();
    public static Vote VOTE2 = new Vote();

    public static int USER2_ID = 100001;
    public static int USER1_ID = 100000;
    public static int ADMIN1_ID = 100002;
    public static int REST1_ID = 100003;
    public static int REST2_ID = 100004;
    public static int MENU1_ID = 100005;
    public static int MENU2_ID = 100006;
    public static int VOTE1_ID = 100007;
    public static int VOTE2_ID = 100008;
    public static int NOT_FOUND_ID = 0;

    static {
        USER1.setId(USER1_ID);
        USER2.setId(USER2_ID);
        ADMIN1.setId(ADMIN1_ID);
        REST1.setId(REST1_ID);
        REST2.setId(REST2_ID);
        MENU1.setId(MENU1_ID);
        VOTE1.setId(VOTE1_ID);
        VOTE2.setId(VOTE2_ID);
    }

    public static Menu newMenuForService() {
        return newMenuForWeb();
    }

    public static Menu updatedMenuForService() {
        return updatedMenuForWeb();
    }

    public static Restaurant newRestForService() {
        return newRestForWeb();
    }

    public static Restaurant updatedRestForService() {
        return updatedRestForWeb();
    }

    public static Vote newVoteForService() {
        Vote newVote = newVoteForWeb();
        newVote.setVotedate(LocalDate.now());
        newVote.setVotetime(LocalTime.now());
        newVote.setUser(USER2);
        return newVote;
    }

    public static Vote updatedVoteForService() {
        Vote updatedVote = updatedVoteForWeb();
        return updatedVote;
    }

    public static Menu newMenuForWeb() {
        return new Menu(null, LocalDate.now().plusDays(1), REST1, "REST1 DISHES");
    }

    public static Menu updatedMenuForWeb() {
        return new Menu(null, LocalDate.now(), REST1, "REST1 DISHES UPDATED");
    }

    public static Restaurant newRestForWeb() {
        return new Restaurant("NEW REST");
    }

    public static Restaurant updatedRestForWeb() {
        return new Restaurant("UPDATED REST");
    }

    public static Vote newVoteForWeb() {
        return new Vote(REST2);
    }

    public static Vote updatedVoteForWeb() {
        return new Vote(REST2);
    }
}
