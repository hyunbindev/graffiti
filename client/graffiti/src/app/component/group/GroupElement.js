import style from './Groupelement.module.css'
export default function GroupElement({group}){
    return (
        <div className={style.groupElement}>
            <div className={style.groupName}>{group.name}</div>
            <div className={style.groupContainer}>
                <button className={style.invite}><img src={'/group/invite.svg'}/>초대</button>
                <button className={style.run}><img src={'/group/run.svg'}/>도망</button>
            </div>
        </div>
    )
}