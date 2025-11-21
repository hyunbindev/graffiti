"use client"
import style from './GroupPage.module.css'
import GroupElement from '@/component/group/GroupElement'
import { useAuthStore } from '@/zustand/useAuthStore.js';
export default function GroupPage(){
    const { groups, selectedGroup} = useAuthStore();
    return(
    <div className={style.groupPageContainer}>
        <h2>그룹 관리</h2>
        <div className={style.groupContainer}>
            {
                groups.map((group)=>(<GroupElement group={group}/>))
            }
        </div>
        <button>
            그룹 만들기
        </button>
    </div>
    )
}