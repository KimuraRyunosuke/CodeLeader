package com.example.codeleader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CLController {
	@GetMapping("/home")
	public String index(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		return "home";
	}

	@GetMapping("/code")
	public String code(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		return "code";
	}

	@GetMapping("/mypage")
	public String mypage(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		return "mypage";
	}

	@GetMapping("/user")
	public String user(Model model) {
		return "user";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

}