package com.example.demo.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Student;
import com.example.demo.entities.Teacher;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DBService implements UserDetailsService{

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public DBService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    // ==== Student CRUD ====

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsStudentById(Long id) {
        return studentRepository.existsById(id);
    }

    // ==== Teacher CRUD ====

    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    public boolean existsTeacherById(Long id) {
        return teacherRepository.existsById(id);
    }

    // ==== Top Student by Subject ====

    public List<Student> getTopByKorean() {
        return studentRepository.findTopByKorean();
    }

    public List<Student> getTopByEnglish() {
        return studentRepository.findTopByEnglish();
    }

    public List<Student> getTopByMath() {
        return studentRepository.findTopByMath();
    }

    public List<Student> getTopByScience() {
        return studentRepository.findTopByScience();
    }

    public List<Student> getTopByHistory() {
        return studentRepository.findTopByHistory();
    }

    public List<Student> getTopByAverage() {
        return studentRepository.findTopByAverage();
    }

    @Override
    public UserDetails loadUserByUsername(String idWithRole) throws UsernameNotFoundException {
        String[] parts = idWithRole.split(":");
        String id = parts[0];
        String role = parts[1];

        if (role.equals("user")) {
            return studentRepository.findById(Long.parseLong(id))
                    .map(student -> User.builder()
                    .username(Long.toString(student.getId()))
                    .password(student.getName()) // 반드시 인코딩되어 있어야 함
                    .roles("user")
                    .build())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        } else if (role.equals("admin")) {
            return teacherRepository.findById(Long.parseLong(id))
                    .map(teacher -> User.builder()
                    .username(Long.toString(teacher.getId()))
                    .password(teacher.getName()) // 반드시 인코딩되어 있어야 함
                    .roles("admin")
                    .build())
                    .orElseThrow(() -> new UsernameNotFoundException("Admin not found with id: " + id));
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
