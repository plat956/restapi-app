package com.epam.esm.controller;

import com.epam.esm.service.CommonService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CommonController {

    @Autowired
    private Environment env;

    @Autowired
    private CommonService commonService;

    @GetMapping
    public String index(Model model, HttpServletRequest req) {
        String authLink = env.getProperty("oauth.server_auth_url") + "?client_id=" + env.getProperty("oauth.client_id") +
                "&response_type=" + env.getProperty("oauth.response_type") + "&scope=" + env.getProperty("oauth.scope");
        model.addAttribute("authLink", authLink);
        model.addAttribute("auth", req.getSession().getAttribute("auth"));
        return "index";
    }

    @ResponseBody
    @GetMapping("/getProfile")
    public Object getProfile(HttpSession session) {
        JsonNode auth = (JsonNode) session.getAttribute("auth");
        String token = auth.path("access_token").asText();
        return commonService.getRemoteData(env.getProperty("oauth.server_profile_url"), token);
    }

    @ResponseBody
    @GetMapping("/getOrders")
    public Object getOrders(HttpSession session) {
        JsonNode auth = (JsonNode) session.getAttribute("auth");
        String token = auth.path("access_token").asText();
        return commonService.getRemoteData(env.getProperty("oauth.server_orders_url"), token);
    }

    @GetMapping("/authCallback")
    public String users(@RequestParam("code") String code, Model model, HttpServletRequest req) {
        req.getSession().setAttribute("auth", commonService.receiveAccessToken(code));
        model.addAttribute("code", code);
        return "redirect:/";
    }
}
