package com.schooldevops.libusedemo;

import com.schooldevops.libdemo.services.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StringController {

    @GetMapping("/upper/{string}")
    public String upper(@PathVariable("string") String string) {
        return StringUtil.toUpper(string);
    }

    @GetMapping("/lower/{string}")
    public String lower(@PathVariable("string") String string) {
        return StringUtil.toLower(string);
    }
}
