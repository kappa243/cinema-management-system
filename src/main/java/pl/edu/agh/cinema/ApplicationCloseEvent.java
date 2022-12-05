package pl.edu.agh.cinema;

import org.springframework.context.ApplicationEvent;

public class ApplicationCloseEvent extends ApplicationEvent {
    public ApplicationCloseEvent(Object source) {
        super(source);
    }

}
