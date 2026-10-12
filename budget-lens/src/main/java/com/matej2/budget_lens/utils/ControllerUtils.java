package com.matej2.budget_lens.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ControllerUtils {
    public static String getRedirectString(HttpServletRequest request) {
        String refUrl = request.getHeader("Referer");
        String redirectString = "redirect:/";

        if (refUrl != null) {
            redirectString = String.format("redirect:%s", refUrl);
        }

        return redirectString;
    }

}
