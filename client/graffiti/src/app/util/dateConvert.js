export function getTimeConvert(isoDateTime){
    // 1. 입력 시간 파싱
    // JavaScript의 Date 객체는 ISO 8601 형식을 잘 처리하지만, 밀리초 이하 정밀도는 자릅니다.
    const inputDate = new Date(isoDateTime);
    
    // 현재 시간
    const now = new Date();
    
    // 시간 차이 (밀리초 단위)
    const diffMs = now.getTime() - inputDate.getTime();
    
    // 과거 시간인지 확인 (미래 시간이라면 '방금 전'으로 처리)
    if (diffMs < 0) {
        return "방금 전"; 
    }

    // 시간 단위 상수 (밀리초)
    const MS_PER_SECOND = 1000;
    const MS_PER_MINUTE = 60 * MS_PER_SECOND;
    const MS_PER_HOUR = 60 * MS_PER_MINUTE;
    const MS_PER_DAY = 24 * MS_PER_HOUR;
    const MS_PER_WEEK = 7 * MS_PER_DAY; // 7일 (1주일) 기준

    // --- 1. 7일(1주일) 미만인 경우 ---
    if (diffMs < MS_PER_WEEK) {
        if (diffMs < MS_PER_MINUTE) {
            return "방금 전"; // 1분 미만
        } else if (diffMs < MS_PER_HOUR) {
            const minutes = Math.floor(diffMs / MS_PER_MINUTE);
            return `${minutes}분 전`;
        } else if (diffMs < MS_PER_DAY) {
            const hours = Math.floor(diffMs / MS_PER_HOUR);
            return `${hours}시간 전`;
        } else { // 1일 이상 7일 미만
            const days = Math.floor(diffMs / MS_PER_DAY);
            return `${days}일 전`;
        }
    } 
    
    // --- 2. 7일(1주일) 이상인 경우 ---
    else {
        // YYYY년 MM월 DD일 형식으로 포맷
        const year = inputDate.getFullYear();
        // getMonth()는 0부터 시작하므로 1을 더해줍니다.
        const month = inputDate.getMonth() + 1;
        const day = inputDate.getDate();

        return `${year}년 ${month.toString().padStart(2, '0')}월 ${day.toString().padStart(2, '0')}일`;
    }
}