package com.project.controller.business;

import com.project.entity.business.StudentInfo;
import com.project.payload.request.business.StudentInfoRequest;
import com.project.payload.request.business.UpdateStudentInfoRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.StudentInfoResponse;
import com.project.service.business.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

    private final StudentInfoService studentInfoService;

    //Note : Save() ***************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<StudentInfoResponse>saveStudentInfo(@RequestBody @Valid StudentInfoRequest studentInfoRequest,
                                                               HttpServletRequest httpServletRequest){

        return studentInfoService.saveStudentInfo(httpServletRequest,studentInfoRequest);
    }

    // Note : Delete() ************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @DeleteMapping("/delete/{studentInfoId}")
    public ResponseMessage delete(@PathVariable Long studentInfoId){
        return  studentInfoService.deleteStudentInfo(studentInfoId);
    }

    // Note: getAllWithPage() ******************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllStudentInfoByPage")
    public Page<StudentInfoResponse>getAllStudentInfoByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return studentInfoService.getAllStudentInfoByPage(page,size,sort,type);
    }


    // Note: Update() **************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PutMapping("/update/{studentInfoId}")
    public ResponseMessage<StudentInfoResponse>update(@PathVariable Long studentInfoId,
                                                      @RequestBody @Valid UpdateStudentInfoRequest studentInfoRequest){
        return studentInfoService.update(studentInfoRequest,studentInfoId);
    }


    // Note: getAllForTeacher() ****************************
    // !!! -> Bir ogretmen kendi ogrencilerinin bilgilerini almak isterse :
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllForTeacher")
    public ResponseEntity<Page<StudentInfoResponse>> getAllForTeacher(HttpServletRequest httpServletRequest,
                                                                      @RequestParam(value = "page")int page,
                                                                      @RequestParam(value = "size")int size){
        return new ResponseEntity<>(studentInfoService.getAllForTeacher(httpServletRequest,page,size), HttpStatus.OK);
    }

    // Note:getAllForStudent() *****************************
    // !!! --> bir ogrenci kendi bilgilerini almak isterse
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllForStudent")
    public ResponseEntity<Page<StudentInfoResponse>>getAllForStudent(HttpServletRequest httpServletRequest,
                                                     @RequestParam(value = "page")int page,
                                                     @RequestParam(value = "size")int size){
        return ResponseEntity.ok(studentInfoService.getAllForSetudent(httpServletRequest,page,size));
    }

    // Note: getStudentInfoByStudentId() *******************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getByStudentId/{studentId}")
    public ResponseEntity<List<StudentInfoResponse>>getStudentInfoByStudentId(@PathVariable Long studentId){
        List<StudentInfoResponse> studentInfoResponses = studentInfoService.getStudentInfoByStudentId(studentId);

        return ResponseEntity.ok(studentInfoResponses);
    }

    // Note: getStudentInfoById() **************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/get/{studentInfoId}")
    public ResponseEntity<StudentInfoResponse>getStudentInfoById(@PathVariable Long studentInfoId){
        return ResponseEntity.ok(studentInfoService.getStudentInfoById(studentInfoId));
    }

}
