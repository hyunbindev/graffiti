import style from './forbidden.module.css'
export default function ForbbidenFeed(){
    return (
        <div className={style.forbiddenContainer}>
            <img src={"/feed/forbidden.svg"}/>
            <div>접근할 수 없는 게시글 입니다.</div>
        </div>
    )
}