package com.test.search.controller;

import com.google.common.base.CaseFormat;
import com.test.search.autocomplete.ArraySearchImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SundararajanSr on 4/5/2016.
 */
@RestController
class AutoComplete {
    private static ArraySearchImpl searchImpl;

    static {
        try {
            ClassPathResource classPathResource = new ClassPathResource("india_cities.txt");
            searchImpl = new ArraySearchImpl(classPathResource.getFile());
        } catch (IOException e) {
            // No point in starting the application..
            System.exit(1);
        }

    }


    @RequestMapping(value = "/suggest/cities", produces = MediaType.TEXT_PLAIN_VALUE)
    public String suggestCities(@RequestParam(value = "q") String q, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        if (q == null || q.length() == 0) {
            throw new IllegalArgumentException();
        }
        ArrayList<String> results = searchImpl.getMatches(q.toLowerCase(), limit);
        StringBuilder out = new StringBuilder();
        if (results == null || results.size() == 0) {
            return "No Matches Found";
        }
        int c=0;
        for (String result : results) {
            if(c<limit) {
                out.append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, result) + "\r\n");
            }else{
                break;
            }
            c++;
        }
        return out.toString();
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler
    void handleNullException(Exception e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @RequestMapping("/hello")
    public String sayHello() {
        return "hello";
    }
}