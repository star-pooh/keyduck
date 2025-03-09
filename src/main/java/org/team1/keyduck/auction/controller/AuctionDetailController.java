package org.team1.keyduck.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuctionDetailController {

    @GetMapping("/auction_detail.html")
    public String auctionDetail(@RequestParam Long auctionId, Model model) {
        model.addAttribute("auctionId", auctionId);
        return "auction_detail";
    }
}
