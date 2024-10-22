package users;

public class Users {

    protected String name;
    public int score = 0;

    public Users() {

    }
    public Users(String name) {
        this.name = name;
    }

    public String getName() {
        return name;

    }
    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }
}
