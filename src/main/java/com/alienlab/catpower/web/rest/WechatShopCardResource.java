package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.web.rest.util.ExecResult;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.WechatShopCard;
import com.alienlab.catpower.service.WechatShopCardService;
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
 * REST controller for managing WechatShopCard.
 */
@RestController
@RequestMapping("/api")
public class WechatShopCardResource {

    private final Logger log = LoggerFactory.getLogger(WechatShopCardResource.class);

    private static final String ENTITY_NAME = "wechatShopCard";

    private final WechatShopCardService wechatShopCardService;

    public WechatShopCardResource(WechatShopCardService wechatShopCardService) {
        this.wechatShopCardService = wechatShopCardService;
    }

    /**
     * POST  /wechat-shop-cards : Create a new wechatShopCard.
     *
     * @param wechatShopCard the wechatShopCard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wechatShopCard, or with status 400 (Bad Request) if the wechatShopCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wechat-shop-cards")
    @Timed
    public ResponseEntity<WechatShopCard> createWechatShopCard(@RequestBody WechatShopCard wechatShopCard) throws URISyntaxException {
        log.debug("REST request to save WechatShopCard : {}", wechatShopCard);
        if (wechatShopCard.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wechatShopCard cannot already have an ID")).body(null);
        }
        WechatShopCard result = wechatShopCardService.save(wechatShopCard);
        return ResponseEntity.created(new URI("/api/wechat-shop-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wechat-shop-cards : Updates an existing wechatShopCard.
     *
     * @param wechatShopCard the wechatShopCard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wechatShopCard,
     * or with status 400 (Bad Request) if the wechatShopCard is not valid,
     * or with status 500 (Internal Server Error) if the wechatShopCard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wechat-shop-cards")
    @Timed
    public ResponseEntity<WechatShopCard> updateWechatShopCard(@RequestBody WechatShopCard wechatShopCard) throws URISyntaxException {
        log.debug("REST request to update WechatShopCard : {}", wechatShopCard);
        if (wechatShopCard.getId() == null) {
            return createWechatShopCard(wechatShopCard);
        }
        WechatShopCard result = wechatShopCardService.save(wechatShopCard);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wechatShopCard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wechat-shop-cards : get all the wechatShopCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wechatShopCards in body
     */
    @GetMapping("/wechat-shop-cards")
    @Timed
    public ResponseEntity<List<WechatShopCard>> getAllWechatShopCards(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WechatShopCards");
        Page<WechatShopCard> page = wechatShopCardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wechat-shop-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wechat-shop-cards/:id : get the "id" wechatShopCard.
     *
     * @param id the id of the wechatShopCard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wechatShopCard, or with status 404 (Not Found)
     */
    @GetMapping("/wechat-shop-cards/{id}")
    @Timed
    public ResponseEntity<WechatShopCard> getWechatShopCard(@PathVariable Long id) {
        log.debug("REST request to get WechatShopCard : {}", id);
        WechatShopCard wechatShopCard = wechatShopCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wechatShopCard));
    }

    /**
     * DELETE  /wechat-shop-cards/:id : delete the "id" wechatShopCard.
     *
     * @param id the id of the wechatShopCard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wechat-shop-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteWechatShopCard(@PathVariable Long id) {
        log.debug("REST request to delete WechatShopCard : {}", id);
        wechatShopCardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/wechat-shop-cards/refresh")
    @Timed
    public ResponseEntity refreshWechatShopCards() {
        boolean flag=wechatShopCardService.refreshShopCard();
        ExecResult er=new ExecResult(flag,"");
        return  ResponseEntity.ok(er);
    }


}
