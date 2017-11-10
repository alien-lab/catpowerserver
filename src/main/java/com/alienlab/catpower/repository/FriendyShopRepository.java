package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.FriendyShop;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FriendyShop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendyShopRepository extends JpaRepository<FriendyShop, Long> {

}
