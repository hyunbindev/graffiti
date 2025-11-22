"use client";
import WhisperDetail from '@/component/feed/whisper/WhisperDetail';
import { useParams } from 'next/navigation';
import useGetFeed from '@/viewmodel/useGetFeed';
import { useEffect } from 'react'; // 디버깅을 위해 import 추가
import FeedSkeleton from '@/component/skeleton/FeedSkeleton';
export default function FeedDetailPage() {
    const params = useParams();
    
    const { feed, } = useGetFeed(params.id);
    
    if (!feed) {
        return <FeedSkeleton />; 
    }
    
    return (
        <WhisperDetail feed={feed} />
    )
}