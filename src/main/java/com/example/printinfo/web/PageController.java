package com.example.printinfo.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping(value = "/assetGrid", produces = "text/html; charset=UTF-8")
    public String assetGrid() {
        return "assetGrid"; // /WEB-INF/jsp/assetGrid.jsp 를 찾음
    }
}
