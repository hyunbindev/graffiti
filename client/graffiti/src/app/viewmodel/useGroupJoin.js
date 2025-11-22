
"user client";
import { useEffect, useState } from "react";
import { getGroupNameByCode , joinGroupByCode } from '@/model/GroupModel';
import {  useRouter } from 'next/navigation';
import { useAuthStore } from '../zustand/useAuthStore'
import api from '@/lib/api'
const useGroupJoin = ({code}) => {
    const [groupName, setGroupName] = useState(null);
    const router = useRouter();
    const setGroups = useAuthStore((state) => state.setGroups);
    const setSelectedGroup = useAuthStore((state) => state.setSelectedGroup);
    const getCode = async (code)=>{
        try{
            const data = await getGroupNameByCode(code);
            setGroupName(data);
            console.log(data);
        }catch(err){
            console.error(err);
            alert("만료됐거나 잘못된 코드입니다 다시 시도해 주세요.");
        }
    }

    const joinGroup = async (code)=>{
        try{
            const data = await joinGroupByCode(code.code);
            alert("그룹에 성공적으로 참여 되었습니다.");
            api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/member/group/me`)
            .then((res)=>{
                console.log(res);
                setGroups(res.data);
                setSelectedGroup(res.data[0]);
            })
        }catch(err){
            console.error(err);
            alert(err.response.data.message);
        }finally{
            router.push('/feed');
        }
    }

    useEffect(()=>{
        getCode(code);
    },[])

    return {groupName, joinGroup};
}
export default useGroupJoin;