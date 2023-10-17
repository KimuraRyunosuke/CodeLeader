package com.example.codeleader.controller;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.codeleader.data.CodeSet;
import com.example.codeleader.data.PostData;
import com.example.codeleader.data.StringURL;
import com.example.codeleader.entity.Bookmark;
import com.example.codeleader.entity.Code;
import com.example.codeleader.entity.Post;
import com.example.codeleader.entity.User;
import com.example.codeleader.repository.BookmarkRepository;
import com.example.codeleader.repository.CodeRepository;
import com.example.codeleader.repository.PostRepository;
import com.example.codeleader.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Controller
public class CLController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	CodeRepository codeRepository;

	@Autowired
	BookmarkRepository bookmarkRepository;

	long userId = 2;
	Optional<User> anUser;

	@PostConstruct
	public void init() {
		// User
		User u1 = new User();
		u1.setName("Yamada");
		userRepository.save(u1);
		User u2 = new User();
		u2.setName("Tanaka");
		userRepository.save(u2);
		User u3 = new User();
		u3.setName("Sato");
		userRepository.save(u3);
		anUser = userRepository.findById(userId);
	}

	@GetMapping("/home")
	public String home(Model model) {
		Iterable<User> list = userRepository.findAll();
		model.addAttribute("list", list);
		this.setHeader(model);
		return "home";
	}

	@GetMapping("/code")
	public String code(Model model) {
		List<Post> postList = postRepository.findAllByOrderByPostedAtDesc();
		List<CodeSet> newCodeSets = this.makeCodeSetList(postList);
		model.addAttribute("newCodeSets", newCodeSets);
		model.addAttribute("bookmark", false);
		this.setHeader(model);
		return "code";
	}

	@GetMapping("/code/{postId}")
	public String code(Model model, @PathVariable long postId) {
		Optional<Post> post = postRepository.findById(postId);
		CodeSet codeSet = this.makeCodeSet(post.get());
		model.addAttribute("codeSet", codeSet);
		model.addAttribute("bookmark", false);
		this.setHeader(model);
		return "post_code";
	}

	@GetMapping("/mypage")
	public String mypage(Model model) {
		List<Post> postList = postRepository.findAllByOrderByPostedAtDesc();
		List<CodePath> bookmarkCodePaths = this.makeCodeSetList(postList);
		model.addAttribute("bookmarkCodeSets", bookmarkCodePaths);
		this.setHeader(model);
		return "mypage";
	}

	@GetMapping("/post")
	public String post(Model model) {
		PostData postData = new PostData();
		model.addAttribute("postData", postData);
		this.setHeader(model);
		return "post";
	}

	@PostMapping("/post")
	@Transactional(readOnly = false)
	public String submitPost(@Validated @ModelAttribute PostData postData, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			errorList = this.getCodeListErrors(errorList);
			model.addAttribute("validationError", errorList);
			model.addAttribute("postData", postData);
			this.setHeader(model);
			return "post";
		}
		Post aPost = new Post();
		String lang = this.getLang(postData.getCodeList());
		this.setPost(aPost, postData.getTitle(), postData.getComment(), lang);
		this.setCodes(postData.getCodeList(), aPost.getId());
		model.addAttribute("postData", postData);
		this.setHeader(model);
		return "check";
	}

	@GetMapping("/user")
	public String user(Model model) {
		return "user";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/edit/{codeId}")
	public String edit(Model model, @PathVariable long codeId) {
		this.setHeader(model);
		Optional<Code> code = codeRepository.findById(codeId);
		model.addAttribute("codeId", codeId);
		model.addAttribute("postId", code.get().getPostId());
		model.addAttribute("codeTitle", code.get().getFileName());
		model.addAttribute("editUrl", StringURL.getEditURL(code.get().getUrl()));
		model.addAttribute("codeUrl", StringURL.getRawFileURL(code.get().getUrl()));
		if (!bookmarkRepository.findByUserIdAndCodeId(this.userId, code.get().getId()).isEmpty()) {
			return "edit_bookmark";
		}
		return "edit";
	}

	@PostMapping(value = "/edit/{codeId}", params = "bookmark")
	@Transactional(readOnly = false)
	public String bookmark(Model model, @PathVariable long codeId) {
		System.out.println("ok");
		this.setHeader(model);
		Optional<Code> code = codeRepository.findById(codeId);
		model.addAttribute("codeId", codeId);
		model.addAttribute("postId", code.get().getPostId());
		model.addAttribute("codeTitle", code.get().getFileName());
		model.addAttribute("editUrl", StringURL.getEditURL(code.get().getUrl()));
		model.addAttribute("codeUrl", StringURL.getRawFileURL(code.get().getUrl()));
		if (bookmarkRepository.findByUserIdAndCodeId(this.userId, code.get().getId()).isEmpty()) {
			Bookmark aBookmark = new Bookmark();
			aBookmark.setUserId(userId);
			aBookmark.setCodeId(codeId);
			bookmarkRepository.save(aBookmark);
		}
		return "edit_bookmark";
	}

	@PostMapping(value = "/edit/{codeId}", params = "remove")
	@Transactional(readOnly = false)
	public String removeBookmark(Model model, @PathVariable long codeId) {
		this.setHeader(model);
		Optional<Code> code = codeRepository.findById(codeId);
		model.addAttribute("codeId", codeId);
		model.addAttribute("postId", code.get().getPostId());
		model.addAttribute("codeTitle", code.get().getFileName());
		model.addAttribute("editUrl", StringURL.getEditURL(code.get().getUrl()));
		model.addAttribute("codeUrl", StringURL.getRawFileURL(code.get().getUrl()));
		if (!bookmarkRepository.findByUserIdAndCodeId(this.userId, code.get().getId()).isEmpty()) {
			bookmarkRepository
					.delete(bookmarkRepository.findByUserIdAndCodeId(this.userId, code.get().getId()).get(0));
		}
		model.addAttribute("bookmark", true);
		return "edit";
	}

	public void setHeader(Model model) {
		Integer lv = anUser.get().getLv();
		String uName = anUser.get().getName();
		model.addAttribute("lv", lv);
		model.addAttribute("uname", uName);
	}

	public void setPost(Post aPost, String title, String comment, String lang) {
		long millis = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(millis);
		aPost.setUserId(userId);
		aPost.setTitle(title);
		aPost.setComment(comment);
		aPost.setLang(lang);
		aPost.setPostedAt(timestamp);
		postRepository.save(aPost);
	}

	public void setCodes(List<String> codeList, long postId) {
		for (String stringUrl : codeList) {
			Integer loc = StringURL.getLoc(stringUrl);
			Code aCode = new Code();
			aCode.setPostId(postId);
			aCode.setUrl(stringUrl);
			aCode.setFileName(StringURL.getFileName(stringUrl));
			aCode.setLang(StringURL.getExtension(stringUrl));
			aCode.setLoc(loc);
			aCode.setPoint(loc / 51 + 1);
			codeRepository.save(aCode);
		}
	}

	public String getLang(List<String> codeList) {
		List<String> langList = new ArrayList<>();
		for (String stringUrl : codeList) {
			langList.add(StringURL.getExtension(stringUrl));
		}
		String language = langList.get(0);
		String lang = langList.get(0);
		for (Integer i = 1; i < langList.size(); i++) {
			if (!lang.equals(langList.get(i))) {
				lang = langList.get(i);
				language = language + ", " + lang;
			}
		}
		return language;
	}

	public List<String> getCodeListErrors(List<String> errorList) {
		errorList.remove("タイトルを入力してください");
		errorList.remove("コメントを入力してください");
		Integer size = errorList.size();
		if (size <= 1)
			return errorList;
		else if (size % 2 == 1) {
			errorList.clear();
			errorList.add("https://github.com/で始まるURLを入力してください");
		}
		errorList.clear();
		errorList.add("空の入力欄がありました.これで良ければ,もう一度投稿ボタンを押してください");
		return errorList;
	}

	public List<CodeSet> makeCodeSetList(List<Post> postList) {
		List<CodeSet> codeSetList = new ArrayList<>();
		for (Post post : postList) {
			codeSetList.add(this.makeCodeSet(post));
		}
		return codeSetList;
	}

	public CodeSet makeCodeSet(Post post) {
		CodeSet codeSet = new CodeSet();
		codeSet.setPostId(post.getId());
		codeSet.setTitle(post.getTitle());
		codeSet.setComment(post.getComment());
		codeSet.setLang(post.getLang());
		codeSet.setPostedAt(post.getPostedAt());
		codeSet.setCodeList(codeRepository.findByPostId(post.getId()));
		return codeSet;
	}

	public List<CodePath> makeCodePathList(List<Bookmark> bookmark){

	}
	public CodePath mekeCodePath(long codeId){
		
	}

}