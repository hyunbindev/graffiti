import style from './MemberElement.module.css'


export default function MemberElement ({member,clickHandler}){
    return(
        <div className={style.elementContainer} onClick={()=>clickHandler(member)}>
            <img src={member.profileImg} alt='profileImg'/>
            <span>{member.nickName}</span>
        </div>
    )
}