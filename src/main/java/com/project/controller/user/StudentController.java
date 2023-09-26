package com.project.controller.user;

import com.project.payload.request.user.StudentRequest;
import com.project.payload.request.user.StudentRequestWithoutPassword;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    //Note: saveStudent() ************************************
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<ResponseMessage<StudentResponse>>saveStudent(@RequestBody @Valid StudentRequest studentRequest){
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }

    // Note: updateStudentForStudents() **********************
    //ogrenci kendi bilgilerini guncelleme islemi
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PatchMapping("/update")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentRequestWithoutPassword studentRequestWithoutPassword,
                                                HttpServletRequest request){
        return studentService.updateStudent(studentRequestWithoutPassword,request);
    }

    //Note: updateStudent() **********************************
    //yoneticilerin ogrenci bilgilerini guncelleme islemi
    @PreAuthorize("hasAnyAithority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PutMapping("update/{userId}")
    public ResponseMessage<StudentResponse>updateStudentForManagers(@PathVariable Long userId,
                                                                    @RequestBody @Valid StudentRequest studentRequest){
        return studentService.updateStudentForManagers(userId,studentRequest);
    }
}
