
"use client"
import style from './InviteCodePage.module.css';
import { useParams } from 'next/navigation';

import useGroupJoin from '@/viewmodel/group/useGroupJoin';

export default function InviteCodePage(){
    const params = useParams();
    const {groupName ,joinGroup} = useGroupJoin(params); 
    return (
        <div className={style.pageContainer}>
            <div id={style.groupName}>{groupName}</div>
            <p>그룹에 오신걸 환영합니다!!</p>
            <img id={style.greeting} src={"/group/greeting.svg"}/>
            <button id={style.join} onClick={()=>joinGroup(params)}>그룹 참여</button>
        </div>
    )
}