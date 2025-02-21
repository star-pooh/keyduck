package org.team1.keyduck.auction.repository;

import static org.team1.keyduck.auction.entity.QAuction.auction;
import static org.team1.keyduck.keyboard.entity.QKeyboard.keyboard;
import static org.team1.keyduck.member.entity.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;

@Repository
@RequiredArgsConstructor
public class AuctionQueryDslRepositoryImpl implements AuctionQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuctionReadAllResponseDto> findAllAuction(Pageable pageable,
            String keyboardName, String auctionTitle, String sellerName) {
        List<AuctionReadAllResponseDto> auctionList = queryFactory.select(
                        Projections.constructor(
                                AuctionReadAllResponseDto.class,
                                auction
                        )
                )
                .from(auction)
                .where(buildSearchConditions(keyboardName, auctionTitle,
                        sellerName))
                .orderBy(getSortOrders(pageable))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long totalCount = Optional.ofNullable(queryFactory.select(
                        Wildcard.count)
                .from(auction)
                .fetchOne()).orElse(0L);
        return new PageImpl<>(auctionList, pageable, totalCount);
    }

    private BooleanExpression buildSearchConditions(String keyboardName, String auctionTitle,
            String sellerName) {

        BooleanExpression condition = null;

        if (keyboardName != null && !keyboardName.isEmpty()) {
            condition = auction.keyboard.name.like("%" + keyboardName + "%");
        }

        if (auctionTitle != null && !auctionTitle.isEmpty()) {
            condition = auction.title.like("%" + auctionTitle + "%");
        }

        if (sellerName != null && !sellerName.isEmpty()) {
            condition = auction.keyboard.member.name.like("%" + sellerName + "%");
        }

        return condition != null ? condition : null;

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
                    case "keyboardName" -> isAscending ? keyboard.name.asc() : keyboard.name.desc();
                    case "auctionTitle" -> isAscending ? auction.title.asc() : auction.title.desc();
                    case "sellerName" -> isAscending ? member.name.asc() : member.name.desc();
                    default -> auction.id.desc();
                };
                orders.add(orderSpecifier);
            }
        }
        return orders.toArray(new OrderSpecifier[]{});
    }

}

