package org.team1.keyduck.common.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {


    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {

        //resultType은 DOUBLE타입이며,functionContributions 는 사용자가 정의 함수를 등록 할 수 있게 해주는 것
        BasicType<Double> resultType = functionContributions
                // 타입 설정 정보를 가지고오는 메서드
                .getTypeConfiguration()
                //기본적인 데이터 타입들을 관리하는 레지스트리, 하이버네이트가 지원하는 기본 데이터 타입에 대한 정보를 저장하고 제공함
                .getBasicTypeRegistry()
                //DOUBLE타입에 대한 basicType객체를 반환
                .resolve(StandardBasicTypes.DOUBLE);

        //전체정리 : 하이버네이트의 기본 타입 레지스트리에서 DOUBLE타입에 대한 정보를 가지고 오는과정

        //사용자 정의 함수를 등록
        functionContributions.getFunctionRegistry()
                //함수의 이름은 "match_against"이며, 실제 쿼리에서 사용하는 형식의 패턴을 등록
                .registerPattern("match_against", "match(?1) against (?2 in boolean mode)",
                        resultType);

    }


}
