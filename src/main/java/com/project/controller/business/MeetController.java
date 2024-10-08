package com.project.controller.business;

import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meet")
public class MeetController {

    private final MeetService meetService;

    //Note: save() **********************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<MeetResponse>saveMeet(HttpServletRequest httpServletRequest,
                                                 @RequestBody @Valid MeetRequest meetRequest){
        return meetService.saveMeet(httpServletRequest,meetRequest);
    }

    // Note : (ODEV) getAll() ************************************
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/getAll")  //http://localhost:8081/meet/getAll
    public List<MeetResponse>getAll(){
        return meetService.getAll();
    }


    // Note: (ODEV) getAllWithPage() *****************************
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/getAllMeetByPage")  //http://localhost:8081/meet/getAllMeetByPage?page=0&size=1
    public Page<MeetResponse>getAllMeetByPage(
            @RequestParam(value = "page")int page,
            @RequestParam(value = "size")int size
    ){
        return meetService.getAllMeetByPage(page,size);
    }


    // Note: delete() ********************************************
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    @DeleteMapping("/delete/{meetId}")
    public ResponseMessage delete(@PathVariable Long meetId,
                                  HttpServletRequest httpServletRequest){
        return meetService.delete(meetId,httpServletRequest);
    }

    //Note: update() *********************************************
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    @PutMapping("/update/{meetId}")
    public ResponseMessage<MeetResponse>updateMeet(@PathVariable Long meetId,
                                                   @RequestBody @Valid MeetRequest meetRequest,
                                                   HttpServletRequest httpServletRequest){
        return meetService.updateMeet(meetRequest,meetId,httpServletRequest);
    }


    // Note: getAllByAdvTeacher() ********************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllMeetByAdvisorTeacherAsList")
    public ResponseEntity<List<MeetResponse>>getAllMeetByTeacher(HttpServletRequest httpServletRequest){
        return meetService.getAllMeetByTeacher(httpServletRequest);
    }


    // Note: (ODEV) getAllByAdvTeacherByPage() *******************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllMeetByAdvisorAsPage")
    public ResponseEntity<Page<MeetResponse>>getAllMeetByTeacher(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page")int page,
            @RequestParam(value = "size")int size
    ){
        return meetService.getAllMeetByAdvTeacherByPage(httpServletRequest,page,size);
    }

    // Note: getAllMeetByStudent() *******************************
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllMeetByStudent")
    public List<MeetResponse>getAllMeetByStudent(HttpServletRequest httpServletRequest){
        return meetService.getAllMeetByStudent(httpServletRequest);
    }

}
