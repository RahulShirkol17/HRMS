package HRMS_project.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class IndexController implements ErrorController{
    private final static String PATH = "/error";
    @RequestMapping(PATH)
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return "redirect:/employee/leaves/status?email=test@example.com";
    }

}