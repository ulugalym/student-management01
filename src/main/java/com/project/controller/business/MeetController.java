package com.project.controller.business;

import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.MeetService;
import lombok.RequiredArgsConstructor;
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

    // Note: (ODEV) getAllWithPage() *****************************

    // Note: delete() ********************************************
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    @DeleteMapping("/delete/{meetId}")
    public ResponseMessage delete(@PathVariable Long meetId){
        return meetService.delete(meetId);
    }

    //Note: update() *********************************************
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    @PutMapping("/update/{meetId}")
    public ResponseMessage<MeetResponse>updateMeet(@PathVariable Long meetId,
                                                   @RequestBody @Valid MeetRequest meetRequest){
        return meetService.updateMeet(meetRequest,meetId);
    }


    // Note: getAllByAdvTeacher() ********************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("getAllMeetByAdvisorTeacherAsList")
    public ResponseEntity<List<MeetResponse>>getAllMeetByTeacher(HttpServletRequest httpServletRequest){
        return meetService.getAllMeetByTeacher(httpServletRequest);
    }


    // Note: (ODEV) getAllByAdvTeacherByPage() *******************

    // Note: getAllMeetByStudent() *******************************
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllMeetByStudent")
    public List<MeetResponse>getAllMeetByStudent(HttpServletRequest httpServletRequest){
        return meetService.getAllMeetByStudent(httpServletRequest);
    }

}
