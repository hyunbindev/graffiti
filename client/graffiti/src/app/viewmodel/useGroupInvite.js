
"user client";
import { useEffect, useState } from "react";
import {getInviteCode} from '@/model/GroupModel';

const useGroupInvite = () => {
    const [inviteCode, setInviteCode] = useState(null);
    const getCode = async (groupUuid)=>{
        try{
            const data = await getInviteCode(groupUuid);
            console.log(data);
            setInviteCode(data);
        }catch(err){
            console.error(err);
            alert("초대코드 발급실패 다시 시도해 주세요");
        }
    }
    return {inviteCode, getCode};
}
export default useGroupInvite;