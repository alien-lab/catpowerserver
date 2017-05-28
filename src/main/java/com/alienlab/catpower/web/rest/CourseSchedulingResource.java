package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CourseScheduling.
 */
@RestController
@RequestMapping("/api")
public class CourseSchedulingResource {

    private final Logger log = LoggerFactory.getLogger(CourseSchedulingResource.class);

    private static final String ENTITY_NAME = "courseScheduling";
        
    private final CourseSchedulingService courseSchedulingService;

    public CourseSchedulingResource(CourseSchedulingService courseSchedulingService) {
        this.courseSchedulingService = courseSchedulingService;
    }

    /**
     * POST  /course-schedulings : Create a new courseScheduling.
     *
     * @param courseScheduling the courseScheduling to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseScheduling, or with status 400 (Bad Request) if the courseScheduling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-schedulings")
    @Timed
    public ResponseEntity<CourseScheduling> createCourseScheduling(@RequestBody CourseScheduling courseScheduling) throws URISyntaxException {
        log.debug("REST request to save CourseScheduling : {}", courseScheduling);
        if (courseScheduling.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseScheduling cannot already have an ID")).body(null);
        }
        CourseScheduling result = courseSchedulingService.save(courseScheduling);
        return ResponseEntity.created(new URI("/api/course-schedulings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-schedulings : Updates an existing courseScheduling.
     *
     * @param courseScheduling the courseScheduling to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseScheduling,
     * or with status 400 (Bad Request) if the courseScheduling is not valid,
     * or with status 500 (Internal Server Error) if the courseScheduling couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-schedulings")
    @Timed
    public ResponseEntity<CourseScheduling> updateCourseScheduling(@RequestBody CourseScheduling courseScheduling) throws URISyntaxException {
        log.debug("REST request to update CourseScheduling : {}", courseScheduling);
        if (courseScheduling.getId() == null) {
            return createCourseScheduling(courseScheduling);
        }
        CourseScheduling result = courseSchedulingService.save(courseScheduling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseScheduling.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-schedulings : get all the courseSchedulings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseSchedulings in body
     */
    @GetMapping("/course-schedulings")
    @Timed
    public ResponseEntity<List<CourseScheduling>> getAllCourseSchedulings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseSchedulings");
        Page<CourseScheduling> page = courseSchedulingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-schedulings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-schedulings/:id : get the "id" courseScheduling.
     *
     * @param id the id of the courseScheduling to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseScheduling, or with status 404 (Not Found)
     */
    @GetMapping("/course-schedulings/{id}")
    @Timed
    public ResponseEntity<CourseScheduling> getCourseScheduling(@PathVariable Long id) {
        log.debug("REST request to get CourseScheduling : {}", id);
        CourseScheduling courseScheduling = courseSchedulingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseScheduling));
    }

    /**
     * DELETE  /course-schedulings/:id : delete the "id" courseScheduling.
     *
     * @param id the id of the courseScheduling to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-schedulings/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseScheduling(@PathVariable Long id) {
        log.debug("REST request to delete CourseScheduling : {}", id);
        courseSchedulingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
