package com.project.contactmessage.controller;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.dto.ContactMessageUpdateRequest;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.service.ContactMessageService;
import com.project.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    //Not: save() ********************************************
    @PostMapping("/save") //http://localhost:8080/contactMessages/save + POST + JSON
    public ResponseMessage<ContactMessageResponse> save(@RequestBody @Valid ContactMessageRequest contactMessateRequest) {

        return contactMessageService.save(contactMessateRequest);

    }

    //Not: getAll() ******************************************
    @GetMapping("/getAll")
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.getAll(page, size, sort, type);
    }

    //Note: searchByEmail*************************************
    @GetMapping("/searchByEmail") //http://localhost:8081/contactMessage/searchByEmail?email=aaa@bbb.com
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchByEmail(email, page, size, sort, type);
    }

    //Note: searchBySubject **********************************
    @GetMapping("/searchBySubject") //http://localhost:8081/contactMessages/searchBySubject?sumject=deneme
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchBySubject(subject, page, size, sort, type);
    }

    //Note: deleteByIdParam **********************************
    @DeleteMapping("/deleteByIdParam") //http://localhost:8081/contactMessages/deleteByIdParam?contactMessageId=1
    public ResponseEntity<String> deleteById(@RequestParam(value = "contactMessageId") Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    //Note: deleteByIdWithPath *******************************
    @DeleteMapping("/deleteById/{contactMessageId}")//http://localhost:8081/contactMessages/deleteById/1 + delete
    public ResponseEntity<String> deleteByIdPath(@PathVariable Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    //Note: getByIdParam *************************************
    @GetMapping("/getByIdParam") //http://localhost:8081/contactMessages/getBByIdParam?contactMessageId
    public ResponseEntity<ContactMessage> getById(@RequestParam(value = "contactMessageId") Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    //Note: getById ******************************************
    @GetMapping("/getById/{contactMessageId}") //http://localhost:8081/contactMessages/getById/1 + GET
    public ResponseEntity<ContactMessage> getByIdPath(@PathVariable Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    //Note: updateById ***************************************
    @PutMapping("/updateById/{contactMessageId}")//http://localhost:8081/contactMessages/updateById/1 + PUT + JSON
    public ResponseEntity<ResponseMessage<ContactMessageResponse>> updateById(@PathVariable Long contactMessageId,
                                                                              @RequestBody @NotNull ContactMessageUpdateRequest contactMessageUpdateRequest) {

        return ResponseEntity.ok(contactMessageService.updateById(contactMessageId, contactMessageUpdateRequest));
    }

    //Note: Odev --->searchByDateBetween *********************
    @GetMapping("/searchBetweenDates") //http://localhost:8081/contactMessages/searchBetweenDates?beginDate=2023-09-13&endDate=2023-09-15
    public ResponseEntity<List<ContactMessage>>searchByDateBetween(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString){
        List<ContactMessage>contactMessages = contactMessageService.searchByDateBetween(beginDateString,endDateString);
        return ResponseEntity.ok(contactMessages);
    }

    //Note: Odev --->searchByTimeBetween *********************
    @GetMapping("/searchBetweenTimes") //http://localhost:8081.contactMessages/searchBetweenTime?startHour=09&startMinute=00&endHour=17&endMinute=00
    public ResponseEntity<List<ContactMessage>>searchByTimeBetween(
            @RequestParam(value = "startHour") String startHour,
            @RequestParam(value = "startMinute") String startMinute,
            @RequestParam(value = "endHour") String endHour,
            @RequestParam(value = "endMinute") String endMinute){
        List<ContactMessage>contactMessages = contactMessageService.searchByTimeBetween(startHour,startMinute,endHour,endMinute);
        return ResponseEntity.ok(contactMessages);
    }

}
