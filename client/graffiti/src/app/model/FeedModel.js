import api from "../lib/api";

export const getRecentFeeds = async (lastId, size) => {
    try{
        const res = await api.get('http://localhost:9081/api/v1/feed',{
            params: {lastId: lastId, size: size}
        });
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};