import static spark.Spark.*;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class HelloWorld {
    public static void main(String[] args) {
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
        Configuration freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(HelloWorld.class, "/"));
        freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

        get("/hello", (req, res) ->
        {
            Map<String, Object> model = new HashMap<>();
            return freeMarkerEngine.render(new ModelAndView(model, "hello.ftl"));
        });
    }
}
