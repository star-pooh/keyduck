package org.team1.keyduck.auction.repository;

import static org.team1.keyduck.auction.entity.QAuction.auction;
import static org.team1.keyduck.keyboard.entity.QKeyboard.keyboard;
import static org.team1.keyduck.member.entity.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.dto.response.QAuctionSearchResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Repository
@RequiredArgsConstructor
public class AuctionQueryDslRepositoryImpl implements AuctionQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuctionSearchResponseDto> findAllAuction(Pageable pageable,
            String keyboardName, String auctionTitle, String sellerName) {
        List<AuctionSearchResponseDto> auctionList = queryFactory.select(

                        new QAuctionSearchResponseDto(
                                auction.id,
                                auction.keyboard.id,
                                auction.keyboard.name,
                                auction.keyboard.description,
                                auction.title,
                                auction.currentPrice,
                                auction.immediatePurchasePrice,
                                auction.auctionStatus,
                                auction.member.id,
                                auction.member.name
                        )

                )
                .from(auction)
                .leftJoin(auction.keyboard, keyboard)
                .leftJoin(auction.member, member)
                .where(
                        keyboard(keyboardName),
                        auctionTitle(auctionTitle),
                        sellerName(sellerName)
                )
                .orderBy(getSortOrders(pageable))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long totalCount = Optional.ofNullable(queryFactory.select(
                        Wildcard.count)
                .from(auction)
                .leftJoin(auction.keyboard, keyboard)
                .leftJoin(auction.member, member)
                .where(
                        keyboard(keyboardName),
                        auctionTitle(auctionTitle),
                        sellerName(sellerName)
                )
                .fetchOne()).orElse(0L);
        return new PageImpl<>(auctionList, pageable, totalCount);
    }

    @Override
    public List<Auction> findStartTargetAuction(LocalDateTime now) {
        return queryFactory.selectFrom(auction)
                .where(auction.auctionStartDate.year().eq(now.getYear())
                        .and(auction.auctionStartDate.month().eq(now.getMonthValue()))
                        .and(auction.auctionStartDate.dayOfMonth().eq(now.getDayOfMonth()))
                        .and(auction.auctionStartDate.hour().eq(now.getHour()))
                        .and(auction.auctionStatus.eq(AuctionStatus.NOT_STARTED))
                )
                .fetch();
    }

    private BooleanExpression keyboard(String keyboardName) {
        if (keyboardName == null) {
            return null;
        }
        return auction.keyboard.name.like("%" + keyboardName + "%");
    }

    private BooleanExpression auctionTitle(String auctionTitle) {
        if (auctionTitle == null) {
            return null;
        }
        return auction.title.like("%" + auctionTitle + "%");
    }

    private BooleanExpression sellerName(String sellerName) {
        if (sellerName == null) {
            return null;
        }
        return auction.keyboard.member.name.like("%" + sellerName + "%");
    }

    private OrderSpecifier<?>[] getSortOrders(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (pageable.getSort().isEmpty()) {
            orders.add(auction.id.desc());  // 기본적으로 아이디 내림차순 정렬
        } else {
            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                boolean isAscending = order.isAscending();

                OrderSpecifier<?> orderSpecifier = switch (property) {
                    case "keyboardName" -> isAscending ? auction.keyboard.name.asc()
                            : auction.keyboard.name.desc();
                    case "auctionTitle" -> isAscending ? auction.title.asc() : auction.title.desc();
                    case "sellerName" -> isAscending ? auction.keyboard.member.name.asc()
                            : auction.keyboard.member.name.desc();
                    default -> auction.id.desc();
                };
                orders.add(orderSpecifier);
            }
        }
        return orders.toArray(new OrderSpecifier[]{});
    }

}

