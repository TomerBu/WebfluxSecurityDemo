package edu.tomerbu.webfluxsecuritydemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@Controller
public class ErrorHandler implements ErrorController {

//    private final ErrorAttributes errorAttributes;
//
//    public ErrorHandler(ErrorAttributes errorAttributes) {
//        this.errorAttributes = errorAttributes;
//    }

    @RequestMapping("/error")
    public Mono<ServerResponse> handleError(ServerRequest request, ErrorAttributes errorAttributes) {
        Throwable error = errorAttributes.getError(request);

        Map<String, Object> errorPropertiesMap = errorAttributes.getErrorAttributes(
                request,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.MESSAGE
                )
        );

        errorPropertiesMap.put("message", error.getMessage());
        errorPropertiesMap.put("error", error.getClass().getSimpleName());

        var status = (Integer) errorPropertiesMap.get("status");

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}