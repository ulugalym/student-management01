package com.project.repository.business;

import com.project.entity.business.LessonProgram;
import com.project.payload.response.business.LessonProgramResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface LessonProgramRepository extends JpaRepository<LessonProgram,Long> {

    @Query("SELECT l FROM LessonProgram l WHERE l.id IN :myProperty")
    Set<LessonProgram> getLessonProgramNyLessonProgramIdList(Set<Long> myProperty);

    List<LessonProgram> findByUsers_IdNull();

    List<LessonProgram> findByUsers_IdNotNull();

    @Query("SELECT l FROM LessonProgram l INNER JOIN l.users users WHERE users.username = ?1")
    Set<LessonProgram>getLessonProgramByUsersUsername(String username);


    Set<LessonProgram> findByUsers_IdEquals(Long teacherId);
}
