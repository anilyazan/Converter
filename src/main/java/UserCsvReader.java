import com.csvreader.CsvReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCsvReader {
    public List<User> readUsers(String csvFilePath) throws IOException {
        CsvReader csvReader = new CsvReader(csvFilePath);
        csvReader.readHeaders();

        Map<String, User> userMap = new HashMap<>();

        List<User> users = new ArrayList<>();

        while (csvReader.readRecord()) {
            String username = csvReader.get("username");
            String firstname = csvReader.get("firstname");
            String lastname = csvReader.get("lastname");
            String email = csvReader.get("email");
            String role = csvReader.get("role");

            if (userMap.containsKey(username)) {
                userMap.get(username).getRoles().add(role);
            } else {
                userMap.put(username, new User(username, firstname, lastname, email, new ArrayList<>(Arrays.asList(role))));
                users.add(userMap.get(username));
            }
        }

        csvReader.close();
        return users;
    }
}
