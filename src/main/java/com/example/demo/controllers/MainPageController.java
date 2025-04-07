package com.example.demo.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dto.LoginForm;
import com.example.demo.entities.Student;
import com.example.demo.services.DBService;

@Controller
public class MainPageController {
    private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private final DBService dbService;

    public MainPageController(DBService dbService) {
        this.dbService = dbService;
    }

    @RequestMapping("/")
    public String mainPage(Model model, HttpSession session) {
        LoginForm form = (LoginForm) session.getAttribute("loginUser");
        if (form != null) {
            String role = form.getRole();
            switch (role) {
                case "admin":
                    logger.info("로그인한 선생님: " + form.getId() + ", " + form.getName());
                    List<Student> students = dbService.findAllStudents();
                    if (students.isEmpty()) {
                        logger.info("학생 정보가 없습니다.");
                        model.addAttribute("message", "학생 정보가 없습니다.");
                        return "mainPage";
                    }
                    List<Double> averages = students.stream()
                            .mapToDouble(s -> (s.getKorean() + s.getEnglish() + s.getMath() + s.getScience() + s.getHistory()) / 5.0)
                            .boxed()
                            .toList();              
                    model.addAttribute("students", students);
                    model.addAttribute("averages", averages);
                    // 순서 : 국어, 영어, 수학, 과학, 역사, 평균
                    Map<String, List<Student>> topStudentsMap = new LinkedHashMap<>();
                    topStudentsMap.put("국어", dbService.getTopByKorean());
                    topStudentsMap.put("영어", dbService.getTopByEnglish());
                    topStudentsMap.put("수학", dbService.getTopByMath());
                    topStudentsMap.put("과학", dbService.getTopByScience());
                    topStudentsMap.put("역사", dbService.getTopByHistory());
                    List<Student> averageTop = dbService.getTopByAverage();
                    topStudentsMap.put("평균", averageTop);
                    model.addAttribute("topStudentsMap", topStudentsMap);

                    Student averageTop0 = averageTop.get(0);
                    Double averageTopScore = (averageTop0.getKorean() + averageTop0.getEnglish() + averageTop0.getMath() + averageTop0.getScience() + averageTop0.getHistory()) / 5.0;
                    model.addAttribute("averageTopScore", averageTopScore);

                    topStudentsMap.forEach((subject, studentsList) -> {
                        logger.info(subject + " 최고 점수 학생: " + studentsList);
                    });
                    break;
                case "user":
                    logger.info("로그인한 학생: "  + form.getId() + ", " + form.getName());
                    dbService.findStudentById(Long.parseLong(form.getId()))
                            .ifPresentOrElse(student -> {
                                double average = (student.getKorean() + student.getEnglish() + student.getMath() + student.getScience() + student.getHistory()) / 5.0;
                                model.addAttribute("students", List.of(student));
                                model.addAttribute("averages", List.of(average));
                            }, () -> {
                                throw new IllegalArgumentException("학생 정보가 없습니다.");
                            });
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 사용자 유형입니다.");                    
            }
        } else {
            logger.info("로그인한 유저가 없습니다.");
        }
        model.addAttribute("message", "Hello, Spring MVC!");
        return "mainPage";
    }
}
