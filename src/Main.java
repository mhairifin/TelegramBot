import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.json.*;

public class Main {

    static ArrayList<Chat> chats = new ArrayList<Chat>();
    static ArrayList<User> users = new ArrayList<User>();
    public static final String TOKEN = "268526572:AAFSb2yCvpt5UqrZz9x0r5pkIq1VVKJ7ooU";
    static long updateNo;

    public static void main(String[] args) {
        try {
            updateNo = Chat.setUp();
            while(true) {
                Message mess = null;
                while (mess == null) {
                    mess = Chat.getUpdate(updateNo);
                }
                boolean found = false;
                for (Chat chat : chats) {
                    if (mess.getChatID() == chat.getChatID()) {
                        chat.receive(mess);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    chats.add(new Chat(mess.getChatID(), mess, mess.getUser()));
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static User findUser(String username, String name)
    {
        for (User user: users)
        {
            if (user.getUsername().equals(username))
            {
                return user;
            }
        }
        User nuse = new User(username, name);
        return nuse;
    }



}
