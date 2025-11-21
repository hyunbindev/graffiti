"use client";
import MenuComponent from "@/component/menu/MenuComponent";
import HeaderComponent from "@/component/header/HeaderComponent";
import FeedPreView from "@/component/feed/FeedPreView";
import useRecentFeed from "@/viewmodel/useRecentFeed";
import { useInView } from 'react-intersection-observer';
import { useEffect } from "react";

export default function Home() {
  const {feeds,getNextPage}=useRecentFeed(10);
  const [ref, inView] = useInView();
  useEffect(()=>{getNextPage()},[inView]);
  return (
    <>
    <HeaderComponent/>
    <div style={{"marginTop":"3rem"}}>
      {feeds.map((feed)=>(<FeedPreView key={feed.id} data={feed}/>))}
    </div>
    <MenuComponent/>
    {feeds.length > 0 &&<div ref={ref}style={{height:"5rem"}}></div>}
    </>
  );
}
