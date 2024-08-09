import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            UserCsvReader csvReader = new UserCsvReader();
            List<User> users = csvReader.readUsers("src/main/resources/input.csv");

            UserXmlWriter userXmlWriter = new UserXmlWriter();
            boolean isSuccess = userXmlWriter.writeXml(users);

            if (isSuccess) {
                System.out.println("XML file created successfully!");
            } else {
                System.out.println("XML file couldn't be created!");
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
