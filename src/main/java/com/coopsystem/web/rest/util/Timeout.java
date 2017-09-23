package com.coopsystem.web.rest.util;

import net.logstash.logback.encoder.org.apache.commons.lang.UnhandledException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.*;

/**
 * Created by Mateusz Niedośpiał on 14.06.2017.
 */
public class Timeout{

    //Prevent instantiating, all of the methods are static
    private Timeout(){}

    public static <T> T setTimeout(JpaRepository<T, Long> repository, T entity, Long waitFor){
        //Callable override using lambda:
        FutureTask<T> timeoutTask = new FutureTask<>(() -> {return repository.save(entity);});
        new Thread(timeoutTask).start();
        try {
            //If timeout is reached timeoutException is thrown and caught later:
            return timeoutTask.get(waitFor, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException();
        } catch (TimeoutException e) {
            timeoutTask.cancel(true);
            return null;
        }
    }
}
