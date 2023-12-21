package com.example.codeleader.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.codeleader.entity.Grade;
import com.example.codeleader.entity.User;
import com.example.codeleader.repository.GradeRepository;
import com.example.codeleader.repository.UserRepository;

@Component
@EnableScheduling
public class ScheduledComponent {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GradeRepository gradeRepository;

	@Scheduled(cron = "0 0 4 ? * THU", zone = "Asia/Tokyo")
    public void initData() {
        Iterable<User> userList = userRepository.findAll();
        Integer average = this.averageOfPoints(userList);
        this.initThreshold(average);
        this.initPointAndGrade(userList);
    }

    public Integer averageOfPoints(Iterable<User> userList) {
        Integer total = 0;
        Integer size = 0;
        for (User user : userList) {
            total += user.getPoint();
            size++;
        }
        return total / size;
    }

    public void initThreshold(Integer average) {
        // Aに上がるための閾値
        Grade grade = gradeRepository.findById("A").get();
        grade.setValue(average / 2 * 3);
        gradeRepository.save(grade);
        // Bに上がるための閾値
        grade = gradeRepository.findById("B").get();
        grade.setValue(average);
        gradeRepository.save(grade);
        // Cに上がるための閾値
        grade = gradeRepository.findById("C").get();
        grade.setValue(average / 2);
        gradeRepository.save(grade);
    }

    public void initPointAndGrade(Iterable<User> userList) {
        for (User user : userList) {
            user.setPoint(0);
            user.setGrade("D");
            userRepository.save(user);
        }
    }
}
