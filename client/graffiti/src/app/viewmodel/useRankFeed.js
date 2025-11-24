"use client";
import { getRankFeed } from "../model/FeedModel";
import { useEffect, useState, useCallback } from "react";
import { useAuthStore } from '@/zustand/useAuthStore.js';
import next from "next";

const useRankFeed = (size = 10) => {
    const { selectedGroup } = useAuthStore();
    const selectedGroupId = selectedGroup?.uuid; // 그룹 ID 추출

    const [feeds, setFeeds] = useState([]);
    const [nextPage, setNextPage] = useState(0);
    // 데이터 패칭 로직
    const fetchFeeds = async (page) => {
        if (!selectedGroupId) return;
        try {
            const data = await getRankFeed(page, 30, selectedGroupId);
            setNextPage(nextPage+1);
            setFeeds(data);
        } catch (error) {
            console.error("Error fetching feeds:", error);
        }
    } 

    useEffect(() => {
        if (selectedGroupId) {
            // 1. 상태 초기화
            setFeeds([]);
            // 2. 첫 페이지 로드 시작 (새로운 그룹 ID를 사용하여 로드)
            fetchFeeds(0); 
        }
        
    }, [selectedGroupId]); 
    return { 
        feeds,
    };
}

export default useRankFeed;