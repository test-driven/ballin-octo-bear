package com.accel.acme.web.controller;

import com.accel.acme.web.common.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AcmeNavigationController {

    @RequestMapping(value = Constants.CONTACTUS_URL, method = RequestMethod.GET)
    public ModelAndView handleContactusPageNavigation() {
        ModelAndView model = new ModelAndView(Constants.CONTACTUS);
        return model;
    }

    @RequestMapping(value = Constants.ABOUTUS_URL, method = RequestMethod.GET)
    public ModelAndView handleAboutUsPageNavigation() {
        ModelAndView model = new ModelAndView(Constants.ABOUTUS);
        return model;
    }
}
