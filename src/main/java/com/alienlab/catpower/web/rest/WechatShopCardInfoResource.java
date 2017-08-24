package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.WechatShopCardInfo;
import com.alienlab.catpower.service.WechatShopCardInfoService;
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
 * REST controller for managing WechatShopCardInfo.
 */
@RestController
@RequestMapping("/api")
public class WechatShopCardInfoResource {

    private final Logger log = LoggerFactory.getLogger(WechatShopCardInfoResource.class);

    private static final String ENTITY_NAME = "wechatShopCardInfo";

    private final WechatShopCardInfoService wechatShopCardInfoService;

    public WechatShopCardInfoResource(WechatShopCardInfoService wechatShopCardInfoService) {
        this.wechatShopCardInfoService = wechatShopCardInfoService;
    }

    /**
     * POST  /wechat-shop-card-infos : Create a new wechatShopCardInfo.
     *
     * @param wechatShopCardInfo the wechatShopCardInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wechatShopCardInfo, or with status 400 (Bad Request) if the wechatShopCardInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wechat-shop-card-infos")
    @Timed
    public ResponseEntity<WechatShopCardInfo> createWechatShopCardInfo(@RequestBody WechatShopCardInfo wechatShopCardInfo) throws URISyntaxException {
        log.debug("REST request to save WechatShopCardInfo : {}", wechatShopCardInfo);
        if (wechatShopCardInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wechatShopCardInfo cannot already have an ID")).body(null);
        }
        WechatShopCardInfo result = wechatShopCardInfoService.save(wechatShopCardInfo);
        return ResponseEntity.created(new URI("/api/wechat-shop-card-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wechat-shop-card-infos : Updates an existing wechatShopCardInfo.
     *
     * @param wechatShopCardInfo the wechatShopCardInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wechatShopCardInfo,
     * or with status 400 (Bad Request) if the wechatShopCardInfo is not valid,
     * or with status 500 (Internal Server Error) if the wechatShopCardInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wechat-shop-card-infos")
    @Timed
    public ResponseEntity<WechatShopCardInfo> updateWechatShopCardInfo(@RequestBody WechatShopCardInfo wechatShopCardInfo) throws URISyntaxException {
        log.debug("REST request to update WechatShopCardInfo : {}", wechatShopCardInfo);
        if (wechatShopCardInfo.getId() == null) {
            return createWechatShopCardInfo(wechatShopCardInfo);
        }
        WechatShopCardInfo result = wechatShopCardInfoService.save(wechatShopCardInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wechatShopCardInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wechat-shop-card-infos : get all the wechatShopCardInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wechatShopCardInfos in body
     */
    @GetMapping("/wechat-shop-card-infos")
    @Timed
    public ResponseEntity<List<WechatShopCardInfo>> getAllWechatShopCardInfos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WechatShopCardInfos");
        Page<WechatShopCardInfo> page = wechatShopCardInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wechat-shop-card-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wechat-shop-card-infos/:id : get the "id" wechatShopCardInfo.
     *
     * @param id the id of the wechatShopCardInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wechatShopCardInfo, or with status 404 (Not Found)
     */
    @GetMapping("/wechat-shop-card-infos/{id}")
    @Timed
    public ResponseEntity<WechatShopCardInfo> getWechatShopCardInfo(@PathVariable Long id) {
        log.debug("REST request to get WechatShopCardInfo : {}", id);
        WechatShopCardInfo wechatShopCardInfo = wechatShopCardInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wechatShopCardInfo));
    }

    /**
     * DELETE  /wechat-shop-card-infos/:id : delete the "id" wechatShopCardInfo.
     *
     * @param id the id of the wechatShopCardInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wechat-shop-card-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteWechatShopCardInfo(@PathVariable Long id) {
        log.debug("REST request to delete WechatShopCardInfo : {}", id);
        wechatShopCardInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
