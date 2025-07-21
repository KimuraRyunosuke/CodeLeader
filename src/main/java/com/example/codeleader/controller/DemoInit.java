package com.example.codeleader.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.codeleader.data.StringURL;
import com.example.codeleader.entity.Code;
import com.example.codeleader.entity.Post;
import com.example.codeleader.entity.User;
import com.example.codeleader.repository.CodeRepository;
import com.example.codeleader.repository.PostRepository;
import com.example.codeleader.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Controller
public class DemoInit {

    CLController clController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CodeRepository codeRepository;

    @PostConstruct
    public void init() {
        this.makeDemoUser();
        this.makeDemoPost();
    }

    public void makeDemoUser() {
        this.setUser("Yamada");
        this.setUser("Tanaka");
        this.setUser("Sato");
    }

    public void makeDemoPost() {
        String title;
        String comment;
        List<String> codeList = new ArrayList<>();
        List<Integer> readerCount = new ArrayList<>();
        // post1
        title = "HelloWorld(java)";
        comment = "javaで書かれたHelloWorldです.\n出力方法などjavaについて学びましょう.";
        codeList.add(
                "https://github.com/tamadalab/CodeLeader/blob/sample/HelloWorld.java");
        readerCount.add(9);
        this.setPost(title, comment, codeList, readerCount);
        // post2
        codeList = new ArrayList<>();
        readerCount = new ArrayList<>();
        title = "HelloWorld(python)";
        comment = "pythonで書かれたHelloWorldです.\n出力方法などpythonについて学びましょう.";
        codeList.add(
                "https://github.com/tamadalab/CodeLeader/blob/sample/HelloWorld.py");
        readerCount.add(10);
        this.setPost(title, comment, codeList, readerCount);
        // post3
        codeList = new ArrayList<>();
        readerCount = new ArrayList<>();
        title = "HelloWorld(c)";
        comment = "c言語で書かれたHelloWorldです.\n出力方法などpythonについて学びましょう.";
        codeList.add(
                "https://github.com/tamadalab/CodeLeader/blob/sample/HelloWorld.c");
        readerCount.add(8);
        this.setPost(title, comment, codeList, readerCount);
        // post3
        codeList = new ArrayList<>();
        readerCount = new ArrayList<>();
        title = "BuildTool検出システム";
        comment = "何のBuildToolを使っているか検出してくれるシステムです.\nファイルやフォルダの操作,コマンドラインツールのオプションの作り方などについて学びましょう.";
        codeList.add(
                "https://github.com/tamadalab/beacher/blob/main/app/src/main/java/com/github/tamadalab/beacher/Example.java");
        readerCount.add(5);
        codeList.add(
                "https://github.com/tamadalab/beacher/blob/main/app/src/main/java/com/github/tamadalab/beacher/Cli.java");
        readerCount.add(5);
        codeList.add(
                "https://github.com/tamadalab/beacher/blob/main/app/src/main/java/com/github/tamadalab/beacher/Beacher.java");
        readerCount.add(5);
        this.setPost(title, comment, codeList, readerCount);

        // post4
        codeList = new ArrayList<>();
        readerCount = new ArrayList<>();
        title = "CodeLeader";
        comment = "このWebアプリのプログラムです.\nSpringBootを使用したWebアプリの作り方などについて学びましょう.";
        codeList.add(
                "https://github.com/tamadalab/CodeLeader/blob/code/src/main/java/com/example/codeleader/controller/CLController.java");
        readerCount.add(7);
        codeList.add(
                "https://github.com/tamadalab/CodeLeader/blob/code/src/main/resources/templates/mypage.html");
        readerCount.add(6);
        codeList.add(
                "https://github.com/tamadalab/CodeLeader/blob/code/src/main/java/com/example/codeleader/entity/User.java");
        readerCount.add(5);
        this.setPost(title, comment, codeList, readerCount);

    }

    public void setUser(String name) {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
    }

    public void setPost(String title, String comment, List<String> codeList, List<Integer> readerCount) {
        Post post = new Post();
        String lang = this.getLangs(codeList);
        this.addPost(post, title, comment, lang);
        this.addCodes(codeList, post.getId(), readerCount);
    }

    public void addPost(Post aPost, String title, String comment, String lang) {
        long millis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(millis);
        aPost.setUserId(1);
        aPost.setTitle(title);
        aPost.setComment(comment);
        aPost.setLang(lang);
        aPost.setPostedAt(timestamp);
        postRepository.save(aPost);
    }

    public void addCodes(List<String> codeList, long postId, List<Integer> readerCount) {
        Integer i = 0;
        for (String stringUrl : codeList) {
            Integer loc = StringURL.getLoc(stringUrl);
            Code aCode = new Code();
            aCode.setPostId(postId);
            aCode.setUrl(stringUrl);
            aCode.setFileName(StringURL.getFileName(stringUrl));
            aCode.setLang(StringURL.getExtension(stringUrl));
            aCode.setLoc(loc);
            aCode.setPoint(loc / 51 + 1);
            aCode.setReaderCount(readerCount.get(i));
            codeRepository.save(aCode);
            i++;
        }
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

}
