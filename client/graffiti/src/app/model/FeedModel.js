import api from "../lib/api";

export const getRecentFeeds = async (lastId, size) => {
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed`,{
            params: {lastId: lastId, size: size}
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