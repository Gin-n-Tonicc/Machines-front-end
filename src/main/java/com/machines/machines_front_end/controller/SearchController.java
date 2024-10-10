package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.annotations.PageRoleGuard;
import com.machines.machines_front_end.clients.SearchClient;
import com.machines.machines_front_end.dtos.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/searches")
public class SearchController {
    private final SearchClient searchClient;

    @GetMapping
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String listSearches(Model model) {
        List<Search> searches = searchClient.getAll();

        model.addAttribute("searches", searches);
        return "searches/list";
    }
}