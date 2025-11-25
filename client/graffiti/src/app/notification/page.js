"use client"
import style from './notificationPage.module.css'
import NotificationElement from "@/component/notification/NotificationElement"
import useNotification from "@/viewmodel/notification/useNotification"
export default function NotificationPage(){
    const {notifications}=useNotification();
    return(<div className={style.NotificationPage}>
        {
            notifications.map((n,i)=>(<NotificationElement key={i} notification={n}/>))
        }
    </div>)
}