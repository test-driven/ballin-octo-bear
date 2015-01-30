package com.accel.acme.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import static com.accel.acme.web.common.Constants.*;

@Controller
public class AcmeNavigationController {

    @RequestMapping(value = CONTACTUS_URL, method = RequestMethod.GET)
    public ModelAndView handleContactusPageNavigation() {
        ModelAndView model = new ModelAndView(CONTACTUS);
        return model;
    }

    @RequestMapping(value = ABOUTUS_URL, method = RequestMethod.GET)
    public ModelAndView handleAboutUsPageNavigation() {
        ModelAndView model = new ModelAndView(ABOUTUS);
        return model;
    }
}
