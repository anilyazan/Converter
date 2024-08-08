import com.csvreader.CsvReader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;

public class Converter {

    public static void main(String[] args) {
        try {
            // Initialize Freemarker configuration
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
            cfg.setDefaultEncoding("UTF-8");

            // Read CSV data
            Map<String, List<Map<String, String>>> usersData = readCSV("input.csv");

            // Generate XML file
            generateXML(usersData, "template.ftl", "output.xml", cfg);

            System.out.println("XML file generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<Map<String, String>>> readCSV(String filename) throws IOException {
        InputStream inputStream = Converter.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + filename);
        }

        Map<String, List<Map<String, String>>> usersMap = new LinkedHashMap<>();

        CsvReader reader = new CsvReader(new InputStreamReader(inputStream));
        reader.readHeaders();

        while (reader.readRecord()) {
            String username = reader.get("username");
            Map<String, String> userDetails = new LinkedHashMap<>();
            userDetails.put("firstname", reader.get("firstname"));
            userDetails.put("lastname", reader.get("lastname"));
            userDetails.put("email", reader.get("email"));
            String role = reader.get("role");

            if (!usersMap.containsKey(username)) {
                usersMap.put(username, new ArrayList<>());
            }
            userDetails.put("role", role);
            usersMap.get(username).add(userDetails);
        }
        reader.close();

        return usersMap;
    }

    private static void generateXML(Map<String, List<Map<String, String>>> usersData, String templateName, String outputFileName, Configuration cfg) throws IOException, TemplateException {
        Template template = cfg.getTemplate(templateName);

        try (FileWriter writer = new FileWriter(outputFileName)) {
            Map<String, Object> dataModel = new HashMap<>();
            List<Map<String, Object>> usersList = new ArrayList<>();

            for (Map.Entry<String, List<Map<String, String>>> entry : usersData.entrySet()) {
                Map<String, Object> userMap = new LinkedHashMap<>();
                userMap.put("username", entry.getKey());
                userMap.put("firstname", entry.getValue().get(0).get("firstname"));
                userMap.put("lastname", entry.getValue().get(0).get("lastname"));
                userMap.put("email", entry.getValue().get(0).get("email"));

                List<String> roles = new ArrayList<>();
                for (Map<String, String> userDetail : entry.getValue()) {
                    roles.add(userDetail.get("role"));
                }
                userMap.put("roles", roles);

                usersList.add(userMap);
            }

            dataModel.put("users", usersList);
            template.process(dataModel, writer);
        }
    }
}
