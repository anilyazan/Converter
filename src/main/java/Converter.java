import com.csvreader.CsvReader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Converter {

    public static void main(String[] args) {
        try {
            List<Map<String, Object>> users = readCSV();

            // Freemarker configuration object
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassForTemplateLoading(Converter.class, "/");
            cfg.setDefaultEncoding("UTF-8");

            // Load template from resources folder
            Template template = cfg.getTemplate("template.ftl");

            // Output file path
            String outputPath = "src/main/resources/output.xml";

            // Write to output.xml
            try (FileWriter writer = new FileWriter(outputPath)) {
                Map<String, Object> dataModel = new HashMap<>();
                dataModel.put("users", users);

                // Process template with data and write to output.xml
                template.process(dataModel, writer);
            }

            System.out.println("XML file created successfully!");

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    // CSV reading method
    public static List<Map<String, Object>> readCSV() throws IOException {
        List<Map<String, Object>> users = new ArrayList<>();
        CsvReader csvReader = new CsvReader("src/main/resources/input.csv");

        csvReader.readHeaders();
        Map<String, Map<String, Object>> userMap = new HashMap<>();

        while (csvReader.readRecord()) {
            String username = csvReader.get("username");
            String firstname = csvReader.get("firstname");
            String lastname = csvReader.get("lastname");
            String email = csvReader.get("email");
            String role = csvReader.get("role");

            if (!userMap.containsKey(username)) {
                Map<String, Object> user = new HashMap<>();
                user.put("username", username);
                user.put("firstname", firstname);
                user.put("lastname", lastname);
                user.put("email", email);
                user.put("roles", new ArrayList<String>());
                userMap.put(username, user);
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) userMap.get(username).get("roles");
            roles.add(role);
        }

        users.addAll(userMap.values());
        csvReader.close();
        return users;
    }
}
