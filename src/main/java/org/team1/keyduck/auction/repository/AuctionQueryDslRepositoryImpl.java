package org.team1.keyduck.auction.repository;

import static org.team1.keyduck.auction.entity.QAuction.auction;
import static org.team1.keyduck.keyboard.entity.QKeyboard.keyboard;
import static org.team1.keyduck.member.entity.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import org.team1.keyduck.auction.entity.AuctionStatus;

@Repository
@RequiredArgsConstructor
public class AuctionQueryDslRepositoryImpl implements AuctionQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuctionSearchResponseDto> findAllAuction(Pageable pageable,
            String keyboardName, String auctionTitle, String sellerName, String auctionStatus,
            String startDate, String endDate) {

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
                        sellerName(sellerName),
                        auctionStatus(auctionStatus),
                        auctionStartDate(startDate),
                        auctionEndDate(endDate)
                )
                .orderBy(getSortOrders(pageable))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long totalCount = Optional.ofNullable(queryFactory.select(
                        auction.count())
                .from(auction)
                .innerJoin(auction.keyboard, keyboard)
                .innerJoin(keyboard.member, member)
                .where(
                        countKeyboard(keyboardName),
                        countAuctionTitle(auctionTitle),
                        countSeller(sellerName),
                        auctionStatus(auctionStatus),
                        auctionStartDate(startDate),
                        auctionEndDate(endDate)
                )
                .fetchOne()).orElse(0L);
        return new PageImpl<>(auctionList, pageable, totalCount);
    }

    private BooleanExpression countAuctionTitle(String auctionTitle) {
        if (auctionTitle == null) {
            return null;
        }

        return auction.title.like("%" + auctionTitle + "%");
    }

    private BooleanExpression countKeyboard(String keyboardName) {
        if (keyboardName == null) {
            return null;
        }

        return auction.keyboard.name.like("%" + keyboardName + "%");
    }

    private BooleanExpression countSeller(String sellerName) {
        if (sellerName == null) {
            return null;
        }

        return auction.keyboard.member.name.like("%" + sellerName + "%");
    }


    private BooleanExpression keyboard(String keyboardName) {
        if (keyboardName == null) {
            return null;
        }

        return Expressions.numberTemplate(Double.class,
                "function('match_against', {0}, {1})",
                auction.keyboard.name, keyboardName
        ).gt(0);
    }


    private BooleanExpression auctionTitle(String auctionTitle) {
        if (auctionTitle == null) {
            return null;
        }
        return Expressions.numberTemplate(Double.class,
                "function('match_against', {0}, {1})",
                auction.title, auctionTitle
        ).gt(0);
    }

    private BooleanExpression sellerName(String sellerName) {
        if (sellerName == null) {
            return null;
        }
        return Expressions.numberTemplate(Double.class,
                "function('match_against', {0}, {1})",
                auction.keyboard.member.name, sellerName
        ).gt(0);
    }

    private BooleanExpression auctionStatus(String auctionStatus) {
        if (auctionStatus == null) {
            return null;
        }
        return auction.auctionStatus.eq(AuctionStatus.valueOf(auctionStatus));
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

    private BooleanExpression auctionStartDate(String startDate) {
        return filterByDate(startDate, auction.auctionStartDate);
    }

    // 경매 종료 날짜가 특정 날짜와 같은지 체크
    private BooleanExpression auctionEndDate(String endDate) {
        return filterByDate(endDate, auction.auctionEndDate);
    }

    // 공통 메서드: 날짜를 LocalDate로 변환 후 DB 날짜 포맷과 비교
    private BooleanExpression filterByDate(String date, DateTimePath<LocalDateTime> dateField) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

            StringTemplate formattedDate = Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%Y-%m-%d')", dateField
            );

            return formattedDate.eq(parsedDate.toString());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}

