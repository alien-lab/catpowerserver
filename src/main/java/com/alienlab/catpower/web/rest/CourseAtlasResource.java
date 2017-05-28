package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.CourseAtlas;
import com.alienlab.catpower.service.CourseAtlasService;
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
 * REST controller for managing CourseAtlas.
 */
@RestController
@RequestMapping("/api")
public class CourseAtlasResource {

    private final Logger log = LoggerFactory.getLogger(CourseAtlasResource.class);

    private static final String ENTITY_NAME = "courseAtlas";
        
    private final CourseAtlasService courseAtlasService;

    public CourseAtlasResource(CourseAtlasService courseAtlasService) {
        this.courseAtlasService = courseAtlasService;
    }

    /**
     * POST  /course-atlases : Create a new courseAtlas.
     *
     * @param courseAtlas the courseAtlas to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseAtlas, or with status 400 (Bad Request) if the courseAtlas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-atlases")
    @Timed
    public ResponseEntity<CourseAtlas> createCourseAtlas(@RequestBody CourseAtlas courseAtlas) throws URISyntaxException {
        log.debug("REST request to save CourseAtlas : {}", courseAtlas);
        if (courseAtlas.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseAtlas cannot already have an ID")).body(null);
        }
        CourseAtlas result = courseAtlasService.save(courseAtlas);
        return ResponseEntity.created(new URI("/api/course-atlases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-atlases : Updates an existing courseAtlas.
     *
     * @param courseAtlas the courseAtlas to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseAtlas,
     * or with status 400 (Bad Request) if the courseAtlas is not valid,
     * or with status 500 (Internal Server Error) if the courseAtlas couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-atlases")
    @Timed
    public ResponseEntity<CourseAtlas> updateCourseAtlas(@RequestBody CourseAtlas courseAtlas) throws URISyntaxException {
        log.debug("REST request to update CourseAtlas : {}", courseAtlas);
        if (courseAtlas.getId() == null) {
            return createCourseAtlas(courseAtlas);
        }
        CourseAtlas result = courseAtlasService.save(courseAtlas);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseAtlas.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-atlases : get all the courseAtlases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseAtlases in body
     */
    @GetMapping("/course-atlases")
    @Timed
    public ResponseEntity<List<CourseAtlas>> getAllCourseAtlases(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseAtlases");
        Page<CourseAtlas> page = courseAtlasService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-atlases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-atlases/:id : get the "id" courseAtlas.
     *
     * @param id the id of the courseAtlas to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseAtlas, or with status 404 (Not Found)
     */
    @GetMapping("/course-atlases/{id}")
    @Timed
    public ResponseEntity<CourseAtlas> getCourseAtlas(@PathVariable Long id) {
        log.debug("REST request to get CourseAtlas : {}", id);
        CourseAtlas courseAtlas = courseAtlasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseAtlas));
    }

    /**
     * DELETE  /course-atlases/:id : delete the "id" courseAtlas.
     *
     * @param id the id of the courseAtlas to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-atlases/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseAtlas(@PathVariable Long id) {
        log.debug("REST request to delete CourseAtlas : {}", id);
        courseAtlasService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
