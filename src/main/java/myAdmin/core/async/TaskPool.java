package myAdmin.core.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskPool {

    @Async
    void execute(Runnable runnable){
        runnable.run();
    }

}
