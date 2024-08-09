import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserXmlWriter {

    public boolean writeXml(List<User> users) throws IOException, TemplateException {
        // Freemarker configuration object
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(Main.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        // Load template from resources folder
        Template template = cfg.getTemplate("template.ftl");

        // Output file path
        String outputPath = "src/main/resources/output.xml";

        // Write to output.xml
        try (FileWriter writer = new FileWriter(outputPath)) {
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("users", users);

            // Process template with data and write to .xml
            template.process(dataModel, writer);
        }

        return true;
    }
}
