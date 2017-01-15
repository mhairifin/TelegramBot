
public class User {

    private String id;
    private String firstName;

    public User(String username, String name)
    {
        this.id = username;
        this.firstName = name;
    }
    public String getUsername()
    {
        return id;
    }

    public String getName()
    {
        return firstName;
    }

}
