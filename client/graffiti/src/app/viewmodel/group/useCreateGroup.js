"use client";
import { useRouter } from "next/navigation";
import {postNewGroup} from '@/model/GroupModel';
import { useAuthStore } from '@/zustand/useAuthStore'
import { useState } from 'react';
import api from '@/lib/api'
const useCreateGroup = () => {
    const [groupName, setGroupName] = useState("");
    const setGroups = useAuthStore((state) => state.setGroups);
    const router = useRouter();
    const createGroup = async()=>{
        try {
            if(groupName.trim().length<1){
                alert("그룹이름을 입력해 주세요.");
                return;
            }
            const res = await postNewGroup(groupName);
            api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/member/group/me`)
            .then((res)=>{
                setGroups(res.data);
                router.push('/group');
            })
        } catch (error) {
            console.error(error);
        }
    }
    return { groupName, setGroupName , createGroup };
}
export default useCreateGroup;