package com.project.controller.business;

import com.project.entity.business.Lesson;
import com.project.payload.request.business.LessonRequest;
import com.project.payload.response.business.LessonResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    //Note: save() ************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/save")
    public ResponseMessage<LessonResponse>saveLesson(@RequestBody @Valid LessonRequest lessonRequest){
        return lessonService.saveLesson(lessonRequest);
    }
    //Note : deleteById() ******************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage deleteLesson(@PathVariable Long id){
        return lessonService.deleteLessonById(id);
    }

    //Note: getByName() ********************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getLessonByName")
    public ResponseMessage<LessonResponse>getLessonByLessonName(@RequestParam String lessonName){
        return lessonService.getLessonByLessonName(lessonName);
    }

    // Note: getAllWithPAge() **************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/findLessonByPage")
    public Page<LessonResponse>findLessonByPage(
            @RequestParam(value = "page")int page,
            @RequestParam(value = "size")int size,
            @RequestParam(value = "sort")String sort,
            @RequestParam(value = "type")String type
    ){
        return lessonService.findLessonByPage(page,size,sort,type);
    }

    // Note: getLessonsByIdList() *********************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllLessonByLessonId") //http://localhost:8081/lessons/getAllLessonByLessonId?LessonId=1,2,3 +GET
    public Set<Lesson>getAllLessonByLessonId(@RequestParam(name = "LessonId")Set<Long>idSet){
        return lessonService.getLessonByLessonIdSet(idSet);
    }

    //Note: updateById() ********************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PutMapping("/update/{lessonId}") //http://localhost:8081/lessons/update/3
    public ResponseEntity<LessonResponse>updateLessonById(@PathVariable Long lessonId,
                                                          @RequestBody LessonRequest lessonRequest){
        return ResponseEntity.ok(lessonService.updateLessonById(lessonId,lessonRequest));
    }

}
