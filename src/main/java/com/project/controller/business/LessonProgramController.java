package com.project.controller.business;

import com.project.entity.business.LessonProgram;
import com.project.payload.request.business.LessonProgramRequest;
import com.project.payload.response.business.LessonProgramResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessonPrograms")
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;

    //Note: save() *****************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/save") //http://localhost:8081/lessonPrograms/save  + POST + JSON
    public ResponseMessage<LessonProgramResponse>saveLessonProgram(@RequestBody @Valid LessonProgramRequest lessonProgramRequest){
        return lessonProgramService.saveLessonProgram(lessonProgramRequest);
    }

    // Note: getAll() **********************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    @GetMapping("/getAll")
    public List<LessonProgramResponse>getAllLessonProgramByList(){
        return lessonProgramService.getAllLessonProgramByList();
    }

    // Note: getById() *************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getById/{id}")
    public LessonProgramResponse getLessonProgramById(@PathVariable Long id){
        return lessonProgramService.getLessonProgramById(id);
    }

    //Note: getAllLessonProgramUnassigned() ****************
    //Teacher atamasi yapilmamis butun ders programlari getirecegiz
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    @GetMapping("/getAllUnassigned")
    public List<LessonProgramResponse>getAllUnassigned(){
        return lessonProgramService.getAllLessonProgramUnassigned();
    }

    // Note: getAllLessonProgramAssigned() ******************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    @GetMapping("/getAllAssigned")
    public List<LessonProgramResponse>getAllAssigned(){
        return lessonProgramService.getAllAssigned();
    }

    // Note: delete() ****************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage deleteLessonProgramById(@PathVariable Long id){
        return lessonProgramService.deleteLessonProgramById(id);
    }

    // Note: getAllWithPage() ********************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    @GetMapping("/getAllLessonProgramByPage")
    public Page<LessonProgramResponse>getAllLessonProgramByPage(
            @RequestParam(value = "page")int page,
            @RequestParam(value = "size")int size,
            @RequestParam(value = "sort")String sort,
            @RequestParam(value = "type")String type
    ){
        return lessonProgramService.getAllLessonProgramByPage(page, size, sort, type);
    }

    //Note: getLessonProgramByTeacher() ****************
    //bir Ogtermen kendine ait lessonProgram lari getiriyor
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllLessonProgramByTeacher")
    public Set<LessonProgramResponse>getAllLessonProgramByTeacherUsername(HttpServletRequest httpServletRequest){
        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);
    }

    //Note: getLessonProgramByStudent() ****************
    //bir Odrenci kendine ait lessonProgram lari getiriyor
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllLessonProgramByStudent")
    public Set<LessonProgramResponse>getAllLessonProgramByStudent(HttpServletRequest httpServletRequest){
        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);
    }

    //Note: getLessonProgramByTeacherId() ****************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllLessonProgramByTeacherId/{teacherId}")
    public Set<LessonProgramResponse>getByTeacherId(@PathVariable Long teacherId){
        return lessonProgramService.getByTeacherId(teacherId);
    }

    //Note: getLessonProgramByStudentId() ****************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllLessonProgramByStudentId/{studentId}")
    public Set<LessonProgramResponse>getByStudentId(@PathVariable Long studentId){
        return lessonProgramService.getByStudentId(studentId);
    }

}
