"user client";

import { useEffect, useState } from "react";

import { useAuthStore } from '@/zustand/useAuthStore.js';
import {createWhiperFeed} from "@/model/FeedModel"
import { useRouter } from "next/navigation";
const useCreateWhisper = () => {
    const router = useRouter();
    const [text,setText] =useState("");
    const { selectedGroup} = useAuthStore();
    const createFeed = async (image)=>{
        try{
            if(text.trim().length<1){
                alert("글을 입려해 주세요")
                return;
            }
            const res = await createWhiperFeed(selectedGroup.uuid,image,text);
            router.push(`/feed/${res}`);
        }catch(err){
            alert(err);
        }
    }
    return { text ,createFeed ,setText};
}
export default useCreateWhisper;