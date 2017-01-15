
public class Message {
    private User user;
    private String txt;
    private String chatid;

    public String getChatID()
    {
        return chatid;
    }

    public User getUser()
    {
        return user;
    }

    public Message(String chatid, User user, String txt)
    {
        this.user = user;
        this.chatid = chatid;
        this.txt = txt;

    }

    public String getText()
    {
        return txt;
    }
}
