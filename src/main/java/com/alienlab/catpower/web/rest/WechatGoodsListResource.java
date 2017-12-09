package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.WechatGoodsList;
import com.alienlab.catpower.service.WechatGoodsListService;
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
 * REST controller for managing WechatGoodsList.
 */
@RestController
@RequestMapping("/api")
public class WechatGoodsListResource {

    private final Logger log = LoggerFactory.getLogger(WechatGoodsListResource.class);

    private static final String ENTITY_NAME = "wechatGoodsList";

    private final WechatGoodsListService wechatGoodsListService;

    public WechatGoodsListResource(WechatGoodsListService wechatGoodsListService) {
        this.wechatGoodsListService = wechatGoodsListService;
    }

    /**
     * POST  /wechat-goods-lists : Create a new wechatGoodsList.
     *
     * @param wechatGoodsList the wechatGoodsList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wechatGoodsList, or with status 400 (Bad Request) if the wechatGoodsList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wechat-goods-lists")
    @Timed
    public ResponseEntity<WechatGoodsList> createWechatGoodsList(@RequestBody WechatGoodsList wechatGoodsList) throws URISyntaxException {
        log.debug("REST request to save WechatGoodsList : {}", wechatGoodsList);
        if (wechatGoodsList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wechatGoodsList cannot already have an ID")).body(null);
        }
        WechatGoodsList result = wechatGoodsListService.save(wechatGoodsList);
        return ResponseEntity.created(new URI("/api/wechat-goods-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wechat-goods-lists : Updates an existing wechatGoodsList.
     *
     * @param wechatGoodsList the wechatGoodsList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wechatGoodsList,
     * or with status 400 (Bad Request) if the wechatGoodsList is not valid,
     * or with status 500 (Internal Server Error) if the wechatGoodsList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wechat-goods-lists")
    @Timed
    public ResponseEntity<WechatGoodsList> updateWechatGoodsList(@RequestBody WechatGoodsList wechatGoodsList) throws URISyntaxException {
        log.debug("REST request to update WechatGoodsList : {}", wechatGoodsList);
        if (wechatGoodsList.getId() == null) {
            return createWechatGoodsList(wechatGoodsList);
        }
        WechatGoodsList result = wechatGoodsListService.save(wechatGoodsList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wechatGoodsList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wechat-goods-lists : get all the wechatGoodsLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wechatGoodsLists in body
     */
    @GetMapping("/wechat-goods-lists")
    @Timed
    public ResponseEntity<List<WechatGoodsList>> getAllWechatGoodsLists(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WechatGoodsLists");
        Page<WechatGoodsList> page = wechatGoodsListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wechat-goods-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wechat-goods-lists/:id : get the "id" wechatGoodsList.
     *
     * @param id the id of the wechatGoodsList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wechatGoodsList, or with status 404 (Not Found)
     */
    @GetMapping("/wechat-goods-lists/{id}")
    @Timed
    public ResponseEntity<WechatGoodsList> getWechatGoodsList(@PathVariable Long id) {
        log.debug("REST request to get WechatGoodsList : {}", id);
        WechatGoodsList wechatGoodsList = wechatGoodsListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wechatGoodsList));
    }

    /**
     * DELETE  /wechat-goods-lists/:id : delete the "id" wechatGoodsList.
     *
     * @param id the id of the wechatGoodsList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wechat-goods-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteWechatGoodsList(@PathVariable Long id) {
        log.debug("REST request to delete WechatGoodsList : {}", id);
        wechatGoodsListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
