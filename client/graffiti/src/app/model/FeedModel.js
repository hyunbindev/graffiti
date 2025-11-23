import api from "../lib/api";

export const getRecentFeeds = async (lastId, size ,groupUuid) => {
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed`,{
            params: {lastId: lastId, size: size, groupId: groupUuid}
        });
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};

export const deleteWhisperFeed = async (feedId)=>{
    try{
        await api.delete(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/whisper/${feedId}`);
    }catch(e){
        console.error
    }
}

export const createWhiperFeed = async (groupUuid, image, text, mentions,invisibleMention)=>{
    const request ={
        "text":text,
        "groupUuid":groupUuid,
        "mentionMembers":mentions,
        "invisibleMention":invisibleMention
    }
    const formData = new FormData();
    if(image){
        const fileType = image.type;
        const imageBlob = new Blob([image], { type: fileType });
        formData.append("image",imageBlob, image.name);
    }
    const requestBlob = new Blob([JSON.stringify(request)], { type: 'application/json' });
    formData.append("feed", requestBlob);
    try{
        const res = await api.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/whisper`,formData);
        return res.data;
    }catch(e){
        console.error(e);
        throw e;
    }
}

export const getRankFeed=async(lastId, size ,groupUuid)=>{
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed`,{
            params: {lastId: lastId, size: size, groupId: groupUuid}
        });
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
}