package com.devgyu.banchan.todo;


public class TodoClass {
    //TODO 테스트코드 작성 - 1순위 구현
    //TODO NGINX 이용 무중단배포 구현

    //TODO 현재 상담 채팅보낼시 채팅보낸쪽의 반대쪽에서 WebSocket이벤트로인한 채팅방 조회쿼리 나가는거 해결방법 모색하기
    //TODO Entity직접 노출하는 부분 너무 복잡하지 않으면 DTO로 필요한 값만 바인딩해서 보낼것

    /**
     * TODO 버그수정:
     * */

    /**
     * TODO 완료 히스토리
     * StoreOwner 프로필 변경하거나 신규가입, 가게 상품 추가, 수정하면 관리자 승인 받아야 상점리스트에 검색되도록 수정 - 완료
     * StoreOwner, Rider(프로필변경)의 경우 관리자 승인 받아야 사이트 기능 정상이용가능 하도록 수정 - 완료
     * 페이징 구현부분 무한스크롤 구현 및 정상작동 여부 많은 샘플로 테스트 해볼것 - 완료
     * 불량이용자 블럭 / 비밀번호 5회이상 틀릴경우 계정 락 기능 구현 - 완료
     * Store상품 지우는 로직 구현 - 완료. SoftDelete로 구현(주문내역, 리뷰 등에서 조회해올때 필요)
     * 스토어 태그 지운갯수>=추가한갯수 수정안되는 현상 수정할것 - 완료
     * 스토어 태그 지운갯수 < 추가한갯수면 지운거 + 추가한거 모두 다 추가되버리는 현상 수정할것 - 완료
     * 주문취소 안됨 - 완료
     * 장바구니 추가 안됨 - 완료
     * 라이더 주문목록 페이징 Projection조회 - 완료
     * 리뷰 페이징 Projection조회 - 완료
     * Paging 처리할때 Projection 써서 직접 DTO로 조회해올것 -> 완료(default_batch_fetch_size 적용)
     * RememberMe로 로그인할시 공통 Navigation에 로그인 유저 정보 뜨지 않는 버그 수정 - 완료
     * 썸네일인터셉터 ajax조회할땐 작동안되도록 수정(ajax조회할때도 인터셉터때문에 쿼리+1 됨) - 완료
     * 단순히 로그인한 객체 최신화를 위해 조회해서 발생하는 쿼리 다이어트 할것 - 완료
     * 주문한 고객에게 알림 보낼것 - 완료
     * 로그인 한 뒤 로그인 페이지 진입시 메인화면으로 리다이렉트 시킬 것 - 완료
     * 가게 목록 조회, 가게 상세조회시 로그인 필요하도록 변경, 가게 목록 조회시 현재 로그인한 사용자의 주소(시,군,구단위)의 가게만 조회하도록 구현 - 완료
     * 가게 상세조회시 현재 로그인한 사용자의 주소와 가게 주소의 시,군,구 단위가 다를경우 예외발생하도록 수정 - 완료
     * Admin계정 가게, 라이더 권한승인기능 구현- 완료
     * WebSocket이용한 고객상담 채팅도 간단하게 구현해볼것 - 완료
     * (상담사 동일 세션 상담방 진입시 이전에 기본멘트 보냈으면 다시 안보내도록 수정할것, 이전 상담 채팅내용 로그 어떻게할지 구상 <- Message DB기록) - 완료
     * 상담사 상담목록페이지에서 대기목록 페이징시 마지막 페이지일경우 WebSocket 연결하여 새로운 상담방 생성시 Push해서 받아오도록 구현 - 완료
     * 상담사, 고객 모두 상담목록페이지에서 특정한 방의 대화내용이 새롭게 추가될경우 WebSocket이용 Push방식으로 해당 상담방 상대방 닉네임옆에 알림(NEW)생성하도록 구현 - 완료
     * 상담사, 고객 모두 다른페이지에서 상담목록페이지 접근시 특정 방의 대화내용이 추가된 방은 상대방 닉네임옆에 알림(NEW)표시 기능 구현 - 완료
     * 상담사가 아닌계정으로 고객문의에 새로운 채팅이 올라올경우 알림줄것 (nonPush, Interceptor방식구현) - 완료(상담사도 알림볼수있도록 구현)
     * Travis CI + AWS S3 + AWS CodeDeploy 연동해서 CI/CD 자동화
     * Travis CI Auto push 안되는거 수정하기
     */

}
