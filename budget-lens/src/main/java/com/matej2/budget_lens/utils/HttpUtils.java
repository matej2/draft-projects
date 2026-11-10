package com.matej2.budget_lens.utils;

import com.matej2.budget_lens.model.helper.WebPageContext;
import jakarta.servlet.http.HttpServletRequest;

public class HttpUtils {
    public static String getRedirectString(HttpServletRequest request) {
        String refUrl = request.getHeader("Referer");
        String redirectString = "redirect:/";

        if (refUrl != null) {
            redirectString = String.format("redirect:%s", refUrl);
        }

        return redirectString;
    }

    public static String getRedirectString(WebPageContext pageContext) {
        return String.format("redirect:%s", pageContext.path());
    }

}
