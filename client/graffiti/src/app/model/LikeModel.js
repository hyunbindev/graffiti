import api from '@/lib/api'
export const postFeedLike = async (feedId, lastId, size) => {
    try{
        const res = await api.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed/${feedId}/like`);
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};