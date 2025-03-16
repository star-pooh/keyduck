package org.team1.keyduck.auction.repository;

import static org.team1.keyduck.auction.entity.QAuction.auction;
import static org.team1.keyduck.keyboard.entity.QKeyboard.keyboard;
import static org.team1.keyduck.member.entity.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.dto.response.QAuctionSearchResponseDto;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Repository
@RequiredArgsConstructor
public class AuctionQueryDslRepositoryImpl implements AuctionQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<AuctionSearchResponseDto> findAllAuction(Long lastId, Pageable pageable,
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
                        getWhereLastAuctionIdLowerThan(lastId),
                        keyboard(keyboardName),
                        auctionTitle(auctionTitle),
                        sellerName(sellerName),
                        auctionStatus(auctionStatus),
                        auctionStartDate(startDate),
                        auctionEndDate(endDate)
                )
                .orderBy(auction.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (auctionList.size() > pageable.getPageSize()) {
            auctionList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(auctionList, pageable, hasNext);

    }

    private BooleanExpression getWhereLastAuctionIdLowerThan(Long lastId) {

        return lastId == null ? null : auction.id.lt(lastId);
    }

    @Override
    public List<Long> findOpenTargetAuction(LocalDateTime now) {
        return queryFactory.select(auction.id)
                .from(auction)
                .where(auction.auctionStartDate.year().eq(now.getYear())
                        .and(auction.auctionStartDate.month().eq(now.getMonthValue()))
                        .and(auction.auctionStartDate.dayOfMonth().eq(now.getDayOfMonth()))
                        .and(auction.auctionStartDate.hour().eq(now.getHour()))
                        .and(auction.auctionStatus.eq(AuctionStatus.NOT_STARTED))
                )
                .fetch();
    }

    @Override
    public List<Long> findCloseTargetAuction(LocalDateTime now) {
        return queryFactory.select(auction.id)
                .from(auction)
                .where(auction.auctionEndDate.year().eq(now.getYear())
                        .and(auction.auctionEndDate.month().eq(now.getMonthValue()))
                        .and(auction.auctionEndDate.dayOfMonth().eq(now.getDayOfMonth()))
                        .and(auction.auctionEndDate.hour().eq(now.getHour()))
                        .and(auction.auctionStatus.eq(AuctionStatus.IN_PROGRESS))
                )
                .fetch();
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

