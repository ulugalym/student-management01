package com.project.service.validator;

import com.project.entity.business.LessonProgram;
import com.project.exception.ConflictException;
import com.project.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

    public void checkLessonPrograms(Set<LessonProgram>existLessonProgram,
                                    Set<LessonProgram>lessonProgramRequest){
        //TODO : isEmpty control
        if(existLessonProgram.isEmpty()&& lessonProgramRequest.size()>1){
            // !!! talep edilen LessonProgramlar icinde cakisma varmi ??  A
            checkDuplicateLessonPrograms(lessonProgramRequest);
        }else {
            // !!! talep edilen LessonProgramlar icinde cakisma varmi  ?? A
            checkDuplicateLessonPrograms(lessonProgramRequest);

            //!!! talep edilen ile mevcutta cakisma varmi ?? B
            checkDuplicateLessonPrograms(existLessonProgram, lessonProgramRequest);
        }

    }

    private void checkDuplicateLessonPrograms(Set<LessonProgram>lessonPrograms){

        Set<String>uniqueLessonProgramDays = new HashSet<>();
        Set<LocalTime>existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime>existingLessonProgramStopTimes = new HashSet<>();

        for (LessonProgram lessonProgram : lessonPrograms) {

            String lessonProgramDay = lessonProgram.getDay().name();

            // !!! karsilastirilan LessonProgramlar Ayni Gunde ise
            if(uniqueLessonProgramDays.contains(lessonProgramDay)){
                // !!! Baslama saatine gore control
                for (LocalTime startTime : existingLessonProgramStartTimes) {
                    // !!! baslama saati esit ise
                    if(lessonProgram.getStartTime().equals(startTime)){
                        throw new ConflictException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                    // !!! kiyaslanan ders programinin baslama saati eklenmek  istenen dersPrograminin baslama ve bitis saatleri arasindami ??
                    if(lessonProgram.getStartTime().isBefore(startTime)&&lessonProgram.getEndTime().isAfter(startTime)){
                        throw new ConflictException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
                //TODO : tamamen ortada olma

                // !!! Bitis saatine gore control
                for (LocalTime stopTime: existingLessonProgramStopTimes){
                    if (lessonProgram.getStartTime().isBefore(stopTime)&&lessonProgram.getEndTime().isAfter(stopTime)){
                        throw new ConflictException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
            }
        }

    }

    private void checkDuplicateLessonPrograms(Set<LessonProgram>existLessonProgram,
                                              Set<LessonProgram>lessonProgramRequest){

        for (LessonProgram requestLessonProgram: lessonProgramRequest) {
            String requestLessonProgramDay = requestLessonProgram.getDay().name();
            LocalTime requestStart = requestLessonProgram.getStartTime();
            LocalTime requestStop = requestLessonProgram.getEndTime();

            if(existLessonProgram.stream()
                    .anyMatch(lessonProgram -> lessonProgram.getDay().name().equals(requestLessonProgramDay)
                    &&(lessonProgram.getStartTime().equals(requestStart)
                    ||(lessonProgram.getStartTime().isBefore(requestStart)&&lessonProgram.getEndTime().isAfter(requestStart))
                    ||(lessonProgram.getStartTime().isBefore(requestStop)&&lessonProgram.getEndTime().isAfter(requestStop))))
            ){
                throw new ConflictException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
            }
        }
    }
}
