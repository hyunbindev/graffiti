import api from "../lib/api";

export const getWhisperFeed = async (feedId) => {
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/whisper/${feedId}`);
        return res.data;
    }catch(error){
        throw error;
    }
};