
"use client";
import { useState , useEffect } from 'react';
import { getNotification } from '@/model/NotificationModel';
const useNotification = () => {
    const [notifications, setNotifications] = useState([]);
    const getRecentNotification = async()=>{
        try {
            const res = await getNotification();
            console.log(res);
            setNotifications(res);
        } catch (error) {
            console.error(error);
        }
    }
    useEffect(()=>{getRecentNotification()},[]);

    return { notifications };
}
export default useNotification;