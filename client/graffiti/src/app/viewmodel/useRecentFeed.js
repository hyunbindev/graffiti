"use client";
import { getRecentFeeds } from "../model/FeedModel";
import { useEffect, useState, useCallback } from "react";
// SnippetFolderRounded 아이콘과 같은 Material UI import는 훅 로직과 무관하여 제거했습니다.
// 경고: Next.js에서 'use client'가 아닌 다른 파일에서 getRecentFeeds를 서버 컴포넌트처럼 처리해야 할 수 있습니다.

const useRecentFeed = (size = 10) => { // size에 기본값 설정
    const [feeds, setFeeds] = useState([]);
    // 첫 페이지 요청 시 커서로 사용할 매우 큰 값이나 null을 사용합니다.
    // 첫 페이지 요청 시: Long.MAX_VALUE (자바 기준) 또는 null을 사용하여 최신 데이터를 가져옵니다.
    const [lastId, setLastId] = useState(null); 
    const [isLoading, setIsLoading] = useState(false);
    // 다음 페이지가 있는지 여부를 추적합니다. (false이면 더 이상 요청하지 않음)
    const [hasNext, setHasNext] = useState(true); 

    // 데이터 패칭 로직을 useCallback으로 래핑하여 재사용성을 높입니다.
    const fetchFeeds = useCallback(async (currentLastId) => {
        if (!hasNext && currentLastId !== null) return; // 다음 페이지가 없으면 요청 중단
        if (isLoading) return; // 이미 로딩 중이면 요청 중단

        setIsLoading(true);
        try {
            // lastId가 null일 경우 (첫 페이지) API가 적절한 시작 커서를 사용하도록 가정합니다.
            // 만약 API가 null을 처리하지 못하면 여기서 Long.MAX_SAFE_INTEGER 등을 사용해야 합니다.
            const data = await getRecentFeeds(currentLastId, size);
            
            // 데이터 중복 방지 로직 (ID가 1510200 중복 경고 해결에 도움)
            const newFeedsMap = new Map();
            feeds.forEach(feed => newFeedsMap.set(feed.id, feed));
            data.forEach(feed => newFeedsMap.set(feed.id, feed));
            
            const uniqueFeeds = Array.from(newFeedsMap.values()).sort((a, b) => b.id - a.id);
            
            // 다음 페이지 존재 여부 확인 (가져온 데이터가 요청한 size보다 작으면 끝)
            const newHasNext = data.length === size;

            setFeeds(uniqueFeeds);
            setHasNext(newHasNext);

            // 다음 커서 ID 설정 (새로 가져온 데이터의 마지막 ID)
            const newLastId = data.length > 0 ? data[data.length - 1].id : null;
            setLastId(newLastId);
            
            return data.length; // 로드된 항목 수 반환 (선택 사항)

        } catch (error) {
            console.error("Error fetching feeds:", error);
            // 에러 발생 시 isNext를 false로 설정하여 무한 요청 방지
            setHasNext(false); 
        } finally {
            setIsLoading(false);
        }
    }, [size, hasNext, isLoading]); // 의존성 배열 정리

    useEffect(() => {
        // 첫 페이지 로드 시, lastId는 null (또는 API가 기대하는 시작 커서 값)
        // 이미 로드된 데이터가 없다면 초기 로드를 수행합니다.
        if (feeds.length === 0) {
            fetchFeeds(lastId);
        }
    }, []); // 빈 배열: 마운트 시 한 번만 실행

    const getNextPage = useCallback(() => {
        // 현재 lastId를 다음 커서로 사용하여 데이터 요청
        fetchFeeds(lastId);
    }, [fetchFeeds, lastId]); // lastId가 변경될 때마다 함수 재생성

    return { 
        feeds, 
        getNextPage, 
        lastId, 
        isLoading, 
        hasNext 
    };
}
export default useRecentFeed;