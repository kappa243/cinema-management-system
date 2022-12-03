package pl.edu.agh.cinema;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ViewManager {

    private ApplicationContext applicationContext;

    public ViewManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T load(String view) throws IOException {
        return load(view, null);
    }

    public <T> T load(String view, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));

        loader.setControllerFactory(applicationContext::getBean);

        return loader.load();
    }

}
