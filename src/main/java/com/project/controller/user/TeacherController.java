package com.project.controller.user;

import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
import com.project.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    //Note: saveTeacher() **************************************
    @PostMapping("/save")//http://localhost:8081/teacher/save  +  POST  +  JSON
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<TeacherResponse>>saveTeacher(@RequestBody @Valid TeacherRequest teacherRequest){
        return ResponseEntity.ok(teacherService.saveTeacher(teacherRequest));
    }

    // Note : updateTeacherById() ***********************************
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse>updateTeacherForManagers(@RequestBody @Valid TeacherRequest teacherRequest,
                                                                    @PathVariable Long userId){
        return teacherService.updateTeacherForManagers(teacherRequest,userId);
    }

    // !!! Note: GetAllStudentByAdvTeacherUserName() *****************
    //bir rehber ogretmeninin kendi ogrencilerini tamamini getiren method
    @GetMapping("/getAllStudentByAdvisorUserName")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<StudentResponse>getAllStudentByAdvisorUserName(HttpServletRequest request){
        String userName = request.getHeader("username");
        return teacherService.getAllStudentByAdvisorUsername(userName);
    }

}
