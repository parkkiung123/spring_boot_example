package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Student;
import com.example.demo.repositories.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
@Transactional
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student student1;
    private Student student2;
    private Student student3;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();  // 기존 데이터를 모두 삭제합니다.

        // 샘플 학생 데이터 준비
        student1 = new Student(100L, "박영구", 4, "김선생", 80, 90, 85, 70, 60);
        student2 = new Student(101L, "이수봉", 4, "김선생", 95, 85, 90, 90, 88);
        student3 = new Student(102L, "신명철", 4, "김선생", 75, 80, 65, 95, 78);
        
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
    }

    @Test
    public void testFindTopByKorean() {
        List<Student> topKoreanStudent = studentRepository.findTopByKorean();
        assertThat(topKoreanStudent).isNotEmpty();
        assertThat(topKoreanStudent)
            .extracting(Student::getName)
            .contains("이수봉"); // 여러 명 중에 포함되는지만 체크
    }

    @Test
    public void testFindTopByEnglish() {
        List<Student> topEnglishStudent = studentRepository.findTopByEnglish();
        assertThat(topEnglishStudent).isNotEmpty();
        assertThat(topEnglishStudent)
            .extracting(Student::getName)
            .contains("박영구");
    }

    @Test
    public void testFindTopByMath() {
        List<Student> topMathStudent = studentRepository.findTopByMath();
        assertThat(topMathStudent).isNotEmpty();
        assertThat(topMathStudent)
            .extracting(Student::getName)
            .contains("이수봉");
    }

    @Test
    public void testFindTopByScience() {
        List<Student> topScienceStudent = studentRepository.findTopByScience();
        assertThat(topScienceStudent).isNotEmpty();
        assertThat(topScienceStudent)
            .extracting(Student::getName)
            .contains("신명철");
    }

    @Test
    public void testFindTopByHistory() {
        List<Student> topHistoryStudent = studentRepository.findTopByHistory();
        assertThat(topHistoryStudent).isNotEmpty();
        assertThat(topHistoryStudent)
            .extracting(Student::getName)
            .contains("이수봉");
    }

    @Test
    public void testFindTopByAverage() {
        List<Student> topAverageStudent = studentRepository.findTopByAverage();
        assertThat(topAverageStudent).isNotEmpty();
        assertThat(topAverageStudent)
            .extracting(Student::getName)
            .contains("이수봉");
    }

}
