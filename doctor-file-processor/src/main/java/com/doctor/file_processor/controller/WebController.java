package com.doctor.file_processor.controller;

import com.doctor.file_processor.domain.entity.AccessInfo;
import com.doctor.file_processor.service.AccessInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class WebController {
    AccessInfoService accessInfoService;
    @Value("${rateLimit}")
    private Integer rateLimit;

    public WebController(AccessInfoService accessInfoService) {
        this.accessInfoService = accessInfoService;
    }

    private List<AccessInfo> getAccessInfo() {
        Iterable<AccessInfo> accessInfoIterable = accessInfoService.findAll();

        List<AccessInfo> target = new ArrayList<>();
        accessInfoIterable.forEach(target::add);

        return target;
    }

    @GetMapping("/asdf")
    public String home(Model model) {
        List<AccessInfo> accessInfoList = getAccessInfo();

        model.addAttribute("serverTime", new Date());
        model.addAttribute("accessInfoList", accessInfoList);
        model.addAttribute("rateLimit", rateLimit);
        return "index";
    }
}
