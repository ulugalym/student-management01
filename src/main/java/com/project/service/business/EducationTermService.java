package com.project.service.business;

import com.project.entity.business.EducationTerm;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.EducationTermMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.EducationTermRequest;
import com.project.payload.response.business.EducationTermResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.EducationTermRepository;
import com.project.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;
    private final PageableHelper pageableHelper;

    //Note: save() ************************************
    public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest) {
        // !!! tarih control
        validateEducationTermDates(educationTermRequest);
        // !!! DTO --> POJO
        EducationTerm savedEducationTerm = educationTermRepository.save(
                educationTermMapper.mapEducationTermRequestToEducationTerm(educationTermRequest));

        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccessMessages.EDUCATION_TERM_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(savedEducationTerm))
                .build();


    }

    private void validateEducationTermDatesForRequest(EducationTermRequest educationTermRequest){
        // !!! bu method da amacimiz requestden gelen registrationDate, startDate, endDate arasindaki
        // tarih sirasina gore dogru mu setlenmis onu control etmek
        //registration, startDate den once olmali ..
        if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
            throw new ResourceNotFoundException(ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }
        //endDate, startDate den sonra olmali
        if(educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())){
            throw new BadRequestException(ErrorMessages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }

    }

    private void validateEducationTermDates(EducationTermRequest educationTermRequest){
        validateEducationTermDatesForRequest(educationTermRequest);

        // !!! bir yil icinde bir tane Guz bir tane Yaz donemi olabilir
        if(educationTermRepository.existsByTermAndYear(educationTermRequest.getTerm(),educationTermRequest.getStartDate().getYear())){
            throw new BadRequestException(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR);
        }
        // !!! eklenecek education term, muvcutdaki educationTermler ile tarihsel cakisma control
        if(educationTermRepository.findByYear(educationTermRequest.getStartDate().getYear())
                .stream()
                .anyMatch(educationTerm ->
                        (educationTerm.getStartDate().equals(educationTermRequest.getStartDate())
                        ||(educationTerm.getStartDate().isBefore(educationTermRequest.getStartDate())
                        &&(educationTerm.getEndDate().isBefore(educationTermRequest.getStartDate()))
                        ||(educationTerm.getStartDate().isBefore(educationTermRequest.getEndDate())
                        &&(educationTerm.getEndDate().isAfter(educationTermRequest.getEndDate()))
                        ||educationTerm.getStartDate().isAfter(educationTermRequest.getStartDate()))
                        &&(educationTerm.getEndDate().isBefore(educationTermRequest.getEndDate())))))

        ){
            throw new ConflictException(ErrorMessages.EDUCATION_TERM_CONFLICT_MESSAGE);
        }
    }

    // Note: getById() *******************************
    public EducationTermResponse getEducationTermResponseById(Long id) {

        EducationTerm term = isEducationTermExist(id);
        return educationTermMapper.mapEducationTermToEducationTermResponse(term);
    }

    private EducationTerm isEducationTermExist(Long id){

        return educationTermRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.EDUCATION_TERM_NOT_FOUND_MESSAGE)));
    }

    // Note: getAll() ********************************
    public List<EducationTermResponse> getAllEducationTerms() {
        return educationTermRepository.findAll()
                .stream()
                .map(educationTermMapper::mapEducationTermToEducationTermResponse)
                .collect(Collectors.toList());
    }

    //Note: getAllWithPage() *************************
    public Page<EducationTermResponse> getAllEducationTermsByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);

        return educationTermRepository.findAll(pageable)
                .map(educationTermMapper::mapEducationTermToEducationTermResponse);
    }

    // Note: deleteById() *****************************
    public ResponseMessage<?> deleteEducationTermById(Long id) {
        isEducationTermExist(id);
        educationTermRepository.deleteById(id);
        //educationTermRepository.delete(isEducationTermExist(id));

        //silinen education term e ait olan LessonProgramlar ne olacak ???? Cascade Type dan dolayi herhangi bir logic yapmama gerek yok
        return  ResponseMessage.builder()
                .message(SuccessMessages.EDUCATION_TERM_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Note: updateById() ******************************
    public ResponseMessage<EducationTermResponse> updateEducationTerm(Long id, EducationTermRequest educationTermRequest) {
        isEducationTermExist(id);
        validateEducationTermDates(educationTermRequest);

        EducationTerm educationTermUpdated =
                educationTermRepository.save(educationTermMapper.mapEducationTermRequestToUpdatedEducationTerm(id,educationTermRequest));

        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccessMessages.EDUCATION_TERM_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(educationTermUpdated))
                .build();
    }

    // Note: LessonProgramService icin yazindi
    public EducationTerm getEducationTermById(Long id){
        return isEducationTermExist(id);
    }
}
