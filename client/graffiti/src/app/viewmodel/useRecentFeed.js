"use client";
import { getRecentFeeds } from "../model/FeedModel";
import { useEffect, useState, useCallback } from "react";
import { useAuthStore } from '@/zustand/useAuthStore.js';

const useRecentFeed = (size = 10) => {
    const { selectedGroup } = useAuthStore();
    const selectedGroupId = selectedGroup?.uuid; // 그룹 ID 추출

    const [feeds, setFeeds] = useState([]);
    const [lastId, setLastId] = useState(null); 
    const [isLoading, setIsLoading] = useState(false);
    const [hasNext, setHasNext] = useState(true); 

    // 데이터 패칭 로직
    const fetchFeeds = useCallback(async (cursorId) => {
        if (!hasNext && cursorId !== null) return; 
        if (isLoading) return; 
        if (!selectedGroupId) return; // 그룹 ID가 없으면 실행 중단

        setIsLoading(true);
        
        try {
            const data = await getRecentFeeds(cursorId, size, selectedGroupId);
            
            const newHasNext = data.length === size;
            const newLastId = data.length > 0 ? data[data.length - 1].id : null;
            
            // 상태 업데이트: 이전 상태(prevFeeds)를 사용하여 데이터 병합
            // cursorId가 null이면 첫 페이지이므로 기존 피드를 덮어씁니다.
            setFeeds(prevFeeds => cursorId === null ? data : [...prevFeeds, ...data]);
            setHasNext(newHasNext);
            setLastId(newLastId); 

        } catch (error) {
            console.error("Error fetching feeds:", error);
            setHasNext(false); 
        } finally {
            setIsLoading(false);
        }
    // 💡 selectedGroupId에 따라 함수가 재생성되어 최신 ID를 참조합니다.
    }, [size, hasNext, isLoading, selectedGroupId]); 

    // 🔑 핵심: 그룹 ID가 변경되거나 (그룹 변경), 
    //         그룹 ID가 유효한 값으로 처음 설정될 때 (첫 로드) 실행됩니다.
    useEffect(() => {
        if (selectedGroupId) {
            // 1. 상태 초기화
            setFeeds([]);
            setLastId(null);
            setHasNext(true);
            
            // 2. 첫 페이지 로드 시작 (새로운 그룹 ID를 사용하여 로드)
            fetchFeeds(null); 
        }
        
    // 💡 selectedGroupId에만 의존하여 루프를 방지하고 초기/변경 로드를 처리
    }, [selectedGroupId]); 

    // 다음 페이지를 로드하는 함수
    const getNextPage = useCallback(() => {
        fetchFeeds(lastId); 
    }, [fetchFeeds, lastId]); 

    return { 
        feeds, 
        getNextPage, 
        lastId, 
        isLoading, 
        hasNext 
    };
}

export default useRecentFeed;