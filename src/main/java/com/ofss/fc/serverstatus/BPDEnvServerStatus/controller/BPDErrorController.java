package com.ofss.fc.serverstatus.BPDEnvServerStatus.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author - @asmhatre
 * This is a controller for error handling.
 */
@Controller
public class BPDErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "error";
    }
}