package com.example.challenge.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(
        value = "Health Check API",
        description = "REST API for health check",
        tags = {"Health Check"}
)
@RestController
@RequestMapping(path = "/health-check")
public class HealthCheckController {

    @ApiOperation(
            value = "Status check",
            notes = "Returns 200 OK if the service is running",
            response = String.class,
            nickname = "statusCheck"
    )
    @GetMapping
    public ResponseEntity<?> statusCheck(){
        return new ResponseEntity<>("BACKEND SERVICE IS RUNNING OK",HttpStatus.OK);
    }

}
