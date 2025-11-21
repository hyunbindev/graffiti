"use client";


import FeedPreView from "@/component/feed/FeedPreView";
import useRecentFeed from "@/viewmodel/useRecentFeed";
import { useInView } from 'react-intersection-observer';
import { useEffect } from "react";

export default function RecentFeedPage() {
  const {feeds,getNextPage}=useRecentFeed(10);
  const [ref, inView] = useInView();
  useEffect(()=>{getNextPage()},[inView]);
  return (
    <>
    <div style={{"marginTop":"3rem"}}>
      {feeds.map((feed)=>(<FeedPreView key={feed.id} data={feed}/>))}
    </div>
    {feeds.length > 0 &&<div ref={ref}style={{height:"5rem"}}></div>}
    </>
  );
}
