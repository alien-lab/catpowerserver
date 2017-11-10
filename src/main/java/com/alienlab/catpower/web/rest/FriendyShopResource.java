package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.FriendyShop;
import com.alienlab.catpower.service.FriendyShopService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
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
 * REST controller for managing FriendyShop.
 */
@RestController
@RequestMapping("/api")
public class FriendyShopResource {

    private final Logger log = LoggerFactory.getLogger(FriendyShopResource.class);

    private static final String ENTITY_NAME = "friendyShop";

    private final FriendyShopService friendyShopService;

    public FriendyShopResource(FriendyShopService friendyShopService) {
        this.friendyShopService = friendyShopService;
    }

    /**
     * POST  /friendy-shops : Create a new friendyShop.
     *
     * @param friendyShop the friendyShop to create
     * @return the ResponseEntity with status 201 (Created) and with body the new friendyShop, or with status 400 (Bad Request) if the friendyShop has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/friendy-shops")
    @Timed
    public ResponseEntity<FriendyShop> createFriendyShop(@RequestBody FriendyShop friendyShop) throws URISyntaxException {
        log.debug("REST request to save FriendyShop : {}", friendyShop);
        if (friendyShop.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new friendyShop cannot already have an ID")).body(null);
        }
        FriendyShop result = friendyShopService.save(friendyShop);
        return ResponseEntity.created(new URI("/api/friendy-shops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /friendy-shops : Updates an existing friendyShop.
     *
     * @param friendyShop the friendyShop to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated friendyShop,
     * or with status 400 (Bad Request) if the friendyShop is not valid,
     * or with status 500 (Internal Server Error) if the friendyShop couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/friendy-shops")
    @Timed
    public ResponseEntity<FriendyShop> updateFriendyShop(@RequestBody FriendyShop friendyShop) throws URISyntaxException {
        log.debug("REST request to update FriendyShop : {}", friendyShop);
        if (friendyShop.getId() == null) {
            return createFriendyShop(friendyShop);
        }
        FriendyShop result = friendyShopService.save(friendyShop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, friendyShop.getId().toString()))
            .body(result);
    }

    /**
     * GET  /friendy-shops : get all the friendyShops.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of friendyShops in body
     */
    @GetMapping("/friendy-shops")
    @Timed
    public ResponseEntity<List<FriendyShop>> getAllFriendyShops(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FriendyShops");
        Page<FriendyShop> page = friendyShopService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friendy-shops");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /friendy-shops/:id : get the "id" friendyShop.
     *
     * @param id the id of the friendyShop to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the friendyShop, or with status 404 (Not Found)
     */
    @GetMapping("/friendy-shops/{id}")
    @Timed
    public ResponseEntity<FriendyShop> getFriendyShop(@PathVariable Long id) {
        log.debug("REST request to get FriendyShop : {}", id);
        FriendyShop friendyShop = friendyShopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(friendyShop));
    }

    /**
     * DELETE  /friendy-shops/:id : delete the "id" friendyShop.
     *
     * @param id the id of the friendyShop to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/friendy-shops/{id}")
    @Timed
    public ResponseEntity<Void> deleteFriendyShop(@PathVariable Long id) {
        log.debug("REST request to delete FriendyShop : {}", id);
        friendyShopService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @ApiOperation(value = "获取绑定二维码")
    @GetMapping("/friendy-shops/qr/{id}")
    public ResponseEntity getScheQrcode(@PathVariable Long id){
        try{
            FriendyShop shop=friendyShopService.findOne(id);
            QrInfo qr=friendyShopService.getShopBindQr(shop);
            return ResponseEntity.ok(qr);
        }catch(Exception e){
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
}
