package com.leyou.page.controller;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("item/{id}.html")
    public String generateItemHtml(@PathVariable("id")Long id, Model model){
        Map<String,Object> data = pageService.generateItemHtml(id);
        //model.addAllAttributes(data);
        //model.addAttribute(attributes.get("categories"));
        model.addAttribute("title", data.get("title"));
        model.addAttribute("subTitle", data.get("subTutle"));
        model.addAttribute("skus", data.get("skus"));
        model.addAttribute("detail", data.get("detail"));
        model.addAttribute("categories", data.get("categories"));
        model.addAttribute("brand", data.get("brand"));
        model.addAttribute("specs", data.get("specs"));
        return "item";
    }
}
