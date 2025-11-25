"use client";
import Link from 'next/link';
import style from './GroupPage.module.css'
import GroupElement from '@/component/group/GroupElement'
import { useAuthStore } from '@/zustand/useAuthStore.js';
import GroupInviteModal from '@/component/group/invite/GroupInviteModal'
import { useState } from 'react';
export default function GroupPage(){
    const { groups } = useAuthStore();
    const [selectedGroup, setSelectedGroup] = useState(null);

    return(
    <>
    <div className={style.groupPageContainer}>
        {/** <button className={style.joinGroup}><img src={'/group/join.svg'}/>그룹 참여하기</button>*/}
        <h2 style={{"marginBottom":"0.5rem"}}>그룹 관리</h2>
        <div className={style.groupContainer}>
            {
                groups && groups.map((group,idx)=>(<GroupElement key={idx} group={group} setSelectedGroup={setSelectedGroup}/>))
            }
        </div>
        <Link href="/group/create">
        <button className={style.newgroup}>
            <img src="/group/newgroup.svg"/>
            그룹 만들기
        </button>
        </Link>
    </div>
    {selectedGroup && <GroupInviteModal selectedGroup={selectedGroup} setSelectedGroup={setSelectedGroup} />}
    </>
    )
}