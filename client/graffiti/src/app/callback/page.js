"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";
import { useAuthStore } from '../zustand/useAuthStore'
import axios from "axios";
import LoadingComponent from "../component/loading/LoadingComponent";
import api from '@/lib/api'

export default function AuthenticationCallback() {
    const router = useRouter();
    const searchParams = useSearchParams();

    const setToken = useAuthStore((state) => state.setToken);
    const setUserInfo = useAuthStore((state) => state.setUserInfo);
    const setGroups = useAuthStore((state) => state.setGroups);
    const setSelectedGroup = useAuthStore((state) => state.setSelectedGroup);

    useEffect(()=>{
        const accessToken = searchParams.get("accessToken");
        setToken(accessToken);
        console.log("Access Token set:", accessToken);
        axios.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/member/me`,{
            headers: {
                Authorization: `${accessToken}`,},withCredentials: true
        }).then((response) => {
            const { uuid, nickName, profileImg } = response.data;
            setUserInfo(uuid, nickName, profileImg);

            api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/member/group/me`)
            .then((res)=>{
                setGroups(res.data);
                setSelectedGroup(res.data[0]);
            })
            router.push("/feed");
        }).catch((error) => {
            router.push("/login");
        });
    },[]);
    
    return (
        <LoadingComponent>
            로그인 중입니다.
        </LoadingComponent>
    );
}
