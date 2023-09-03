package com.example.codeleader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.codeleader.entity.User;
import com.example.codeleader.repository.UserRepository;

@Controller
public class CLController {

	@Autowired
	UserRepository uRepository;

	@GetMapping("/home")
	public String index(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		User user = new User();
		user.setName("A");
		//user.setGrade("Bronze");
		uRepository.save(user);
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		Iterable<User> list = uRepository.findAll();
		model.addAttribute("list", list);
		
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

	@GetMapping("/post")
	public String post(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		return "post";
	}


	@GetMapping("/user")
	public String user(Model model) {
		return "user";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/edit")
	public String edit(Model model) {
		String rank = "ゲスト";
		String lv = "--";
		String title = "---";
		String uname = "ゲスト";
		model.addAttribute("rank", rank);
		model.addAttribute("lv", lv);
		model.addAttribute("title", title);
		model.addAttribute("uname", uname);
		return "edit";
	}

}