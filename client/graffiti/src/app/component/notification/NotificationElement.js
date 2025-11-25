import style from './NotificationElement.module.css'
import { useRouter } from "next/navigation";
const NotificationElement = ({notification}) =>{
    const router = useRouter();

    const navigate = ()=>{
        if(notification.feedId){
            router.push(`/feed/${notification.feedId}`)
        }
    }

    return(
        <div className={style.NotificationElement} onClick={navigate}>
            <img src={notification.iconUrl ? notification.iconUrl : "/notification/unkown.svg"} alt='icon'/>
            <p>{notification.content}</p>
        </div>
    )
}
export default NotificationElement;