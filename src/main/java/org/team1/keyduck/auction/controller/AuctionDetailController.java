package org.team1.keyduck.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuctionDetailController {

    @GetMapping("/auction_detail.html")
    public String auctionDetail() {
        return "auction_detail";
    }
}
