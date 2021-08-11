package fc.frontend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserParser {

    public UserParser() {
        //EMPTY
    }

    public User search(String username) throws FileNotFoundException, IOException, ParseException {

        Object array = new JSONParser()
                .parse(new FileReader(ClassLoader.getSystemResource("temp/users-temp.json").getFile()));

        JSONArray jsonObject = (JSONArray) array;

        for (Object object : jsonObject) {
            JSONObject lineItem = (JSONObject) object;
            if (lineItem.get("username").equals(username)) {
                return new User(lineItem.get("username").toString(), lineItem.get("password").toString());
            }
        }

        return null;

    }

}
