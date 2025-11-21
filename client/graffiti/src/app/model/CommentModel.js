import api from "../lib/api";

export const getCommentFeeds = async (feedId, lastId, size) => {
    try{
        const res = await api.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed/${feedId}/comment`,{
            params: {lastId: lastId, size: size}
        });
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};
export const postCommentFeeds = async (feedId, text) => {
    if(text.trim().length <1){
        alert("덧글을 입력해 주세요");
        return;
    }
    try{
        const res = await api.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed/${feedId}/comment`,{
                "text":text,
        });
        console.log(res);
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};
export const deleteCommentFeeds = async (commentId) => {
    try{
        const res = await api.delete(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/feed/comment/${commentId}`);
        console.log(res);
        return res.data;
    }catch(error){
        console.error("Error fetching recent feeds:", error);
        return [];
    }
};