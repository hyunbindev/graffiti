"user client";

import { postFeedLike } from "@/model/LikeModel";
import { useEffect, useState } from "react";

const useLike = (feedId, count, liked) => {
    const [likeCount, setLikeCount] = useState(count);
    const [isLiked, setIsLiked] = useState(liked);
    const postLike = useCallback(async () => {
        try {
            const data = await postFeedLike(feedId);
            setIsLiked(data.like);
            setLikeCount(data.likeCount)
        } catch (error) {
            console.error("Error fetching whisper feed:", error);
        }
    });

    return { postLike , likeCount ,isLiked};
}
export default useLike;