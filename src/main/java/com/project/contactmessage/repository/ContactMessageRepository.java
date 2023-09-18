package com.project.contactmessage.repository;

import com.project.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByEmailEquals(String email, Pageable pageable);
    Page<ContactMessage> findBySubjectEquals(String subject, Pageable pageable);
/*
FUNCTION('DATE', c.dateTime): Bu bolum c.dateTime alaninin tarih bilesenini cikarmak icin kullanilir.
c.dateTime bir tarih-saat nesnesi icerdiginden ve bu sorgu yalnizca tarih bilesenini kullanmak istediginden,
FUNCTION islevi ile tarih bileseni cikarilir.
 */
    @Query("select c from ContactMessage c where FUNCTION('DATE', c.dateTime) between ?1 and ?2")
    List<ContactMessage> findMessagesBetweenDates(LocalDate beginDate, LocalDate endDate);

    @Query("SELECT c FROM ContactMessage c WHERE" +
            "(EXTRACT(HOUR FROM c.dateTime)BETWEEN :startHour AND :endHour) AND " +
            "(EXTRACT(HOUR FROM c.dateTime) != :startHour OR EXTRACT(MINUTE FROM c.dateTime) >= :startMinute) AND" +
            "(EXTRACT(HOUR FROM c.dateTime) != :endHour OR EXTRACT(MINUTE FROM c.dateTime) <= :endMinute)")
    List<ContactMessage> findMessagesBetweenTimes(@Param("startHour") int startHour,
                                                  @Param("startMinute") int startMinute,
                                                  @Param("endHour") int endHour,
                                                  @Param("endMinute") int endMinute);
}
