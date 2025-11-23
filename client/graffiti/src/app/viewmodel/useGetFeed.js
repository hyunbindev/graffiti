"user client";
import { useRouter } from 'next/navigation';
import { getWhisperFeed } from "@/model/WhisperModel";
import { useEffect, useState } from "react";

const useGetFeed = (feedId) => {
    const [feed, setFeed] = useState(null);
    const router = useRouter();
    const fetchFeed = useCallback(async () => {
        try {
            const data = await getWhisperFeed(feedId);
            setFeed(data);
            console.log(data);
        } catch (error) {
            console.error("Error fetching whisper feed:", error);
            router.replace(`/forbidden`);
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