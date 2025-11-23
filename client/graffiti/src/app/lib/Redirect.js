"use client"
import {useEffect} from 'react';
import { useAuthStore } from '@/zustand/useAuthStore.js';
export default function Redirect(){
    const { accessToken } = useAuthStore();
    useEffect(()=>{
        console.log(accessToken);
        if(!accessToken){
            if (typeof window !== 'undefined') {
                const currentPath = window.location.pathname + window.location.search;
                console.log(window.location.pathname);
                if(window.location.pathname=="/callback"){
                    return;
                }
                if (!sessionStorage.getItem('redirect')) {
                    sessionStorage.setItem('redirect', currentPath);
                    console.log(`현재 주소 저장: ${currentPath}`);
                }
                window.location.href = process.env.NEXT_PUBLIC_API_URL+"/api/oauth2/authorization/kakao";
            }
        }
    },[]);
    return (<div></div>)
}