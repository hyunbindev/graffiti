"user client";

import { getWhisperFeed } from "@/model/WhisperModel";
import { useEffect, useState } from "react";

const useGetFeed = (feedId) => {
    const [feed, setFeed] = useState(null);
    
    const fetchFeed = useCallback(async () => {
        try {
            const data = await getWhisperFeed(feedId);
            setFeed(data);
        } catch (error) {
            console.error("Error fetching whisper feed:", error);
        }
    });
    useEffect(() => {
        if (feedId) {
            fetchFeed();
        }
    }, []);

    useEffect(()=>{
        if (feedId) fetchFeed();
    },[])

    return { feed };
}
export default useGetFeed;