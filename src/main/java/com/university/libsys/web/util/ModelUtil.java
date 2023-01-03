package com.university.libsys.web.util;

import org.springframework.ui.Model;

import java.util.List;

public class ModelUtil {

    public static void fillInfoModelWithArguments(Model model, String header, List<String> messages) {
        model.addAttribute("infoHeader", header);
        model.addAttribute("infoMessages", messages);
    }

    public static void fillWithError(Model model, String errorMessage) {
        model.addAttribute("error", errorMessage);
    }
}
