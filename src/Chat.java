import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Chat {

    private HashMap<User, Integer> userpoints;
    private String chatid;


    public Chat(String chatid, Message msg, User user) throws IOException
    {
        userpoints = new HashMap<User, Integer>();
        this.chatid = chatid;
        userpoints.put(user, 0);
        receive(msg);
    }

    public String getChatID()
    {
        return chatid;
    }

    public void receive(Message msg) throws IOException
    {
        System.out.println("Message received");
        String content = msg.getText();
        switch (content)
        {
            case "Hi": sendMessage("Hi " + msg.getUser().getName());
                break;
            case "Question": sendMCQuestion();
                break;
            default: sendMessage("cool");
        }
    }

    public static long setUp() throws IOException
    {
        int updateNo = 0;

        while (updateNo == 0) {
            StringBuilder url = new StringBuilder(baseUrl("getUpdates"));
            String response = sendURL(url.toString());
            if (response.equals("{\"ok\":true,\"result\":[]}")) {
                updateNo = 0;
            }
            else
            {
                JsonReader updates = Json.createReader(new StringReader(response.toString()));
                JsonObject j = updates.readObject();
                updateNo = j.getJsonArray("result").getJsonObject(0).getInt("update_id");
            }
        }
        return updateNo;
    }

    public static Message getUpdate(long updateNo) throws IOException
    {
        StringBuilder url = new StringBuilder(baseUrl("getUpdates"));
        url.append("?offset=");
        url.append(updateNo);
        String response = sendURL(url.toString());
        if (response.equals("{\"ok\":true,\"result\":[]}"))
        {
            return null;
        }
        System.out.println(response);
        JsonReader updates = Json.createReader(new StringReader(response.toString()));
        JsonObject j = updates.readObject();
        String chatid = String.valueOf(j.getJsonArray("result").getJsonObject(0).getJsonObject("message").getJsonObject("chat").getInt("id"));
        String text = j.getJsonArray("result").getJsonObject(0).getJsonObject("message").getString("text");
        String username = String.valueOf(j.getJsonArray("result").getJsonObject(0).getJsonObject("message").getJsonObject("from").getInt("id"));
        String name = j.getJsonArray("result").getJsonObject(0).getJsonObject("message").getJsonObject("from").getString("first_name");
        Main.updateNo = j.getJsonArray("result").getJsonObject(0).getInt("update_id") + 1;
        return new Message(chatid, Main.findUser(username, name), text);
    }

    public static String baseUrl(String method)
    {
        return "https://api.telegram.org/bot" + Main.TOKEN + "/" + method;
    }

    public String sendMessage(String content) throws IOException
    {
        StringBuilder url = new StringBuilder(baseUrl("sendMessage"));
        addMessParams(url, new String[][]{{"text", content}});
        return sendURL(url.toString());
    }

    public void addMessParams(StringBuilder url, String[][] params)
    {
        url.append("?chat_id=");
        url.append(chatid);
        for (int i = 0; i<params.length; i++)
        {
            url.append("&");
            url.append(params[i][0]);
            url.append("=");
            url.append(params[i][1]);
        }
    }

    public String sendMCQuestion() throws IOException
    {

        StringBuilder url = new StringBuilder(baseUrl("sendMessage"));
        String[] question = new String[]{"What is the name of the first President of the USA?", "George Washington", "Thomas Jefferson", "Benjamin Franklin" };
        addMessParams(url, new String[][]{{"text", question[0]}});
        ArrayList<String> answers = new ArrayList<String>();
        for (int i = 1; i<question.length; i++)
        {
            answers.add(question[i]);
        }
        Collections.shuffle(answers);
        url.append("&reply_markup={\"keyboard\":[[\"" + answers.get(0));
        url. append("\"],[\"" + answers.get(1));
        url.append("\"],[\""+ answers.get(2));
        url.append("\"]],\"one_time_keyboard\":true}");
        return sendURL(url.toString());
    }

    public static String sendURL(String url) throws IOException
    {
        URL url1 = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");

        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);

        br.close();
        return responseSB.toString();
    }
}
