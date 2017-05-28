package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CourseAtlas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourseAtlas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseAtlasRepository extends JpaRepository<CourseAtlas,Long> {

}
