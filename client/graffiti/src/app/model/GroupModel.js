import api from "../lib/api";

export const getInviteCode = async (groupUuid) => {
    try{
        const res = await api.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/group/${groupUuid}/code`);
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};

export const getGroupNameByCode = async(code)=>{
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/group/code/${code}/preview`);
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return null;
    }
}

export const joinGroupByCode = async(code)=>{
    try{
        const res = await api.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/group/join`,{
            "code":code,
        });
        return res.data;
    }catch(error){
        throw error;
    }
}

export const getMemberInGroup = async(groupUuid, keyWord)=>{
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/group/${groupUuid}/member`,{
            params:{keyWord:keyWord}
        });
        return res.data;
    }catch(error){
        throw error;
    }
}