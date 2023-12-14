package com.example.codeleader.controller;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;
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

import com.example.codeleader.data.CodePath;
import com.example.codeleader.data.CodeSet;
import com.example.codeleader.data.Id;
import com.example.codeleader.data.KeyWord;
import com.example.codeleader.data.PostData;
import com.example.codeleader.data.StringURL;
import com.example.codeleader.entity.Access;
import com.example.codeleader.entity.Bookmark;
import com.example.codeleader.entity.Code;
import com.example.codeleader.entity.FinishedReading;
import com.example.codeleader.entity.Grade;
import com.example.codeleader.entity.Memo;
import com.example.codeleader.entity.Post;
import com.example.codeleader.entity.User;
import com.example.codeleader.repository.AccessRepository;
import com.example.codeleader.repository.BookmarkRepository;
import com.example.codeleader.repository.CodeRepository;
import com.example.codeleader.repository.FinishedReadingRepository;
import com.example.codeleader.repository.GradeRepository;
import com.example.codeleader.repository.MemoRepository;
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

	@Autowired
	AccessRepository accessRepository;

	@Autowired
	FinishedReadingRepository finishedReadingRepository;

	@Autowired
	MemoRepository memoRepository;

	@Autowired
	GradeRepository gradeRepository;

	long userId = 0;
	Optional<User> anUser;

	@PostConstruct
	public void init() {
		// User
		User user = new User();
		user.setName("Yamada");
		userRepository.save(user);
		user = new User();
		user.setName("Tanaka");
		userRepository.save(user);
		user = new User();
		user.setName("Sato");
		userRepository.save(user);

		// ランクの閾値
		Grade grade = new Grade();
		grade.setId("A");
		grade.setValue(30);
		gradeRepository.save(grade);
		grade.setId("B");
		grade.setValue(20);
		gradeRepository.save(grade);
		grade.setId("C");
		grade.setValue(10);
		gradeRepository.save(grade);

		anUser = userRepository.findById(this.userId);
	}

	@PostMapping("/home")
	public String start(@ModelAttribute("userId") Id Id, Model model) {
		this.userId = Id.getUserId();
		if (!this.checkLogin(model))
			return "login";
		this.anUser = userRepository.findById(this.userId);
		Iterable<User> list = userRepository.findAll();
		model.addAttribute("list", list);
		this.setPopularityCodePathsModel(model);
		this.setHeaderModel(model);
		return "home";
	}

	@GetMapping("/home")
	public String home(Model model) {
		if (!this.checkLogin(model))
			return "login";
		Iterable<User> list = userRepository.findAll();
		model.addAttribute("list", list);
		this.setRecommendCodePathsModel(model);
		this.setPopularityCodePathsModel(model);
		this.setHeaderModel(model);
		return "home";
	}

	@GetMapping("/code")
	public String code(Model model) {
		if (!this.checkLogin(model))
			return "login";
		model.addAttribute("keyWord", new KeyWord());
		this.setNewCodeSetsModel(model);
		this.setHeaderModel(model);
		return "code";
	}

	@PostMapping("/code")
	public String search(@ModelAttribute("keyWord") KeyWord keyWord, Model model) {
		this.setFindByPostModel(keyWord.getKey(), model);
		this.setFindByCodeModel(keyWord.getKey(), model);
		this.setHeaderModel(model);
		return "result";
	}

	@GetMapping("/code/{postId}")
	public String code(Model model, @PathVariable long postId) {
		if (!this.checkLogin(model))
			return "login";
		this.setCodeSetModel(model, postId);
		this.setHeaderModel(model);
		return "post_code";
	}

	@GetMapping("/memo/{codeId}")
	@Transactional(readOnly = false)
	public String memo(Model model, @PathVariable long codeId) {
		if (!this.checkLogin(model))
			return "login";
		this.setMemoModel(model, codeId);
		this.setHeaderModel(model);
		return "memo";
	}

	@PostMapping("/memo/{codeId}")
	@Transactional(readOnly = false)
	public String saveMemo(Model model, @ModelAttribute("memo") Memo bcMemo, @PathVariable long codeId) {
		Memo memo = memoRepository.findByUserIdAndCodeId(this.userId, codeId).get(0);
		if (memo.getAddPoint() > 0) {
			this.updateMPoint(memo.getId());
			this.updateMExp(memo.getId());
			memo.setAddPoint(0);
		}
		memo.setText(bcMemo.getText());
		memoRepository.save(memo);
		this.setHeaderModel(model);
		this.setMemoModel(model, codeId);
		return "memo";
	}

	@GetMapping("/mypage")
	public String mypage(Model model) {
		if (!this.checkLogin(model))
			return "login";
		this.setHeaderModel(model);
		this.setBookmarkCodePathsModel(model);
		this.setAccessCodePathsModel(model);
		this.setMyCodeSetsModel(model);
		return "mypage";
	}

	@GetMapping("/post")
	public String post(Model model) {
		if (!this.checkLogin(model))
			return "login";
		model.addAttribute("postData", new PostData());
		this.setHeaderModel(model);
		return "post";
	}

	@PostMapping("/post")
	@Transactional(readOnly = false)
	public String submitPost(@Validated @ModelAttribute PostData postData, BindingResult bindingResult, Model model) {
		if (!this.checkLogin(model))
			return "login";
		if (bindingResult.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			errorList = this.getCodeListErrors(errorList);
			model.addAttribute("validationError", errorList);
			model.addAttribute("postData", postData);
			this.setHeaderModel(model);
			return "post";
		}
		Post aPost = new Post();
		String lang = this.getLangs(postData.getCodeList());
		this.addPost(aPost, postData.getTitle(), postData.getComment(), lang);
		this.addCodes(postData.getCodeList(), aPost.getId());
		model.addAttribute("postData", postData);
		this.setHeaderModel(model);
		return "check";
	}

	@GetMapping("/user")
	public String user(Model model) {
		if (!this.checkLogin(model))
			return "login";
		return "user";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("Id", new Id());
		return "login";
	}

	@GetMapping("/edit/{codeId}")
	@Transactional(readOnly = false)
	public String edit(Model model, @PathVariable long codeId) {
		if (!this.checkLogin(model))
			return "login";
		this.setHeaderModel(model);
		this.access(codeId);
		this.setEditModel(model, codeId);
		if (!bookmarkRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			if (!finishedReadingRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
				return "finished_bookmark";
			}
			return "edit_bookmark";
		}
		if (!finishedReadingRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			return "finished";
		}
		return "edit";
	}

	@PostMapping(value = "/edit/{codeId}", params = "bookmark")
	@Transactional(readOnly = false)
	public String bookmark(Model model, @PathVariable long codeId) {
		if (!this.checkLogin(model))
			return "login";
		System.out.println("ok");
		this.setHeaderModel(model);
		this.setEditModel(model, codeId);
		if (bookmarkRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			Bookmark aBookmark = new Bookmark();
			aBookmark.setUserId(this.userId);
			aBookmark.setCodeId(codeId);
			bookmarkRepository.save(aBookmark);
		}
		if (!finishedReadingRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			return "finished_bookmark";
		}
		return "edit_bookmark";
	}

	@PostMapping(value = "/edit/{codeId}", params = "remove")
	@Transactional(readOnly = false)
	public String removeBookmark(Model model, @PathVariable long codeId) {
		if (!this.checkLogin(model))
			return "login";
		this.setHeaderModel(model);
		this.setEditModel(model, codeId);
		if (!bookmarkRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			bookmarkRepository
					.delete(bookmarkRepository.findByUserIdAndCodeId(this.userId, codeId).get(0));
		}
		model.addAttribute("bookmark", true);
		if (!finishedReadingRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			return "finished";
		}
		return "edit";
	}

	@PostMapping(value = "/edit/{codeId}", params = "finish")
	@Transactional(readOnly = false)
	public String finishedReading(Model model, @PathVariable long codeId) {
		if (!this.checkLogin(model))
			return "login";
		if (finishedReadingRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			this.updatePoint(codeId);
			this.updateExp(codeId);
			this.updateReaderCount(codeId);
			this.addFinishedReading(codeId);
		}
		this.setEditModel(model, codeId);
		this.setHeaderModel(model);
		if (!bookmarkRepository.findByUserIdAndCodeId(this.userId, codeId).isEmpty()) {
			return "finished_bookmark";
		}
		return "finished";
	}

	public boolean checkLogin(Model model) {
		if (this.userId != 0) {
			return true;
		}
		model.addAttribute("Id", new Id());
		return false;
	}

	public void setHeaderModel(Model model) {
		anUser = userRepository.findById(this.userId);
		Integer lv = anUser.get().getLv();
		String uName = anUser.get().getName();
		String rank = anUser.get().getGrade();
		model.addAttribute("lv", lv);
		model.addAttribute("uname", uName);
		model.addAttribute("rank", rank);
	}

	public void setRecommendCodePathsModel(Model model) {
		List<Code> codeList = codeRepository.findAllByOrderByReaderCountDesc();
		List<Code> recommendCodeList = this.makeRecommendCodeList(codeList);
		List<CodePath> recommendCodePaths = this.makeCodePathList(recommendCodeList);
		model.addAttribute("recommendCodePaths", recommendCodePaths);
	}

	public void setPopularityCodePathsModel(Model model) {
		List<Code> codeList = codeRepository.findAllByOrderByReaderCountDesc();
		List<CodePath> popularityCodePaths = this.makeCodePathList(codeList);
		model.addAttribute("popularityCodePaths", popularityCodePaths);
	}

	public void setCodeSetModel(Model model, long postId) {
		Optional<Post> post = postRepository.findById(postId);
		CodeSet codeSet = this.makeCodeSet(post.get());
		model.addAttribute("codeSet", codeSet);
	}

	public void setNewCodeSetsModel(Model model) {
		List<Post> postList = postRepository.findAllByOrderByPostedAtDesc();
		List<CodeSet> newCodeSets = this.makeCodeSetList(postList);
		model.addAttribute("newCodeSets", newCodeSets);
	}

	public void setBookmarkCodePathsModel(Model model) {
		List<Bookmark> bookmarkList = bookmarkRepository.findByUserIdOrderByIdDesc(this.userId);
		List<CodePath> bookmarkCodePaths = this.makeBookmarkCodePathList(bookmarkList);
		model.addAttribute("bookmarkCodePaths", bookmarkCodePaths);
	}

	public void setAccessCodePathsModel(Model model) {
		List<Access> accessList = accessRepository.findByUserIdOrderByAccessedAtDesc(this.userId);
		List<CodePath> accessCodePaths = this.makeAccessCodePathList(accessList);
		model.addAttribute("accessCodePaths", accessCodePaths);
	}

	public void setMyCodeSetsModel(Model model) {
		List<Post> postList = postRepository.findByUserIdOrderByPostedAtDesc(this.userId);
		List<CodeSet> myCodeSets = this.makeCodeSetList(postList);
		model.addAttribute("myCodeSets", myCodeSets);
	}

	public void setFindByPostModel(String keyWord, Model model) {
		List<Post> postList = postRepository.findByTitleContaining(keyWord);
		postList.addAll(postRepository.findByCommentContaining(keyWord));
		List<CodeSet> resultCodeSets = this.makeCodeSetList(postList);
		model.addAttribute("resultCodeSets", resultCodeSets);
	}

	public void setFindByCodeModel(String keyWord, Model model) {
		List<Code> codeList = codeRepository.findByFileNameContaining(keyWord);
		List<CodePath> fileNameCodePaths = this.makeCodePathList(codeList);
		codeList = codeRepository.findByLangContaining(keyWord);
		List<CodePath> langCodePaths = this.makeCodePathList(codeList);
		model.addAttribute("fileNameCodePaths", fileNameCodePaths);
		model.addAttribute("langCodePaths", langCodePaths);
	}

	public void setEditModel(Model model, long codeId) {
		Code code = codeRepository.findById(codeId).get();
		model.addAttribute("codeId", codeId);
		model.addAttribute("postId", code.getPostId());
		model.addAttribute("codeTitle", code.getFileName());
		model.addAttribute("editUrl", StringURL.getEditURL(code.getUrl()));
		model.addAttribute("codeUrl", StringURL.getRawFileURL(code.getUrl()));
	}

	public void setMemoModel(Model model, long codeId) {
		Code code = codeRepository.findById(codeId).get();
		model.addAttribute("codeId", codeId);
		model.addAttribute("codeTitle", code.getFileName());
		model.addAttribute("memo", this.getMemo(codeId));
	}

	public void addPost(Post aPost, String title, String comment, String lang) {
		long millis = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(millis);
		aPost.setUserId(userId);
		aPost.setTitle(title);
		aPost.setComment(comment);
		aPost.setLang(lang);
		aPost.setPostedAt(timestamp);
		postRepository.save(aPost);
	}

	public void addCodes(List<String> codeList, long postId) {
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

	public void addFinishedReading(long codeId) {
		long millis = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(millis);
		FinishedReading finishedReading = new FinishedReading();
		finishedReading.setUserId(this.userId);
		finishedReading.setCodeId(codeId);
		finishedReading.setFinishedAt(timestamp);
		finishedReadingRepository.save(finishedReading);
	}

	public void updatePoint(long codeId) {
		User user = userRepository.findById(this.userId).get();
		Code code = codeRepository.findById(codeId).get();
		Integer point = user.getPoint() + code.getPoint();
		user.setPoint(point);
		this.updateGrade(user);
	}

	public void updateMPoint(long memoId) {
		User user = userRepository.findById(this.userId).get();
		Memo memo = memoRepository.findById(memoId).get();
		Integer point = user.getPoint() + memo.getAddPoint();
		user.setPoint(point);
		this.updateGrade(user);
	}

	public void updateGrade(User user) {
		if (user.getPoint() >= gradeRepository.findById("A").get().getValue()) {
			user.setGrade("A");
		} else if (user.getPoint() >= gradeRepository.findById("B").get().getValue()) {
			user.setGrade("B");
		} else if (user.getPoint() >= gradeRepository.findById("C").get().getValue()) {
			user.setGrade("C");
		}
		userRepository.save(user);
	}

	public void updateExp(long codeId) {
		User user = userRepository.findById(this.userId).get();
		Code code = codeRepository.findById(codeId).get();
		Integer exp = user.getExp() + code.getPoint();
		user.setExp(exp);
		this.updateLv(user);
	}

	public void updateMExp(long memoId) {
		User user = userRepository.findById(this.userId).get();
		Memo memo = memoRepository.findById(memoId).get();
		Integer exp = user.getExp() + memo.getAddPoint();
		user.setExp(exp);
		this.updateLv(user);
	}

	public void updateLv(User user) {
		while (user.getExp() / (user.getLv() * 5) >= 1) {
			user.setExp(user.getExp() - (user.getLv() * 5));
			user.setLv(user.getLv() + 1);
		}
		userRepository.save(user);
	}

	public void updateReaderCount(long codeId) {
		Code code = codeRepository.findById(codeId).get();
		code.setReaderCount(code.getReaderCount() + 1);
		codeRepository.save(code);
	}

	public Memo getMemo(long codeId) {
		List<Memo> memos = memoRepository.findByUserIdAndCodeId(this.userId, codeId);
		if (memos.isEmpty()) {
			Integer addPoint = codeRepository.findById(codeId).get().getPoint();
			Memo newMemo = new Memo();
			newMemo.setUserId(userId);
			newMemo.setCodeId(codeId);
			newMemo.setAddPoint(addPoint);
			memoRepository.save(newMemo);
			return newMemo;
		}
		return memos.get(0);
	}

	public String getLangs(List<String> codeList) {
		List<String> langList = new ArrayList<>();
		for (String stringUrl : codeList) {
			langList.add(StringURL.getExtension(stringUrl));
		}
		String language = "";
		for (int i = 0; i < langList.size(); i++) {
			String lang = langList.get(i);
			this.removeLang(langList, lang, i);
			language += lang;
			if ((i + 1) < langList.size())
				language += ", ";
		}
		return language;
	}

	public void removeLang(List<String> langList, String lang, int start) {
		for (int i = start + 1; i < langList.size(); i++) {
			if (lang.equals(langList.get(i))) {
				langList.remove(i);
				i--;
			}
		}
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

	public List<Code> makeRecommendCodeList(List<Code> codeList) {
		List<String> langList = this.getRecommendLang();
		String prevLang = langList.get(0);
		List<Code> recommendCodeList = new ArrayList<>();
		List<Code> popularCodeList = codeRepository.findByLangOrderByReaderCountDesc(prevLang);
		for (String lang : langList) {
			if (!prevLang.equals(lang)) {
				popularCodeList = codeRepository.findByLangOrderByReaderCountDesc(lang);
			}
			this.removeAlreadyRead(popularCodeList);
			this.takeOutCodeList(popularCodeList, recommendCodeList);
			prevLang = lang;
		}
		return recommendCodeList;
	}

	public void removeAlreadyRead(List<Code> popularCodeList) {
		int cnt = 0;
		for (int i = 0; cnt < 10; i++) {
			if (i >= popularCodeList.size())
				break;
			Code popularCode = popularCodeList.get(i);
			List<FinishedReading> finishedReading = finishedReadingRepository.findByUserIdAndCodeId(userId,
					popularCode.getId());
			if (finishedReading.isEmpty())
				cnt++;
			else {
				popularCodeList.remove(i);
				i--;
			}
		}
	}

	public void takeOutCodeList(List<Code> popularCodeList, List<Code> recommendCodeList) {
		if (popularCodeList.size() >= 2) {
			recommendCodeList.add(popularCodeList.get(0));
			popularCodeList.remove(0);
			recommendCodeList.add(popularCodeList.get(0));
			popularCodeList.remove(0);
			return;
		}
		while (popularCodeList.size() > 0) {
			recommendCodeList.add(popularCodeList.get(0));
			popularCodeList.remove(0);
		}
		return;
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

	public List<CodePath> makeCodePathList(List<Code> codeList) {
		List<CodePath> codePathList = new ArrayList<>();
		for (Code code : codeList) {
			codePathList.add(this.makeCodePath(code.getId()));
		}
		return codePathList;
	}

	public List<CodePath> makeBookmarkCodePathList(List<Bookmark> bookmarkList) {
		List<CodePath> codePathList = new ArrayList<>();
		for (Bookmark bookmark : bookmarkList) {
			codePathList.add(this.makeCodePath(bookmark.getCodeId()));
		}
		return codePathList;
	}

	public List<CodePath> makeAccessCodePathList(List<Access> accessList) {
		List<CodePath> codePathList = new ArrayList<>();
		for (Access access : accessList) {
			codePathList.add(this.makeCodePath(access.getCodeId()));
		}
		return codePathList;
	}

	public CodePath makeCodePath(long codeId) {
		Code code = codeRepository.findById(codeId).get();
		CodePath codePath = new CodePath();
		String path = postRepository.findById(code.getPostId()).get().getTitle() + "/" + code.getFileName();
		codePath.setCodeId(code.getId());
		codePath.setPath(path);
		return codePath;
	}

	public List<String> makeLangList(List<FinishedReading> finishedReadingList) {
		List<String> langList = new ArrayList<>();
		for (FinishedReading finishedReading : finishedReadingList) {
			langList.add(codeRepository.findById(finishedReading.getCodeId()).get().getLang());
		}
		if (finishedReadingList.isEmpty()) {
			langList.add("java");
			langList.add("java");
			langList.add("py");
			langList.add("py");
			langList.add("c");
		}
		while (langList.size() < 5) {
			Random rand = new Random();
			int rNum = rand.nextInt(3);
			switch (rNum) {
				case 0:
					langList.add("java");
					break;
				case 1:
					langList.add("py");
					break;
				case 2:
					langList.add("c");
					break;
				default:
					langList.add("c");
					break;
			}
		}
		langList.sort(null);
		return langList;
	}

	public void access(long codeId) {
		List<Access> accessList = accessRepository.findByUserIdAndCodeId(this.userId, codeId);
		long millis = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(millis);
		if (accessList.isEmpty()) {
			Access access = new Access();
			access.setUserId(this.userId);
			access.setCodeId(codeId);
			access.setAccessedAt(timestamp);
			accessRepository.save(access);
		} else {
			Access access = accessList.get(0);
			access.setAccessedAt(timestamp);
			accessRepository.save(access);
		}
	}

	public List<String> getRecommendLang() {
		List<FinishedReading> recentFive = finishedReadingRepository.findTop5ByUserIdOrderByFinishedAtDesc(this.userId);
		List<String> langList = this.makeLangList(recentFive);
		return langList;
	}



}