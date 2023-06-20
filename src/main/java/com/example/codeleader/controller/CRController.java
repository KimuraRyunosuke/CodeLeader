package com.example.codeleader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CRController {
	@GetMapping("/Home")
	public String index(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		return "index";
	}

	@GetMapping("/User")
	public String user(Model model) {
		return "user";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
}