package pl.edu.agh.cinema;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ui.StageAware;

import java.io.IOException;

@Component
public class ViewManager {

    private final ApplicationContext applicationContext;

    public ViewManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T, K> Pair<T, K> load(String view) throws IOException {
        return load(view, null);
    }


    public <T, K> Pair<T, K> load(String view, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));

        loader.setControllerFactory(applicationContext::getBean);

        T parent = loader.load();

        if (loader.getController() instanceof StageAware) {
            ((StageAware) loader.getController()).setStage(stage);
        }

        return Pair.of(parent, loader.getController());
    }


    public <T> T loadObject(String view) throws IOException {
        return loadObject(view, null);
    }

    public <T> T loadObject(String view, Stage stage) throws IOException {
        Pair<T, ?> pair = load(view, stage);
        return pair.getFirst();
    }

    public <K> K loadController(String view) throws IOException {
        return loadController(view, null);
    }

    public <K> K loadController(String view, Stage stage) throws IOException {
        Pair<?, K> pair = load(view, stage);
        return pair.getSecond();
    }


}
