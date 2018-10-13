package com.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.ModelMap;

@Controller
public class AppController {

    public static ModelMap model= new ModelMap();

    @RequestMapping("/")
    public String index() {

        return "index";
    }
    
    @RequestMapping("/home")
    public String SpringBootHello(ModelMap mod) {
        mod.addAllAttributes(model);
        return "home";
    }

    public void setModelAttribute(String index, String value)
    {
        model.addAttribute(index, value);
    }
}