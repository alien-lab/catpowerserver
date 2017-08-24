package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.WechatVipcard;
import com.alienlab.catpower.service.WechatVipcardService;
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
 * REST controller for managing WechatVipcard.
 */
@RestController
@RequestMapping("/api")
public class WechatVipcardResource {

    private final Logger log = LoggerFactory.getLogger(WechatVipcardResource.class);

    private static final String ENTITY_NAME = "wechatVipcard";

    private final WechatVipcardService wechatVipcardService;

    public WechatVipcardResource(WechatVipcardService wechatVipcardService) {
        this.wechatVipcardService = wechatVipcardService;
    }

    /**
     * POST  /wechat-vipcards : Create a new wechatVipcard.
     *
     * @param wechatVipcard the wechatVipcard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wechatVipcard, or with status 400 (Bad Request) if the wechatVipcard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wechat-vipcards")
    @Timed
    public ResponseEntity<WechatVipcard> createWechatVipcard(@RequestBody WechatVipcard wechatVipcard) throws URISyntaxException {
        log.debug("REST request to save WechatVipcard : {}", wechatVipcard);
        if (wechatVipcard.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wechatVipcard cannot already have an ID")).body(null);
        }
        WechatVipcard result = wechatVipcardService.save(wechatVipcard);
        return ResponseEntity.created(new URI("/api/wechat-vipcards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wechat-vipcards : Updates an existing wechatVipcard.
     *
     * @param wechatVipcard the wechatVipcard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wechatVipcard,
     * or with status 400 (Bad Request) if the wechatVipcard is not valid,
     * or with status 500 (Internal Server Error) if the wechatVipcard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wechat-vipcards")
    @Timed
    public ResponseEntity<WechatVipcard> updateWechatVipcard(@RequestBody WechatVipcard wechatVipcard) throws URISyntaxException {
        log.debug("REST request to update WechatVipcard : {}", wechatVipcard);
        if (wechatVipcard.getId() == null) {
            return createWechatVipcard(wechatVipcard);
        }
        WechatVipcard result = wechatVipcardService.save(wechatVipcard);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wechatVipcard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wechat-vipcards : get all the wechatVipcards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wechatVipcards in body
     */
    @GetMapping("/wechat-vipcards")
    @Timed
    public ResponseEntity<List<WechatVipcard>> getAllWechatVipcards(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WechatVipcards");
        Page<WechatVipcard> page = wechatVipcardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wechat-vipcards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wechat-vipcards/:id : get the "id" wechatVipcard.
     *
     * @param id the id of the wechatVipcard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wechatVipcard, or with status 404 (Not Found)
     */
    @GetMapping("/wechat-vipcards/{id}")
    @Timed
    public ResponseEntity<WechatVipcard> getWechatVipcard(@PathVariable Long id) {
        log.debug("REST request to get WechatVipcard : {}", id);
        WechatVipcard wechatVipcard = wechatVipcardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wechatVipcard));
    }

    /**
     * DELETE  /wechat-vipcards/:id : delete the "id" wechatVipcard.
     *
     * @param id the id of the wechatVipcard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wechat-vipcards/{id}")
    @Timed
    public ResponseEntity<Void> deleteWechatVipcard(@PathVariable Long id) {
        log.debug("REST request to delete WechatVipcard : {}", id);
        wechatVipcardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
