package com.accel.acme.web.common;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final List<String> LANGUAGE_LIST = Arrays.asList("English", "Hindi", "Malayalam");
    public static final String NONE = "NONE";
    public static final String ACME_TITLE = "ACME-";

    // ACME View names
    public static final String HOME = "home";
    public static final String NEWSPAPERS = "newspapers";
    public static final String ADVERTISEMENTS = "advertisements";
    public static final String ABOUTUS = "aboutus";
    public static final String CONTACTUS = "contactus";

    public static final String VIEW_ADVERTISEMENT_FORM = "advertisementsform";
    public static final String VIEW_NEWSPAPER_FORM = "newspapersform";

    public static final String VIEW_SUCCESS = "success";
    public static final String VIEW_INTERNAL_SERVER_ERROR = "error/500";
    public static final String VIEW_NOT_FOUND = "error/404";

    // ACME Navigations
    public static final String ACME_ROOT = "/";
    public static final String HOME_URL = "/home";
    public static final String NEWSPAPERS_URL = "/newspapers";
    public static final String ADVERTISEMENTS_URL = "/advertisements";
    public static final String ABOUTUS_URL = "/aboutus";
    public static final String CONTACTUS_URL = "/contact";

    public static final String ADD_NEWSPAPER_URL = "/newspapers/new";
    public static final String CHANGE_NEWSPAPER_URL = "/newspapers/{newspaperId}";

    public static final String ADD_ADVERTISEMENT_URL = "/advertisements/new";
    public static final String CHANGE_ADVERTISEMENT_URL = "/advertisements/{advertisementId}";

    public static final String INTERNAL_SERVER_ERROR_URL = "/error/500";
    public static final String NOT_FOUND_URL = "/error/404";

    // Model Attributes
    public static final String MODEL_ATTR_LANGUAGELIST = "languageList";
    public static final String MODEL_ATTR_NEWSPAPERLIST = "newspaperList";
    public static final String MODEL_ATTR_NEWSPAPER = "newspaper";

    public static final String MODEL_ATTR_FORMTITLE = "formTitle";
    public static final String MODEL_ATTR_PAGETITLE = "pageTitle";
    public static final String MODEL_ATTR_ITEM = "item";
    public static final String MODEL_ATTR_ITEMIDENTITY = "itemIdentity";
    public static final String MODEL_ATTR_ACTION = "action";
    public static final String MODEL_ATTR_ADDNEWURL = "addNewUrl";
    public static final String MODEL_ATTR_ERRORS = "errors";

    public static final String MODEL_ATTR_ADVERTISEMENT = "advertisement";

}
